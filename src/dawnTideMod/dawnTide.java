package dawnTideMod;

import dawnTideMod.content.*;
import mindustry.mod.*;


public class dawnTide extends Mod {
    public dawnTide(){}

    @Override
    public void loadContent(){
        dawnTideItems.load();
        dawnTideBlocks.load();
        super.loadContent();
        dawnTideLiquids.load();
        dawnTideTechTree.load();
        dawnTideStatuses.load();
    }
    /*@Override
    public void init(){
        Table t = new Table();
        t.setFillParent(true);
        t.top().left();
        t.visibility = () -> !Vars.state.isMenu();
        button b = new button("曙光潮涌", () -> Vars.ui.showInfo("[accent]曙光潮涌[]：这是按钮添加的一段文字！"));
        t.add(b).size(140f, 44f).padTop(80f).padLeft(12f);
        Vars.ui.hudGroup.addChild(t);
    }*/
}
