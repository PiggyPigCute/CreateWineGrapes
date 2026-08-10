package com.piggypig.createwinegrapes.blocks.custom.press_basin;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PressBasinBlock extends KineticBlock implements IRotate, IBE<PressBasinBlockEntity> {

    public PressBasinBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    @Override
    public Class<PressBasinBlockEntity> getBlockEntityClass() {
        return PressBasinBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PressBasinBlockEntity> getBlockEntityType() {
        return ModBlockEntities.PRESS_BASIN.get();
    }
}
