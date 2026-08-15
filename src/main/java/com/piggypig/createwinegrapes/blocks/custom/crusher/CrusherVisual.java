package com.piggypig.createwinegrapes.blocks.custom.crusher;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visual.TickableVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;

public class CrusherVisual extends SingleAxisRotatingVisual<CrusherBlockEntity> implements SimpleDynamicVisual {

    public CrusherVisual(VisualizationContext context, CrusherBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.SHAFT));
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {

    }

    @Override
    public Plan<DynamicVisual.Context> planFrame() {
        return SimpleDynamicVisual.super.planFrame();
    }

    @Override
    public Plan<TickableVisual.Context> planTick() {
        return super.planTick();
    }
}
