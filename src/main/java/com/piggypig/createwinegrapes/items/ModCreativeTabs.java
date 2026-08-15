package com.piggypig.createwinegrapes.items;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.ModBlocks;
import com.piggypig.createwinegrapes.data.custom.*;
import com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem;
import com.piggypig.createwinegrapes.items.custom.GrapeLikeItem;
import com.piggypig.createwinegrapes.items.custom.MustBottleItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateWineGrapes.MOD_ID);

    public static final Supplier<CreativeModeTab> CREATE_WINE_GRAPES_TAB = CREATIVE_MODE_TAB.register("create_wine_grapes_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> {
                        ItemStack icon = new ItemStack(ModItems.BUNCH_OF_GRAPES.get());
                        GrapeLikeItem.setGrapeData(icon, new GrapeData(
                                GrapeVariety.TEMPRANILLO,
                                Vineyard.DEFAULT,
                                false,
                                0
                        ));
                        GrapeLikeItem.setGrapeCount(icon, BunchOfGrapesItem.MAX_GRAPES);
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
                            GrapeLikeItem.setGrapeData(bunchOfGrapes, new GrapeData(
                                    grapeVariety,
                                    Vineyard.DEFAULT,
                                    false,
                                    0
                            ));
                            GrapeLikeItem.setGrapeCount(bunchOfGrapes, BunchOfGrapesItem.MAX_GRAPES);
                            output.accept(bunchOfGrapes);
                        }
                        output.accept(ModItems.STEM);
                        for (GrapeVariety grapeVariety : GRAPE_VARIETIES) {
                            ItemStack grape = new ItemStack(ModItems.GRAPE.get());
                            GrapeLikeItem.setGrapeData(grape, new GrapeData(
                                    grapeVariety,
                                    Vineyard.DEFAULT,
                                    false,
                                    0
                            ));
                            GrapeLikeItem.setGrapeCount(grape, 1);
                            output.accept(grape);
                        }
                        output.accept(ModItems.GRAPE);
                        output.accept(ModItems.GRAPE_MARC);
                        output.accept(ModItems.MUST_BOTTLE);
                        output.accept(ModBlocks.MECHANICAL_DESTEMMER);
                        output.accept(ModBlocks.PRESS_BASIN);
                        output.accept(ModBlocks.CRUSHER);
                        output.accept(ModBlocks.VAT);
                        ArrayList<MustData> musts = new ArrayList<>(List.of(
                                new MustData(
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData(
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                )
                        ));
                        for (MustData mustData : musts) {
                            ItemStack mustBottle = new ItemStack(ModItems.MUST_BOTTLE.get());
                            MustBottleItem.setMustData(mustBottle, mustData);
                            output.accept(mustBottle);
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
