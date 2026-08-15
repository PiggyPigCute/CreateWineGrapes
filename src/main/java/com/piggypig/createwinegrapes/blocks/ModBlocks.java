package com.piggypig.createwinegrapes.blocks;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.custom.crusher.CrusherBlock;
import com.piggypig.createwinegrapes.blocks.custom.press_basin.PressBasinBlock;
import com.piggypig.createwinegrapes.blocks.custom.mechanical_destemmer.MechanicalDestemmer;
import com.piggypig.createwinegrapes.blocks.custom.VineBlock;
import com.piggypig.createwinegrapes.blocks.custom.vat.VatBlock;
import com.piggypig.createwinegrapes.blocks.custom.vat.VatBlockItem;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

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

    public static final BlockEntry<CrusherBlock> CRUSHER =
            CreateWineGrapes.REGISTRATE
                    .block("crusher", CrusherBlock::new)
                    .properties(
                            p -> p
                                    .strength(2f, 2f)
                                    .requiresCorrectToolForDrops()
                                    .noOcclusion()
                    )
                    .simpleItem()
                    .register();

    public static final BlockEntry<PressBasinBlock> PRESS_BASIN =
            CreateWineGrapes.REGISTRATE
                    .block("press_basin", PressBasinBlock::new)
                    .properties(
                            p -> p
                                    .strength(2f, 2f)
                                    .requiresCorrectToolForDrops()
                                    .noOcclusion()
                    )
                    .simpleItem()
                    .register();

    public static final BlockEntry<VatBlock> VAT =
            CreateWineGrapes.REGISTRATE
                    .block("vat", VatBlock::new)
                    .properties(
                            p -> p
                                    .strength(3f, 6f)
                                    .requiresCorrectToolForDrops()
                                    .sound(SoundType.METAL)
                                    .pushReaction(PushReaction.BLOCK)
                    )
                    .item(VatBlockItem::new)
                    .build()
                    .register();

    public static void register() {}
}
