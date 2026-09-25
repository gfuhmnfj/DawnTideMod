package dawnTideMod;

import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import dawnTideMod.content.DawnControlPanel;
import dawnTideMod.content.DawnRegistry;
import dawnTideMod.TideClean.ui.UnitEditorDialog;
import mindustry.Vars;
import mindustry.mod.Mod;

public class DawnTide extends Mod{

    public DawnTide(){
        DawnRegistry.registerClasses();
        Log.info("");
    }

    @Override
    public void loadContent(){
        DawnRegistry.registerContent();
        super.loadContent();
        DawnRegistry.registerWorld();
        DawnRegistry.apply();
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

    public static void unload(){
        DawnRegistry.unload();
        Log.info("");
    }
}
