package dawnTideMod.content;

import arc.Events;
import dawnTideMod.logic.EnemyPathEstimate;
import dawnTideMod.logic.WorldLogicRegistry;
import dawnTideMod.multicrafter.MultiCrafterBlock;
import dawnTideMod.multicrafter.type.DrawRecipe;
import dawnTideMod.multicrafter.world.AttributeMultiCrafterBlock;
import dawnTideMod.planets.DawnTidePlanet;
import dawnTideMod.segment.DawnTideSegmentUnits;
import dawnTideMod.world.WorldState;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.ClassMap;

public class DawnRegistry{

    public static void registerClasses(){
        ClassMap.classes.put("MultiCrafter", MultiCrafterBlock.class);
        ClassMap.classes.put("AttributeMultiCrafter", AttributeMultiCrafterBlock.class);
        ClassMap.classes.put("DrawRecipe", DrawRecipe.class);
    }

    public static void registerContent(){
        DawnTideItems.load();
        DawnTideLiquids.load();
        DawnTideBlocks.load();
    }

    public static void registerWorld(){
        DawnTidePlanet.load();
        DawnTideSegmentUnits.load();
        DawnTideTechTree.load();
        WorldState.init();
        WorldLogicRegistry.register();
        DawnTideStatuses.load();
        DawnBullets.load();
        DawnTurrets.load();
        EnemyPathEstimate.register();
        Events.on(ClientLoadEvent.class, e -> DawnControlPanel.install());
    }

    public static void unload(){
        DawnControlPanel.unload();
        dawnTideMod.planets.DawnTideRing.dispose();
        DawnTidePlanet.unload();
        WorldState.clear();
    }
}
