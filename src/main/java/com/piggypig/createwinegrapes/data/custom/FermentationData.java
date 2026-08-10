package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class FermentationData {
    private int fermentation;
    private boolean qvevriFermented;
    private int fermentationLight;
    private int carbonicLevel;
    private boolean hasBrandy;
    private int oakAging;
    private boolean newOakAging;
    private boolean heatAging;
    private boolean sugarAdded; // TODO à voir (champagne)
    private int bottleAging;

    public FermentationData (
            int fermentation,
            boolean qvevriFermented,
            int fermentationLight,
            int carbonicLevel,
            boolean hasBrandy,
            int oakAging,
            boolean newOakAging,
            boolean heatAging,
            boolean sugarAdded,
            int bottleAging
    ) {
        this.fermentation = fermentation;
        this.qvevriFermented = qvevriFermented;
        this.fermentationLight = fermentationLight;
        this.carbonicLevel = carbonicLevel;
        this.hasBrandy = hasBrandy;
        this.oakAging = oakAging;
        this.newOakAging = newOakAging;
        this.heatAging = heatAging;
        this.sugarAdded = sugarAdded;
        this.bottleAging = bottleAging;
    }

    public static FermentationData DEFAULT = new FermentationData(
            0,
            false,
            -1,
            -1,
            false,
            -1,
            false,
            false,
            false,
            -1
    );

    public static final Codec<FermentationData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("fermentation").forGetter(FermentationData::getFermentation),
            Codec.BOOL.fieldOf("qvevri_fermented").forGetter(FermentationData::isQvevriFermented),
            Codec.INT.fieldOf("fermentation_light").forGetter(FermentationData::getFermentationLight),
            Codec.INT.fieldOf("carbonic_level").forGetter(FermentationData::getCarbonicLevel),
            Codec.BOOL.fieldOf("has_brandy").forGetter(FermentationData::isHasBrandy),
            Codec.INT.fieldOf("oak_aging").forGetter(FermentationData::getOakAging),
            Codec.BOOL.fieldOf("new_oak_aging").forGetter(FermentationData::isNewOakAging),
            Codec.BOOL.fieldOf("heat_aging").forGetter(FermentationData::isHeatAging),
            Codec.BOOL.fieldOf("sugar_added").forGetter(FermentationData::isSugarAdded),
            Codec.INT.fieldOf("bottle_aging").forGetter(FermentationData::getBottleAging)
    ).apply(i, FermentationData::new));

    // StreamCodec.composite() only supports up to 6 fields, but FermentationData has 10,
    // so the stream codec is written manually instead.
    public static final StreamCodec<RegistryFriendlyByteBuf, FermentationData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        buf.writeVarInt(data.getFermentation());
                        buf.writeBoolean(data.isQvevriFermented());
                        buf.writeVarInt(data.getFermentationLight());
                        buf.writeVarInt(data.getCarbonicLevel());
                        buf.writeBoolean(data.isHasBrandy());
                        buf.writeVarInt(data.getOakAging());
                        buf.writeBoolean(data.isNewOakAging());
                        buf.writeBoolean(data.isHeatAging());
                        buf.writeBoolean(data.isSugarAdded());
                        buf.writeVarInt(data.getBottleAging());
                    },
                    buf -> new FermentationData(
                            buf.readVarInt(),
                            buf.readBoolean(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readBoolean(),
                            buf.readVarInt(),
                            buf.readBoolean(),
                            buf.readBoolean(),
                            buf.readBoolean(),
                            buf.readVarInt()
                    )
            );



    public int getFermentation() {
        return fermentation;
    }

    public void setFermentation(int fermentation) {
        this.fermentation = fermentation;
    }

    public boolean isQvevriFermented() {
        return qvevriFermented;
    }

    public void setQvevriFermented(boolean qvevriFermented) {
        this.qvevriFermented = qvevriFermented;
    }

    public int getFermentationLight() {
        return fermentationLight;
    }

    public void setFermentationLight(int fermentationLight) {
        this.fermentationLight = fermentationLight;
    }

    public int getCarbonicLevel() {
        return carbonicLevel;
    }

    public void setCarbonicLevel(int carbonicLevel) {
        this.carbonicLevel = carbonicLevel;
    }

    public boolean isHasBrandy() {
        return hasBrandy;
    }

    public void setHasBrandy(boolean hasBrandy) {
        this.hasBrandy = hasBrandy;
    }

    public int getOakAging() {
        return oakAging;
    }

    public void setOakAging(int oakAging) {
        this.oakAging = oakAging;
    }

    public boolean isNewOakAging() {
        return newOakAging;
    }

    public void setNewOakAging(boolean newOakAging) {
        this.newOakAging = newOakAging;
    }

    public boolean isHeatAging() {
        return heatAging;
    }

    public void setHeatAging(boolean heatAging) {
        this.heatAging = heatAging;
    }

    public boolean isSugarAdded() {
        return sugarAdded;
    }

    public void setSugarAdded(boolean sugarAdded) {
        this.sugarAdded = sugarAdded;
    }

    public int getBottleAging() {
        return bottleAging;
    }

    public void setBottleAging(int bottleAging) {
        this.bottleAging = bottleAging;
    }
}
