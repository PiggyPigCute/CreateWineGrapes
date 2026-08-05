package com.piggypig.createwinegrapes.blocks.custom;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class MechanicalDestemmer extends KineticBlock implements ICogWheel, IBE<MechanicalDestemmerBlockEntity> {

    public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS; // X ou Z uniquement

    public MechanicalDestemmer(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(HORIZONTAL_AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_AXIS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return false;
    }

    // Les deux faces "ports" perpendiculaires à l'axe de rotation
    public Direction getPortA(BlockState state) {
        return state.getValue(HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis preferredAxis = context.getHorizontalDirection().getAxis();
        return defaultBlockState().setValue(HORIZONTAL_AXIS, preferredAxis);
    }

    @Override
    public Class<MechanicalDestemmerBlockEntity> getBlockEntityClass() {
        return MechanicalDestemmerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalDestemmerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.MECHANICAL_DESTEMMER.get();
    }
}