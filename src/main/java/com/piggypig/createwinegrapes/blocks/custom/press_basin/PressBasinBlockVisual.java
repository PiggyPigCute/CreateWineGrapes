package com.piggypig.createwinegrapes.blocks.custom.press_basin;

import com.piggypig.createwinegrapes.blocks.ModPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;

import java.util.function.Consumer;

public class PressBasinBlockVisual extends SingleAxisRotatingVisual<PressBasinBlockEntity> implements SimpleDynamicVisual {
    private final OrientedInstance press;
    private final PressBasinBlockEntity pressBasin;

    public PressBasinBlockVisual(VisualizationContext context, PressBasinBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(ModPartialModels.PRESS_BASIN_SHAFT));
        this.pressBasin = blockEntity;

        press = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartialModels.PRESS_BASIN_PRESS))
                .createInstance();

        animate();
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        animate();
    }

    private void animate() {
        float height = pressBasin.getPressHeight() * 0.75f;

        press.position(getVisualPosition())
                .translatePosition(0, height, 0)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);

        relight(press);
    }


    @Override
    protected void _delete() {
        super._delete();
        press.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        consumer.accept(press);
    }
}
