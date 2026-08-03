package com.piggypig.createwinegrapes.items;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.ModBlocks;
import com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateWineGrapes.MOD_ID);

    public static final Supplier<CreativeModeTab> CREATE_WINE_GRAPES_TAB = CREATIVE_MODE_TAB.register("create_wine_grapes_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.BUNCH_OF_GRAPES.get()))
                    .title(Component.translatable("creativetab.create_wine_grapes.create_wine_grapes_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        for (int grapes = BunchOfGrapesItem.MAX_GRAPES; grapes >= 1; grapes--) {
                            ItemStack bunch = new ItemStack(ModItems.BUNCH_OF_GRAPES.get());
                            bunch.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(grapes));
                            output.accept(bunch);
                        }
                        output.accept(ModItems.STEM);
                        output.accept(ModItems.GRAPE);
                        output.accept(ModBlocks.MECHANICAL_DESTEMMER);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
