package dawnTideMod;

import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import dawnTideMod.content.*;
import dawnTideMod.multicrafter.MultiCrafterBlock;
import dawnTideMod.multicrafter.type.DrawRecipe;
import dawnTideMod.multicrafter.world.AttributeMultiCrafterBlock;
import mindustry.Vars;
import mindustry.mod.ClassMap;
import mindustry.mod.*;


public class dawnTide extends Mod {
    public dawnTide(){
        // 注册 JSON 反序列化的类名映射，使 hjson 中 type: MultiCrafter / AttributeMultiCrafter / DrawRecipe 可用
        ClassMap.classes.put("MultiCrafter", MultiCrafterBlock.class);
        ClassMap.classes.put("AttributeMultiCrafter", AttributeMultiCrafterBlock.class);
        ClassMap.classes.put("DrawRecipe", DrawRecipe.class);
    }

    @Override
    public void loadContent(){
        // 依赖顺序：items/liquids 先于 blocks（blocks 里 consumeLiquid 引用自定义液体）
        dawnTideItems.load();
        dawnTideLiquids.load();
        dawnTideBlocks.load();
        super.loadContent();
        dawnTidePlanets.load();
        dawnTideTechTree.load();
        dawnTideStatuses.load();
        dawnBullets.load();
        dawnTurrets.load();
    }

    @Override
    public void init(){
        if(!Vars.headless){
            // HUD 左上角入口：一段文字，点击打开单位调试器
            Table t = new Table();
            t.setFillParent(true);
            t.top().left();
            t.touchable = Touchable.childrenOnly; // 只让子元素接收点击，不挡全屏操作
            t.visibility = () -> !Vars.state.isMenu();
            Label entry = t.add("[accent]曙光潮涌[]").padTop(80f).padLeft(12f).get();
            entry.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    new UnitEditorDialog().show();
                }
            });
            Vars.ui.hudGroup.addChild(t);
        }
    }
}
