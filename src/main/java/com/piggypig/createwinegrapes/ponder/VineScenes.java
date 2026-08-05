package com.piggypig.createwinegrapes.ponder;

import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;

public class VineScenes {
    public static void basics(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("vine_basics", "Growing Wine Grapes on Vines");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(2, 1, 2, 2, 3, 2), Direction.DOWN);
        scene.idle(20);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Grapes ripen over time on the Vine")
                .placeNearTarget();
    }
}

