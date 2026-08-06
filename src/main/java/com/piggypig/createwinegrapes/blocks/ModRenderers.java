package com.piggypig.createwinegrapes.blocks;

import com.piggypig.createwinegrapes.blocks.custom.MechanicalDestemmerRenderer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModRenderers {

    public static void register(IEventBus eventBus) {
        eventBus.addListener(ModRenderers::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.MECHANICAL_DESTEMMER.get(),
                MechanicalDestemmerRenderer::new
        );
    }
}