package com.piggypig.createwinegrapes.fluids;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.fluids.custom.Must;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class ModFluids {

    public static final FluidEntry<Must> MUST =
            CreateWineGrapes.REGISTRATE
                    .fluid(
                            "must",
                            fluid("red_wine"),
                            fluid("red_wine"),
                            Must::new
                    )
                    .lang("must")
                    .tag(net.minecraft.tags.FluidTags.create(
                            ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "must"))
                    )
                    .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> ETHANOL =
            CreateWineGrapes.REGISTRATE
                    .fluid("ethanol", fluid("ethanol"), fluid("ethanol"))
                    .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> BRANDY =
            CreateWineGrapes.REGISTRATE
                    .fluid("brandy", fluid("brandy"), fluid("brandy"))
                    .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> GRAPE_JUICE =
            CreateWineGrapes.REGISTRATE
                    .fluid("grape_juice", fluid("grape_juice"), fluid("grape_juice"))
                    .register();


    private static ResourceLocation fluid(String path) {
        return ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "block/fluid/" + path);
    }

    public static void register() {}
}