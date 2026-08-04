package com.piggypig.createwinegrapes.items.custom;

import com.piggypig.createwinegrapes.items.ModDataComponents;
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
import org.jetbrains.annotations.NotNull;

public class BunchOfGrapesItem extends Item {
    public static final int MAX_GRAPES = 6;

    public BunchOfGrapesItem(Properties properties) {
        super(properties);
    }

    public static int getGrapeCount(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.GRAPE_COUNT.get(), 6);
    }

    public static void setGrapeCount(ItemStack stack, int count) {
        stack.set(ModDataComponents.GRAPE_COUNT.get(), Math.max(0, count));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(heldStack);
        }

        int remainingGrapes = getGrapeCount(heldStack) - 1;
        ItemStack resultHandStack;

        if (remainingGrapes > 0) {
            heldStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(remainingGrapes));
            setGrapeCount(heldStack, remainingGrapes);
            resultHandStack = heldStack;
        } else {
            heldStack.shrink(1);
            ItemStack stem = new ItemStack(ModItems.STEM.get());
            player.setItemInHand(hand, stem);
            resultHandStack = stem;
        }

        ItemStack grape = new ItemStack(ModItems.GRAPE.get());
        if (!player.getInventory().add(grape)) {
            player.drop(grape, false);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.PLAYERS, 1.0F, 1.0F);

        return InteractionResultHolder.success(resultHandStack);
    }
}
