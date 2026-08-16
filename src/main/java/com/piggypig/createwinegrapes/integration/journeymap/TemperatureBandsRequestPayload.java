package com.piggypig.createwinegrapes.integration.journeymap;

import com.piggypig.createwinegrapes.CreateWineGrapes;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Client -> server payload: "compute the Temperature bands for every chunk I've discovered so far".
 * Sent once when the JourneyMap toolbar toggle button is switched on - never on a timer or on movement.
 */
record TemperatureBandsRequestPayload() implements CustomPacketPayload {

    static final CustomPacketPayload.Type<TemperatureBandsRequestPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "temperature_bands_request"));

    static final StreamCodec<RegistryFriendlyByteBuf, TemperatureBandsRequestPayload> STREAM_CODEC =
            StreamCodec.unit(new TemperatureBandsRequestPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
