package com.piggypig.createwinegrapes.blocks.custom.mechanicalDestemmer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MechanicalDestemmerRenderer extends KineticBlockEntityRenderer<MechanicalDestemmerBlockEntity> {

    public MechanicalDestemmerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull MechanicalDestemmerBlockEntity be) {
        return true;
    }

    @Override
    protected void renderSafe(MechanicalDestemmerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        if (VisualizationManager.supportsVisualization(be.getLevel()))  {
            return;
        }

        BlockState blockState = be.getBlockState();

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        SuperByteBuffer superBuffer = CachedBuffers.partial(AllPartialModels.SHAFTLESS_COGWHEEL, blockState);
        standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);

    }

//    @Override
//    protected void renderSafe(MechanicalDestemmerBlockEntity be, BlockState state, float partialTicks,
//                              PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
//
//        if (be.getSpeed() == 0) return;
//
//        // Angle cinétique standard, autour de l'axe de rotation du shaft (Y)
//        float angle = getAngleForBe(be, be.getBlockPos(), Direction.Axis.Y);
//
//        SuperByteBuffer cogwheel = CachedBufferer.partial(ModPartialModels.COGWHEEL_GEARLESS, state);
//        cogwheel.rotateCentered(Direction.UP, angle * Mth.DEG_TO_RAD)
//                .light(light)
//                .renderInto(ms, buffer.getBuffer(net.minecraft.client.renderer.RenderType.cutoutMipped()));
//
//        // Le pipe : même vitesse/angle, mais axe différent (horizontal, selon l'orientation du bloc)
//        Direction.Axis pipeAxis = state.getValue(MechanicalDestemmer.HORIZONTAL_AXIS);
//        Direction pipeDirection = pipeAxis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
//
//        SuperByteBuffer pipe = CachedBufferer.partial(ModPartialModels.PIPE, state);
//        pipe.rotateCentered(pipeDirection, angle * Mth.DEG_TO_RAD)
//                .light(light)
//                .renderInto(ms, buffer.getBuffer(net.minecraft.client.renderer.RenderType.cutoutMipped()));
//    }
}