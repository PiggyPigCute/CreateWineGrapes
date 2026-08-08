package com.piggypig.createwinegrapes.blocks.custom.mechanicalDestemmer;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.piggypig.createwinegrapes.items.ModItems;
import com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MechanicalDestemmerBlockEntity extends KineticBlockEntity {

    private int processingTicks = -1;
    private static final int PROCESS_DURATION = 50;

    public ItemStackHandler inputInv;
    public IItemHandler capability;

    private Direction lastInputFace = null;

    public MechanicalDestemmerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInv = new ItemStackHandler(1);
        capability = new MechanicalDestemmerInventoryHandler();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MECHANICAL_DESTEMMER.get(),
                MechanicalDestemmerBlockEntity::getItemHandlerFor
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        super.addBehaviours(behaviours);
    }

    private boolean isForward() {
        return getSpeed() >= 0;
    }

    private Direction getPortA() {
        MechanicalDestemmer block = (MechanicalDestemmer) getBlockState().getBlock();
        return block.getPortA(getBlockState());
    }

    public Direction getInputFace() {
        return isForward() ? getPortA() : getPortA().getOpposite();
    }

    public Direction getOutputFace() {
        return getInputFace().getOpposite();
    }

    public IItemHandler getItemHandlerFor(Direction side) {
        if (side != null && side.getOpposite() != getInputFace()) {
            return null;
        }
        return capability;
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    @Override
    public void tick() {
        super.tick();
        if (level != null && level.isClientSide) return;

        Direction currentInputFace = getSpeed() != 0 ? getInputFace() : null;
        if (currentInputFace != lastInputFace) {
            lastInputFace = currentInputFace;
            invalidateCapabilities();
        }

        ItemStack held = inputInv.getStackInSlot(0);
        if (held.isEmpty()) {
            processingTicks = -1;
            return;
        }

        if (getSpeed() == 0) {
            return; // pause : la rotation s'est arrêtée, on ne perd pas la progression
        }

        if (processingTicks == -1) {
            processingTicks = PROCESS_DURATION;
            sendData();
            return;
        }

        if (processingTicks > 0) {
            processingTicks -= getProcessingSpeed();
            return;
        }

        finishProcessing(held);
        processingTicks = -1;
        sendData();
    }

    private void finishProcessing(ItemStack bunch) {
        int grapeCount = BunchOfGrapesItem.getGrapeCount(bunch);
        dropGrapeBelow(grapeCount);
        dropStem();

        inputInv.setStackInSlot(0, ItemStack.EMPTY);
    }

    private void dropGrapeBelow(int grapeCount) {
        if (level == null) return;
        ItemStack grape = new ItemStack(ModItems.GRAPE.get(), grapeCount);
        dropOrInsert(grape, worldPosition);
    }

    private void dropOrInsert(ItemStack stack, BlockPos abovePos) {
        if (level == null) {
            return;
        }

        BlockPos below = abovePos.below();
        IItemHandler handlerBelow = level.getCapability(Capabilities.ItemHandler.BLOCK, below, Direction.UP);

        if (handlerBelow != null) {
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(handlerBelow, stack, false);
            if (remainder.isEmpty()) return; // tout absorbé par la chute
            stack = remainder; // la chute est pleine, on drop le reste
        }

        ItemEntity entity = new ItemEntity(level,
                abovePos.getX() + 0.5,
                abovePos.getY() - 0.3,
                abovePos.getZ() + 0.5,
                stack);
        entity.setDeltaMovement(0, 0, 0);
        level.addFreshEntity(entity);
    }

    private void dropStem() {
        if (level == null) return;

        ItemStack stem = new ItemStack(ModItems.STEM.get());
        Direction outputFace = getOutputFace();
        BlockPos outputPos = worldPosition.relative(outputFace);

        // 1. Essayer d'insérer directement sur une belt via son comportement dédié
        DirectBeltInputBehaviour beltInput = BlockEntityBehaviour.get(level, outputPos, DirectBeltInputBehaviour.TYPE);
        if (beltInput != null) {
            ItemStack remainder = beltInput.handleInsertion(stem, outputFace, false);
            if (remainder.isEmpty()) return;
            stem = remainder;
        } else {
            // 2. Sinon, capability IItemHandler générique (hopper, coffre, etc.)
            IItemHandler handlerAtOutput = level.getCapability(Capabilities.ItemHandler.BLOCK, outputPos, outputFace.getOpposite());
            if (handlerAtOutput != null) {
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(handlerAtOutput, stem, false);
                if (remainder.isEmpty()) return;
                stem = remainder;
            }
        }

        // 3. Sinon on le lâche au sol devant la sortie
        ItemEntity entity = new ItemEntity(level,
                outputPos.getX() + 0.5,
                worldPosition.getY() + 0.5,
                outputPos.getZ() + 0.5,
                stem);
        entity.setDeltaMovement(0, 0, 0);
        level.addFreshEntity(entity);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.put("InputInv", inputInv.serializeNBT(registries));
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        inputInv.deserializeNBT(registries, compound.getCompound("InputInv"));
        super.read(compound, registries, clientPacket);
    }




    private class MechanicalDestemmerInventoryHandler extends CombinedInvWrapper {
        public MechanicalDestemmerInventoryHandler() {
            super(inputInv);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (getSpeed() == 0) {
                return false;
            }
            return stack.is(ModItems.BUNCH_OF_GRAPES.get());
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (!isItemValid(slot, stack)) {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    }
}