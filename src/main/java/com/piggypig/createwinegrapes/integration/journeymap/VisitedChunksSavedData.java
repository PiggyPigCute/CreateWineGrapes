package com.piggypig.createwinegrapes.integration.journeymap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Persists, per player, the set of Overworld chunks they've discovered - i.e. had loaded/tracked for
 * them at some point. This is passive bookkeeping only (no Temperature computation happens here);
 * it just answers "which chunks should the on-demand overlay cover" when the player asks for it.
 */
public final class VisitedChunksSavedData extends SavedData {

    private static final String ID = "create_wine_grapes_visited_chunks";

    private final Map<UUID, LongSet> visitedByPlayer = new HashMap<>();

    public static VisitedChunksSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(VisitedChunksSavedData::new, VisitedChunksSavedData::load),
                ID
        );
    }

    public void markVisited(UUID player, long chunkPos) {
        if (visitedByPlayer.computeIfAbsent(player, ignored -> new LongOpenHashSet()).add(chunkPos)) {
            setDirty();
        }
    }

    public LongSet getVisited(UUID player) {
        return visitedByPlayer.getOrDefault(player, LongSets.EMPTY_SET);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag players = new ListTag();
        for (Map.Entry<UUID, LongSet> entry : visitedByPlayer.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("player", entry.getKey());
            playerTag.putLongArray("chunks", entry.getValue().toLongArray());
            players.add(playerTag);
        }
        tag.put("players", players);
        return tag;
    }

    private static VisitedChunksSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        VisitedChunksSavedData data = new VisitedChunksSavedData();
        ListTag players = tag.getList("players", Tag.TAG_COMPOUND);
        for (int i = 0; i < players.size(); i++) {
            CompoundTag playerTag = players.getCompound(i);
            data.visitedByPlayer.put(playerTag.getUUID("player"), new LongOpenHashSet(playerTag.getLongArray("chunks")));
        }
        return data;
    }
}
