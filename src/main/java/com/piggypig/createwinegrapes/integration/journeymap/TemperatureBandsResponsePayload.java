package com.piggypig.createwinegrapes.integration.journeymap;

import java.util.ArrayList;
import java.util.List;

import com.piggypig.createwinegrapes.CreateWineGrapes;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Server -> client payload carrying, for every 4-block cell (matching vanilla's quart climate-noise
 * resolution) within a chunk the requesting player has discovered, which {@link TemperatureBand} it
 * falls into. Each cell is encoded as {@code BlockPos.asLong(minX, 0, minZ)} of its min corner.
 * Sent once in response to a {@link TemperatureBandsRequestPayload}.
 * Pure Minecraft/NeoForge types only; only the client-side handler cares about JourneyMap.
 */
record TemperatureBandsResponsePayload(
        ResourceKey<Level> dimension,
        List<Long> coldCells,
        List<Long> mildCells,
        List<Long> hotCells
) implements CustomPacketPayload {

    static final CustomPacketPayload.Type<TemperatureBandsResponsePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "temperature_bands_response"));

    static final StreamCodec<RegistryFriendlyByteBuf, TemperatureBandsResponsePayload> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), TemperatureBandsResponsePayload::dimension,
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_LONG), TemperatureBandsResponsePayload::coldCells,
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_LONG), TemperatureBandsResponsePayload::mildCells,
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_LONG), TemperatureBandsResponsePayload::hotCells,
            TemperatureBandsResponsePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
