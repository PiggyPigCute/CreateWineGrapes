package com.piggypig.createwinegrapes.items;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import com.piggypig.createwinegrapes.data.custom.Vineyard;
import com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem;
import com.piggypig.createwinegrapes.items.custom.GrapeItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomModelData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateWineGrapes.MOD_ID);

    public static final DeferredItem<BunchOfGrapesItem> BUNCH_OF_GRAPES = ITEMS.register(
            "bunch_of_grapes",
            () -> new BunchOfGrapesItem(new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(BunchOfGrapesItem.MAX_GRAPES))
                    .component(ModDataComponents.GRAPE_COUNT.get(), 6)
                    .component(ModDataComponents.GRAPE_VARIETY.get(), GrapeVariety.NONE)
                    .component(ModDataComponents.VINEYARD.get(), Vineyard.DEFAULT)
            )
    );

    public static final DeferredItem<GrapeItem> GRAPE = ITEMS.register(
            "grape",
            () -> new GrapeItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.1f)
                            .build()
                    )
                    .component(ModDataComponents.GRAPE_VARIETY.get(), GrapeVariety.NONE)
                    .component(ModDataComponents.VINEYARD.get(), Vineyard.DEFAULT)
            )
    );

    public static final DeferredItem<Item> STEM = ITEMS.register("stem",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GRAPE_MARC = ITEMS.register("grape_marc",
            () -> new Item(new Item.Properties()));

    // Never obtainable in survival: it is only ever exposed by PressBasinBlockEntity's item capability, as a
    // fixed ingredient that gates the compacting recipes to that basin and excludes a vanilla Create basin.
    public static final DeferredItem<Item> PRESS_MECHANISM = ITEMS.register("press_mechanism",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
