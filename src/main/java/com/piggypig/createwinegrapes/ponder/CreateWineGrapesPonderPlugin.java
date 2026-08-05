package com.piggypig.createwinegrapes.ponder;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.ModBlocks;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class CreateWineGrapesPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return CreateWineGrapes.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(ModBlocks.VINE.get())
                .addStoryBoard("vine/basics", VineScenes::basics);
    }
}

