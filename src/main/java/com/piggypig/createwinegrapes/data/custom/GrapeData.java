package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GrapeData(
        GrapeVariety grapeVariety,
        Vineyard vineyard,
        boolean frozen,
        int passerillage
) {
    public static final GrapeData DEFAULT = new GrapeData(
            GrapeVariety.NONE,
            Vineyard.DEFAULT,
            false,
            0
    );

    public static final Codec<GrapeData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeVariety.CODEC.fieldOf("grape_variety").forGetter(GrapeData::grapeVariety),
            Vineyard.CODEC.fieldOf("vineyard").forGetter(GrapeData::vineyard),
            Codec.BOOL.fieldOf("frozen").forGetter(GrapeData::frozen),
            Codec.INT.fieldOf("passerillage").forGetter(GrapeData::passerillage)
    ).apply(i, GrapeData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrapeData> STREAM_CODEC =
            StreamCodec.composite(
                    GrapeVariety.STREAM_CODEC, GrapeData::grapeVariety,
                    Vineyard.STREAM_CODEC, GrapeData::vineyard,
                    ByteBufCodecs.BOOL, GrapeData::frozen,
                    ByteBufCodecs.VAR_INT, GrapeData::passerillage,
                    GrapeData::new
            );

    public GrapeData withPasserillage(int passerillage) {
        return new GrapeData(grapeVariety, vineyard, frozen, passerillage);
    }
}
