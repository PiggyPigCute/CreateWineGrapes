package com.piggypig.createwinegrapes.items;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.ModBlocks;
import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem.setGrapeCount;
import static com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem.setGrapeVariety;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateWineGrapes.MOD_ID);

    public static final Supplier<CreativeModeTab> CREATE_WINE_GRAPES_TAB = CREATIVE_MODE_TAB.register("create_wine_grapes_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> {
                        ItemStack icon = new ItemStack(ModItems.BUNCH_OF_GRAPES.get());
                        setGrapeCount(icon, 6);
                        setGrapeVariety(icon, GrapeVariety.TEMPRANILLO);
                        return icon;
                    })
                    .title(Component.translatable("creativetab.create_wine_grapes.create_wine_grapes_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        ArrayList<GrapeVariety> GRAPE_VARIETIES = new ArrayList<>(List.of(
                                GrapeVariety.CABERNET_SAUVIGNON,
                                GrapeVariety.TEMPRANILLO,
                                GrapeVariety.PINOT_NOIR,
                                GrapeVariety.GAMAY,
                                GrapeVariety.RIESLING,
                                GrapeVariety.CHARDONNAY,
                                GrapeVariety.RKATSITELI,
                                GrapeVariety.MALVASIA
                        ));
                        for (GrapeVariety grapeVariety : GRAPE_VARIETIES) {
                            ItemStack bunchOfGrapes = new ItemStack(ModItems.BUNCH_OF_GRAPES.get());
                            setGrapeCount(bunchOfGrapes, 6);
                            setGrapeVariety(bunchOfGrapes, grapeVariety);
                            output.accept(bunchOfGrapes);
                        }
                        output.accept(ModItems.STEM);
                        for (GrapeVariety grapeVariety : GRAPE_VARIETIES) {
                            ItemStack grape = new ItemStack(ModItems.GRAPE.get());
                            setGrapeVariety(grape, grapeVariety);
                            output.accept(grape);
                        }
                        output.accept(ModBlocks.MECHANICAL_DESTEMMER);
                        output.accept(ModBlocks.PRESS_BASSIN);
                        output.accept(ModBlocks.VAT);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
