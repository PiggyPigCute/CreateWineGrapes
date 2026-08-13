package com.piggypig.createwinegrapes.data.custom;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * Purely a display/naming classification derived from {@link MustData} - the underlying fluid and
 * item are always Must (see {@link com.piggypig.createwinegrapes.fluids.custom.MustFluid}), since
 * further classifying steps (aging, etc.) apply the same regardless of how fermented the batch is.
 */
public enum MustKind {
    // generics
    MUST("must"),
    BAD_WINE("bad_wine"),
    THICK_WINE("thick_wine"),
    RED_WINE("red_wine"),
    WHITE_WINE("white_wine"),
    ROSE_WINE("rose_wine"),
    ORANGE_WINE("orange_wine"),
    // cabernet sauvignon
    MEDOC("medoc"), YOUNG_MEDOC("young_medoc"),
    NIAGARA_ICEWINE("niagara_icewine"),
    NAPA_VALLEY("napa_valley"), YOUNG_NAPA_VALLEY("young_napa_valley"),
    SUPER_TUSCAN("super_tuscan"), YOUNG_SUPER_TUSCAN("young_super_tuscan"),
    // tempranillo
    PORTO("porto"),
    RIOJA("rioja"), YOUNG_RIOJA("young_rioja"),
    ALENTEJO_ARAGONEZ("alentejo_aragonez"),
    MENDOZA_TEMPRANILLO("mendoza_tempranillo"), YOUNG_MENDOZA_TEMPRANILLO("young_mendoza_tempranillo"),
    // pinot noir
    ROSE_CHAMPAGNE("rose_champagne"), YOUNG_ROSE_CHAMPAGNE("young_rose_champagne"),
    NUITS_SAINT_GEORGES("nuits_saint_georges"), YOUNG_NUITS_SAINT_GEORGES("young_nuits_saint_georges"),
    SANCERRE_ROUGE("sancerre_rouge"), YOUNG_SANCERRE_ROUGE("young_sancerre_rouge"),
    MARSANNAY_ROSE("marsannay_rose"),
    // gamay
    BEAUJOLAIS_NOUVEAU("beaujolais_nouveau"),
    MOULIN_A_VENT("moulin_a_vent"), YOUNG_MOULIN_A_VENT("young_moulin_a_vent"),
    BEAUJOLAIS_ROSE("beaujolais_rose"),
    SORRENBERG_GAMAY("sorrenberg_gamay"), YOUNG_SORRENBERG_GAMAY("young_sorrenberg_gamay"),
    // riesling
    EISWEIN("eiswein"),
    RIESLING_ELSASS("riesling_elsass"),
    CONTROGUERRA_RIESLING("controguerra_riesling"),
    CLARE_VALLEY_RIESLING("clare_valley_riesling"),
    // chardonnay
    CHAMPAGNE("champagne"), YOUNG_CHAMPAGNE("young_champagne"),
    CHABLIS("chablis"),
    VIN_DE_PAILLE("vin_de_paille"),
    RUSSIAN_RIVER_VALLEY("russian_river_valley"), YOUNG_RUSSIAN_RIVER_VALLEY("young_russian_river_valley"),
    // rkatsiteli
    KAKHURI_QVEVRI_AMBER("kakhuri_qvevri_amber"),
    KARDEMAKHI("kardemakhi"),
    ODESA_RKATSITELI("odesa_rkatsiteli"),
    TRAKIJA_RKATSITELI("trakija_rkatsiteli"),
    // malvasia
    MADEIRA_MALMSEY("madeira_malmsey"), YOUNG_MADEIRA_MALMSEY("young_madeira_malmsey"),
    MALVASIA_DELLE_LIPARI("malvasia_delle_lipari"),
    MALVAZIJA_ISTARSKA("malvazija_istarska"),
    MALVASIA_DE_RIOJA("malvasia_de_rioja")
    ;

    private final String name;

    MustKind(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MustKind classify(MustData data) {
        if (data.fermentationData().fermentation() < FermentationData.MAX_FERMENTATION) {
            return MUST;
        }
        // TODO classify hasBrandy()
        return switch (data.residue()) {
            case LOW, NONE -> classifyWine(data);
            case STEMS, SKINS -> THICK_WINE;
        };
    }

    private static MustKind classifyWine(MustData data) {
        // TODO classify badWine
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
