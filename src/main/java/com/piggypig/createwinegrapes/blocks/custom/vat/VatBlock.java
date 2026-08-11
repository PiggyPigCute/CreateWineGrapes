package com.piggypig.createwinegrapes.blocks.custom.vat;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A single cell of the 2(wide) x 3(tall) x 2(deep) Vat multiblock. All 12 cells share this
 * same block; which one is the controller (and therefore owns the {@link VatBlockEntity}'s
 * fluid tank) is tracked in the block entity, not in the block state, since the footprint is
 * square and every cell looks and behaves the same from the outside.
 * <p>
 * Assembly happens atomically in {@link VatBlockItem} - a lone Vat block never exists placed
 * by hand, so no runtime "try to connect to neighbours" logic is needed here. Breaking any one
 * of the 12 cells tears down the whole structure (see {@link #onRemove}).
 */
public class VatBlock extends Block implements IBE<VatBlockEntity>, IWrenchable {

    public static final int WIDTH = 2;
    public static final int HEIGHT = 3;

    private boolean disassembling;

    public VatBlock(Properties properties) {
        super(properties);
    }

    /** All 12 world positions of the structure anchored at {@code controllerPos}, expanding along {@code forward}/its clockwise side and upward. */
    public static List<BlockPos> structurePositions(BlockPos controllerPos, Direction forward) {
        Direction right = forward.getClockWise();
        List<BlockPos> positions = new ArrayList<>(WIDTH * WIDTH * HEIGHT);
        for (int f = 0; f < WIDTH; f++)
            for (int r = 0; r < WIDTH; r++)
                for (int y = 0; y < HEIGHT; y++)
                    positions.add(controllerPos.relative(forward, f).relative(right, r).above(y));
        return positions;
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        boolean genuinelyRemoved = state.getBlock() != newState.getBlock() || !newState.hasBlockEntity();
        if (genuinelyRemoved && !disassembling && !isMoving && !level.isClientSide)
            disassembleFrom(level, pos);
        IBE.onRemove(state, level, pos, newState);
    }

    private void disassembleFrom(Level level, BlockPos pos) {
        BlockEntity beRaw = level.getBlockEntity(pos);
        if (!(beRaw instanceof VatBlockEntity be))
            return;
        BlockPos controllerPos = be.getControllerPos();
        Direction forward = be.getForward();
        if (controllerPos == null || forward == null)
            return;

        disassembling = true;
        try {
            for (BlockPos p : structurePositions(controllerPos, forward)) {
                if (p.equals(pos))
                    continue;
                if (level.getBlockState(p).is(this))
                    level.setBlock(p, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
        } finally {
            disassembling = false;
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                                       @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (stack.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        VatBlockEntity be = getBlockEntity(level, pos);
        if (be == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (FluidHelper.tryEmptyItemIntoBE(level, player, hand, stack, be))
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        if (FluidHelper.tryFillItemFromBE(level, player, hand, stack, be))
            return ItemInteractionResult.sidedSuccess(level.isClientSide);

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return getBlockEntityOptional(level, pos)
                .map(be -> Math.round(be.getFillState() * 15f))
                .orElse(0);
    }

    @Override
    public Class<VatBlockEntity> getBlockEntityClass() {
        return VatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VatBlockEntity> getBlockEntityType() {
        return ModBlockEntities.VAT.get();
    }
}
