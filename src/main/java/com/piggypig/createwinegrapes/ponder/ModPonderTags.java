package com.piggypig.createwinegrapes.ponder;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.ModBlocks;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class ModPonderTags {

    public static final ResourceLocation VINICULTURE = loc("viniculture");

    private static ResourceLocation loc(String id) {
        return ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, id);
    }

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<ItemLike> itemHelper =
                helper.withKeyFunction(RegisteredObjectsHelper::getKeyOrThrow);

        helper.registerTag(VINICULTURE)
                .addToIndex()
                .item(ModBlocks.VINE.get(), true, false)
                .title("Viniculture")
                .description("Growing grapes on the Vine and processing them into famous Wines")
                .register();

        itemHelper.addToTag(VINICULTURE)
                .add(ModBlocks.VINE.get())
                .add(ModBlocks.MECHANICAL_DESTEMMER.get())
                .add(ModBlocks.PRESS_BASSIN.get());
    }
}
