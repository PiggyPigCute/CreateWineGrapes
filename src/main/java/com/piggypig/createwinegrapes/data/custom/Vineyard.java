package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.NoiseRouterData;

public record Vineyard(
        float continentalness,
        float temperature,
        float humidity,
        float peakAndValley
) {
    public static final Vineyard DEFAULT = new Vineyard(0f, 0f, 0f, 0f);

    public static Vineyard sample(ServerLevel level, BlockPos pos) {
        Climate.Sampler sampler = level.getChunkSource().randomState().sampler();
        Climate.TargetPoint point = sampler.sample(
                QuartPos.fromBlock(pos.getX()),
                QuartPos.fromBlock(pos.getY()),
                QuartPos.fromBlock(pos.getZ())
        );
        float weirdness = Climate.unquantizeCoord(point.weirdness());
        return new Vineyard(
                Climate.unquantizeCoord(point.continentalness()),
                Climate.unquantizeCoord(point.temperature()),
                Climate.unquantizeCoord(point.humidity()),
                NoiseRouterData.peaksAndValleys(weirdness)
        );
    }

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
