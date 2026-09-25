package dawnTideMod.content;

import arc.Events;
import dawnTideMod.TideClean.ui.logic.EnemyPathEstimate;
import dawnTideMod.TideClean.ui.logic.WorldLogicRegistry;
import dawnTideMod.TideClean.ui.multicrafter.MultiCrafterBlock;
import dawnTideMod.TideClean.ui.multicrafter.type.DrawRecipe;
import dawnTideMod.TideClean.ui.multicrafter.world.AttributeMultiCrafterBlock;
import dawnTideMod.TideClean.ui.world.WorldState;
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
        DawnTideUnitTypes.load();
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
        dawnTideMod.TideClean.ui.planets.DawnTideRing.dispose();
        DawnTidePlanet.unload();
        WorldState.clear();
    }

    public static void apply(){
        DawnTweaks.apply();
    }
}
