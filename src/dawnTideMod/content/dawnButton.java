package dawnTideMod.content;

import arc.graphics.Color;
import arc.scene.style.Drawable;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.util.Align;
import mindustry.ui.Styles;

public class DawnButton extends Button{

    public final Label label;

    public DawnButton(String text){
        super(Styles.defaultt);
        margin(8f);
        marginTop(6f).marginBottom(8f);

        label = add(text).grow().get();
        label.setAlignment(Align.center);

        setSize(getPrefWidth(), getPrefHeight());
    }

    public DawnButton(String text, Runnable onClick){
        this(text);
        changed(onClick);
    }

    public DawnButton(String text, Drawable icon, Runnable onClick){
        this(text);
        add(new Image(icon)).size(24f).padRight(6f);
        getChildren().swap(0, 1);
        changed(onClick);
    }

    public DawnButton text(String text){
        label.setText(text);
        return this;
    }

    public static DawnButton toggle(String text, boolean initial, Runnable onChange){
        DawnButton button = new DawnButton(text);
        button.setChecked(initial);
        button.changed(onChange);
        return button;
    }

    @Override
    public void draw(){
        label.setColor(isOver() ? Color.white : Color.lightGray);
        if(isDisabled()) label.setColor(Color.darkGray);
        super.draw();
    }
}
