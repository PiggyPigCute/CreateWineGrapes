package com.piggypig.createwinegrapes.blocks.custom.press_basin;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.piggypig.createwinegrapes.data.custom.*;
import com.piggypig.createwinegrapes.fluids.ModFluids;
import com.piggypig.createwinegrapes.fluids.custom.MustFluid;
import com.piggypig.createwinegrapes.items.ModItems;
import com.piggypig.createwinegrapes.items.custom.GrapeLikeItem;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PressBasinBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, Clearable {
    private static final int PROCESS_DURATION = 100;
    private int processingTicks = PROCESS_DURATION;

    private IItemHandler itemCapability;
    private IFluidHandler fluidCapability;
    private IFluidHandler inputSideFluidCapability;
    private IFluidHandler outputSideFluidCapability;

    public SmartInventory inputInventory;
    public SmartFluidTankBehaviour inputTank;
    protected SmartInventory outputInventory;
    protected SmartFluidTankBehaviour outputTank;

    private float pressHeight = 1f;
    private boolean locked = false;

    public PressBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new SmartInventory(9, this).forbidExtraction().withMaxStackSize(6);
        outputInventory = new SmartInventory(1, this).allowExtraction().withMaxStackSize(64);
        itemCapability = new PressBasinInventoryHandler();
    }

    public IFluidHandler getFluidHandler(Direction side) {
        if (side == null) {
            return null;
        }
        if (side == Direction.UP) {
            return null;
        }
        if (side == Direction.DOWN) {
            return outputSideFluidCapability;
        }
        return inputSideFluidCapability;
    }

    public float getPressHeight() {
        return pressHeight;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.PRESS_BASIN.get(),
                (PressBasinBlockEntity be, Direction context) -> be.itemCapability
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.PRESS_BASIN.get(),
                PressBasinBlockEntity::getFluidHandler
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        inputTank = new SmartFluidTankBehaviour(
                SmartFluidTankBehaviour.INPUT,
                this,
                1,
                1000,
                true
        ).forbidExtraction();
        outputTank = new SmartFluidTankBehaviour(
                SmartFluidTankBehaviour.OUTPUT,
                this,
                1,
                1000,
                true
        );
        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = new PressBasinFluidHandler(true, true);
        inputSideFluidCapability = new PressBasinFluidHandler(true, false);
        outputSideFluidCapability = new PressBasinFluidHandler(false, true);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        inputInventory.deserializeNBT(registries, compound.getCompound("InputItems"));
        outputInventory.deserializeNBT(registries, compound.getCompound("OutputItems"));
        pressHeight = compound.getFloat("PressHeight");
        locked = compound.getBoolean("Locked");
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("InputItems", inputInventory.serializeNBT(registries));
        compound.put("OutputItems", outputInventory.serializeNBT(registries));
        compound.putFloat("PressHeight", pressHeight);
        compound.putBoolean("Locked", locked);
    }

    @Override
    public void clearContent() {
        inputInventory.clearContent();
        outputInventory.clearContent();
    }
    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
        ItemHelper.dropContents(level, worldPosition, outputInventory);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    @Override
    public float calculateStressApplied() {
        if (locked) {
            return 1000000f;
        }
        return super.calculateStressApplied();
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
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) {
            return;
        }

        float speed = getSpeed();

        if (locked) {
            if (processingTicks >= 0) {
                processingTicks -= 1;
            }
            if (processingTicks == 0) {
                process();
            }
        }

        if (pressHeight == 1f && speed >= 0) {
            return;
        }

        if (pressHeight == 0f) {
            if (getTheoreticalSpeed() <= 0) {
                return;
            }
            locked = false;
            if (hasNetwork()) {
                getOrCreateNetwork().updateStressFor(this, calculateStressApplied());
            }
        }

        if (speed == 0) {
            return;
        }

        pressHeight += speed / 1000;
        sendData();
        if (pressHeight <= 0f) {
            pressHeight = 0f;
            locked = true;
            if (hasNetwork()) {
                getOrCreateNetwork().updateStressFor(this, calculateStressApplied());
            }
            processingTicks = PROCESS_DURATION;
        } else if (pressHeight > 1f) {
            pressHeight = 1f;
        }
//        CreateWineGrapes.LOGGER.debug(String.valueOf(pressHeight));
    }

    private void process() {
        FluidStack output = new FluidStack(ModFluids.MUST.get(), 0);

        int volume;
        GrapeData grapeData;
        int herbaceousness = 0;
        boolean pressedWithStem = false;
        FermentationData fermentationData;

        if (!inputInventory.isEmpty()) {
            volume = countInputGrapes() * 10;
            ItemStack grapeStack = inputInventory.getStackInSlot(0);
            grapeData = GrapeLikeItem.getGrapeData(grapeStack);
            for (int i = 0; i < inputInventory.getSlots(); i++) {
                ItemStack stack = inputInventory.getStackInSlot(i);
                if (stack.is(ModItems.BUNCH_OF_GRAPES.get())) {
                    pressedWithStem = true;
                    break;
                }
            }
            fermentationData = FermentationData.DEFAULT;
        }
        else if (!inputTank.isEmpty()) {
            volume = inputTank.getPrimaryHandler().getFluidAmount();
            MustData inputMustData = MustFluid.getMustData(inputTank.getPrimaryHandler().getFluid());
            grapeData = inputMustData.grapeData();
            herbaceousness = inputMustData.herbaceousness();
            if (inputMustData.residue() == Residue.STEMS) {
                pressedWithStem = true;
            }
            fermentationData = inputMustData.fermentationData();
        }
        else {
            return;
        }

        // S'il n'y a pas assez de place dans le outputTank ou le outputInventory
        if (volume > 1000 - outputTank.getPrimaryHandler().getFluidAmount()) {
            return;
        }
        if (volume / 20 > 64 - outputInventory.getStackInSlot(0).getCount()) {
            return;
        }

        output.setAmount(volume);
        MustFluid.setMustData(
                output,
                new MustData(
                        grapeData,
                        Residue.LOW,
                        herbaceousness,
                        pressedWithStem,
                        fermentationData,
                        false
                )
        );

        outputTank.getCapability().fill(output, IFluidHandler.FluidAction.EXECUTE);

        if (!inputInventory.isEmpty()) {
            inputInventory.clearContent();
        }
        else if (!inputTank.isEmpty()) {
            inputTank.getPrimaryHandler().setFluid(FluidStack.EMPTY);
        }
        outputInventory.insertItem(
                0,
                new ItemStack(ModItems.GRAPE_MARC.get(), volume / 20),
                false
        );
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.translate("gui.goggles.basin_contents")
                .forGoggles(tooltip);

        if (itemCapability == null)
            itemCapability = new ItemStackHandler();
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

        int marcCount = outputInventory.getStackInSlot(0).getCount();
        if (marcCount > 0) {
            CreateLang.text("")
                    .add(Component.translatable(ModItems.GRAPE_MARC.get().getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(CreateLang.text(" x" + marcCount)
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
        }

        if (isEmpty) {
            tooltip.removeLast();
            return false;
        }

        return true;
    }





    private class PressBasinInventoryHandler extends CombinedInvWrapper {
        public PressBasinInventoryHandler() {
            super(inputInventory, outputInventory);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot >= inputInventory.getSlots()) {
                return false;
            }
            if (pressHeight < 1f) {
                return false;
            }
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
            if (slot < inputInventory.getSlots()) {
                return ItemStack.EMPTY;
            }
            return super.extractItem(slot, amount, simulate);
        }
    }

    private class PressBasinFluidHandler extends CombinedTankWrapper {
        private final boolean allowFill;
        private final boolean allowDrain;

        public PressBasinFluidHandler(boolean allowFill, boolean allowDrain) {
            super(outputTank.getCapability(), inputTank.getCapability());
            this.allowFill = allowFill;
            this.allowDrain = allowDrain;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            if (!stack.is(ModFluids.MUST.get())) {
                return false;
            }
            return super.isFluidValid(tank, stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!allowFill)
                return 0;
            if (!resource.is(ModFluids.MUST.get()))
                return 0;
            if (pressHeight != 1f)
                return 0;
            if (!inputInventory.isEmpty())
                return 0;
            return inputTank.getCapability().fill(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (!allowDrain)
                return FluidStack.EMPTY;
            return super.drain(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (!allowDrain)
                return FluidStack.EMPTY;
            return super.drain(maxDrain, action);
        }
    }
}
