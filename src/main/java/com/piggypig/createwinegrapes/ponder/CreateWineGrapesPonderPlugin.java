package com.piggypig.createwinegrapes.ponder;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.blocks.ModBlocks;
import com.piggypig.createwinegrapes.ponder.custom.Vine;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CreateWineGrapesPonderPlugin implements PonderPlugin {
    @Override
    public @NotNull String getModId() {
        return CreateWineGrapes.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<RegistryEntry<?, ?>> registrateHelper =
                helper.withKeyFunction(RegistryEntry::getId);

        registrateHelper.forComponents(ModBlocks.VINE)
                .addStoryBoard("vine/vine_1", Vine::vine1)
                .addStoryBoard("vine/vine_2", Vine::vine2)
                .addStoryBoard("vine/vine_3", Vine::vine3);
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
        ModPonderTags.register(helper);
    }
}

