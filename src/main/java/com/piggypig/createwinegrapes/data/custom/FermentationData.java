package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record FermentationData(
        int fermentation,
        boolean qvevriFermented,
        int carbonicLevel,
        boolean hasBrandy,
        int oakAging,
        boolean newOakAging,
        boolean heatAging,
        boolean sugarAdded,
        int bottleAging
) {
    public static final int MAX_FERMENTATION = 64;

    public static final FermentationData DEFAULT = new FermentationData(
            0,
            false,
            -1,
            false,
            -1,
            false,
            false,
            false,
            -1
    );

    public static final Codec<FermentationData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("fermentation").forGetter(FermentationData::fermentation),
            Codec.BOOL.fieldOf("qvevri_fermented").forGetter(FermentationData::qvevriFermented),
            Codec.INT.fieldOf("carbonic_level").forGetter(FermentationData::carbonicLevel),
            Codec.BOOL.fieldOf("has_brandy").forGetter(FermentationData::hasBrandy),
            Codec.INT.fieldOf("oak_aging").forGetter(FermentationData::oakAging),
            Codec.BOOL.fieldOf("new_oak_aging").forGetter(FermentationData::newOakAging),
            Codec.BOOL.fieldOf("heat_aging").forGetter(FermentationData::heatAging),
            Codec.BOOL.fieldOf("sugar_added").forGetter(FermentationData::sugarAdded),
            Codec.INT.fieldOf("bottle_aging").forGetter(FermentationData::bottleAging)
    ).apply(i, FermentationData::new));

    // StreamCodec.composite() only supports up to 6 fields, but FermentationData has 10,
    // so the stream codec is written manually instead.
    public static final StreamCodec<RegistryFriendlyByteBuf, FermentationData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        buf.writeVarInt(data.fermentation());
                        buf.writeBoolean(data.qvevriFermented());
                        buf.writeVarInt(data.carbonicLevel());
                        buf.writeBoolean(data.hasBrandy());
                        buf.writeVarInt(data.oakAging());
                        buf.writeBoolean(data.newOakAging());
                        buf.writeBoolean(data.heatAging());
                        buf.writeBoolean(data.sugarAdded());
                        buf.writeVarInt(data.bottleAging());
                    },
                    buf -> new FermentationData(
                            buf.readVarInt(),
                            buf.readBoolean(),
                            buf.readVarInt(),
                            buf.readBoolean(),
                            buf.readVarInt(),
                            buf.readBoolean(),
                            buf.readBoolean(),
                            buf.readBoolean(),
                            buf.readVarInt()
                    )
            );

    public FermentationData withFermentation(int fermentation) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }

    public FermentationData withQvevriFermented(boolean qvevriFermented) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }

    public FermentationData withCarbonicLevel(int carbonicLevel) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }

    public FermentationData withHasBrandy(boolean hasBrandy) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }

    public FermentationData withOakAging(int oakAging) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }

    public FermentationData withNewOakAging(boolean newOakAging) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }

    public FermentationData withHeatAging(boolean heatAging) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }

    public FermentationData withSugarAdded(boolean sugarAdded) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }

    public FermentationData withBottleAging(int bottleAging) {
        return new FermentationData(fermentation, qvevriFermented, carbonicLevel, hasBrandy, oakAging, newOakAging, heatAging, sugarAdded, bottleAging);
    }
}
