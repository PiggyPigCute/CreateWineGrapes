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

import static net.minecraft.world.item.Items.OAK_BUTTON;

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
                                new MustData( // red_must
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT
                                ),
                                new MustData( // white_must
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT
                                ),
                                new MustData( // rosé must
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT
                                ),
                                new MustData( //orange must
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT
                                ),
                                new MustData( // thick red wine
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData( //thick orange wine
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.SKINS,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData( // red wine
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData( //white wine
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData( // rosé wine
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData( // orange wine
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64)
                                ),
                                new MustData( // young medoc
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // medoc
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(32)
                                ),
                                new MustData( // niagara icewine
                                        new GrapeData(GrapeVariety.CABERNET_SAUVIGNON, Vineyard.DEFAULT,true, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                // NAPA_VALLEY, YOUNG_NAPA_VALLEY, SUPER_TUSCAN, YOUNG_SUPER_TUSCAN are unreachable:
                                // classifyCabernetSauvignon's "if (true) {...} else if (true) {...}" chain means
                                // only the MEDOC branch above can ever be taken.

                                // tempranillo
                                new MustData( // rioja
                                        new GrapeData(GrapeVariety.TEMPRANILLO, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(64).withBottleAging(64)
                                ),
                                new MustData( // young rioja
                                        new GrapeData(GrapeVariety.TEMPRANILLO, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0).withBottleAging(0)
                                ),
                                new MustData( // alentejo aragonez
                                        new GrapeData(GrapeVariety.TEMPRANILLO, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        128,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withQvevriFermented(true).withOakAging(0)
                                ),
                                // PORTO is unreachable: never returned by classifyTempranillo (the `// TODO classify
                                // hasBrandy()` in classify() suggests fortified wines like this were never wired up).
                                // MENDOZA_TEMPRANILLO / YOUNG_MENDOZA_TEMPRANILLO are unreachable: their branch has
                                // the exact same condition (herb == 64 && !qvevriFermented) as the RIOJA branch
                                // above it, so RIOJA/YOUNG_RIOJA always wins.

                                // pinot noir
                                new MustData( // young rosé champagne
                                        new GrapeData(GrapeVariety.PINOT_NOIR, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        0,
                                        true,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0).withBottleAging(0)
                                ),
                                new MustData( // rosé champagne
                                        new GrapeData(GrapeVariety.PINOT_NOIR, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        0,
                                        true,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0).withBottleAging(64).withSugarAdded(true)
                                ),
                                new MustData( // nuits-saint-georges
                                        new GrapeData(GrapeVariety.PINOT_NOIR, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(32)
                                ),
                                new MustData( // young nuits-saint-georges
                                        new GrapeData(GrapeVariety.PINOT_NOIR, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // marsannay rosé
                                        new GrapeData(GrapeVariety.PINOT_NOIR, Vineyard.DEFAULT, false, 0),
                                        Residue.NONE,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                // SANCERRE_ROUGE / YOUNG_SANCERRE_ROUGE are unreachable: shadowed by the identical
                                // condition (residue == LOW && herb == 64) on the NUITS_SAINT_GEORGES branch above.

                                // gamay
                                new MustData( // beaujolais nouveau
                                        new GrapeData(GrapeVariety.GAMAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        128,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(64).withOakAging(0)
                                ),
                                new MustData( // moulin-à-vent
                                        new GrapeData(GrapeVariety.GAMAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        128,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(32).withOakAging(16)
                                ),
                                new MustData( // young moulin-à-vent
                                        new GrapeData(GrapeVariety.GAMAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        128,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(32).withOakAging(0)
                                ),
                                new MustData( // beaujolais rosé
                                        new GrapeData(GrapeVariety.GAMAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // sorrenberg gamay
                                        new GrapeData(GrapeVariety.GAMAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(32)
                                ),
                                new MustData( // young sorrenberg gamay
                                        new GrapeData(GrapeVariety.GAMAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        64,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),

                                // riesling
                                new MustData( // eiswein
                                        new GrapeData(GrapeVariety.RIESLING, Vineyard.DEFAULT, true, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // riesling elsass
                                        new GrapeData(GrapeVariety.RIESLING, Vineyard.DEFAULT, false, 0),
                                        Residue.NONE,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // controguerra riesling
                                        new GrapeData(GrapeVariety.RIESLING, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                // CLARE_VALLEY_RIESLING is unreachable: shadowed by the identical condition
                                // (!frozen && residue == LOW) on the CONTROGUERRA_RIESLING branch above.

                                // chardonnay
                                new MustData( // young champagne
                                        new GrapeData(GrapeVariety.CHARDONNAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        0,
                                        true,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0).withBottleAging(0)
                                ),
                                new MustData( // champagne
                                        new GrapeData(GrapeVariety.CHARDONNAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        0,
                                        true,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0).withBottleAging(64).withSugarAdded(true)
                                ),
                                new MustData( // chablis
                                        new GrapeData(GrapeVariety.CHARDONNAY, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // vin de paille
                                        new GrapeData(GrapeVariety.CHARDONNAY, Vineyard.DEFAULT, false, 64),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // young russian river valley
                                        new GrapeData(GrapeVariety.CHARDONNAY, Vineyard.DEFAULT, false, 0),
                                        Residue.NONE,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // russian river valley
                                        new GrapeData(GrapeVariety.CHARDONNAY, Vineyard.DEFAULT, false, 0),
                                        Residue.NONE,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(64)
                                ),

                                // rkatsiteli
                                new MustData( // kakhuri qvevri amber
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT, false, 0),
                                        Residue.LOW,
                                        128,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0).withQvevriFermented(true)
                                ),
                                new MustData( // odesa rkatsiteli
                                        new GrapeData(GrapeVariety.RKATSITELI, Vineyard.DEFAULT, false, 0),
                                        Residue.NONE,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                // KARDEMAKHI is unreachable: never returned by classifyRkatsiteli.
                                // TRAKIJA_RKATSITELI is unreachable: shadowed by the identical condition
                                // (residue == NONE && herb == 0 && !qvevriFermented) on the ODESA_RKATSITELI branch above.

                                // malvasia
                                new MustData( // malvasia delle lipari
                                        new GrapeData(GrapeVariety.MALVASIA, Vineyard.DEFAULT, false, 64),
                                        Residue.LOW,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                ),
                                new MustData( // malvazija istarska
                                        new GrapeData(GrapeVariety.MALVASIA, Vineyard.DEFAULT, false, 0),
                                        Residue.NONE,
                                        0,
                                        false,
                                        FermentationData.DEFAULT.withFermentation(64).withCarbonicLevel(0).withOakAging(0)
                                )
                                // MADEIRA_MALMSEY / YOUNG_MADEIRA_MALMSEY are unreachable: never returned by
                                // classifyMalvasia.
                                // MALVASIA_DE_RIOJA is unreachable: shadowed by the identical condition
                                // (passerillage == 0 && residue == NONE) on the MALVAZIJA_ISTARSKA branch above.
                        ));
                        for (MustData mustData : musts) {
                            ItemStack mustBottle = new ItemStack(ModItems.MUST_BOTTLE.get());
                            MustBottleItem.setMustData(mustBottle, mustData);
                            MustBottleItem.setCap(mustBottle, OAK_BUTTON);
                            output.accept(mustBottle);
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
