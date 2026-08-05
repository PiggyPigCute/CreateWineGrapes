package com.piggypig.createwinegrapes.blocks;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.custom.MechanicalDestemmerBlockEntity;
import com.piggypig.createwinegrapes.blocks.custom.MechanicalDestemmerRenderer;
import com.piggypig.createwinegrapes.blocks.custom.MechanicalDestemmerVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntities {

    public static final BlockEntityEntry<MechanicalDestemmerBlockEntity> MECHANICAL_DESTEMMER =
            CreateWineGrapes.REGISTRATE
                    .blockEntity("mechanical_destemmer", MechanicalDestemmerBlockEntity::new)
                    .visual(() -> MechanicalDestemmerVisual::new)
                    .validBlocks(ModBlocks.MECHANICAL_DESTEMMER)
                    .renderer(() -> MechanicalDestemmerRenderer::new)
                    .register();

    public static void register() {}
}