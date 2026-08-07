package com.piggypig.createwinegrapes.blocks;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.custom.PressBassinBlock;
import com.piggypig.createwinegrapes.blocks.custom.mechanicalDestemmer.MechanicalDestemmer;
import com.piggypig.createwinegrapes.blocks.custom.VineBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {

    public static final BlockEntry<VineBlock> VINE =
            CreateWineGrapes.REGISTRATE
                    .block("vine", VineBlock::new)
                    .properties(
                            p -> p
                                    .strength(0f, 0f)
                                    .sound(SoundType.WOOD)
                                    .noOcclusion()
                                    .noCollission()
                                    .randomTicks()
                                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    )
                    .simpleItem()
                    .register();


    public static final BlockEntry<MechanicalDestemmer> MECHANICAL_DESTEMMER =
            CreateWineGrapes.REGISTRATE
                    .block("mechanical_destemmer", MechanicalDestemmer::new)
                    .properties(
                            p -> p
                                    .strength(2f, 2f)
                                    .requiresCorrectToolForDrops()
                                    .noOcclusion()
                    )
                    .simpleItem()
                    .register();

    public static final BlockEntry<PressBassinBlock> PRESS_BASSIN =
            CreateWineGrapes.REGISTRATE
                    .block("press_bassin", PressBassinBlock::new)
                    .properties(
                            p -> p
                                    .strength(2f, 2f)
                                    .requiresCorrectToolForDrops()
                                    .noOcclusion()
                    )
                    .simpleItem()
                    .register();

    public static void register() {}
}
