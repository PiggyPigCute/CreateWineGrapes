package com.piggypig.createwinegrapes.items.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.GrapeData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class GrapeLikeItem extends Item {
    public GrapeLikeItem(Item.Properties properties) {
        super(properties);
    }

    public static GrapeData getGrapeData(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.GRAPE_DATA.get(), GrapeData.DEFAULT);
    }

    public static void setGrapeData(ItemStack stack, GrapeData grapeData) {
        stack.set(ModDataComponents.GRAPE_DATA.get(), grapeData);
    }

    public static int getGrapeCount(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.GRAPE_COUNT.get(), 1);
    }

    public static void setGrapeCount(ItemStack stack, int grapeCount) {
        stack.set(ModDataComponents.GRAPE_COUNT.get(), grapeCount);
    }
}
