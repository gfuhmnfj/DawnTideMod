package dawnTideMod.content.ui;

import arc.graphics.Color;
import arc.scene.style.Drawable;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.util.Align;
import mindustry.ui.Styles;

public class button extends Button{public Label label;
    public button(String text, Runnable onClick){
        this(text);
        changed(onClick);
    }
    public button(String text){
        super(Styles.defaultt);
        margin(8f);
        label = add(text).grow().padTop(6f).padBottom(8f).get(); // 按钮即表格：add() 往里排内容
        label.setAlignment(Align.center);
        setSize(getPrefWidth(), getPrefHeight());
    }
    /*public button(String text, Drawable icon, Runnable onClick){
        this(text);
        add(new Image(icon)).size(24f).padRight(6f);
        getChildren().swap(0, 1);
        changed(onClick);
    }
    public button(Label label) {
        super(Styles.defaultt);
        this.label = label;
    }
    //public button() {this("曙光潮涌");}
    /*public button text(String text){
        label.setText(text);
        return this;
    }*/
    @Override
    public void draw(){
        label.setColor(isOver() ? Color.white : Color.lightGray);
        if(isDisabled()) label.setColor(Color.darkGray);
        super.draw();
    }
    /*public static button toggle(String text, boolean initial, Runnable onChange){
        button b = new button(text);
        b.setChecked(initial);
        b.changed(onChange);
        return b;
    }*/
}