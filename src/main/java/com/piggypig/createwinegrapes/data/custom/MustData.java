package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MustData(
        GrapeVariety grapeVariety,
        Vineyard vineyard,
        int residueLevel, // 0 -> 100
        boolean badWine
) {
    public static final MustData DEFAULT = new MustData(
            GrapeVariety.NONE,
            Vineyard.DEFAULT,
            0,
            true
    );

    public static MustData fromGrapeData(GrapeVariety variety, Vineyard vineyard, int residueLevel) {
        return new MustData(
                variety,
                vineyard,
                residueLevel,
                false
        );
    }

    public static final Codec<MustData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeVariety.CODEC.fieldOf("grape_variety").forGetter(MustData::grapeVariety),
            Vineyard.CODEC.fieldOf("vineyard").forGetter(MustData::vineyard),
            Codec.INT.fieldOf("residue_level").forGetter(MustData::residueLevel),
            Codec.BOOL.fieldOf("bad_wine").forGetter(MustData::badWine)
    ).apply(i, MustData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MustData> STREAM_CODEC =
            StreamCodec.composite(
                    GrapeVariety.STREAM_CODEC, MustData::grapeVariety,
                    Vineyard.STREAM_CODEC, MustData::vineyard,
                    ByteBufCodecs.VAR_INT, MustData::residueLevel,
                    ByteBufCodecs.BOOL, MustData::badWine,
                    MustData::new
            );
}