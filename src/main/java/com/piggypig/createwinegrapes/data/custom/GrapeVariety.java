package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GrapeVariety implements StringRepresentable {
    NONE(""),
    CABERNET_SAUVIGNON("cabernet_sauvignon"),
    TEMPRANILLO("tempranillo"),
    PINOT_NOIR("pinot_noir"),
    GAMAY("gamay"),
    RIESLING("riesling"),
    CHARDONNAY("chardonnay"),
    RKATSITELI("rkatsiteli"),
    MALVASIA("malvasia");

    public static final Codec<GrapeVariety> CODEC = StringRepresentable.fromEnum(GrapeVariety::values);
    public static final StreamCodec<ByteBuf, GrapeVariety> STREAM_CODEC =
            ByteBufCodecs.idMapper(id -> GrapeVariety.values()[id], GrapeVariety::ordinal);


    private final String name;

    GrapeVariety(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }



}
