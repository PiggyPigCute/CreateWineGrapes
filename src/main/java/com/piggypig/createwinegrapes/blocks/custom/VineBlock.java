package com.piggypig.createwinegrapes.blocks.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class VineBlock extends Block {
    public VineBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected float getMaxHorizontalOffset() {
        return 0.1F;
    }
}
