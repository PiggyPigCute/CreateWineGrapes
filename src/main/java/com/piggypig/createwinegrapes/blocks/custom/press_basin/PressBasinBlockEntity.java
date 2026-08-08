package com.piggypig.createwinegrapes.blocks.custom.press_basin;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class PressBasinBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, Clearable {
    private IItemHandler itemCapability;
    private IFluidHandler fluidCapability;

    public SmartInventory inputInventory;
    public SmartFluidTankBehaviour inputTank;
    protected SmartInventory outputInventory;
    protected SmartFluidTankBehaviour outputTank;
    private boolean contentsChanged;

    private Couple<SmartInventory> invs;
    private Couple<SmartFluidTankBehaviour> tanks;

    private float pressHeight = 1f;

    public PressBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new SmartInventory(9, this).forbidExtraction().withMaxStackSize(6);
        inputInventory.whenContentsChanged($ -> contentsChanged = true);
        outputInventory = new SmartInventory(9, this).forbidInsertion().withMaxStackSize(64);
        itemCapability = new CombinedInvWrapper(inputInventory, outputInventory);
        contentsChanged = true;

        invs = Couple.create(inputInventory, outputInventory);
        tanks = Couple.create(inputTank, outputTank);
    }

    public IFluidHandler getFluidHandler(Direction side) {
        if (side != null && side != Direction.DOWN) {
            return null;
        }
        return fluidCapability;
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
                2,
                1000,
                true
        ).whenFluidUpdates(() -> contentsChanged = true);
        outputTank = new SmartFluidTankBehaviour(
                SmartFluidTankBehaviour.OUTPUT,
                this,
                2,
                1000,
                true
        ).whenFluidUpdates(() -> contentsChanged = true).forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = new CombinedTankWrapper(outputTank.getCapability(), inputTank.getCapability());
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        inputInventory.deserializeNBT(registries, compound.getCompound("InputItems"));
        outputInventory.deserializeNBT(registries, compound.getCompound("OutputItems"));
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("InputItems", inputInventory.serializeNBT(registries));
        compound.put("OutputItems", outputInventory.serializeNBT(registries));
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
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) {
            return;
        }

        pressHeight = Math.clamp(pressHeight + getSpeed() / 1000, 0f, 1f);
        CreateWineGrapes.LOGGER.debug(String.valueOf(pressHeight));
    }




}
