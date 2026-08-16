package com.piggypig.createwinegrapes.blocks.custom.crusher;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.piggypig.createwinegrapes.data.custom.FermentationData;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.Residue;
import com.piggypig.createwinegrapes.fluids.ModFluids;
import com.piggypig.createwinegrapes.fluids.custom.MustFluid;
import com.piggypig.createwinegrapes.items.ModItems;
import com.piggypig.createwinegrapes.items.custom.GrapeLikeItem;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CrusherBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, Clearable {
    private static final int PROCESS_DURATION = 100;
    private int processingTicks = PROCESS_DURATION;

    private IItemHandler itemCapability;
    private IFluidHandler fluidCapability;

    public SmartInventory inputInventory;
    protected SmartFluidTankBehaviour outputTank;


    public CrusherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new SmartInventory(9, this).forbidExtraction().withMaxStackSize(6);
        itemCapability = new CrusherInventoryHandler();
    }

    public IFluidHandler getFluidHandler(Direction side) {
        if (side == null || side == Direction.DOWN) {
            return fluidCapability;
        }
        return null;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CRUSHER.get(),
                (CrusherBlockEntity be, Direction context) -> be.itemCapability
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.CRUSHER.get(),
                CrusherBlockEntity::getFluidHandler
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        outputTank = new SmartFluidTankBehaviour(
                SmartFluidTankBehaviour.OUTPUT,
                this,
                1,
                1000,
                true
        );
        behaviours.add(outputTank);
        fluidCapability = outputTank.getCapability();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("InputItems", inputInventory.serializeNBT(registries));
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        inputInventory.deserializeNBT(registries, compound.getCompound("InputItems"));
    }

    @Override
    public void clearContent() {
        inputInventory.clearContent();
    }
    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    private int countInputGrapes() {
        int sum = 0;

        for (int i = 0; i < inputInventory.getSlots(); i++) {
            ItemStack stack = inputInventory.getStackInSlot(i);
            sum += GrapeLikeItem.getGrapeCount(stack) * stack.getCount();
        }

        return sum;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.translate("gui.goggles.basin_contents")
                .forGoggles(tooltip);

        if (fluidCapability == null)
            fluidCapability = new FluidTank(0);

        boolean isEmpty = true;

        int grapeCount = countInputGrapes();
        if (grapeCount > 0) {
            CreateLang.text("")
                    .add(Component.translatable(ModItems.GRAPE.get().getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(CreateLang.text(" x" + grapeCount)
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }

        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
        for (int i = 0; i < fluidCapability.getTanks(); i++) {
            FluidStack fluidStack = fluidCapability.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;
            CreateLang.text("")
                    .add(CreateLang.fluidName(fluidStack)
                            .add(CreateLang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(CreateLang.number(fluidStack.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
            isEmpty = false;

            MustFluid.addGoggleTooltip(tooltip, isPlayerSneaking, fluidStack, 2);
        }

        if (isEmpty) {
            tooltip.removeLast();
            return false;
        }

        return true;
    }


    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) {
            return;
        }
        if (getSpeed() == 0) {
            return;
        }
        if (inputInventory.isEmpty()) {
            return;
        }

        int processingSpeed = getProcessingSpeed();

        processingTicks -= processingSpeed;
        if (processingTicks <= 0) {
            processingTicks = PROCESS_DURATION;
            process();
        }
    }

    private void process() {
        for (int i = 0; i < inputInventory.getSlots(); i++) {
            ItemStack stack = inputInventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                int volume = stack.getCount() * 10;
                if (volume <= 1000 - outputTank.getPrimaryHandler().getFluidAmount()) {
                    FluidStack output = new FluidStack(ModFluids.MUST.get(), volume);
                    Residue residue;
                    if (stack.is(ModItems.BUNCH_OF_GRAPES.get())) {
                        residue = Residue.STEMS;
                    }
                    else {
                        residue = Residue.SKINS;
                    }
                    MustFluid.setMustData(
                            output,
                            new MustData(
                                    GrapeLikeItem.getGrapeData(stack),
                                    residue,
                                    0,
                                    false,
                                    FermentationData.DEFAULT
                            )
                    );

                    int filled = outputTank.getCapability().fill(output, IFluidHandler.FluidAction.EXECUTE);
                    if (volume == filled) {
                        inputInventory.extractItem(i, stack.getCount(), false);
                        return;
                    }
                }
            }
        }
    }






    private class CrusherInventoryHandler extends CombinedInvWrapper {
        public CrusherInventoryHandler() {
            super(inputInventory);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(ModItems.BUNCH_OF_GRAPES.get()) || stack.is(ModItems.GRAPE.get());
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
