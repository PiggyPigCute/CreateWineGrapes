package com.piggypig.createwinegrapes.blocks.custom;

import com.piggypig.createwinegrapes.items.ModItems;
import com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class VineBlock extends Block {
    public static final int MAX_STAGE = 5;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);

    public VineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
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
            ItemStack bunchOfGrapes = new ItemStack(ModItems.BUNCH_OF_GRAPES.get());
            BunchOfGrapesItem.setGrapeCount(bunchOfGrapes, 4 + level.random.nextInt(3));
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
