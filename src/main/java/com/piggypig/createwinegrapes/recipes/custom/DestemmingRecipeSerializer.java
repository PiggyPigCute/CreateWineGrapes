package com.piggypig.createwinegrapes.recipes.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class DestemmingRecipeSerializer implements RecipeSerializer<DestemmingRecipe> {

    // Sérialisation JSON (lecture des fichiers de recette dans data/tonmodid/recipe/)
    public static final MapCodec<DestemmingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(DestemmingRecipe::getInput),
                    ItemStack.CODEC.listOf().fieldOf("results").forGetter(DestemmingRecipe::getResults),
                    Codec.INT.optionalFieldOf("processingTime", 100).forGetter(DestemmingRecipe::getProcessingTime)
            ).apply(instance, DestemmingRecipe::new)
    );

    // Sérialisation réseau (sync client/serveur, plus compact que le JSON)
    public static final StreamCodec<RegistryFriendlyByteBuf, DestemmingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, DestemmingRecipe::getInput,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), DestemmingRecipe::getResults,
            ByteBufCodecs.VAR_INT, DestemmingRecipe::getProcessingTime,
            DestemmingRecipe::new
    );

    @Override
    public @NotNull MapCodec<DestemmingRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, DestemmingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}