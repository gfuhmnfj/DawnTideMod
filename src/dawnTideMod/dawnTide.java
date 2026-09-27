package dawnTideMod;

import arc.Events;
import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
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
import mindustry.gen.Icon;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.ClassMap;
import mindustry.mod.Mod;
import mindustry.ui.Styles;

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
            installSettingsCategory();
        });
    }

    private static void installSettingsCategory(){
        if(Vars.headless || Vars.ui == null || Vars.ui.settings == null) return;
        Vars.ui.settings.addCategory("DTMod辅助", Icon.settings, table -> {
            table.button("变速调节", Icon.hammer, Styles.flatt, () -> DawnControlPanel.showSpeedDialog()).growX().margin(4f).row();
            table.button("单位调试器", Icon.pencil, Styles.flatt, () -> new UnitEditorDialog().show()).growX().margin(4f).row();
            table.add("[lightgray]曙光潮涌辅助工具：调节游戏速度、生成测试单位[]").left().padTop(8f);
        });
    }
}
