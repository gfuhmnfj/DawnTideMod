package dawnTideMod;

import arc.Events;
import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import dawnTideMod.content.DawnBullets;
import dawnTideMod.content.DawnTideBlocks;
import dawnTideMod.content.DawnTideItems;
import dawnTideMod.content.DawnTideLiquids;
import dawnTideMod.content.DawnTidePlanet;
import dawnTideMod.content.DawnTideStatuses;
import dawnTideMod.content.DawnTideTechTree;
import dawnTideMod.content.DawnTideUnitTypes;
import dawnTideMod.content.DawnTurrets;
import dawnTideMod.content.DawnTweaks;
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
        registerWorld();
        DawnTweaks.load();
        Log.info("");
    }

    @Override
    public void init(){
        if(!Vars.headless){
            buildHudEntry();
            DawnControlPanel.install();
        }
    }

    private void buildHudEntry(){
        Table root = new Table();
        root.setFillParent(true);
        root.top().left();
        root.touchable = Touchable.childrenOnly;
        root.visibility = () -> !Vars.state.isMenu();
        Label entry = root.add("[accent]曙光潮涌[]").padTop(80f).padLeft(12f).get();
        entry.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new UnitEditorDialog().show();
            }
        });
        Vars.ui.hudGroup.addChild(root);
    }

    private static void registerClasses(){
        ClassMap.classes.put("MultiCrafter", MultiCrafterBlock.class);
        ClassMap.classes.put("AttributeMultiCrafter", AttributeMultiCrafterBlock.class);
        ClassMap.classes.put("DrawRecipe", DrawRecipe.class);
    }

    private static void registerContent(){
        DawnTideItems.load();
        DawnTideLiquids.load();
        DawnTideBlocks.load();
    }

    private static void registerWorld(){
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
}
