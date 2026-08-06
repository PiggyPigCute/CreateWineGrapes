package com.piggypig.createwinegrapes.fluids;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class ModFluids {

    public static final FluidEntry<BaseFlowingFluid.Flowing> MUST =
            CreateWineGrapes.REGISTRATE
                    .fluid("must",
                            ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "block/fluid/must_still"),
                            ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "block/fluid/must_flow"))
                    .lang("Must")
                    .tag(net.minecraft.tags.FluidTags.create(
                            ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "must")))
//                    .fluidProperties(p -> p
//                            .density(1050)
//                            .viscosity(1500)
//                            .temperature(300))
//                    .properties(p -> p.block(net.minecraft.world.level.material.PushReaction.DESTROY)) // exemple, à ajuster
//                    .bucket()
                    .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> ETHANOL =
            CreateWineGrapes.REGISTRATE
                    .fluid("ethanol", stillTex("ethanol"), flowTex("ethanol"))
//                    .bucket()
                    .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> BRANDY =
            CreateWineGrapes.REGISTRATE
                    .fluid("brandy", stillTex("brandy"), flowTex("brandy"))
//                    .bucket()
                    .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> GRAPE_JUICE =
            CreateWineGrapes.REGISTRATE
                    .fluid("grape_juice", stillTex("grape_juice"), flowTex("grape_juice"))
//                    .bucket()
                    .register();

    private static ResourceLocation stillTex(String name) {
        return ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "block/fluid/" + name + "_still");
    }
    private static ResourceLocation flowTex(String name) {
        return ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "block/fluid/" + name + "_flow");
    }

    public static void register() {}
}