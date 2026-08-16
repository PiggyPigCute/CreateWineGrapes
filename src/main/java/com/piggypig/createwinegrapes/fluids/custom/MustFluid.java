package com.piggypig.createwinegrapes.fluids.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.FermentationData;
import com.piggypig.createwinegrapes.data.custom.GrapeData;
import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.MustKind;
import com.piggypig.createwinegrapes.data.custom.Vineyard;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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

    /**
     * Appends goggle-overlay lines describing the {@link MustData} carried by {@code fluidStack}.
     * Nothing is shown unless the player is sneaking, in which case every field is listed.
     *
     * @return whether any line was added
     */
    public static boolean addGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, FluidStack fluidStack, int indent) {
        return addGoggleTooltip(tooltip, isPlayerSneaking, false, fluidStack, indent);
    }

    /**
     * Same as {@link #addGoggleTooltip(List, boolean, FluidStack, int)}, but the overall
     * fermentation progress is shown even without sneaking - used for the Vat, where fermentation
     * is the one piece of information relevant enough to always surface.
     *
     * @return whether any line was added
     */
    public static boolean addVatGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, FluidStack fluidStack, int indent) {
        return addGoggleTooltip(tooltip, isPlayerSneaking, true, fluidStack, indent);
    }

    private static boolean addGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, boolean alwaysShowFermentation, FluidStack fluidStack, int indent) {
        if (!(fluidStack.getFluid() instanceof MustFluid))
            return false;

        MustData data = getMustData(fluidStack);
        GrapeData grapeData = data.grapeData();
        if (grapeData.grapeVariety() == GrapeVariety.NONE)
            return false;

        FermentationData fermentationData = data.fermentationData();

        if (!isPlayerSneaking) {
            if (!alwaysShowFermentation)
                return false;
            detailLine(tooltip, indent, "fermentation", fermentationValue(fermentationData.fermentation()));
            return true;
        }

        detailLine(
                tooltip,
                indent,
                "grape_variety",
                Component.translatable(
                        "create_wine_grapes.grape_variety." + grapeData.grapeVariety().getSerializedName()
                ).withStyle(ChatFormatting.LIGHT_PURPLE)
        );
        detailLine(tooltip, indent, "fermentation", fermentationValue(fermentationData.fermentation()));

        detailLine(
                tooltip,
                indent,
                "residue",
                Component.translatable(
                        "create_wine_grapes.residue." + data.residue().getSerializedName()
                ).withStyle(ChatFormatting.GOLD)
        );
        intDetailLine(tooltip, indent, "herbaceousness", data.herbaceousness());
        boolDetailLine(tooltip, indent, "pressed_with_stem", data.pressedWithStem());
        boolDetailLine(tooltip, indent, "frozen", grapeData.frozen());
        sentinelIntDetailLine(tooltip, indent, "passerillage", grapeData.passerillage());
//        vineyardDetailLines(tooltip, indent, grapeData.vineyard());
        boolDetailLine(tooltip, indent, "qvevri_fermented", fermentationData.qvevriFermented());
        sentinelIntDetailLine(tooltip, indent, "carbonic_level", fermentationData.carbonicLevel());
        boolDetailLine(tooltip, indent, "has_brandy", fermentationData.hasBrandy());
        sentinelIntDetailLine(tooltip, indent, "oak_aging", fermentationData.oakAging());
        boolDetailLine(tooltip, indent, "new_oak_aging", fermentationData.newOakAging());
        boolDetailLine(tooltip, indent, "heat_aging", fermentationData.heatAging());
        boolDetailLine(tooltip, indent, "sugar_added", fermentationData.sugarAdded());
        sentinelIntDetailLine(tooltip, indent, "bottle_aging", fermentationData.bottleAging());

        return true;
    }

    private static final int FERMENTATION_BAR_LENGTH = 20;

    /**
     * Vertical-bar gauge in the same style as Create's boiler heat/water/size goggle bars
     * (a run of colored {@code |} characters), followed by the raw fraction.
     */
    private static Component fermentationValue(int fermentation) {
        if (fermentation == 0) {
            return Component.translatable("create_wine_grapes.goggles.fermentation.none").withStyle(ChatFormatting.DARK_GRAY);
        }
        if (fermentation == 64) {
            return Component.translatable("create_wine_grapes.goggles.fermentation.max").withStyle(ChatFormatting.GREEN);
        }
        int filled = Mth.clamp(
                Math.round(FERMENTATION_BAR_LENGTH * (fermentation / (float) FermentationData.MAX_FERMENTATION)),
                0, FERMENTATION_BAR_LENGTH);

        return Component.literal("|".repeat(filled)).withStyle(ChatFormatting.GREEN)
                .append(Component.literal("|".repeat(FERMENTATION_BAR_LENGTH - filled)).withStyle(ChatFormatting.DARK_GRAY));
    }

    private static void detailLine(List<Component> tooltip, int indent, String labelKey, Component value) {
        CreateLang.text("")
                .add(Component.translatable("create_wine_grapes.goggles." + labelKey).withStyle(ChatFormatting.GRAY))
                .add(CreateLang.text(": "))
                .add(value)
                .forGoggles(tooltip, indent);
    }

    private static void intDetailLine(List<Component> tooltip, int indent, String labelKey, int value) {
        detailLine(tooltip, indent, labelKey, Component.literal(String.valueOf(value)).withStyle(ChatFormatting.AQUA));
    }

    /**
     * -1 is the {@link FermentationData#DEFAULT} sentinel for "not decided yet", as opposed
     * to 0 which is an explicit choice (e.g. no oak aging); rendered as a dash instead of "-1".
     */
    private static void sentinelIntDetailLine(List<Component> tooltip, int indent, String labelKey, int value) {
        if (value > 0) {
            intDetailLine(tooltip, indent, labelKey, value);
        }
    }

    private static void boolDetailLine(List<Component> tooltip, int indent, String labelKey, boolean value) {
        if (value) {
            CreateLang.text("")
                    .add(Component.translatable("create_wine_grapes.goggles." + labelKey).withStyle(ChatFormatting.GREEN))
                    .forGoggles(tooltip, indent);
        }
    }

    private static void vineyardDetailLines(List<Component> tooltip, int indent, Vineyard vineyard) {
        CreateLang.translate("create_wine_grapes.goggles.vineyard")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, indent);
        floatDetailLine(tooltip, indent + 1, "continentalness", vineyard.continentalness());
        floatDetailLine(tooltip, indent + 1, "temperature", vineyard.temperature());
        floatDetailLine(tooltip, indent + 1, "humidity", vineyard.humidity());
        floatDetailLine(tooltip, indent + 1, "peak_and_valley", vineyard.peakAndValley());
    }

    private static void floatDetailLine(List<Component> tooltip, int indent, String labelKey, float value) {
        detailLine(tooltip, indent, labelKey, Component.literal(String.format("%.2f", value)).withStyle(ChatFormatting.AQUA));
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
