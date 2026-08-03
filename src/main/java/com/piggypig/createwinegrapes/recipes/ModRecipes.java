package com.piggypig.createwinegrapes.recipes;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.recipes.custom.DestemmingRecipe;
import com.piggypig.createwinegrapes.recipes.custom.DestemmingRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, CreateWineGrapes.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateWineGrapes.MOD_ID);

    public static final Supplier<RecipeType<DestemmingRecipe>> DESTEMMING_TYPE =
            RECIPE_TYPES.register("destemming", () -> new RecipeType<>() {
                @Override public String toString() { return "destemming"; }
            });

    public static final Supplier<RecipeSerializer<DestemmingRecipe>> DESTEMMING_SERIALIZER =
            RECIPE_SERIALIZERS.register("destemming", DestemmingRecipeSerializer::new);

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}