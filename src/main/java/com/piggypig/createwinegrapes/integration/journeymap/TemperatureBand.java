package com.piggypig.createwinegrapes.integration.journeymap;

/**
 * The three Temperature-noise bands requested for the JourneyMap overlay,
 * matching {@link com.piggypig.createwinegrapes.data.custom.Vineyard#temperature()}.
 * <p>
 * Pure data, no JourneyMap types involved, so it is safe to load whether or not JourneyMap is installed.
 */
enum TemperatureBand {
    COLD(0x3B82F6),
    MILD(0x22C55E),
    HOT(0xEF4444);

    private final int color;

    TemperatureBand(int color) {
        this.color = color;
    }

    static TemperatureBand fromTemperature(float temperature) {
        if (temperature <= -0.45f) return COLD;
        if (temperature <= 0.55f) return MILD;
        return HOT;
    }

    int color() {
        return color;
    }
}
