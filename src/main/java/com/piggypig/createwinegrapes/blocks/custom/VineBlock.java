package com.piggypig.createwinegrapes.blocks.custom;

import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import com.piggypig.createwinegrapes.data.custom.Vineyard;
import com.piggypig.createwinegrapes.items.ModItems;
import com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class VineBlock extends Block {
    public static final int MAX_STAGE = 5;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);
    public static final EnumProperty<GrapeVariety> GRAPE_VARIETY = EnumProperty.create("grape_variety", GrapeVariety.class);
    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    public VineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(STAGE, 0).setValue(GRAPE_VARIETY, GrapeVariety.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, GRAPE_VARIETY);
    }

    protected int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return 15;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.makeStuckInBlock(state, new Vec3(0.8, 0.75, 0.8));
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) < MAX_STAGE;
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        int stage = state.getValue(STAGE);
        if (stage < MAX_STAGE && random.nextInt(5) == 0) {
            BlockState grown = state.setValue(STAGE, stage + 1);
            level.setBlock(pos, grown, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(grown));
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (state.getValue(STAGE) != MAX_STAGE) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }

        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            ItemStack bunchOfGrapes = new ItemStack(ModItems.BUNCH_OF_GRAPES.get());
            BunchOfGrapesItem.setGrapeCount(bunchOfGrapes, 4 + level.random.nextInt(3));
            BunchOfGrapesItem.setGrapeVariety(bunchOfGrapes, state.getValue(GRAPE_VARIETY));
            BunchOfGrapesItem.setVineyard(bunchOfGrapes, Vineyard.sample(serverLevel, pos));
            if (!player.getInventory().add(bunchOfGrapes)) {
                player.drop(bunchOfGrapes, false);
            }

            BlockState harvested = state.setValue(STAGE, MAX_STAGE-1);
            level.setBlock(pos, harvested, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, harvested));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected float getMaxHorizontalOffset() {
        return 0.1F;
    }
}
