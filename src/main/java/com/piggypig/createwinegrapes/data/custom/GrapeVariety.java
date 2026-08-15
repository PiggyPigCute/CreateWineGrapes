package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GrapeVariety implements StringRepresentable {
    NONE("none", 0xFFFFFF),
    CABERNET_SAUVIGNON("cabernet_sauvignon", 0x3A0A1E),
    TEMPRANILLO("tempranillo", 0x6E1423),
    PINOT_NOIR("pinot_noir", 0x8C1F3F),
    GAMAY("gamay", 0xA31545),
    RIESLING("riesling", 0xD7E29A),
    CHARDONNAY("chardonnay", 0xE8D27A),
    RKATSITELI("rkatsiteli", 0xC7D98C),
    MALVASIA("malvasia", 0xE3C77A);

    public static final Codec<GrapeVariety> CODEC = StringRepresentable.fromEnum(GrapeVariety::values);
    public static final StreamCodec<ByteBuf, GrapeVariety> STREAM_CODEC =
            ByteBufCodecs.idMapper(id -> GrapeVariety.values()[id], GrapeVariety::ordinal);


    private final String name;
    private final int color;

    GrapeVariety(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    /**
     * Opaque ARGB (alpha forced to 0xFF) — Minecraft's item tint pipeline reads the alpha
     * channel of this value directly as the render opacity, so an unset alpha renders invisible.
     */
    public int getColor() {
        return 0xFF000000 | color;
    }

    public final boolean isRed() {
        return switch (this) {
            case CABERNET_SAUVIGNON, TEMPRANILLO, PINOT_NOIR, GAMAY -> true;
            case RIESLING, CHARDONNAY, RKATSITELI, MALVASIA, NONE-> false;
        };
    }

}
