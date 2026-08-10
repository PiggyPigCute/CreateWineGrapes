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
}
