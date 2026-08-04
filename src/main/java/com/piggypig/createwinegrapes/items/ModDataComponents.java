package com.piggypig.createwinegrapes.items;

import com.mojang.serialization.Codec;
import com.piggypig.createwinegrapes.CreateWineGrapes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CreateWineGrapes.MOD_ID);

    public static final Supplier<DataComponentType<Integer>> GRAPE_COUNT =
            DATA_COMPONENTS.registerComponentType(
                    "grape_count",
                    builder -> builder
                            .persistent(Codec.intRange(0, 6))
                            .networkSynchronized(ByteBufCodecs.VAR_INT)
            );

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}