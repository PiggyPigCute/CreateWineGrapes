package com.piggypig.createwinegrapes.blocks.custom;

import com.piggypig.createwinegrapes.blocks.ModBlockEntities;
import com.piggypig.createwinegrapes.items.ModItems;
import com.piggypig.createwinegrapes.recipes.ModRecipes;
import com.piggypig.createwinegrapes.recipes.custom.DestemmingRecipe;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;

public class MechanicalDestemmerBlockEntity extends KineticBlockEntity {

    private ItemStack heldInput = ItemStack.EMPTY;
    private int processingTicks = -1;
    private static final int PROCESS_DURATION = 50;

    public MechanicalDestemmerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MECHANICAL_DESTEMMER.get(), pos, state);
    }

    // Face par laquelle les grappes ENTRENT actuellement
    public Direction getInputFace() {
        MechanicalDestemmer block = (MechanicalDestemmer) getBlockState().getBlock();
        Direction portA = block.getPortA(getBlockState());
        return getSpeed() >= 0 ? portA : portA.getOpposite();
    }

    // Face par laquelle les tiges SORTENT actuellement (toujours l'opposé de l'entrée)
    public Direction getStemOutputFace() {
        return getInputFace().getOpposite();
    }

    @Override
    public void tick() {
        // TODO : remplir le tick pour le process
        super.tick();
        if (level != null && level.isClientSide) return;
        if (heldInput.isEmpty()) return;
        if (Math.abs(getSpeed()) < 4) return; // pas assez de rotation

        if (processingTicks == -1) processingTicks = PROCESS_DURATION;

        if (processingTicks > 0) {
            processingTicks--;
            return;
        }

        DestemmingRecipe recipe = findRecipeFor(heldInput);
        if (recipe != null) outputResults(recipe);

        heldInput = ItemStack.EMPTY;
        processingTicks = -1;
    }

    private DestemmingRecipe findRecipeFor(ItemStack stack) {
        SingleRecipeInput recipeInput = new SingleRecipeInput(stack);
        if (level == null) {
            return null;
        }
        return level.getRecipeManager()
                .getRecipeFor(ModRecipes.DESTEMMING_TYPE.get(), recipeInput, level)
                .map(net.minecraft.world.item.crafting.RecipeHolder::value)
                .orElse(null);
    }

    private void outputResults(DestemmingRecipe recipe) {
        for (ItemStack result : recipe.getResults()) {
            ItemStack toEject = result.copy();
            // Le grape sort toujours par le bas, tout le reste (stem) sort par le port de sortie courant
            Direction dir = toEject.getItem() == ModItems.GRAPE.get()
                    ? Direction.DOWN
                    : getStemOutputFace();
            ejectItem(toEject, dir);
        }
    }

    // Appelé par la capability IItemHandler : n'accepte l'insertion QUE depuis le port d'entrée courant
    public boolean tryAcceptInput(ItemStack stack, Direction fromFace) {
        if (fromFace != getInputFace()) return false;
        if (!heldInput.isEmpty()) return false;
        if (findRecipeFor(stack) == null) return false;
        heldInput = stack.copy();
        return true;
    }

    private void ejectItem(ItemStack stack, Direction dir) {
        BlockPos targetPos = worldPosition.relative(dir);
        boolean inserted = tryInsertIntoNeighbor(targetPos, dir.getOpposite(), stack);
        if (!inserted && level != null) {
            level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(
                    level,
                    worldPosition.getX() + 0.5 + dir.getStepX() * 0.6,
                    worldPosition.getY() + 0.5 + dir.getStepY() * 0.6,
                    worldPosition.getZ() + 0.5 + dir.getStepZ() * 0.6,
                    stack));
        }
    }

    private boolean tryInsertIntoNeighbor(BlockPos targetPos, Direction fromDirection, ItemStack stack) {
        if (level == null) {
            return false;
        }
        var itemHandler = level.getCapability(
                net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
                targetPos,
                fromDirection
        );

        if (itemHandler == null) return false;

        ItemStack remainder = net.neoforged.neoforge.items.ItemHandlerHelper.insertItemStacked(itemHandler, stack, false);
        return remainder.isEmpty();
    }
}