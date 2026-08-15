package com.piggypig.createwinegrapes;

import com.piggypig.createwinegrapes.blocks.ModBlocks;
import com.piggypig.createwinegrapes.data.ModDataComponents;
import com.piggypig.createwinegrapes.data.custom.MustData;
import com.piggypig.createwinegrapes.data.custom.MustKind;
import com.piggypig.createwinegrapes.items.ModItems;
import com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem;
import com.piggypig.createwinegrapes.ponder.CreateWineGrapesPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = CreateWineGrapes.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = CreateWineGrapes.MOD_ID, value = Dist.CLIENT)
public class CreateWineGrapesClient {
    public CreateWineGrapesClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        CreateWineGrapes.LOGGER.info("HELLO FROM CLIENT SETUP");
        CreateWineGrapes.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MECHANICAL_DESTEMMER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.VINE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PRESS_BASIN.get(), RenderType.cutout());
            PonderIndex.addPlugin(new CreateWineGrapesPonderPlugin());
        });
    }

    @SubscribeEvent
    static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> tintIndex == 0 ? BunchOfGrapesItem.getGrapeData(stack).grapeVariety().getColor() : -1,
                ModItems.GRAPE.get(), ModItems.BUNCH_OF_GRAPES.get()
        );
        event.register(
                (stack, tintIndex) -> {
                    if (tintIndex != 0) {
                        return -1;
                    }
                    MustData data = stack.getOrDefault(ModDataComponents.MUST_DATA.get(), MustData.DEFAULT);
                    MustKind kind = MustKind.classify(data);
                    return kind == null ? -1 : kind.getColor();
                },
                ModItems.MUST_BOTTLE.get()
        );
    }
}
