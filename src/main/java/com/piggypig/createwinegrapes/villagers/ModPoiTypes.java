package com.piggypig.createwinegrapes.villagers;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModPoiTypes {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, CreateWineGrapes.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> WINEMAKER = POI_TYPES.register(
            "winemaker",
            () -> new PoiType(
                    Set.copyOf(ModBlocks.PRESS_BASSIN.get().getStateDefinition().getPossibleStates()),
                    1,
                    1
            )
    );

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
    }
}
