package com.piggypig.createwinegrapes.villagers;

import com.google.common.collect.ImmutableSet;
import com.piggypig.createwinegrapes.CreateWineGrapes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModVillagerProfessions {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, CreateWineGrapes.MOD_ID);

    public static final DeferredHolder<VillagerProfession, VillagerProfession> WINEMAKER = PROFESSIONS.register(
            "winemaker",
            () -> new VillagerProfession(
                    "winemaker",
                    holder -> holder.is(ModPoiTypes.WINEMAKER.getKey()),
                    holder -> holder.is(ModPoiTypes.WINEMAKER.getKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_FARMER
            )
    );

    public static void register(IEventBus eventBus) {
        PROFESSIONS.register(eventBus);
    }
}
