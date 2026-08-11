package com.piggypig.createwinegrapes.data.custom;

/**
 * Purely a display/naming classification derived from {@link MustData} - the underlying fluid and
 * item are always Must (see {@link com.piggypig.createwinegrapes.fluids.custom.MustFluid}), since
 * further processing steps (aging, etc.) apply the same regardless of how fermented the batch is.
 */
public enum MustKind {
    MUST,
    WINE,
    THICK_WINE;

    public static MustKind classify(MustData data) {
        if (data.fermentationData().fermentation() < FermentationData.MAX_FERMENTATION) {
            return MUST;
        }
        return switch (data.residue()) {
            case LOW, NONE -> WINE;
            case STEMS, SKINS -> THICK_WINE;
        };
    }
}
