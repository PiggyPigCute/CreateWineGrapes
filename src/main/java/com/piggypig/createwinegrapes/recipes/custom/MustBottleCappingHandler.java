package com.piggypig.createwinegrapes.recipes.custom;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.items.ModItems;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

/**
 * Lets a Create Deployer cap a must_bottle by applying any wooden button to it.
 * <p>
 * A plain {@code create:deploying} JSON recipe always produces a static output stack, which would wipe the
 * bottle's per-instance {@link com.piggypig.createwinegrapes.data.custom.MustData} (grape variety, vineyard,
 * fermentation state, ...). Instead we hook {@link DeployerRecipeSearchEvent} - the same extension point Create
 * itself uses for Sequenced Assembly - to build the recipe result from a copy of the actual input stack via
 * {@link com.simibubi.create.content.processing.recipe.ProcessingRecipe#enforceNextResult}, only flipping
 * {@link ModDataComponents#CAPPED}.
 */
@EventBusSubscriber(modid = CreateWineGrapes.MOD_ID)
public class MustBottleCappingHandler {

    private static final ResourceLocation RECIPE_ID =
            ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "cap_must_bottle");

    // Create's own DeployerBlockEntity.getRecipe() registers its "deploying"/"item_application" candidates at
    // priority 50 before posting the event. We need to outrank that so our dynamic result wins the search.
    private static final int PRIORITY = 60;

    @SubscribeEvent
    public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        if (!event.shouldAddRecipeWithPriority(PRIORITY))
            return;

        RecipeWrapper inv = event.getInventory();
        ItemStack bottle = inv.getItem(0);
        ItemStack tool = inv.getItem(1);

        if (!bottle.is(ModItems.MUST_BOTTLE.get()))
            return;
        if (bottle.has(ModDataComponents.CAPPED.get()))
            return;
        if (!tool.is(ItemTags.WOODEN_BUTTONS))
            return;

        event.addRecipe(() -> Optional.of(buildRecipe(bottle, tool.getItem())), PRIORITY);
    }

    private static RecipeHolder<DeployerApplicationRecipe> buildRecipe(ItemStack bottle, Item corkItem) {
        DeployerApplicationRecipe recipe = new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, RECIPE_ID)
                .require(ModItems.MUST_BOTTLE.get())
                .require(ItemTags.WOODEN_BUTTONS)
                .output(bottle.copyWithCount(1))
                .build();

        // Captures the real input stack at match time, so the result keeps its MUST_DATA/GRAPE_VARIETY/VINEYARD
        // instead of the static "output" template above (which only serves as a JEI/validation placeholder).
        recipe.enforceNextResult(() -> {
            ItemStack capped = bottle.copyWithCount(1);
            capped.set(ModDataComponents.CAPPED.get(), corkItem);
            return capped;
        });

        return new RecipeHolder<>(RECIPE_ID, recipe);
    }
}
