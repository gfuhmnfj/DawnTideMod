package dawnTideMod.content;

import arc.Core;
import arc.math.Mathf;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Time;
import dawnTideMod.TideClean.ui.logic.EnemyPathEstimate;
import mindustry.Vars;
import mindustry.game.Saves;
import mindustry.ui.Styles;

public class DawnControlPanel{

    public static final float[] speedSteps = {0.5f, 1f, 2f, 4f};
    public static final String[] speedLabels = {"0.5x", "1x", "2x", "4x"};

    public static float speed = 1f;

    private static boolean installed = false;
    private static Table panel;

    public static void install(){
        if(installed || Vars.headless) return;
        if(Core.scene == null || Vars.ui == null || Vars.ui.hudGroup == null) return;
        installed = true;

        applyDeltaProvider();

        panel = new Table();
        panel.setFillParent(true);
        panel.bottom().left();
        panel.marginBottom(10f);
        panel.marginLeft(10f);
        panel.visibility = () -> !Vars.state.isMenu();

        Table box = new Table(Styles.black6);
        box.margin(6f);

        box.add("[accent]速度[]").padRight(6f);

        ButtonGroup<TextButton> group = new ButtonGroup<>();
        group.setMinCheckCount(1);
        group.setMaxCheckCount(1);

        for(int i = 0; i < speedSteps.length; i++){
            float step = speedSteps[i];
            TextButton button = new TextButton(speedLabels[i], Styles.flatTogglet);
            button.clicked(() -> setSpeed(step));
            group.add(button);
            box.add(button).size(52f, 34f).padRight(2f);
        }

        group.setChecked(speedLabels[indexOf(speed)]);

        TextButton pathButton = new TextButton("[accent]敌人路径[]", Styles.flatTogglet);
        pathButton.clicked(() -> {
            EnemyPathEstimate.toggle();
            pathButton.setText(EnemyPathEstimate.enabled ? "[accent]路径：开[]" : "[accent]敌人路径[]");
        });
        box.add(pathButton).size(96f, 34f).padLeft(10f);

        box.button("[accent]快速存档[]", Styles.flatt, DawnControlPanel::quickSave).size(96f, 34f).padLeft(6f);

        panel.add(box);
        Vars.ui.hudGroup.addChild(panel);
    }

    public static void unload(){
        speed = 1f;
        applyDeltaProvider();
        if(panel != null){
            panel.remove();
            panel = null;
        }
        installed = false;
    }

    public static void setSpeed(float value){
        speed = Mathf.clamp(value, 0.25f, 8f);
        applyDeltaProvider();
        Vars.ui.showInfoToast("游戏速度：" + speed + "x", 1.2f);
    }

    private static void applyDeltaProvider(){
        float mult = speed;
        Time.setDeltaProvider(() -> {
            float result = Core.graphics.getDeltaTime() * 60f * mult;
            return (Float.isNaN(result) || Float.isInfinite(result)) ? 1f
                : Mathf.clamp(result, 0.0001f, Vars.maxDeltaClient * Math.max(1f, mult));
        });
    }

    public static void quickSave(){
        if(Vars.headless || !Vars.state.isGame()) return;

        try{
            Saves.SaveSlot current = Vars.control.saves.getCurrent();
            if(current != null){
                current.save();
                Vars.ui.showInfoToast("已覆盖当前存档", 1.5f);
            }else{
                String name = Vars.state.map == null ? "曙光潮涌" : Vars.state.map.name();
                Vars.control.saves.addSave(name);
                Vars.ui.showInfoToast("已新建存档：" + name, 1.5f);
            }
        }catch(Throwable error){
            Log.err(error);
            Vars.ui.showInfoToast("存档失败：" + error.getMessage(), 3f);
        }
    }

    private static int indexOf(float value){
        for(int i = 0; i < speedSteps.length; i++){
            if(Mathf.equal(speedSteps[i], value)) return i;
        }
        return 1;
    }
}
