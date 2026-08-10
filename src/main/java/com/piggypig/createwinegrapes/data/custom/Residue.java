package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Residue implements StringRepresentable {
    STEMS("stems"),
    SKINS("skins"),
    LOW("low"),
    NONE("none");

    public static final Codec<Residue> CODEC = StringRepresentable.fromEnum(Residue::values);
    public static final StreamCodec<ByteBuf, Residue> STREAM_CODEC =
            ByteBufCodecs.idMapper(id -> Residue.values()[id], Residue::ordinal);

    private final String name;

    Residue(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
