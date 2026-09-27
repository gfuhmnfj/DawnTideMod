package dawnTideMod.TideClean.ui;

import arc.Core;
import arc.graphics.Color;
import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.scene.event.Touchable;
import arc.math.Mathf;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import dawnTideMod.TideClean.logic.EnemyPathEstimate;
import mindustry.Vars;
import mindustry.game.Saves;
import mindustry.gen.Icon;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class UnitEditorDialog extends BaseDialog{
    private static final int UNIT_COLUMNS = 5;
    private static final int STATUS_COLUMNS = 4;
    private static final int TEAM_COLUMNS = 3;
    private UnitType selected;
    private StatusEffect selectedStatus;
    private Team selectedTeam;
    private TextField healthField, shieldField, durationField;
    private Table resultTable;

    public UnitEditorDialog(){
        super("测试单位");
        selectedTeam = Vars.player != null ? Vars.player.team() : Team.crux;

        cont.margin(16f);
        cont.table(this::buildUnitList).width(320f).growY();
        cont.add().pad(16f);
        cont.table(this::buildEditor).width(480f).grow();
        addCloseButton();
    }
    private void buildUnitList(Table table){
        table.add("单位列表").color(Pal.accent).padBottom(8f).row();
        Table grid = new Table();
        table.pane(grid).grow();
        ButtonGroup<Button> group = new ButtonGroup<>();
        int index = 0;
        for(UnitType type : Vars.content.units()){
            grid.button(new TextureRegionDrawable(type.uiIcon), Styles.clearTogglei, () -> select(type))
                .size(54f).pad(3f).group(group);
            if(++index % UNIT_COLUMNS == 0) grid.row();
        }
    }

    private void buildEditor(Table table){
        table.add("数值调整").color(Pal.accent).colspan(2).padBottom(10f).row();
        table.add("血量").left().padTop(8f);
        healthField = table.field("200", text -> {}).growX().padTop(8f).get();
        table.row();
        table.add("护盾").left().padTop(8f);
        shieldField = table.field("0", text -> {}).growX().padTop(8f).get();
        table.row();
        table.add("Buff").color(Pal.accent).left().colspan(2).padTop(12f).padBottom(4f).row();
        Table statusGrid = new Table();
        table.add(statusGrid).colspan(2).left();
        table.row();
        ButtonGroup<TextButton> statusGroup = new ButtonGroup<>();
        statusGrid.button("无", Styles.flatTogglet, () -> {
            selectedStatus = null;
            recalc();
        }).width(104f).pad(3f).group(statusGroup);

        int statusIndex = 1;
        for(StatusEffect status : Vars.content.statusEffects()){
            statusGrid.button(status.localizedName, Styles.flatTogglet, () -> {
                selectedStatus = status;
                recalc();
            }).width(104f).pad(3f).group(statusGroup);
            if(++statusIndex % STATUS_COLUMNS == 0) statusGrid.row();
        }

        table.add("时长(秒)").left().padTop(8f);
        durationField = table.field("5", text -> {}).growX().padTop(8f).get();
        table.row();
        table.add("队伍").color(Pal.accent).left().colspan(2).padTop(12f).padBottom(4f).row();
        Table teamGrid = new Table();
        table.add(teamGrid).colspan(2).left();
        table.row();
        ButtonGroup<TextButton> teamGroup = new ButtonGroup<>();
        int teamIndex = 0;
        for(Team team : Team.baseTeams){
            TextButton button = teamGrid.button(team.name, Styles.flatTogglet, () -> {
                selectedTeam = team;
                recalc();
            }).width(148f).pad(3f).group(teamGroup).get();
            button.getLabel().setColor(team.color);
            if(++teamIndex % TEAM_COLUMNS == 0) teamGrid.row();
        }

        table.button("计算调整后数值", Styles.defaultt, this::recalc)
            .fillX().colspan(2).padTop(14f).row();
        resultTable = table.table().growX().get();
        resultTable.add("[gray]← 左侧选择单位后点击计算[]").left().row();

        table.button("生成该单位", Styles.defaultt, this::spawnUnit)
            .fillX().colspan(2).padTop(8f).row();
    }

    private void select(UnitType type){
        selected = type;
        healthField.setText(Integer.toString((int)type.health));
        shieldField.setText("0");
        recalc();
    }

    private void recalc(){
        resultTable.clear();
        if(selected == null){
            resultTable.add("[scarlet]请先在左侧选择一个单位[]").left().row();
            return;
        }

        float health = parse(healthField, selected.health);
        float shield = parse(shieldField, 0f);
        float duration = parse(durationField, 5f);
        float healthMult = selectedStatus == null ? 1f : selectedStatus.healthMultiplier;
        float damageMult = selectedStatus == null ? 1f : selectedStatus.damageMultiplier;
        float speedMult = selectedStatus == null ? 1f : selectedStatus.speedMultiplier;
        resultTable.add("[accent]" + selected.localizedName + "[] 调整后数值：").left().row();
        resultTable.add("血量 " + (int)health + " × " + healthMult + " = [yellow]" + (int)(health * healthMult) + "[]").left().row();
        resultTable.add("护盾 [yellow]" + (int)shield + "[]").left().row();
        resultTable.add("增伤 ×" + damageMult + "　速度 ×" + speedMult).left().row();
        resultTable.add("队伍 [accent]" + selectedTeam.name + "[]　buff " + duration + "秒").left().row();
    }

    private void spawnUnit(){
        if(selected == null){
            Vars.ui.showInfoToast("请先选择单位", 2f);
            return;
        }

        float health = parse(healthField, selected.health);
        float shield = parse(shieldField, 0f);
        float duration = parse(durationField, 5f);
        Unit unit = selected.spawn(selectedTeam, Core.camera.position.x, Core.camera.position.y);
        unit.maxHealth(health);
        unit.health(health);
        unit.shield(shield);
        if(selectedStatus != null) unit.apply(selectedStatus, duration * 60f);
        Vars.ui.showInfoToast("已生成: " + selected.localizedName, 2f);
    }

    private float parse(TextField field, float fallback){
        try{
            return Float.parseFloat(field.getText().trim());
        }catch(Exception ignored){
            return fallback;
        }
    }

    public static class DawnButton extends Button{

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

    /** HUD 左上角入口（原 DawnTide.buildHudEntry，按归属规则搬入本文件） */
    public static void installHudEntry(){
        if(Vars.headless || Core.scene == null || Vars.ui == null || Vars.ui.hudGroup == null) return;
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

    public static class DawnControlPanel{

        /** 速度档位（可用 [-]/[+] 按钮动态增删） */
        public static final Seq<Float> speedSteps = Seq.with(0.5f, 1f, 2f, 4f);

        public static float speed = 1f;

        private static boolean installed = false;
        private static Table panel;
        private static Table box;

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

            box = new Table(Styles.black6);
            box.margin(6f);
            rebuildBox();

            panel.add(box);
            Vars.ui.hudGroup.addChild(panel);
        }

        /** 设置菜单「DTMod辅助」分类（原 DawnTide.installSettingsCategory，按归属规则搬入本文件） */
        private static boolean settingsInstalled = false;

        public static void installSettings(){
            if(settingsInstalled || Vars.headless) return;
            if(Vars.ui == null || Vars.ui.settings == null) return;
            settingsInstalled = true;
            Vars.ui.settings.addCategory("DTMod辅助", Icon.settings, table -> {
                table.button("变速调节", Icon.hammer, Styles.flatt, () -> DawnControlPanel.showSpeedDialog()).growX().margin(4f).row();
                table.button("单位调试器", Icon.pencil, Styles.flatt, () -> new UnitEditorDialog().show()).growX().margin(4f).row();
                table.add("[lightgray]曙光潮涌辅助工具：调节游戏速度、生成测试单位[]").left().padTop(8f);
            });
        }

        private static void rebuildBox(){
            box.clear();
            box.add("[accent]速度[]").padRight(6f);

            ButtonGroup<TextButton> group = new ButtonGroup<>();
            group.setMinCheckCount(1);
            group.setMaxCheckCount(1);

            for(int i = 0; i < speedSteps.size; i++){
                float step = speedSteps.get(i);
                TextButton button = new TextButton(labelOf(step), Styles.flatTogglet);
                button.clicked(() -> setSpeed(step));
                group.add(button);
                box.add(button).size(52f, 34f).padRight(2f);
            }

            group.setChecked(labelOf(nearestStep(speed)));

            // [+] 新增速度档（当前最大档 ×2）
            box.button("[+]", Styles.flatt, () -> {
                float next = Mathf.clamp(speedSteps.peek() * 2f, 0.25f, 16f);
                if(next <= speedSteps.peek() + 0.001f){
                    Vars.ui.showInfoToast("已达最大速度档", 1f);
                    return;
                }
                speedSteps.add(next);
                setSpeed(next);
                rebuildBox();
            }).size(34f, 34f).padLeft(4f);

            // [-] 删除最后一个速度档
            box.button("[-]", Styles.flatt, () -> {
                if(speedSteps.size <= 1){
                    Vars.ui.showInfoToast("至少保留一个速度档", 1f);
                    return;
                }
                float removed = speedSteps.pop();
                if(Mathf.equal(speed, removed)) setSpeed(speedSteps.peek());
                rebuildBox();
            }).size(34f, 34f);

            TextButton pathButton = new TextButton("[accent]敌人路径[]", Styles.flatTogglet);
            pathButton.clicked(() -> {
                EnemyPathEstimate.toggle();
                pathButton.setText(EnemyPathEstimate.enabled ? "[accent]路径：开[]" : "[accent]敌人路径[]");
            });
            box.add(pathButton).size(96f, 34f).padLeft(10f);

            box.button("[accent]快速存档[]", Styles.flatt, DawnControlPanel::quickSave).size(96f, 34f).padLeft(6f);
        }

        private static String labelOf(float step){
            return Strings.autoFixed(step, 2) + "x";
        }

        private static float nearestStep(float value){
            float best = speedSteps.first();
            for(float s : speedSteps){
                if(Math.abs(s - value) < Math.abs(best - value)) best = s;
            }
            return best;
        }

        public static void setSpeed(float value){
            setSpeed(value, true);
        }

        public static void setSpeed(float value, boolean toast){
            speed = Mathf.clamp(value, 0.25f, 16f);
            applyDeltaProvider();
            if(toast) Vars.ui.showInfoToast("游戏速度：" + speed + "x", 1.2f);
        }

        /** 设置菜单「变速调节」滑条对话框 */
        public static void showSpeedDialog(){
            BaseDialog dialog = new BaseDialog("变速调节");
            dialog.cont.margin(16f);

            Label valueLabel = new Label(Strings.autoFixed(speed, 2) + "x");
            valueLabel.setFontScale(1.2f);

            Slider slider = new Slider(0.25f, 16f, 0.25f, false);
            slider.setValue(speed);
            slider.changed(() -> {
                setSpeed(slider.getValue(), false);
                valueLabel.setText(Strings.autoFixed(speed, 2) + "x");
            });

            dialog.cont.add("游戏速度").padRight(10f);
            dialog.cont.add(slider).width(320f);
            dialog.cont.add(valueLabel).padLeft(10f).row();
            dialog.cont.button("恢复 1x", () -> {
                setSpeed(1f, false);
                slider.setValue(1f);
                valueLabel.setText("1x");
            }).padTop(12f);

            dialog.addCloseButton();
            dialog.show();
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
    }
}
