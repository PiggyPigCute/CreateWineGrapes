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
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

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
            return MustFluid.getMustData(stack).grapeData().grapeVariety().getColor();
        }

        @Override
        public @NotNull String getDescriptionId(@NotNull FluidStack stack) {
            return "fluid.create_wine_grapes." + MustKind.classify(MustFluid.getMustData(stack)).name();
        }

        @Override
        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
            return NO_TINT;
        }

    }
}
