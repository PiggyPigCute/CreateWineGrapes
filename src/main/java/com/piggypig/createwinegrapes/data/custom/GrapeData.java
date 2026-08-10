package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class GrapeData {
    private GrapeVariety grapeVariety;
    private Vineyard vineyard;
    private int grapeCount;
    private boolean frozen;
    private int passerillage;

    public GrapeData(
            GrapeVariety grapeVariety,
            Vineyard vineyard,
            int grapeCount,
            boolean frozen,
            int passerillage
    ) {
        this.grapeVariety = grapeVariety;
        this.vineyard = vineyard;
        this.grapeCount = grapeCount;
        this.frozen = frozen;
        this.passerillage = passerillage;
    }

    public GrapeData(GrapeVariety grapeVariety, Vineyard vineyard, int grapeCount, boolean frozen) {
        new GrapeData(grapeVariety, vineyard, grapeCount, frozen, 0);
    }

    public static final GrapeData DEFAULT = new GrapeData(
            GrapeVariety.NONE,
            Vineyard.DEFAULT,
            0,
            false
    );

    public static final Codec<GrapeData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeVariety.CODEC.fieldOf("grape_variety").forGetter(GrapeData::getGrapeVariety),
            Vineyard.CODEC.fieldOf("vineyard").forGetter(GrapeData::getVineyard),
            Codec.INT.fieldOf("grape_count").forGetter(GrapeData::getGrapeCount),
            Codec.BOOL.fieldOf("frozen").forGetter(GrapeData::isFrozen),
            Codec.INT.fieldOf("paserillage").forGetter(GrapeData::getPasserillage)
    ).apply(i, GrapeData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrapeData> STREAM_CODEC =
            StreamCodec.composite(
                    GrapeVariety.STREAM_CODEC, GrapeData::getGrapeVariety,
                    Vineyard.STREAM_CODEC, GrapeData::getVineyard,
                    ByteBufCodecs.VAR_INT, GrapeData::getGrapeCount,
                    ByteBufCodecs.BOOL, GrapeData::isFrozen,
                    ByteBufCodecs.VAR_INT, GrapeData::getPasserillage,
                    GrapeData::new
            );

    public GrapeVariety getGrapeVariety() {
        return grapeVariety;
    }

    public Vineyard getVineyard() {
        return vineyard;
    }

    public int getGrapeCount() {
        return grapeCount;
    }

    public void setGrapeCount(int grapeCount) {
        this.grapeCount = grapeCount;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public int getPasserillage() {
        return passerillage;
    }

    public void setPasserillage(int passerillage) {
        this.passerillage = passerillage;
    }
}
