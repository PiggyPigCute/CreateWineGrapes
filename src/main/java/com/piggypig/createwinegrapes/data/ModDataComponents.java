package com.piggypig.createwinegrapes.data;

import com.mojang.serialization.Codec;
import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.data.custom.GrapeData;
import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.Vineyard;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
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

    public static final Supplier<DataComponentType<GrapeData>> GRAPE_DATA =
            DATA_COMPONENTS.registerComponentType(
                    "grape_data",
                    builder -> builder
                            .persistent(GrapeData.CODEC)
                            .networkSynchronized(GrapeData.STREAM_CODEC)
            );

    public static final Supplier<DataComponentType<MustData>> MUST_DATA =
            DATA_COMPONENTS.registerComponentType(
                    "must_data",
                    builder -> builder
                            .persistent(MustData.CODEC)
                            .networkSynchronized(MustData.STREAM_CODEC)
            );

    public static final Supplier<DataComponentType<GrapeVariety>> GRAPE_VARIETY =
            DATA_COMPONENTS.registerComponentType(
                    "grape_variety",
                    builder -> builder
                            .persistent(GrapeVariety.CODEC)
                            .networkSynchronized(GrapeVariety.STREAM_CODEC)
            );

    public static final Supplier<DataComponentType<Vineyard>> VINEYARD =
            DATA_COMPONENTS.registerComponentType(
                    "vineyard",
                    builder -> builder
                            .persistent(Vineyard.CODEC)
                            .networkSynchronized(Vineyard.STREAM_CODEC)
            );

    // Absent when the bottle isn't capped; holds the exact button item used as a cork otherwise.
    public static final Supplier<DataComponentType<Item>> CAPPED =
            DATA_COMPONENTS.registerComponentType(
                    "capped",
                    builder -> builder
                            .persistent(BuiltInRegistries.ITEM.byNameCodec())
                            .networkSynchronized(ByteBufCodecs.registry(Registries.ITEM))
            );

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}