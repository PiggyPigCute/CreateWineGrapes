package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MustData(
        GrapeData grapeData,
        Residue residue,
        int herbaceousness,
        FermentationData fermentationData,
        boolean badWine
) {
    public static final MustData DEFAULT = new MustData(
            GrapeData.DEFAULT,
            Residue.NONE,
            0,
            FermentationData.DEFAULT,
            false
    );

    public static final Codec<MustData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeData.CODEC.fieldOf("grape_data").forGetter(MustData::grapeData),
            Residue.CODEC.fieldOf("residue").forGetter(MustData::residue),
            Codec.INT.fieldOf("herbaceousness").forGetter(MustData::herbaceousness),
            FermentationData.CODEC.fieldOf("fermentation_data").forGetter(MustData::fermentationData),
            Codec.BOOL.fieldOf("bad_wine").forGetter(MustData::badWine)
    ).apply(i, MustData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MustData> STREAM_CODEC =
            StreamCodec.composite(
                    GrapeData.STREAM_CODEC, MustData::grapeData,
                    Residue.STREAM_CODEC, MustData::residue,
                    ByteBufCodecs.VAR_INT, MustData::herbaceousness,
                    FermentationData.STREAM_CODEC, MustData::fermentationData,
                    ByteBufCodecs.BOOL, MustData::badWine,
                    MustData::new
            );

    public MustData withGrapeData(GrapeData grapeData) {
        return new MustData(grapeData, residue, herbaceousness, fermentationData, badWine);
    }

    public MustData withResidue(Residue residue) {
        return new MustData(grapeData, residue, herbaceousness, fermentationData, badWine);
    }

    public MustData withHerbaceousness(int herbaceousness) {
        return new MustData(grapeData, residue, herbaceousness, fermentationData, badWine);
    }

    public MustData withFermentationData(FermentationData fermentationData) {
        return new MustData(grapeData, residue, herbaceousness, fermentationData, badWine);
    }

    public MustData withBadWine(boolean badWine) {
        return new MustData(grapeData, residue, herbaceousness, fermentationData, badWine);
    }
}
