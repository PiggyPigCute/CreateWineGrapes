package com.piggypig.createwinegrapes.fluids;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.fluids.custom.MustFluid;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class ModFluids {

    public static final FluidEntry<MustFluid> MUST =
            CreateWineGrapes.REGISTRATE
                    .virtualFluid(
                            "must",
                            MustFluid.MustFluidType::new,
                            MustFluid::createSource,
                            MustFluid::createFlowing
                    )
                    .lang("must")
                    .tag(net.minecraft.tags.FluidTags.create(
                            ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "must"))
                    )
                    .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> ETHANOL =
            CreateWineGrapes.REGISTRATE
                    .fluid(
                            "ethanol",
                            fluid("ethanol"),
                            fluid("ethanol")
                    )
                    .register();

    public static final FluidEntry<VirtualFluid> BRANDY =
            CreateWineGrapes.REGISTRATE
                    .virtualFluid(
                            "brandy",
                            fluid("brandy"),
                            fluid("brandy")
                    )
                    .register();

    public static final FluidEntry<VirtualFluid> GRAPE_JUICE =
            CreateWineGrapes.REGISTRATE
                    .virtualFluid(
                            "grape_juice",
                            fluid("grape_juice"),
                            fluid("grape_juice")
                    )
                    .register();


    private static ResourceLocation fluid(String path) {
        return ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "block/fluid/" + path);
    }

    public static void register() {}
}