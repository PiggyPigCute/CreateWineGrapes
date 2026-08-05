package com.piggypig.createwinegrapes.blocks.custom;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;

public class MechanicalDestemmerVisual extends SingleAxisRotatingVisual<MechanicalDestemmerBlockEntity> implements SimpleDynamicVisual {

    public MechanicalDestemmerVisual(VisualizationContext context, MechanicalDestemmerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.SHAFTLESS_COGWHEEL));
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {

    }
}
