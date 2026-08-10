package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GrapeData (
    GrapeVariety grapeVariety,
    Vineyard vineyard,
    int grapeCount,
    int passerillage
) {
    public static final GrapeData DEFAULT = new GrapeData(
            GrapeVariety.NONE,
            Vineyard.DEFAULT,
            0,
            0
    );

    public static final Codec<GrapeData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeVariety.CODEC.fieldOf("grape_variety").forGetter(GrapeData::grapeVariety),
            Vineyard.CODEC.fieldOf("vineyard").forGetter(GrapeData::vineyard),
            Codec.INT.fieldOf("grape_count").forGetter(GrapeData::grapeCount),
            Codec.INT.fieldOf("paserillage").forGetter(GrapeData::passerillage)
    ).apply(i, GrapeData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrapeData> STREAM_CODEC =
            StreamCodec.composite(
                    GrapeVariety.STREAM_CODEC, GrapeData::grapeVariety,
                    Vineyard.STREAM_CODEC, GrapeData::vineyard,
                    ByteBufCodecs.VAR_INT, GrapeData::grapeCount,
                    ByteBufCodecs.VAR_INT, GrapeData::passerillage,
                    GrapeData::new
            );
}
