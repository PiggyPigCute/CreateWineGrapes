package com.piggypig.createwinegrapes.items.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import com.piggypig.createwinegrapes.data.custom.Vineyard;
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
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(count));
    }

    public static GrapeVariety getGrapeVariety(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.GRAPE_VARIETY.get(), GrapeVariety.NONE);
    }

    public static void setGrapeVariety(ItemStack stack, GrapeVariety variety) {
        stack.set(ModDataComponents.GRAPE_VARIETY.get(), variety);
    }

    public static Vineyard getVineyard(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.VINEYARD.get(), Vineyard.DEFAULT);
    }

    public static void setVineyard(ItemStack stack, Vineyard vineyard) {
        stack.set(ModDataComponents.VINEYARD.get(), vineyard);
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
            setGrapeCount(heldStack, remainingGrapes);
            resultHandStack = heldStack;
        } else {
            heldStack.shrink(1);
            ItemStack stem = new ItemStack(ModItems.STEM.get());
            player.setItemInHand(hand, stem);
            resultHandStack = stem;
        }

        ItemStack grape = new ItemStack(ModItems.GRAPE.get());
        setGrapeVariety(grape, getGrapeVariety(heldStack));
        setVineyard(grape, getVineyard(heldStack));
        if (!player.getInventory().add(grape)) {
            player.drop(grape, false);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.PLAYERS, 1.0F, 1.0F);

        return InteractionResultHolder.success(resultHandStack);
    }
}
