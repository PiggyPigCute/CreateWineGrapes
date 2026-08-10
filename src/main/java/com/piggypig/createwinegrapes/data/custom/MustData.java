package com.piggypig.createwinegrapes.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class MustData {
    private GrapeVariety grapeVariety;
    private Vineyard vineyard;
    private Residue residue;
    private boolean frozen;
    private int passerillage;
    private int herbaceousness;
    private FermentationData fermentationData;
    private boolean badWine;

    public MustData(
            GrapeVariety grapeVariety,
            Vineyard vineyard,
            Residue residue,
            boolean frozen,
            int passerillage,
            int herbaceousness,
            FermentationData fermentationData,
            boolean badWine
    ) {
        this.grapeVariety = grapeVariety;
        this.vineyard = vineyard;
        this.residue = residue;
        this.frozen = frozen;
        this.passerillage = passerillage;
        this.herbaceousness = herbaceousness;
        this.fermentationData = fermentationData;
        this.badWine = badWine;
    }

    public MustData(
            GrapeVariety grapeVariety,
            Vineyard vineyard,
            Residue residue,
            boolean frozen,
            int passerillage,
            int herbaceousness
    ) {
        new MustData(
                grapeVariety,
                vineyard,
                residue,
                frozen,
                passerillage,
                herbaceousness,
                FermentationData.DEFAULT,
                false
        );
    }

    public static final MustData DEFAULT = new MustData(
            GrapeVariety.NONE,
            Vineyard.DEFAULT,
            Residue.NONE,
            false,
            0,
            0,
            FermentationData.DEFAULT,
            false
    );

    public static final Codec<MustData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GrapeVariety.CODEC.fieldOf("grape_variety").forGetter(MustData::getGrapeVariety),
            Vineyard.CODEC.fieldOf("vineyard").forGetter(MustData::getVineyard),
            Residue.CODEC.fieldOf("residue").forGetter(MustData::getResidue),
            Codec.BOOL.fieldOf("frozen").forGetter(MustData::isFrozen),
            Codec.INT.fieldOf("passerillage").forGetter(MustData::getPasserillage),
            Codec.INT.fieldOf("herbaceousness").forGetter(MustData::getHerbaceousness),
            FermentationData.CODEC.fieldOf("fermentation_data").forGetter(MustData::getFermentationData),
            Codec.BOOL.fieldOf("bad_wine").forGetter(MustData::isBadWine)
    ).apply(i, MustData::new));

    // StreamCodec.composite() only supports up to 6 fields, but MustData has 8,
    // so the stream codec is written manually instead.
    public static final StreamCodec<RegistryFriendlyByteBuf, MustData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        GrapeVariety.STREAM_CODEC.encode(buf, data.getGrapeVariety());
                        Vineyard.STREAM_CODEC.encode(buf, data.getVineyard());
                        Residue.STREAM_CODEC.encode(buf, data.getResidue());
                        buf.writeBoolean(data.isFrozen());
                        buf.writeVarInt(data.getPasserillage());
                        buf.writeVarInt(data.getHerbaceousness());
                        FermentationData.STREAM_CODEC.encode(buf, data.getFermentationData());
                        buf.writeBoolean(data.isBadWine());
                    },
                    buf -> new MustData(
                            GrapeVariety.STREAM_CODEC.decode(buf),
                            Vineyard.STREAM_CODEC.decode(buf),
                            Residue.STREAM_CODEC.decode(buf),
                            buf.readBoolean(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            FermentationData.STREAM_CODEC.decode(buf),
                            buf.readBoolean()
                    )
            );


    public GrapeVariety getGrapeVariety() {
        return grapeVariety;
    }

    public void setGrapeVariety(GrapeVariety grapeVariety) {
        this.grapeVariety = grapeVariety;
    }

    public Vineyard getVineyard() {
        return vineyard;
    }

    public void setVineyard(Vineyard vineyard) {
        this.vineyard = vineyard;
    }

    public Residue getResidue() {
        return residue;
    }

    public void setResidue(Residue residue) {
        this.residue = residue;
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

    public int getHerbaceousness() {
        return herbaceousness;
    }

    public void setHerbaceousness(int herbaceousness) {
        this.herbaceousness = herbaceousness;
    }

    public FermentationData getFermentationData() {
        return fermentationData;
    }

    public void setFermentationData(FermentationData fermentationData) {
        this.fermentationData = fermentationData;
    }

    public boolean isBadWine() {
        return badWine;
    }

    public void setBadWine(boolean badWine) {
        this.badWine = badWine;
    }
}