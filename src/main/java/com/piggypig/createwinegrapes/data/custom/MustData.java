package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MustData(
        GrapeVariety grapeVariety,
        int residueLevel, // 0 -> 100
        Vineyard vineyard,
        boolean badWine
) {
    public static final MustData DEFAULT = new MustData(
            GrapeVariety.NONE,
            0,
            Vineyard.DEFAULT,
            true
    );

    public static MustData fromGrapeData(GrapeVariety variety, Vineyard vineyard, int residueLevel) {
        return new MustData(
                variety,
                residueLevel,
                vineyard,
                false
        );
    }

    public static final Codec<MustData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeVariety.CODEC.fieldOf("grape_variety").forGetter(MustData::grapeVariety),
            Codec.INT.fieldOf("residue_level").forGetter(MustData::residueLevel),
            Vineyard.CODEC.fieldOf("vineyard").forGetter(MustData::vineyard),
            Codec.BOOL.fieldOf("bad_wine").forGetter(MustData::badWine)
    ).apply(i, MustData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MustData> STREAM_CODEC =
            StreamCodec.composite(
                    GrapeVariety.STREAM_CODEC, MustData::grapeVariety,
                    ByteBufCodecs.VAR_INT, MustData::residueLevel,
                    Vineyard.STREAM_CODEC, MustData::vineyard,
                    ByteBufCodecs.BOOL, MustData::badWine,
                    MustData::new
            );
}