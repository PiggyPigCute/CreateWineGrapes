package com.piggypig.createwinegrapes.items.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.GrapeData;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.MustKind;
import com.piggypig.createwinegrapes.fluids.custom.MustFluid;
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
        Component kindName = Component.translatable(
                "fluid.create_wine_grapes.must." + MustKind.classify(data).getName()
        );
        Component name = Component.translatable("item.create_wine_grapes.must_bottle.format", kindName);
        if (!stack.has(ModDataComponents.CAPPED.get())) {
            return Component.translatable("item.create_wine_grapes.must_bottle.uncapped", name);
        }
        return name;
    }

    public static void setMustData(ItemStack stack, MustData mustData) {
        stack.set(ModDataComponents.MUST_DATA.get(), mustData);
    }
}
