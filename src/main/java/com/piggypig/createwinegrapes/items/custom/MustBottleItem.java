package com.piggypig.createwinegrapes.items.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.MustKind;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MustBottleItem extends Item {
    public MustBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        MustData data = stack.getOrDefault(ModDataComponents.MUST_DATA.get(), MustData.DEFAULT);
        return Component.translatable(switch (MustKind.classify(data)) {
            case MUST -> "item.create_wine_grapes.must_bottle";
            case WINE -> "item.create_wine_grapes.wine_bottle";
            case THICK_WINE -> "item.create_wine_grapes.thick_wine_bottle";
        });
    }
}
