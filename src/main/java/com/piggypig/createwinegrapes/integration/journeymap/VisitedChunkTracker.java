package com.piggypig.createwinegrapes.integration.journeymap;

import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;

/**
 * Passively records which Overworld chunks each player has discovered, for {@link VisitedChunksSavedData}.
 * Purely bookkeeping - no Temperature sampling and no network traffic happens here.
 */
public final class VisitedChunkTracker {

    public void register() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChunkWatch(ChunkWatchEvent.Watch event) {
        if (event.getLevel().dimension() != Level.OVERWORLD) {
            return;
        }
        VisitedChunksSavedData.get(event.getLevel()).markVisited(event.getPlayer().getUUID(), event.getPos().toLong());
    }
}
