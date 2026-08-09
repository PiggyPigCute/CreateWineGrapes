package com.piggypig.createwinegrapes.villagers;

import com.piggypig.createwinegrapes.CreateWineGrapes;
import com.piggypig.createwinegrapes.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

@EventBusSubscriber(modid = CreateWineGrapes.MOD_ID)
public class ModVillagerTrades {

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() != ModVillagerProfessions.WINEMAKER.get()) return;

        var trades = event.getTrades();

        trades.get(1).add(new BasicItemListing(
                new ItemStack(ModItems.GRAPE.get(), 20), new ItemStack(Items.EMERALD), 12, 2, 0.05f));
        trades.get(2).add(new BasicItemListing(
                new ItemStack(ModItems.STEM.get(), 24), new ItemStack(Items.EMERALD), 12, 5, 0.05f));
        trades.get(3).add(new BasicItemListing(
                new ItemStack(ModItems.BUNCH_OF_GRAPES.get(), 12), new ItemStack(Items.EMERALD), 12, 10, 0.05f));
        trades.get(4).add(new BasicItemListing(
                6, new ItemStack(ModItems.BUNCH_OF_GRAPES.get(), 3), 12, 15));
        trades.get(5).add(new BasicItemListing(
                20, new ItemStack(ModItems.BUNCH_OF_GRAPES.get(), 5), 6, 30));
    }
}
