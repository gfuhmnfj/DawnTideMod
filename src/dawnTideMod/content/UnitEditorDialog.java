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

/**
 * 「曙光潮涌」单位调试器：
 * 左侧：原版+模组所有单位的贴图网格（5 列，滚动）
 * 右侧：1.血量 2.护盾 3.buff+时长 4.队伍 5.计算调整后数值 + 生成单位
 * 入口：游戏内 HUD 左上角「曙光潮涌」。
 */
public class UnitEditorDialog extends BaseDialog{

    private UnitType selected;
    private StatusEffect selectedStatus;          // null = 不上 buff
    private Team selectedTeam;

    private TextField healthField, shieldField, durationField;
    private Table resultTable;

    public UnitEditorDialog(){
        super("单位调试器");
        selectedTeam = Vars.player != null ? Vars.player.team() : Team.crux;

        cont.margin(16f);
        // 左：单位贴图网格
        cont.table(this::buildUnitList).width(320f).growY();
        cont.add().pad(16f);
        // 右：数值区
        cont.table(this::buildEditor).width(480f).grow();
        addCloseButton();
    }

    /** 左列：所有单位的贴图按钮（5 列网格，ButtonGroup 互斥单选） */
    private void buildUnitList(Table t){
        t.add("单位列表").color(Pal.accent).padBottom(8f).row();
        Table list = new Table();
        t.pane(list).grow();
        ButtonGroup<Button> group = new ButtonGroup<>();
        int i = 0;
        for(UnitType type : Vars.content.units()){
            list.button(new TextureRegionDrawable(type.uiIcon), Styles.clearTogglei, () -> select(type))
                .size(54f).pad(3f).group(group);
            if(++i % 5 == 0) list.row();
        }
    }

    /** 右列：数值调整区 */
    private void buildEditor(Table t){
        t.add("数值调整").color(Pal.accent).colspan(2).padBottom(10f).row();

        // 1. 血量
        t.add("血量").left().padTop(8f);
        healthField = t.field("200", s -> {}).growX().padTop(8f).get();
        t.row();

        // 2. 护盾
        t.add("护盾").left().padTop(8f);
        shieldField = t.field("0", s -> {}).growX().padTop(8f).get();
        t.row();

        // 3. buff 选择（4 列网格，含原版+模组全部状态效果）
        t.add("Buff").color(Pal.accent).left().colspan(2).padTop(12f).padBottom(4f).row();
        Table statusGrid = new Table();
        t.add(statusGrid).colspan(2).left();
        t.row();

        ButtonGroup<TextButton> statusGroup = new ButtonGroup<>();
        statusGrid.button("无", Styles.flatTogglet, () -> {
            selectedStatus = null;
            recalc();
        }).width(104f).pad(3f).group(statusGroup);
        int i = 1;
        for(StatusEffect st : Vars.content.statusEffects()){
            statusGrid.button(st.localizedName, Styles.flatTogglet, () -> {
                selectedStatus = st;
                recalc();
            }).width(104f).pad(3f).group(statusGroup);
            if(++i % 4 == 0) statusGrid.row();
        }

        // buff 时长（秒）
        t.add("时长(秒)").left().padTop(8f);
        durationField = t.field("5", s -> {}).growX().padTop(8f).get();
        t.row();

        // 4. 队伍（原版 6 个基础队伍，3 列网格）
        t.add("队伍").color(Pal.accent).left().colspan(2).padTop(12f).padBottom(4f).row();
        Table teamGrid = new Table();
        t.add(teamGrid).colspan(2).left();
        t.row();
        ButtonGroup<TextButton> teamGroup = new ButtonGroup<>();
        int j = 0;
        for(Team team : Team.baseTeams){
            TextButton b = teamGrid.button(team.name, Styles.flatTogglet, () -> {
                selectedTeam = team;
                recalc();
            }).width(148f).pad(3f).group(teamGroup).get();
            b.getLabel().setColor(team.color);
            if(++j % 3 == 0) teamGrid.row();
        }

        // 5. 计算调整后数值
        t.button("计算调整后数值", Styles.defaultt, this::recalc)
            .fillX().colspan(2).padTop(14f).row();
        resultTable = t.table().growX().get();
        resultTable.add("[gray]← 左侧选择单位后点击计算[]").left().row();

        // 附赠：按当前数值生成单位到屏幕中心
        t.button("生成该单位", Styles.defaultt, this::spawnUnit)
            .fillX().colspan(2).padTop(8f).row();
    }

    private void select(UnitType type){
        selected = type;
        healthField.setText((int)type.health + "");
        shieldField.setText("0");
        recalc();
    }

    /** 计算按钮：输入血量 × buff 血量倍率 = 调整后数值 */
    private void recalc(){
        resultTable.clear();
        if(selected == null){
            resultTable.add("[scarlet]请先在左侧选择一个单位[]").left().row();
            return;
        }
        float hp  = parse(healthField, selected.health);
        float sh  = parse(shieldField, 0f);
        float dur = parse(durationField, 5f);
        float mH  = selectedStatus == null ? 1f : selectedStatus.healthMultiplier;
        float mD  = selectedStatus == null ? 1f : selectedStatus.damageMultiplier;
        float mS  = selectedStatus == null ? 1f : selectedStatus.speedMultiplier;

        resultTable.add("[accent]" + selected.localizedName + "[] 调整后数值：").left().row();
        resultTable.add("血量 " + (int)hp + " × " + mH + " = [yellow]" + (int)(hp * mH) + "[]").left().row();
        resultTable.add("护盾 [yellow]" + (int)sh + "[]").left().row();
        resultTable.add("增伤 ×" + mD + "　速度 ×" + mS).left().row();
        resultTable.add("队伍 [accent]" + selectedTeam.name + "[]　buff " + dur + "秒").left().row();
    }

    /** 按当前输入生成单位（屏幕中心），血盾/队伍/buff 全部应用 */
    private void spawnUnit(){
        if(selected == null){ Vars.ui.showInfoToast("请先选择单位", 2f); return; }
        float hp  = parse(healthField, selected.health);
        float sh  = parse(shieldField, 0f);
        float dur = parse(durationField, 5f);

        Unit u = selected.spawn(selectedTeam, Core.camera.position.x, Core.camera.position.y);
        u.maxHealth(hp);
        u.health(hp);
        u.shield(sh);
        if(selectedStatus != null) u.apply(selectedStatus, dur * 60f);   // 秒→帧

        Vars.ui.showInfoToast("已生成: " + selected.localizedName, 2f);
    }

    private float parse(TextField f, float def){
        try{ return Float.parseFloat(f.getText().trim()); }
        catch(Exception e){ return def; }
    }
}
