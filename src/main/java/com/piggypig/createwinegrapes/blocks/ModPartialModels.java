package com.piggypig.createwinegrapes.blocks;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class ModPartialModels {

    public static final PartialModel PIPE = block("mechanical_destemmer/pipe");

    public static final PartialModel PRESS_BASIN_SHAFT = block("press_basin/shaft");
    public static final PartialModel PRESS_BASIN_PRESS = block("press_basin/press");

    private static PartialModel block(String path) {
        return PartialModel.of(
                ResourceLocation.fromNamespaceAndPath(
                        CreateWineGrapes.MOD_ID,
                        "block/" + path
                )
        );
    }

    public static void init() {}
}