package com.piggypig.createwinegrapes.blocks.custom.vat;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.piggypig.createwinegrapes.data.custom.FermentationData;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.Residue;
import com.piggypig.createwinegrapes.fluids.ModFluids;
import com.piggypig.createwinegrapes.fluids.custom.MustFluid;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * One of the 12 blocks making up a formed Vat. Exactly one of them (the one the
 * structure was placed from) is the "controller" and owns the actual {@link FluidTank};
 * the other 11 only remember the controller position and forward all fluid access to it,
 * so pipes/pumps/spouts can attach to any face of any of the 12 blocks.
 */
public class VatBlockEntity extends SmartBlockEntity {

    public static final int CAPACITY_PER_BLOCK = 8000;
    public static final int TOTAL_CAPACITY =
            CAPACITY_PER_BLOCK * VatBlock.WIDTH * VatBlock.WIDTH * VatBlock.HEIGHT;

    @Nullable
    private BlockPos controllerPos;
    @Nullable
    private Direction forward;
    @Nullable
    private FluidTank tank;

    private static final int PROCESS_DURATION = 20;
    private int processingTicks = PROCESS_DURATION;

    public VatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.VAT.get(),
                (be, direction) -> be.getFluidHandler()
        );
    }

    public void makeController(Direction forward) {
        this.controllerPos = null;
        this.forward = forward;
        this.tank = newTank();
        setChanged();
    }

    public void makePart(BlockPos controllerPos, Direction forward) {
        this.controllerPos = controllerPos;
        this.forward = forward;
        this.tank = null;
        setChanged();
    }

    private FluidTank newTank() {
        return new FluidTank(TOTAL_CAPACITY) {
            @Override
            protected void onContentsChanged() {
                notifyUpdate();
            }
        };
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
        processingTicks = PROCESS_DURATION;
    }

    public boolean isController() {
        return controllerPos == null;
    }

    @Nullable
    public BlockPos getControllerPos() {
        return isController() ? worldPosition : controllerPos;
    }

    @Nullable
    public Direction getForward() {
        return forward;
    }

    @Nullable
    public VatBlockEntity getControllerBE() {
        if (isController())
            return this;
        if (level == null || controllerPos == null)
            return null;
        BlockEntity be = level.getBlockEntity(controllerPos);
        return be instanceof VatBlockEntity vatBE ? vatBE : null;
    }

    @Nullable
    public FluidTank getFluidHandler() {
        VatBlockEntity controller = getControllerBE();
        return controller != null ? controller.tank : null;
    }

    public float getFillState() {
        VatBlockEntity controller = getControllerBE();
        if (controller == null || controller.tank == null || controller.tank.getCapacity() == 0)
            return 0;
        return (float) controller.tank.getFluidAmount() / controller.tank.getCapacity();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        if (forward != null)
            tag.putString("Forward", forward.getSerializedName());
        if (isController()) {
            if (tank != null)
                tag.put("Tank", tank.writeToNBT(registries, new CompoundTag()));
        } else if (controllerPos != null) {
            tag.put("Controller", NbtUtils.writeBlockPos(controllerPos));
        }
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        forward = tag.contains("Forward") ? Direction.byName(tag.getString("Forward")) : null;

        if (tag.contains("Controller")) {
            controllerPos = NbtUtils.readBlockPos(tag, "Controller").orElse(null);
            tank = null;
            return;
        }

        controllerPos = null;
        if (tank == null)
            tank = newTank();
        if (tag.contains("Tank"))
            tank.readFromNBT(registries, tag.getCompound("Tank"));
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) {
            return;
        }

        if (!isController() || tank == null || tank.isEmpty() || !tank.getFluid().is(ModFluids.MUST.get())) {
            return;
        }

        if (MustFluid.getMustData(tank.getFluid()).fermentationData().fermentation() >= FermentationData.MAX_FERMENTATION) {
            return;
        }

        processingTicks -= 1;
        if (processingTicks == 0) {
            process();
            processingTicks = PROCESS_DURATION;
        }
    }

    private void process() {
        assert tank != null;
        FluidStack fluidStack = tank.getFluid();

        MustData currentMustData = MustFluid.getMustData(fluidStack);
        FermentationData currentFermentationData = currentMustData.fermentationData();
        int currentFermentation = currentFermentationData.fermentation();
        int currentHerbaceousness = currentMustData.herbaceousness();
        Residue residue = currentMustData.residue();
        int herbaceousnessAdd = switch (residue) {
            case LOW, NONE -> 0;
            case SKINS -> 1;
            case STEMS -> 2;
        };

        MustFluid.setMustData(fluidStack, currentMustData
                .withFermentationData(
                        currentFermentationData.withFermentation(currentFermentation + 1)
                )
                .withHerbaceousness(currentHerbaceousness + herbaceousnessAdd)
        );
    }
}
