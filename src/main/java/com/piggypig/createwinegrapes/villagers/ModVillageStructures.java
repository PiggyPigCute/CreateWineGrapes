package com.piggypig.createwinegrapes.villagers;

import com.mojang.datafixers.util.Pair;
import com.piggypig.createwinegrapes.CreateWineGrapes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Appends the winery to vanilla's village house pools by mutating the loaded
 * {@link StructureTemplatePool} objects at runtime, instead of overriding the
 * pool JSON files directly. A JSON override replaces the whole file, so it silently
 * breaks compatibility with any other mod/datapack overriding the same file; mutating
 * the resolved in-memory pool after datapack load stacks with whatever anyone else does.
 */
@EventBusSubscriber(modid = CreateWineGrapes.MOD_ID)
public class ModVillageStructures {

    private static final ResourceLocation WINERY = ResourceLocation.fromNamespaceAndPath(
            CreateWineGrapes.MOD_ID, "village/winery/winery_1");
    private static final int WEIGHT = 3;

    private static final ResourceLocation[] HOUSE_POOLS = {
            ResourceLocation.withDefaultNamespace("village/plains/houses"),
            ResourceLocation.withDefaultNamespace("village/savanna/houses"),
            ResourceLocation.withDefaultNamespace("village/desert/houses"),
            ResourceLocation.withDefaultNamespace("village/taiga/houses"),
            ResourceLocation.withDefaultNamespace("village/snowy/houses"),
    };

    private static Field rawTemplatesField;
    private static Field templatesField;

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        Registry<StructureTemplatePool> pools = event.getServer().registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        StructurePoolElement element = StructurePoolElement.single(WINERY.toString()).apply(StructureTemplatePool.Projection.RIGID);

        for (ResourceLocation poolId : HOUSE_POOLS) {
            StructureTemplatePool pool = pools.get(poolId);
            if (pool == null) {
                CreateWineGrapes.LOGGER.warn("Could not find village pool {} to inject winery into", poolId);
                continue;
            }
            addToPool(pool, element, WEIGHT);
        }
    }

    private static void addToPool(StructureTemplatePool pool, StructurePoolElement element, int weight) {
        try {
            if (templatesField == null) {
                rawTemplatesField = StructureTemplatePool.class.getDeclaredField("rawTemplates");
                rawTemplatesField.setAccessible(true);
                templatesField = StructureTemplatePool.class.getDeclaredField("templates");
                templatesField.setAccessible(true);
            }

            List<StructurePoolElement> templates = (List<StructurePoolElement>) templatesField.get(pool);
            for (int i = 0; i < weight; i++) {
                templates.add(element);
            }

            List<Pair<StructurePoolElement, Integer>> rawTemplates = (List<Pair<StructurePoolElement, Integer>>) rawTemplatesField.get(pool);
            List<Pair<StructurePoolElement, Integer>> mutableRaw = new ArrayList<>(rawTemplates);
            mutableRaw.add(Pair.of(element, weight));
            rawTemplatesField.set(pool, mutableRaw);
        } catch (ReflectiveOperationException e) {
            CreateWineGrapes.LOGGER.error("Failed to inject winery into village pool", e);
        }
    }
}
