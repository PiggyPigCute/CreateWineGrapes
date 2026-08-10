package com.piggypig.createwinegrapes.blocks;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.custom.mechanicalDestemmer.MechanicalDestemmerBlockEntity;
import com.piggypig.createwinegrapes.blocks.custom.mechanicalDestemmer.MechanicalDestemmerRenderer;
import com.piggypig.createwinegrapes.blocks.custom.mechanicalDestemmer.MechanicalDestemmerVisual;
import com.piggypig.createwinegrapes.blocks.custom.vat.VatBlockEntity;
import com.piggypig.createwinegrapes.blocks.custom.press_basin.PressBasinBlockEntity;
import com.piggypig.createwinegrapes.blocks.custom.mechanical_destemmer.MechanicalDestemmerBlockEntity;
import com.piggypig.createwinegrapes.blocks.custom.mechanical_destemmer.MechanicalDestemmerRenderer;
import com.piggypig.createwinegrapes.blocks.custom.mechanical_destemmer.MechanicalDestemmerVisual;
import com.piggypig.createwinegrapes.blocks.custom.press_basin.PressBasinBlockVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.neoforged.bus.api.IEventBus;

public class ModBlockEntities {

    public static final BlockEntityEntry<MechanicalDestemmerBlockEntity> MECHANICAL_DESTEMMER =
            CreateWineGrapes.REGISTRATE
                    .blockEntity("mechanical_destemmer", MechanicalDestemmerBlockEntity::new)
                    .visual(() -> MechanicalDestemmerVisual::new)
                    .validBlocks(ModBlocks.MECHANICAL_DESTEMMER)
                    .renderer(() -> MechanicalDestemmerRenderer::new)
                    .register();

    public static final BlockEntityEntry<PressBasinBlockEntity> PRESS_BASIN =
            CreateWineGrapes.REGISTRATE
                    .blockEntity("press_basin", PressBasinBlockEntity::new)
                    .visual(() -> PressBasinBlockVisual::new)
                    .validBlocks(ModBlocks.PRESS_BASIN)
                    .register();

    public static final BlockEntityEntry<VatBlockEntity> VAT =
            CreateWineGrapes.REGISTRATE
                    .blockEntity("vat", VatBlockEntity::new)
                    .validBlocks(ModBlocks.VAT)
                    .register();

    public static void register(IEventBus eventBus) {
        eventBus.addListener(MechanicalDestemmerBlockEntity::registerCapabilities);
        eventBus.addListener(PressBasinBlockEntity::registerCapabilities);
        eventBus.addListener(VatBlockEntity::registerCapabilities);
    }
}