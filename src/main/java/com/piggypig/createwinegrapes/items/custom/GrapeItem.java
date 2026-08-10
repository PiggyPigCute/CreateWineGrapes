package com.piggypig.createwinegrapes.items.custom;

import com.piggypig.createwinegrapes.blocks.ModBlocks;
import com.piggypig.createwinegrapes.blocks.custom.VineBlock;
import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class GrapeItem extends GrapeLikeItem {
    public GrapeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);
        if (!clickedState.is(Blocks.COARSE_DIRT)) {
            return InteractionResult.PASS;
        }

        BlockPos plantPos = clickedPos.above();
        if (!level.getBlockState(plantPos).isAir()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            GrapeVariety variety = getGrapeData(context.getItemInHand()).getGrapeVariety();
            BlockState vineState = ModBlocks.VINE.get().defaultBlockState()
                    .setValue(VineBlock.STAGE, 0)
                    .setValue(VineBlock.GRAPE_VARIETY, variety);
            level.setBlock(plantPos, vineState, 3);
            level.playSound(null, plantPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);

            Player player = context.getPlayer();
            if (player != null && !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
