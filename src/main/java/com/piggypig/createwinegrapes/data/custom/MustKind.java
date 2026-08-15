package com.piggypig.createwinegrapes.data.custom;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * Purely a display/naming classification derived from {@link MustData} - the underlying fluid and
 * item are always Must (see {@link com.piggypig.createwinegrapes.fluids.custom.MustFluid}), since
 * further classifying steps (aging, etc.) apply the same regardless of how fermented the batch is.
 */
public enum MustKind {
    // during process
    RED_MUST("red_must", 0xFF7A1020, BaseTexture.THICK),
    WHITE_MUST("white_must", 0xFFEEDD88, BaseTexture.MUST),
    ROSE_MUST("rose_must", 0xFFA31545, BaseTexture.MUST),
    ORANGE_MUST("orange_must", 0xFFE3C77A, BaseTexture.THICK),
    THICK_RED_WINE("thick_red_wine", 0xFF5A0A18, BaseTexture.THICK),
    THICK_ORANGE_WINE("thick_orange_wine", 0xFFB86A20, BaseTexture.THICK),
    // generic wines
    RED_WINE("red_wine", 0xFF7B1128, BaseTexture.WINE),
    WHITE_WINE("white_wine", 0xFFF0E6A0, BaseTexture.WINE),
    ROSE_WINE("rose_wine", 0xFFE59BB0, BaseTexture.WINE),
    ORANGE_WINE("orange_wine", 0xFFD98A3D, BaseTexture.WINE),
    // cabernet sauvignon
    MEDOC("medoc", 0xFF6E0E1E, BaseTexture.WINE),
    YOUNG_MEDOC("young_medoc", 0xFF8A1B2E, BaseTexture.WINE),
    NIAGARA_ICEWINE("niagara_icewine", 0xFFE8A33D, BaseTexture.WINE),
    NAPA_VALLEY("napa_valley", 0xFF751026, BaseTexture.WINE),
    YOUNG_NAPA_VALLEY("young_napa_valley", 0xFF8F1D33, BaseTexture.WINE),
    SUPER_TUSCAN("super_tuscan", 0xFF63091A, BaseTexture.WINE),
    YOUNG_SUPER_TUSCAN("young_super_tuscan", 0xFF7E1830, BaseTexture.WINE),
    // tempranillo
    PORTO("porto", 0xFF4A0818, BaseTexture.WINE),
    RIOJA("rioja", 0xFF7A1428, BaseTexture.WINE),
    YOUNG_RIOJA("young_rioja", 0xFF95233C, BaseTexture.WINE),
    ALENTEJO_ARAGONEZ("alentejo_aragonez", 0xFF701226, BaseTexture.WINE),
    MENDOZA_TEMPRANILLO("mendoza_tempranillo", 0xFF7C1730, BaseTexture.WINE),
    YOUNG_MENDOZA_TEMPRANILLO("young_mendoza_tempranillo", 0xFF96253F, BaseTexture.WINE),
    // pinot noir
    ROSE_CHAMPAGNE("rose_champagne", 0xFFE9AFC2, BaseTexture.WINE),
    YOUNG_ROSE_CHAMPAGNE("young_rose_champagne", 0xFFF0C4D3, BaseTexture.WINE),
    NUITS_SAINT_GEORGES("nuits_saint_georges", 0xFF8A1F35, BaseTexture.WINE),
    YOUNG_NUITS_SAINT_GEORGES("young_nuits_saint_georges", 0xFFA33049, BaseTexture.WINE),
    SANCERRE_ROUGE("sancerre_rouge", 0xFF8C2238, BaseTexture.WINE),
    YOUNG_SANCERRE_ROUGE("young_sancerre_rouge", 0xFFA6334C, BaseTexture.WINE),
    MARSANNAY_ROSE("marsannay_rose", 0xFFDB9AB0, BaseTexture.WINE),
    // gamay
    BEAUJOLAIS_NOUVEAU("beaujolais_nouveau", 0xFFA83A52, BaseTexture.WINE),
    MOULIN_A_VENT("moulin_a_vent", 0xFF8F2C42, BaseTexture.WINE),
    YOUNG_MOULIN_A_VENT("young_moulin_a_vent", 0xFFA83D57, BaseTexture.WINE),
    BEAUJOLAIS_ROSE("beaujolais_rose", 0xFFE2A6BA, BaseTexture.WINE),
    SORRENBERG_GAMAY("sorrenberg_gamay", 0xFF9A3349, BaseTexture.WINE),
    YOUNG_SORRENBERG_GAMAY("young_sorrenberg_gamay", 0xFFB3455C, BaseTexture.WINE),
    // riesling
    EISWEIN("eiswein", 0xFFF2D889, BaseTexture.WINE),
    RIESLING_ELSASS("riesling_elsass", 0xFFE9E39E, BaseTexture.WINE),
    CONTROGUERRA_RIESLING("controguerra_riesling", 0xFFE2DB8E, BaseTexture.WINE),
    CLARE_VALLEY_RIESLING("clare_valley_riesling", 0xFFEDE7A8, BaseTexture.WINE),
    // chardonnay
    CHAMPAGNE("champagne", 0xFFF0DE93, BaseTexture.WINE),
    YOUNG_CHAMPAGNE("young_champagne", 0xFFF5E6AC, BaseTexture.WINE),
    CHABLIS("chablis", 0xFFE9DC8E, BaseTexture.WINE),
    VIN_DE_PAILLE("vin_de_paille", 0xFFE0B33D, BaseTexture.WINE),
    RUSSIAN_RIVER_VALLEY("russian_river_valley", 0xFFE7C765, BaseTexture.WINE),
    YOUNG_RUSSIAN_RIVER_VALLEY("young_russian_river_valley", 0xFFEFD583, BaseTexture.WINE),
    // rkatsiteli
    KAKHURI_QVEVRI_AMBER("kakhuri_qvevri_amber", 0xFFC17A2E, BaseTexture.WINE),
    KARDEMAKHI("kardemakhi", 0xFFCE9048, BaseTexture.WINE),
    ODESA_RKATSITELI("odesa_rkatsiteli", 0xFFE0C878, BaseTexture.WINE),
    TRAKIJA_RKATSITELI("trakija_rkatsiteli", 0xFFE3CB7E, BaseTexture.WINE),
    // malvasia
    MADEIRA_MALMSEY("madeira_malmsey", 0xFFA8641F, BaseTexture.WINE),
    YOUNG_MADEIRA_MALMSEY("young_madeira_malmsey", 0xFFC07E32, BaseTexture.WINE),
    MALVASIA_DELLE_LIPARI("malvasia_delle_lipari", 0xFFE7CE86, BaseTexture.WINE),
    MALVAZIJA_ISTARSKA("malvazija_istarska", 0xFFE1D08E, BaseTexture.WINE),
    MALVASIA_DE_RIOJA("malvasia_de_rioja", 0xFFDFCB82, BaseTexture.WINE)
    ;

    private final String name;
    private final int color;
    private final BaseTexture texture;

    MustKind(String name, int color, BaseTexture texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    /**
     * ARGB tint applied to {@link #getTexture()}'s gray base texture. Placeholder values, tweak freely.
     */
    public int getColor() {
        return color;
    }

    /**
     * Which of the gray base liquid textures (see {@code textures/block/must/}) this kind is tinted onto.
     * Placeholder assignments, tweak freely.
     */
    public BaseTexture getTexture() {
        return texture;
    }

    /**
     * The gray base liquid textures that every {@link MustKind}'s {@link #getColor()} is tinted onto.
     */
    public enum BaseTexture {
        MUST("must"),
        THICK("thick"),
        WINE("wine");

        private final ResourceLocation location;

        BaseTexture(String fileName) {
            this.location = ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "block/must/" + fileName);
        }

        public ResourceLocation getLocation() {
            return location;
        }
    }

    public static boolean isFamousWine(MustKind kind) {
        return switch (kind) {
            case RED_MUST, WHITE_MUST, ROSE_MUST, ORANGE_MUST,
                 THICK_RED_WINE, THICK_ORANGE_WINE,
                 RED_WINE, WHITE_WINE, ROSE_WINE, ORANGE_WINE -> false;
            default -> true;
        };
    }

    public static MustKind classify(MustData data) {
        if (data.fermentationData().fermentation() < FermentationData.MAX_FERMENTATION) {
            return classifyMust(data);
        }
        // TODO classify hasBrandy()
        return switch (data.residue()) {
            case LOW, NONE -> classifyWine(data);
            case STEMS, SKINS -> data.grapeData().grapeVariety().isRed()
                    ? THICK_RED_WINE
                    : THICK_ORANGE_WINE;
        };
    }

    private static MustKind classifyMust(MustData data) {
        return switch (data.residue()) {
            case LOW, NONE -> data.grapeData().grapeVariety().isRed()
                    ? ROSE_MUST
                    : WHITE_MUST;
            case STEMS, SKINS -> data.grapeData().grapeVariety().isRed()
                    ? RED_MUST
                    : ORANGE_MUST;
        };
    }

    private static MustKind classifyWine(MustData data) {
        return switch (data.grapeData().grapeVariety()) {
            case NONE -> null;
            case CABERNET_SAUVIGNON -> classifyCabernetSauvignon(data);
            case TEMPRANILLO -> classifyTempranillo(data);
            case PINOT_NOIR -> classifyPinotNoir(data);
            case GAMAY -> classifyGamay(data);
            case RIESLING -> classifyRiesling(data);
            case CHARDONNAY -> classifyChardonnay(data);
            case RKATSITELI -> classifyRkatsiteli(data);
            case MALVASIA -> classifyMalvasia(data);
        };
    }

    private static MustKind classifyMust(int herbaceousness) {
        if (herbaceousness >= 64) {
            return RED_WINE;
        }
        return ROSE_WINE;
    }

    private static MustKind classifyRedGrapeWine(int herbaceousness) {
        if (herbaceousness >= 64) {
            return RED_WINE;
        }
        return ROSE_WINE;
    }

    private static MustKind classifyWhiteGrapeWine(int herbaceousness) {
        if (herbaceousness >= 64) {
            return ORANGE_WINE;
        }
        return WHITE_WINE;
    }

    private static MustKind classifyCabernetSauvignon(MustData data) {
        if (
                data.grapeData().passerillage() == 0
                && data.residue() == Residue.LOW
                && !data.pressedWithStem()
                && !data.fermentationData().qvevriFermented()
                && data.fermentationData().carbonicLevel() == 0
                && !data.fermentationData().heatAging()
                && !data.fermentationData().sugarAdded()
        ) {
            if (data.grapeData().frozen()) {
                if (
                        data.herbaceousness() == 0
                        && data.fermentationData().oakAging() == 0
                ) {
                    return NIAGARA_ICEWINE;
                }
            }
            else if (data.herbaceousness() == 64) {
                if (true) {
                    return data.fermentationData().oakAging() >= 32
                            ? MEDOC
                            : YOUNG_MEDOC;
                }
                else if (true) {
                    if (data.fermentationData().oakAging() == 0) {
                        return YOUNG_NAPA_VALLEY;
                    }
                    else if (data.fermentationData().oakAging() >= 32 && data.fermentationData().newOakAging()) {
                        return NAPA_VALLEY;
                    }
                }
                else if (true) {
                    return data.fermentationData().oakAging() >= 64
                            ? SUPER_TUSCAN
                            : YOUNG_SUPER_TUSCAN;
                }
            }
        }
        return classifyRedGrapeWine(data.herbaceousness());
    }

    private static MustKind classifyTempranillo(MustData data) {
        if (
                !data.grapeData().frozen()
                && data.grapeData().passerillage() == 0
                && data.residue() == Residue.LOW
                && data.fermentationData().carbonicLevel() == 0
                && !data.fermentationData().heatAging()
                && !data.fermentationData().sugarAdded()
        ) {
            if (
                    true
                    && data.herbaceousness() == 64
                    && !data.fermentationData().qvevriFermented()
            ) {
                return data.fermentationData().oakAging() >= 64 && data.fermentationData().bottleAging() >= 64
                        ? RIOJA
                        : YOUNG_RIOJA;
            }
            else if (
                    true
                    && data.herbaceousness() == 128
                    && data.fermentationData().qvevriFermented()
                    && data.fermentationData().oakAging() == 0
            ) {
                return ALENTEJO_ARAGONEZ;
            }
            else if (
                    true
                    && data.herbaceousness() == 64
                    && !data.fermentationData().qvevriFermented()
            ) {
                return data.fermentationData().oakAging() >= 32
                        ? MENDOZA_TEMPRANILLO
                        : YOUNG_MENDOZA_TEMPRANILLO;
            }
        }
        return classifyRedGrapeWine(data.herbaceousness());
    }

    private static MustKind classifyPinotNoir(MustData data) {
        if (
                !data.grapeData().frozen()
                && data.grapeData().passerillage() == 0
                && !data.fermentationData().qvevriFermented()
                && data.fermentationData().carbonicLevel() == 0
                && !data.fermentationData().heatAging()
        ) {
            if (
                    true
                    && data.residue() == Residue.LOW
                    && data.herbaceousness() == 0
                    && data.pressedWithStem()
                    && data.fermentationData().oakAging() == 0
            ) {
                if (data.fermentationData().bottleAging() == 0) {
                    return YOUNG_ROSE_CHAMPAGNE;
                }
                else if (data.fermentationData().sugarAdded() && data.fermentationData().fermentation() == 64) {
                    return ROSE_CHAMPAGNE;
                }
            }
            else if (
                    true
                    && data.residue() == Residue.LOW
                    && data.herbaceousness() == 64
            ) {
                return data.fermentationData().oakAging() >= 32
                        ? NUITS_SAINT_GEORGES
                        : YOUNG_NUITS_SAINT_GEORGES;
            }
            else if (
                    true
                    && data.residue() == Residue.LOW
                    && data.herbaceousness() == 64
            ) {
                return data.fermentationData().oakAging() >= 16
                        ? SANCERRE_ROUGE
                        : YOUNG_SANCERRE_ROUGE;
            }
            else if (
                    true
                    && data.residue() == Residue.NONE
                    && data.herbaceousness() == 0
                    && !data.pressedWithStem()
                    && data.fermentationData().oakAging() == 0
            ) {
                return MARSANNAY_ROSE;
            }
        }
        return classifyRedGrapeWine(data.herbaceousness());
    }

    private static MustKind classifyGamay(MustData data) {
        if (
                !data.grapeData().frozen()
                && data.grapeData().passerillage() == 0
                && data.residue() == Residue.LOW
                && !data.pressedWithStem()
                && !data.fermentationData().qvevriFermented()
                && !data.fermentationData().heatAging()
                && !data.fermentationData().sugarAdded()
        ) {
            if (
                    true
                    && data.herbaceousness() == 128
                    && data.fermentationData().carbonicLevel() == 64
                    && data.fermentationData().oakAging() == 0
            ) {
                return BEAUJOLAIS_NOUVEAU;
            }
            else if (
                    true
                    && data.herbaceousness() == 128
                    && data.fermentationData().carbonicLevel() == 32
            ) {
                return data.fermentationData().oakAging() >= 16
                        ? MOULIN_A_VENT
                        : YOUNG_MOULIN_A_VENT;
            }
            else if (
                    true
                    && data.herbaceousness() == 0
                    && data.fermentationData().carbonicLevel() == 0
                    && data.fermentationData().oakAging() == 0
            ) {
                return BEAUJOLAIS_ROSE;
            }
            else if (
                    true
                    && data.herbaceousness() == 64
                    && data.fermentationData().carbonicLevel() == 0
            ) {
                return data.fermentationData().oakAging() >= 32
                        ? SORRENBERG_GAMAY
                        : YOUNG_SORRENBERG_GAMAY;
            }
        }
        return classifyRedGrapeWine(data.herbaceousness());
    }

    private static MustKind classifyRiesling(MustData data) {
        if (
                data.grapeData().passerillage() == 0
                && data.herbaceousness() == 0
                && !data.pressedWithStem()
                && !data.fermentationData().qvevriFermented()
                && data.fermentationData().carbonicLevel() == 0
                && data.fermentationData().oakAging() == 0
                && !data.fermentationData().heatAging()
                && !data.fermentationData().sugarAdded()
        ) {
            if (
                    true
                    && data.grapeData().frozen()
                    && data.residue() == Residue.LOW
            ) {
                return EISWEIN;
            }
            else if (
                    true
                    && !data.grapeData().frozen()
                    && data.residue() == Residue.NONE
            ) {
                return RIESLING_ELSASS;
            }
            else if (
                    true
                    && !data.grapeData().frozen()
                    && data.residue() == Residue.LOW
            ) {
                return CONTROGUERRA_RIESLING;
            }
            else if (
                    true
                    && !data.grapeData().frozen()
                    && data.residue() == Residue.LOW
            ) {
                return CLARE_VALLEY_RIESLING;
            }
        }
        return classifyWhiteGrapeWine(data.herbaceousness());
    }

    private static MustKind classifyChardonnay(MustData data) {
        if (
                !data.grapeData().frozen()
                && data.herbaceousness() == 0
                && !data.fermentationData().qvevriFermented()
                && data.fermentationData().carbonicLevel() == 0
                && !data.fermentationData().heatAging()
        ) {
            if (
                    true
                    && data.grapeData().passerillage() == 0
                    && data.residue() == Residue.LOW
                    && data.pressedWithStem()
                    && data.fermentationData().oakAging() == 0
            ) {
                if (data.fermentationData().bottleAging() == 0) {
                    return YOUNG_CHAMPAGNE;
                }
                else if (data.fermentationData().sugarAdded() && data.fermentationData().fermentation() == 64) {
                    return CHAMPAGNE;
                }
            }
            else if (
                    true
                    && data.grapeData().passerillage() == 0
                    && data.residue() == Residue.LOW
                    && !data.pressedWithStem()
                    && data.fermentationData().oakAging() == 0
                    && !data.fermentationData().sugarAdded()
            ) {
                return CHABLIS;
            }
            else if (
                    true
                    && data.grapeData().passerillage() == 64
                    && data.residue() == Residue.LOW
                    && !data.pressedWithStem()
                    && data.fermentationData().oakAging() == 0
                    && !data.fermentationData().sugarAdded()
            ) {
                return VIN_DE_PAILLE;
            }
            else if (
                    true
                    && data.grapeData().passerillage() == 0
                    && data.residue() == Residue.NONE
                    && !data.pressedWithStem()
                    && !data.fermentationData().sugarAdded()
            ) {
                return data.fermentationData().oakAging() >= 64
                        ? RUSSIAN_RIVER_VALLEY
                        : YOUNG_RUSSIAN_RIVER_VALLEY;
            }
        }
        return classifyWhiteGrapeWine(data.herbaceousness());
    }

    private static MustKind classifyRkatsiteli(MustData data) {
        if (
                !data.grapeData().frozen()
                && data.grapeData().passerillage() == 0
                && !data.pressedWithStem()
                && data.fermentationData().carbonicLevel() == 0
                && data.fermentationData().oakAging() == 0
                && !data.fermentationData().heatAging()
                && !data.fermentationData().sugarAdded()
        ) {
            if (
                    true
                    && data.residue() == Residue.LOW
                    && data.herbaceousness() == 128
                    && data.fermentationData().qvevriFermented()
            ) {
                return KAKHURI_QVEVRI_AMBER;
            }
            else if (
                    true
                    && data.residue() == Residue.NONE
                    && data.herbaceousness() == 0
                    && !data.fermentationData().qvevriFermented()
            ) {
                return ODESA_RKATSITELI;
            }
            else if (
                    true
                    && data.residue() == Residue.NONE
                    && data.herbaceousness() == 0
                    && !data.fermentationData().qvevriFermented()
            ) {
                return TRAKIJA_RKATSITELI;
            }
        }
        return classifyWhiteGrapeWine(data.herbaceousness());
    }

    private static MustKind classifyMalvasia(MustData data) {
        if (
                !data.grapeData().frozen()
                && data.herbaceousness() == 0
                && !data.pressedWithStem()
                && !data.fermentationData().qvevriFermented()
                && data.fermentationData().carbonicLevel() == 0
                && data.fermentationData().oakAging() == 0
                && !data.fermentationData().heatAging()
                && !data.fermentationData().sugarAdded()
        ) {
            if (
                    true
                    && data.grapeData().passerillage() == 64
                    && data.residue() == Residue.LOW
            ) {
                return MALVASIA_DELLE_LIPARI;
            }
            else if (
                    true
                    && data.grapeData().passerillage() == 0
                    && data.residue() == Residue.NONE
            ) {
                return MALVAZIJA_ISTARSKA;
            }
            else if (
                    true
                    && data.grapeData().passerillage() == 0
                    && data.residue() == Residue.NONE
            ) {
                return MALVASIA_DE_RIOJA;
            }
        }
        return classifyWhiteGrapeWine(data.herbaceousness());
    }
}
