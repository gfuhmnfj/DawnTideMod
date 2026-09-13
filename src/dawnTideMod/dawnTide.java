package dawnTideMod;

import arc.scene.ui.layout.Table;
import dawnTideMod.content.*;
import dawnTideMod.content.ui.button;
import mindustry.Vars;
import mindustry.mod.*;


public class dawnTide extends Mod {

    public dawnTide(){
    }

    @Override
    public void loadContent(){
        dawnTideItems.load();
        dawnTideBlocks.load();
        super.loadContent();
        dawnTideLiquids.load();
        dawnTideTechTree.load();
        //dawnTideUnitTypes.load();
    }

    /** Mod.init() 在 UI 初始化(Styles.load)之后才被调用，创建 UI 元素必须放在这里，不能放 loadContent */
    @Override
    public void init(){
        // 之前"看不见按钮"的原因：new 出来的按钮没有挂到任何 UI 场景上。
        // 这里把按钮挂到游戏 HUD 的左上角，进入地图后可见；点击弹出一段文字。
        Table t = new Table();
        t.setFillParent(true);                     // 自动撑满 hudGroup
        t.top().left();                            // 内容对齐到左上角
        t.visibility = () -> !Vars.state.isMenu(); // 主菜单不显示，进图后显示
        button b = new button("曙光潮涌", () -> Vars.ui.showInfo("[accent]曙光潮涌[]：这是按钮添加的一段文字！"));
        t.add(b).size(140f, 44f).padTop(80f).padLeft(12f);
        Vars.ui.hudGroup.addChild(t);
    }
}
