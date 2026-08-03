package com.piggypig.createwinegrapes.items;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
                        output.accept(ModItems.BUNCH_OF_GRAPES);
                        output.accept(ModItems.GRAPE);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
