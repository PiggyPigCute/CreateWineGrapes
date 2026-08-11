package com.piggypig.createwinegrapes.fluids.custom;

import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.fluids.ModFluids;
import com.piggypig.createwinegrapes.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

/**
 * Lets a plain glass bottle be filled with {@link ModFluids#MUST} (e.g. from a Vat) and turn into
 * a {@link ModItems#MUST_BOTTLE}, carrying a copy of the fluid's {@link MustData}, and lets that
 * bottle be drained back into an empty tank, turning back into a glass bottle.
 * <p>
 * Registered on the vanilla {@code minecraft:glass_bottle} and {@code must_bottle} items rather
 * than modelled as a bucket, since Create's {@code GenericItemFilling}/{@code GenericItemEmptying}
 * already drive glass-bottle interactions with tanks through this capability for any fluid it
 * doesn't hardcode itself (water/potion/tea).
 */
public class MustBottleFluidHandler {

    public static final int CAPACITY = 250; // mB, matches Create's own potion/tea bottles

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new EmptyBottleHandler(stack),
                Items.GLASS_BOTTLE
        );
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new FilledBottleHandler(stack),
                ModItems.MUST_BOTTLE.get()
        );
    }

    private static class EmptyBottleHandler implements IFluidHandlerItem {
        private ItemStack container;

        private EmptyBottleHandler(ItemStack container) {
            this.container = container;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return stack.is(ModFluids.MUST.get());
        }

        @Override
        public int fill(@NotNull FluidStack resource, @NotNull FluidAction action) {
            if (!isFluidValid(0, resource) || resource.getAmount() < CAPACITY)
                return 0;
            if (action.execute()) {
                ItemStack filled = new ItemStack(ModItems.MUST_BOTTLE.get());
                filled.set(ModDataComponents.MUST_DATA.get(), MustFluid.getMustData(resource));
                container = filled;
            }
            return CAPACITY;
        }

        @Override
        public @NotNull FluidStack drain(@NotNull FluidStack resource, @NotNull FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack getContainer() {
            return container;
        }
    }

    private static class FilledBottleHandler implements IFluidHandlerItem {
        private ItemStack container;

        private FilledBottleHandler(ItemStack container) {
            this.container = container;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            FluidStack fluid = new FluidStack(ModFluids.MUST.get(), CAPACITY);
            MustFluid.setMustData(fluid, container.getOrDefault(ModDataComponents.MUST_DATA.get(), MustData.DEFAULT));
            return fluid;
        }

        @Override
        public int getTankCapacity(int tank) {
            return CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return false;
        }

        @Override
        public int fill(@NotNull FluidStack resource, @NotNull FluidAction action) {
            return 0;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, @NotNull FluidAction action) {
            if (!resource.is(ModFluids.MUST.get()) || resource.getAmount() < CAPACITY)
                return FluidStack.EMPTY;
            return drain(resource.getAmount(), action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction action) {
            if (maxDrain < CAPACITY)
                return FluidStack.EMPTY;
            FluidStack drained = getFluidInTank(0);
            if (action.execute())
                container = new ItemStack(Items.GLASS_BOTTLE);
            return drained;
        }

        @Override
        public @NotNull ItemStack getContainer() {
            return container;
        }
    }
}
