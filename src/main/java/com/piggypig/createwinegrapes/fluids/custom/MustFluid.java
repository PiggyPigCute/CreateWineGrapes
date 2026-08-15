package com.piggypig.createwinegrapes.fluids.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.MustKind;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MustFluid extends VirtualFluid {

    public MustFluid(Properties properties, boolean source) {
        super(properties, source);
    }

    public static MustFluid createSource(Properties properties) {
        return new MustFluid(properties, true);
    }

    public static MustFluid createFlowing(Properties properties) {
        return new MustFluid(properties, false);
    }

    public static MustData getMustData(FluidStack stack) {
        return stack.getOrDefault(ModDataComponents.MUST_DATA.get(), MustData.DEFAULT);
    }

    public static void setMustData(FluidStack stack, MustData data) {
        stack.set(ModDataComponents.MUST_DATA.get(), data);
    }

    public static class MustFluidType extends AllFluids.TintedFluidType {

        public MustFluidType(net.neoforged.neoforge.fluids.FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        public int getTintColor(FluidStack stack) {
            MustKind kind = MustKind.classify(MustFluid.getMustData(stack));
            return kind == null ? NO_TINT : kind.getColor();
        }

        @Override
        public @NotNull String getDescriptionId(@NotNull FluidStack stack) {
            return "fluid.create_wine_grapes.must." + MustKind.classify(MustFluid.getMustData(stack)).getName();
        }

        @Override
        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
            return NO_TINT;
        }

        private static ResourceLocation textureFor(FluidStack stack) {
            MustKind kind = MustKind.classify(MustFluid.getMustData(stack));
            return (kind == null ? MustKind.BaseTexture.MUST : kind.getTexture()).getLocation();
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {

                @Override
                public ResourceLocation getStillTexture() {
                    return MustKind.BaseTexture.MUST.getLocation();
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return MustKind.BaseTexture.MUST.getLocation();
                }

                @Override
                public ResourceLocation getStillTexture(FluidStack stack) {
                    return textureFor(stack);
                }

                @Override
                public ResourceLocation getFlowingTexture(FluidStack stack) {
                    return textureFor(stack);
                }

                @Override
                public int getTintColor(FluidStack stack) {
                    return MustFluidType.this.getTintColor(stack);
                }

                @Override
                public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                    return MustFluidType.this.getTintColor(state, getter, pos);
                }

            });
        }

    }
}
