package com.piggypig.createwinegrapes.blocks.custom;

import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import com.piggypig.createwinegrapes.data.custom.Vineyard;
import com.piggypig.createwinegrapes.items.ModItems;
import com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem;
import com.piggypig.createwinegrapes.items.custom.GrapeItem;
import net.minecraft.core.Direction;
import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class VineBlock extends Block {
    public static final int MAX_STAGE = 5;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_STAGE);
    public static final EnumProperty<GrapeVariety> GRAPE_VARIETY = EnumProperty.create("grape_variety", GrapeVariety.class);
    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);

    public VineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(STAGE, 0).setValue(AGE, 0).setValue(GRAPE_VARIETY, GrapeVariety.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, AGE, GRAPE_VARIETY);
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
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return level.getBlockState(pos.below()).is(Blocks.COARSE_DIRT);
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.isClientSide) {
            return;
        }

        if (state.getValue(STAGE) == MAX_STAGE && state.getValue(AGE) == 0) {
            BlockState harvested = state.setValue(STAGE, MAX_STAGE - 1).setValue(AGE, MAX_STAGE - 1);
            level.setBlock(pos, harvested, 2);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(@NotNull BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    protected @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder params) {
        if (state.getValue(STAGE) == MAX_STAGE) {
            ServerLevel level = params.getLevel();
            BlockPos pos = BlockPos.containing(params.getParameter(LootContextParams.ORIGIN));

            GrapeVariety variety = state.getValue(GRAPE_VARIETY);
            Vineyard vineyard = Vineyard.sample(level, pos);
            int grapeCount = 4 + level.random.nextInt(3);

            List<ItemStack> drops = new ArrayList<>(grapeCount);
            for (int i = 0; i < grapeCount; i++) {
                ItemStack grape = new ItemStack(ModItems.GRAPE.get());
                GrapeItem.setGrapeVariety(grape, variety);
                GrapeItem.setVineyard(grape, vineyard);
                drops.add(grape);
            }
            return drops;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) < MAX_STAGE;
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        int stage = state.getValue(STAGE);
        if (stage < MAX_STAGE && random.nextInt(5) == 0) {
            BlockState grown = state.setValue(STAGE, stage + 1).setValue(AGE, stage + 1);
            level.setBlock(pos, grown, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(grown));
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
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

            BlockState harvested = state.setValue(STAGE, MAX_STAGE - 1).setValue(AGE, MAX_STAGE - 1);
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
