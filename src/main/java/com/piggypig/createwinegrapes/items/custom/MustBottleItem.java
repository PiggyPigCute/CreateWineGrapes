package com.piggypig.createwinegrapes.items.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.MustKind;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
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
        if (!stack.has(ModDataComponents.CAP.get())) {
            return Component.translatable("item.create_wine_grapes.must_bottle.uncapped", name);
        }
        return name;
    }

    public static void setMustData(ItemStack stack, MustData mustData) {
        stack.set(ModDataComponents.MUST_DATA.get(), mustData);
        MustKind kind = MustKind.classify(mustData);
        int textureOrdinal = kind == null ? MustKind.BaseTexture.MUST.ordinal() : kind.getTexture().ordinal();
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(textureOrdinal));
    }

    public static void setCap(ItemStack stack, Item cap) {
        stack.set(ModDataComponents.CAP.get(), cap);
    }
}
