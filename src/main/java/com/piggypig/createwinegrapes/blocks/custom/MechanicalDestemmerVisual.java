package com.piggypig.createwinegrapes.blocks.custom;

import com.piggypig.createwinegrapes.blocks.ModPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;

import java.util.function.Consumer;

public class MechanicalDestemmerVisual extends SingleAxisRotatingVisual<MechanicalDestemmerBlockEntity> implements SimpleDynamicVisual {

    private final RotatingInstance pipe;
    private final MechanicalDestemmerBlockEntity mechanicalDestemmer;

    public MechanicalDestemmerVisual(VisualizationContext context, MechanicalDestemmerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.SHAFTLESS_COGWHEEL));
        this.mechanicalDestemmer = blockEntity;

        pipe = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(ModPartialModels.PIPE)).createInstance();
        pipe.setRotationAxis(mechanicalDestemmer.getInputFace().getAxis());

        animate(partialTick);
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        animate(ctx.partialTick());
    }

    private void animate(float pt) {
        float speed = mechanicalDestemmer.getSpeed();

        pipe.setPosition(getVisualPosition())
                .nudge(0, 0.625f, 0)
                .setRotationalSpeed(speed * 2 * RotatingInstance.SPEED_MULTIPLIER)
                .setChanged();
    }

    @Override
    protected void _delete() {
        super._delete();
        pipe.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        consumer.accept(pipe);
    }
}
