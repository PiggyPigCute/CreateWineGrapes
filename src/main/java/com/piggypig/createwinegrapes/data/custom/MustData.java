package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MustData(
        GrapeVariety grapeVariety,
        float sugarContent,
        float acidity,
        int residueLevel,
        Vineyard vineyard
) {
    public static final MustData DEFAULT = new MustData(
            GrapeVariety.NONE,
            0f,
            0f,
            0,
            Vineyard.DEFAULT
    );

    public static final Codec<MustData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeVariety.CODEC.fieldOf("grape_variety").forGetter(MustData::grapeVariety),
            Codec.FLOAT.fieldOf("sugar_content").forGetter(MustData::sugarContent),
            Codec.FLOAT.fieldOf("acidity").forGetter(MustData::acidity),
            Codec.INT.fieldOf("residue_level").forGetter(MustData::residueLevel),
            Vineyard.CODEC.fieldOf("vineyard").forGetter(MustData::vineyard)
    ).apply(i, MustData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MustData> STREAM_CODEC =
            StreamCodec.composite(
                    GrapeVariety.STREAM_CODEC, MustData::grapeVariety,
                    ByteBufCodecs.FLOAT, MustData::sugarContent,
                    ByteBufCodecs.FLOAT, MustData::acidity,
                    ByteBufCodecs.VAR_INT, MustData::residueLevel,
                    Vineyard.STREAM_CODEC, MustData::vineyard,
                    MustData::new
            );
}