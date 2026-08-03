package com.piggypig.createwinegrapes.items.custom;

import com.piggypig.createwinegrapes.items.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;

public class BunchOfGrapesItem extends Item {
    public static final int MAX_GRAPES = 6;

    public BunchOfGrapesItem(Properties properties) {
        super(properties);
    }

    public static int getGrapeCount(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT).value();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(heldStack);
        }

        int remainingGrapes = getGrapeCount(heldStack) - 1;
        heldStack.shrink(1);

        ItemStack replacement;
        if (remainingGrapes > 0) {
            replacement = new ItemStack(this);
            replacement.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(remainingGrapes));
        } else {
            replacement = new ItemStack(ModItems.STEM.get());
        }

        ItemStack resultHandStack;
        if (heldStack.isEmpty()) {
            player.setItemInHand(hand, replacement);
            resultHandStack = replacement;
        } else {
            if (!player.getInventory().add(replacement)) {
                player.drop(replacement, false);
            }
            resultHandStack = heldStack;
        }

        ItemStack grape = new ItemStack(ModItems.GRAPE.get());
        if (!player.getInventory().add(grape)) {
            player.drop(grape, false);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.PLAYERS, 1.0F, 1.0F);

        return InteractionResultHolder.success(resultHandStack);
    }
}
