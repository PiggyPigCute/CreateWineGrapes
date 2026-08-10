package com.piggypig.createwinegrapes.ponder.custom;

import com.piggypig.createwinegrapes.data.custom.GrapeVariety;
import com.piggypig.createwinegrapes.items.ModItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.piggypig.createwinegrapes.items.custom.BunchOfGrapesItem.setGrapeVariety;

public class Vine {
    public static void vine1(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("vine_1", "");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);

        Vec3 plantingSpot = new Vec3(1.5, 1.5, 2.5);
        ItemStack grape = new ItemStack(ModItems.GRAPE.get());
        setGrapeVariety(grape, GrapeVariety.CABERNET_SAUVIGNON);
        scene.overlay().showControls(plantingSpot, Pointing.DOWN, 60)
                .rightClick()
                .withItem(grape);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("")
                .pointAt(plantingSpot)
                .placeNearTarget();
        scene.idle(5);
        scene.world().showSection(util.select().layer(1), Direction.UP);
    }


    public static void vine2(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("vine_2", "");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);

        List<Vec3> plantingSpots = List.of(
                new Vec3(1.5, 1.5, 0.5),
                new Vec3(1.5, 1.5, 2.5),
                new Vec3(1.5, 1.5, 4.5),
                new Vec3(3.5, 1.5, 0.5),
                new Vec3(3.5, 1.5, 2.5),
                new Vec3(3.5, 1.5, 4.5)
        );
        List<GrapeVariety> varieties = List.of(
                GrapeVariety.CABERNET_SAUVIGNON,
                GrapeVariety.TEMPRANILLO,
                GrapeVariety.PINOT_NOIR,
                GrapeVariety.RIESLING,
                GrapeVariety.CHARDONNAY,
                GrapeVariety.RKATSITELI
        );
        for (int i=0; i<6; i++) {
            ItemStack grape = new ItemStack(ModItems.GRAPE.get());
            setGrapeVariety(grape, varieties.get(i));
            scene.overlay().showControls(plantingSpots.get(i), Pointing.DOWN, 60)
                    .rightClick()
                    .withItem(grape);
        }
        scene.overlay().showText(100).text("");
        scene.idle(5);
        scene.world().showSection(util.select().layer(1), Direction.UP);
    }


    public static void vine3(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("vine_3", "Growing Wine");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);

        scene.overlay().showText(100).text("");

        for (int i=0; i<5; i++) {
            scene.idle(5);
            scene.world().showSection(util.select().fromTo(0,1,i,4,1,i), Direction.UP);
        }
    }
}

