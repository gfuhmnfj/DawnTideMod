package dawnTideMod;

import arc.Events;
import arc.util.Log;
import dawnTideMod.content.Blocks.*;
import dawnTideMod.content.dawnBullets;
import dawnTideMod.content.dawnTideItems;
import dawnTideMod.content.dawnTideLiquids;
import dawnTideMod.content.DawnTidePlanet;
import dawnTideMod.content.dawnTideStatuses;
import dawnTideMod.content.dawnTideTechTree;
import dawnTideMod.content.dawnTideUnitTypes;
import dawnTideMod.content.dawnTweaks;
import dawnTideMod.TideClean.logic.EnemyPathEstimate;
import dawnTideMod.TideClean.logic.WorldLogicRegistry;
import dawnTideMod.TideClean.multicrafter.MultiCrafterBlock;
import dawnTideMod.TideClean.multicrafter.type.DrawRecipe;
import dawnTideMod.TideClean.multicrafter.world.AttributeMultiCrafterBlock;
import dawnTideMod.TideClean.ui.UnitEditorDialog;
import dawnTideMod.TideClean.ui.UnitEditorDialog.DawnControlPanel;
import dawnTideMod.TideClean.world.WorldState;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.ClassMap;
import mindustry.mod.Mod;

public class DawnTide extends Mod{

    public DawnTide(){
        registerClasses();
        Log.info("");
    }

    @Override
    public void loadContent(){
        registerContent();
        super.loadContent();
        dawnTweaks.load();
        DawnTideCrafter.load();
        DawnTideDefense.load();
        DawnTideDrill.load();
        DawnTideOre.load();
        DawnTideUnits.load();
        DawnTidePower.load();
        DawnTideWall.load();
        DawnTideConveyor.load();
        registerWorld();
        Log.info("");
    }

    @Override
    public void init(){
        if(!Vars.headless){
            UnitEditorDialog.installHudEntry();
            DawnControlPanel.install();
        }
    }

    private static void registerClasses(){
        ClassMap.classes.put("MultiCrafter", MultiCrafterBlock.class);
        ClassMap.classes.put("AttributeMultiCrafter", AttributeMultiCrafterBlock.class);
        ClassMap.classes.put("DrawRecipe", DrawRecipe.class);
    }

    private static void registerContent(){
        dawnTideItems.load();
        dawnTideLiquids.load();
        DawnTideBlocks.load();
    }

    private static void registerWorld(){
        DawnTidePlanet.load();
        dawnTideStatuses.load();
        dawnTideUnitTypes.load();
        dawnTideTechTree.load();
        WorldState.init();
        WorldLogicRegistry.register();
        dawnBullets.load();
        DawnTurrets.load();
        EnemyPathEstimate.register();
        Events.on(ClientLoadEvent.class, e -> {
            DawnControlPanel.install();
            DawnControlPanel.installSettings();
        });
    }
}
