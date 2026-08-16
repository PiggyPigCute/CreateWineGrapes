package com.piggypig.createwinegrapes.integration.journeymap;

import java.awt.geom.Area;
import java.util.List;

import com.piggypig.createwinegrapes.CreateWineGrapes;

import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.client.display.Context;
import journeymap.api.v2.client.display.Overlay;
import journeymap.api.v2.client.display.PolygonOverlay;
import journeymap.api.v2.client.event.FullscreenDisplayEvent;
import journeymap.api.v2.client.fullscreen.IThemeButton;
import journeymap.api.v2.client.fullscreen.ThemeButtonDisplay;
import journeymap.api.v2.client.model.MapPolygon;
import journeymap.api.v2.client.model.MapPolygonWithHoles;
import journeymap.api.v2.client.model.ShapeProperties;
import journeymap.api.v2.client.util.PolygonHelper;
import journeymap.api.v2.common.JourneyMapPlugin;
import journeymap.api.v2.common.event.ClientEventRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * JourneyMap client plugin, auto-discovered by JourneyMap (via the {@link JourneyMapPlugin} annotation)
 * only when JourneyMap is installed client-side. Nothing in the base mod references this class directly;
 * {@link TemperatureOverlayNetworking} only reaches it behind a {@code ModList.isLoaded("journeymap")}
 * check, so if JourneyMap is absent this class is never loaded.
 * <p>
 * Adds a toggle button to the JourneyMap fullscreen toolbar. Turning it on requests a one-shot snapshot
 * of the Temperature bands for every chunk the player has discovered so far (no periodic recomputation,
 * nothing tied to player movement); turning it off just clears the overlay.
 */
@JourneyMapPlugin(apiVersion = "2.0.0")
public class TemperatureOverlayClientPlugin implements IClientPlugin {

    private static final ResourceLocation BUTTON_ICON =
            ResourceLocation.fromNamespaceAndPath(CreateWineGrapes.MOD_ID, "textures/journeymap/temperature_button.png");
    /** Must match {@code TemperatureOverlayNetworking.CELL_SIZE} - the quart (4-block) climate-noise resolution. */
    private static final int CELL_SIZE = 4;

    private static volatile IClientAPI clientApi;
    private static volatile boolean enabled;

    @Override
    @SuppressWarnings("removal") // ADDON_BUTTON_DISPLAY_EVENT is deprecated-for-removal but is API v2.0.0's only fullscreen toolbar button hook
    public void initialize(IClientAPI jmAPI) {
        clientApi = jmAPI;
        ClientEventRegistry.ADDON_BUTTON_DISPLAY_EVENT.subscribe(CreateWineGrapes.MOD_ID, TemperatureOverlayClientPlugin::onAddonButtonDisplay);
    }

    @Override
    public String getModId() {
        return CreateWineGrapes.MOD_ID;
    }

    private static void onAddonButtonDisplay(FullscreenDisplayEvent.AddonButtonDisplayEvent event) {
        ThemeButtonDisplay display = event.getThemeButtonDisplay();
        display.addThemeToggleButton(
                "Temperature bands off",
                "Temperature bands on",
                BUTTON_ICON,
                enabled,
                TemperatureOverlayClientPlugin::onButtonToggled
        );
    }

    private static void onButtonToggled(IThemeButton button) {
        enabled = !enabled;
        button.setToggled(enabled);
        CreateWineGrapes.LOGGER.info("Temperature bands button toggled: {}", enabled);
        if (enabled) {
            PacketDistributor.sendToServer(new TemperatureBandsRequestPayload());
        } else {
            clear();
        }
    }

    private static void clear() {
        IClientAPI api = clientApi;
        if (api != null) {
            api.removeAll(CreateWineGrapes.MOD_ID);
        }
    }

    static void render(TemperatureBandsResponsePayload payload) {
        IClientAPI api = clientApi;
        CreateWineGrapes.LOGGER.info("Temperature bands response received: cold={} mild={} hot={} (api={}, enabled={})",
                payload.coldCells().size(), payload.mildCells().size(), payload.hotCells().size(), api != null, enabled);
        if (api == null || !enabled) {
            return;
        }
        api.removeAll(CreateWineGrapes.MOD_ID);
        showBand(api, payload.dimension(), payload.coldCells(), TemperatureBand.COLD);
        showBand(api, payload.dimension(), payload.mildCells(), TemperatureBand.MILD);
        showBand(api, payload.dimension(), payload.hotCells(), TemperatureBand.HOT);
    }

    @SuppressWarnings("deprecation") // Context.UI/MapType are deprecated but still required by Overlay in API v2.0.0
    private static void showBand(IClientAPI api, ResourceKey<Level> dimension, List<Long> encodedCells, TemperatureBand band) {
        if (encodedCells.isEmpty()) {
            return;
        }
        Area combinedArea = new Area();
        for (long encodedCell : encodedCells) {
            BlockPos min = BlockPos.of(encodedCell);
            MapPolygon square = new MapPolygon(
                    new BlockPos(min.getX(), 0, min.getZ()),
                    new BlockPos(min.getX() + CELL_SIZE, 0, min.getZ()),
                    new BlockPos(min.getX() + CELL_SIZE, 0, min.getZ() + CELL_SIZE),
                    new BlockPos(min.getX(), 0, min.getZ() + CELL_SIZE)
            );
            combinedArea.add(PolygonHelper.toArea(square));
        }
        List<MapPolygonWithHoles> regions = PolygonHelper.createPolygonFromArea(combinedArea, 0);

        ShapeProperties shapeProperties = new ShapeProperties()
                .setFillColor(band.color())
                .setFillOpacity(0.35f)
                .setStrokeColor(band.color())
                .setStrokeOpacity(0f)
                .setStrokeWidth(0f);

        for (MapPolygonWithHoles region : regions) {
            // The first constructor argument is the owning modId (Displayable.id is always an internal
            // random UUID) - it must be CreateWineGrapes.MOD_ID for api.removeAll(MOD_ID) to find these again.
            Overlay overlay = new PolygonOverlay(CreateWineGrapes.MOD_ID, dimension, shapeProperties, region)
                    .setActiveUIs(Context.UI.Fullscreen, Context.UI.Minimap, Context.UI.Webmap)
                    .setActiveMapTypes(Context.MapType.values())
                    .setDisplayOrder(50);
            try {
                api.show(overlay);
            } catch (Exception e) {
                CreateWineGrapes.LOGGER.warn("Failed to show temperature overlay on JourneyMap", e);
            }
        }
    }
}
