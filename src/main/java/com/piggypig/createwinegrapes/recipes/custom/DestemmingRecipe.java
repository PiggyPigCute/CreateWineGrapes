package com.piggypig.createwinegrapes.recipes.custom;

import com.piggypig.createwinegrapes.recipes.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DestemmingRecipe implements Recipe<SingleRecipeInput> {

    private final Ingredient input;
    private final List<ItemStack> results;
    private final int processingTime;

    public DestemmingRecipe(Ingredient input, List<ItemStack> results, int processingTime) {
        this.input = input;
        this.results = results;
        this.processingTime = processingTime;
    }

    public Ingredient getInput() { return input; }
    public List<ItemStack> getResults() { return results; }
    public int getProcessingTime() { return processingTime; }

    @Override
    public boolean matches(SingleRecipeInput recipeInput, @NotNull Level level) {
        return input.test(recipeInput.item());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SingleRecipeInput recipeInput, HolderLookup.@NotNull Provider registries) {
        return results.isEmpty() ? ItemStack.EMPTY : results.getFirst().copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override public @NotNull RecipeSerializer<?> getSerializer() { return ModRecipes.DESTEMMING_SERIALIZER.get(); }
    @Override public @NotNull RecipeType<?> getType() { return ModRecipes.DESTEMMING_TYPE.get(); }
}