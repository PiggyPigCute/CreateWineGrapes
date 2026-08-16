package com.piggypig.createwinegrapes.integration.journeymap;

import java.util.ArrayList;
import java.util.List;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.data.custom.Vineyard;

import it.unimi.dsi.fastutil.longs.LongSet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers the temperature-bands request/response channel. This class never references any JourneyMap
 * type directly, so it is always safe to load; the JourneyMap-specific rendering
 * ({@link TemperatureOverlayClientPlugin}) is only reached behind a {@code ModList.isLoaded("journeymap")}
 * check, at which point the lambda referencing it is only created (and thus resolved) if JourneyMap is present.
 */
public final class TemperatureOverlayNetworking {

    private static final int SAMPLE_Y = 70;
    /** Climate noise (temperature, humidity, ...) is sampled at quart (4-block) resolution in vanilla. */
    private static final int CELL_SIZE = 4;
    private static final int CELLS_PER_CHUNK_AXIS = 16 / CELL_SIZE;

    private TemperatureOverlayNetworking() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(TemperatureOverlayNetworking::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(TemperatureBandsRequestPayload.TYPE, TemperatureBandsRequestPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player) {
                        handleRequest(player);
                    }
                }));
        registrar.playToClient(TemperatureBandsResponsePayload.TYPE, TemperatureBandsResponsePayload.STREAM_CODEC,
                (payload, context) -> {
                    if (ModList.get().isLoaded("journeymap")) {
                        context.enqueueWork(() -> TemperatureOverlayClientPlugin.render(payload));
                    }
                });
    }

    private static void handleRequest(ServerPlayer player) {
        CreateWineGrapes.LOGGER.info("Temperature bands requested by {}", player.getGameProfile().getName());
        if (player.level().dimension() != Level.OVERWORLD) {
            return;
        }
        ServerLevel level = (ServerLevel) player.level();
        LongSet visited = VisitedChunksSavedData.get(level).getVisited(player.getUUID());
        CreateWineGrapes.LOGGER.info("Player has {} discovered chunks recorded", visited.size());

        List<Long> cold = new ArrayList<>();
        List<Long> mild = new ArrayList<>();
        List<Long> hot = new ArrayList<>();
        for (long encodedChunk : visited) {
            ChunkPos chunkPos = new ChunkPos(encodedChunk);
            int chunkMinX = chunkPos.getMinBlockX();
            int chunkMinZ = chunkPos.getMinBlockZ();
            for (int cx = 0; cx < CELLS_PER_CHUNK_AXIS; cx++) {
                for (int cz = 0; cz < CELLS_PER_CHUNK_AXIS; cz++) {
                    int cellMinX = chunkMinX + cx * CELL_SIZE;
                    int cellMinZ = chunkMinZ + cz * CELL_SIZE;
                    float temperature = Vineyard.sample(level,
                            new BlockPos(cellMinX + CELL_SIZE / 2, SAMPLE_Y, cellMinZ + CELL_SIZE / 2)).temperature();
                    long encodedCell = BlockPos.asLong(cellMinX, 0, cellMinZ);
                    switch (TemperatureBand.fromTemperature(temperature)) {
                        case COLD -> cold.add(encodedCell);
                        case MILD -> mild.add(encodedCell);
                        case HOT -> hot.add(encodedCell);
                    }
                }
            }
        }
        PacketDistributor.sendToPlayer(player, new TemperatureBandsResponsePayload(level.dimension(), cold, mild, hot));
    }
}
