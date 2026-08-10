package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MustData(
        GrapeVariety grapeVariety,
        Vineyard vineyard,
        Residue residue,
        int herbaceousness,
        int fermentation,
        boolean badWine
) {
    public static final MustData DEFAULT = new MustData(
            GrapeVariety.NONE,
            Vineyard.DEFAULT,
            Residue.NONE,
            0,
            0,
            true
    );

    public static final Codec<MustData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeVariety.CODEC.fieldOf("grape_variety").forGetter(MustData::grapeVariety),
            Vineyard.CODEC.fieldOf("vineyard").forGetter(MustData::vineyard),
            Residue.CODEC.fieldOf("residue").forGetter(MustData::residue),
            Codec.INT.fieldOf("herbaceousness").forGetter(MustData::herbaceousness),
            Codec.INT.fieldOf("fermentation").forGetter(MustData::fermentation),
            Codec.BOOL.fieldOf("bad_wine").forGetter(MustData::badWine)
    ).apply(i, MustData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MustData> STREAM_CODEC =
            StreamCodec.composite(
                    GrapeVariety.STREAM_CODEC, MustData::grapeVariety,
                    Vineyard.STREAM_CODEC, MustData::vineyard,
                    Residue.STREAM_CODEC, MustData::residue,
                    ByteBufCodecs.VAR_INT, MustData::herbaceousness,
                    ByteBufCodecs.VAR_INT, MustData::fermentation,
                    ByteBufCodecs.BOOL, MustData::badWine,
                    MustData::new
            );
}