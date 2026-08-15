package com.piggypig.createwinegrapes.blocks.custom.crusher;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CrusherBlock extends HorizontalKineticBlock implements IRotate, IBE<CrusherBlockEntity> {

    public CrusherBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public Class<CrusherBlockEntity> getBlockEntityClass() {
        return CrusherBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CrusherBlockEntity> getBlockEntityType() {
        return ModBlockEntities.CRUSHER.get();
    }
}