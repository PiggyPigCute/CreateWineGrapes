package com.piggypig.createwinegrapes.items;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateWineGrapes.MOD_ID);

    public static final DeferredItem<Item> BUNCH_OF_GRAPES = ITEMS.register("bunch_of_grapes",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GRAPE = ITEMS.register("grape",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).build())));

    public static final DeferredItem<Item> STEM = ITEMS.register("stem",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
