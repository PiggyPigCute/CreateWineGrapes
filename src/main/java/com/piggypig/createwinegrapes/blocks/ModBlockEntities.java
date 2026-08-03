package com.piggypig.createwinegrapes.blocks;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.custom.MechanicalDestemmerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateWineGrapes.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MechanicalDestemmerBlockEntity>> MECHANICAL_DESTEMMER =
            BLOCK_ENTITY_TYPES.register("mechanical_destemmer",
                    () -> BlockEntityType.Builder.of(
                            MechanicalDestemmerBlockEntity::new,
                            ModBlocks.MECHANICAL_DESTEMMER.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}