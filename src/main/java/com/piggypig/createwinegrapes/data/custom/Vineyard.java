package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Vineyard(
        float continentalness,
        float temperature,
        float humidity,
        float peakAndValley
) {
    public static final Codec<Vineyard> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.fieldOf("continentalness").forGetter(Vineyard::continentalness),
            Codec.FLOAT.fieldOf("temperature").forGetter(Vineyard::temperature),
            Codec.FLOAT.fieldOf("humidity").forGetter(Vineyard::humidity),
            Codec.FLOAT.fieldOf("peak_and_valley").forGetter(Vineyard::peakAndValley)
    ).apply(i, Vineyard::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Vineyard> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT, Vineyard::continentalness,
                    ByteBufCodecs.FLOAT, Vineyard::temperature,
                    ByteBufCodecs.FLOAT, Vineyard::humidity,
                    ByteBufCodecs.FLOAT, Vineyard::peakAndValley,
                    Vineyard::new
            );
}
