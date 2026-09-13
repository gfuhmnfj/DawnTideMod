package dawnTideMod.content.entities.abilities;

import arc.Events;
import arc.func.Boolf;
import arc.graphics.g2d.Draw;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.entities.units.StatusEntry;
import mindustry.game.EventType.ContentInitEvent;
import mindustry.game.EventType.ResetEvent;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Segmentc;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.TractorBeamTurret.TractorBeamBuild;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild;
import dawnTideMod.graphics.AnnihilationShaders;

/**
 * 隐身状态效果：阻止炮塔自动索敌，并以较低透明度重新绘制受影响的单位。
 *
 * 注意：项目编译目标为 Java 11，不允许使用 instanceof 模式匹配等 Java 16+ 语法。
 */
public class StealthStatusEffect extends StatusEffect{
    /** 隐身单位重新绘制时的最终透明度。 */
    public float opacity = 0.35f;
    /** 当前携带该效果的单位；使用对象身份检查避免重复记录。 */
    private final Seq<Unit> activeUnits = new Seq<>();
    /** 当前帧内暂时从常规绘制组移除的单位。 */
    private final Seq<Unit> drawnUnits = new Seq<>();
    private boolean installed;

    public StealthStatusEffect(String name){
        super(name);
        install();
    }

    /** 内容创建后只注册一次索敌与绘制事件。 */
    public void install(){
        if(installed) return;
        installed = true;
        Events.on(ContentInitEvent.class, event -> installTurretFilters());
        Events.on(ResetEvent.class, event -> clearUnits());
        Events.run(Trigger.afterGameUpdate, this::clearAutomaticTargets);
        if(!Vars.headless){
            Events.run(Trigger.draw, this::registerStealthDraws);
            Events.run(Trigger.postDraw, this::restoreNormalDraw);
        }
    }

    @Override
    public void applied(Unit unit, float time, boolean extend){
        super.applied(unit, time, extend);
        if(!activeUnits.contains(unit, true)) activeUnits.add(unit);
        clearTargetsFor(unit);
    }

    @Override
    public void update(Unit unit, StatusEntry entry){
        super.update(unit, entry);
        if(!activeUnits.contains(unit, true)) activeUnits.add(unit);
    }

    @Override
    public void onRemoved(Unit unit){
        activeUnits.remove(unit, true);
    }

    /** 在每座炮塔原有筛选器上追加隐身判断，不覆盖原本的索敌规则。 */
    private void installTurretFilters(){
        for(Block block : Vars.content.blocks()){
            //Java 11：不能用 instanceof 模式匹配，先判断再强转
            if(block instanceof Turret){
                Turret turret = (Turret)block;
                Boolf<Unit> previous = turret.unitFilter;
                turret.unitFilter = unit -> previous.get(unit) && !unit.hasEffect(this);
            }
        }
    }

    /** 清除已保留的自动目标，但保留玩家和逻辑控制的炮塔目标。 */
    private void clearAutomaticTargets(){
        if(activeUnits.isEmpty() || !Vars.state.isGame()) return;
        activeUnits.removeAll(unit -> !unit.isAdded() || !unit.hasEffect(this));
        if(activeUnits.isEmpty()) return;
        Groups.build.each(this::clearAutomaticTarget);
    }

    private void clearTargetsFor(Unit unit){
        if(!Vars.state.isGame()) return;
        Groups.build.each(building -> clearAutomaticTarget(building, unit));
    }

    private void clearAutomaticTarget(Building building){
        if(building instanceof TurretBuild){
            TurretBuild turret = (TurretBuild)building;
            if(!turret.controlled() && !turret.logicControlled() && turret.target instanceof Unit){
                Unit target = (Unit)turret.target;
                if(target.hasEffect(this)){
                    turret.target = null;
                }
            }
        }else if(building instanceof TractorBeamBuild){
            TractorBeamBuild tractor = (TractorBeamBuild)building;
            if(tractor.target != null && tractor.target.hasEffect(this)){
                tractor.target = null;
            }
        }
    }

    private void clearAutomaticTarget(Building building, Unit unit){
        if(building instanceof TurretBuild){
            TurretBuild turret = (TurretBuild)building;
            if(!turret.controlled() && !turret.logicControlled() && turret.target == unit){
                turret.target = null;
            }
        }else if(building instanceof TractorBeamBuild){
            TractorBeamBuild tractor = (TractorBeamBuild)building;
            if(tractor.target == unit){
                tractor.target = null;
            }
        }
    }

    /** 用一次包裹 Shader 的整单位绘制替代常规绘制组调用。 */
    private void registerStealthDraws(){
        //shader 需要 GL 上下文，只能在渲染阶段惰性加载（幂等）
        AnnihilationShaders.load();
        restoreNormalDraw();
        if(activeUnits.isEmpty() || AnnihilationShaders.stealthAlpha == null) return;
        AnnihilationShaders.stealthAlpha.opacity = opacity;
        for(Unit unit : activeUnits){
            if(!unit.isAdded() || !unit.hasEffect(this)) continue;
            int index = findDrawIndex(unit);
            if(index < 0) continue;
            drawnUnits.add(unit);
            Groups.draw.removeIndex(unit, index);
            Draw.draw(drawLayer(unit), () -> {
                Draw.shader(AnnihilationShaders.stealthAlpha);
                unit.draw();
                Draw.shader();
                Draw.reset();
            });
        }
    }

    private int findDrawIndex(Unit unit){
        for(int i = 0; i < Groups.draw.size(); i++){
            if(Groups.draw.index(i) == unit) return i;
        }
        return -1;
    }

    private float drawLayer(Unit unit){
        if(!unit.isAdded()) return Draw.z();
        if(unit.elevation > 0.5f || unit.type.flying && unit.dead) return unit.type.flyingLayer;
        if(unit instanceof Segmentc){
            Segmentc segment = (Segmentc)unit;
            return unit.type.groundLayer + segment.segmentIndex() / 4000f
                * (unit.type.segmentLayerOrder ? 1f : -1f) + (unit.type.segmentLayerOrder ? 0f : 0.01f);
        }
        return unit.type.groundLayer + Math.min(unit.type.hitSize / 4000f, 0.01f);
    }

    /** 恢复带索引的绘制组成员关系，保证后续帧的实体索引有效。 */
    private void restoreNormalDraw(){
        for(int i = drawnUnits.size - 1; i >= 0; i--){
            Unit unit = drawnUnits.get(i);
            if(unit.isAdded()) unit.setIndex__draw(Groups.draw.addIndex(unit));
        }
        drawnUnits.clear();
    }

    private void clearUnits(){
        restoreNormalDraw();
        activeUnits.clear();
    }
}
