package dawnTideMod.content;

import arc.Core;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
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
        super("单位调试器");
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
}
