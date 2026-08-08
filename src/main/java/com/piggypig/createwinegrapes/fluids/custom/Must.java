package com.piggypig.createwinegrapes.fluids.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.MustData;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class Must extends BaseFlowingFluid.Flowing {

    public Must(BaseFlowingFluid.Properties properties) {
        super(properties);
    }

    public static MustData getMustData(FluidStack stack) {
        return stack.getOrDefault(ModDataComponents.MUST_DATA.get(), MustData.DEFAULT);
    }

    public static void setMustData(FluidStack stack, MustData data) {
        stack.set(ModDataComponents.MUST_DATA.get(), data);
    }
}
