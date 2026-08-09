package com.piggypig.createwinegrapes.blocks.custom.vat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Placing a Vat item places the whole 2x3x2 structure at once, expanding from the clicked
 * position towards the direction the player is facing (and their clockwise side). This is an
 * all-or-nothing placement: if the full volume isn't clear, nothing is placed.
 */
public class VatBlockItem extends BlockItem {

    public VatBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        if (!placeContext.canPlace())
            return InteractionResult.FAIL;

        BlockPos anchor = placeContext.getClickedPos();
        Direction forward = placeContext.getHorizontalDirection();
        List<BlockPos> positions = VatBlock.structurePositions(anchor, forward);

        for (BlockPos p : positions) {
            if (!level.isInWorldBounds(p))
                return fail(level, player);
            BlockState existing = level.getBlockState(p);
            if (!existing.isAir() && !existing.canBeReplaced(placeContext))
                return fail(level, player);
        }

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        Block block = getBlock();
        BlockState placedState = block.defaultBlockState();
        for (BlockPos p : positions)
            level.setBlock(p, placedState, Block.UPDATE_ALL);

        if (level.getBlockEntity(anchor) instanceof VatBlockEntity controllerBE) {
            controllerBE.makeController(forward);
            for (BlockPos p : positions) {
                if (p.equals(anchor))
                    continue;
                if (level.getBlockEntity(p) instanceof VatBlockEntity partBE)
                    partBE.makePart(anchor, forward);
            }
        }

        level.playSound(null, anchor, placedState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1f, 1f);

        if (player != null && !player.getAbilities().instabuild)
            context.getItemInHand().shrink(1);

        return InteractionResult.SUCCESS;
    }

    private InteractionResult fail(Level level, Player player) {
        if (!level.isClientSide && player != null)
            player.displayClientMessage(Component.translatable("block.create_wine_grapes.vat.not_enough_space"), true);
        return InteractionResult.FAIL;
    }
}
