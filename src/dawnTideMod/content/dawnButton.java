package dawnTideMod.content;

import arc.graphics.Color;
import arc.scene.style.Drawable;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.util.Align;
import mindustry.ui.Styles;


public class dawnButton extends Button{

    public final Label label;

    public dawnButton(String text, Runnable onClick){
        this(text);
        changed(onClick);
    }

    public dawnButton(String text){
        super(Styles.defaultt);
        margin(8f);
        label = add(text).grow().get();
        label.setAlignment(Align.center);
        marginTop(6f).marginBottom(8f);
        setSize(getPrefWidth(), getPrefHeight());
    }


    public dawnButton(String text, Drawable icon, Runnable onClick){
        this(text);
        add(new Image(icon)).size(24f).padRight(6f);
        getChildren().swap(0, 1);
        changed(onClick);
    }

    public dawnButton text(String text){
        label.setText(text);
        return this;
    }

    @Override
    public void draw(){
        label.setColor(isOver() ? Color.white : Color.lightGray);
        if(isDisabled()) label.setColor(Color.darkGray);
        super.draw();
    }

    public static dawnButton toggle(String text, boolean initial, Runnable onChange){
        dawnButton b = new dawnButton(text);
        b.setChecked(initial);
        b.changed(onChange);
        return b;
    }
}
