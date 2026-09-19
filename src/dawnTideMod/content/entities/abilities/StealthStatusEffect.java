package dawnTideMod.content.entities.abilities;

import dawnTideMod.graphics.DawnShaders;
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


public class StealthStatusEffect extends StatusEffect{

    public float opacity = 0.35f;
    private final Seq<Unit> activeUnits = new Seq<>();
    private final Seq<Unit> drawnUnits = new Seq<>();
    private boolean installed;


    public StealthStatusEffect(String name){
        super(name);
    }

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

    private void installTurretFilters(){
        for(Block block : Vars.content.blocks()){
            if(block instanceof Turret turret){
                Boolf<Unit> previous = turret.unitFilter;
                turret.unitFilter = unit -> previous.get(unit) && !unit.hasEffect(this);
            }
        }
    }

    private void clearAutomaticTargets(){
        if(activeUnits.isEmpty() || !Vars.state.isGame()) return; // 检查活动单位列表是否为空或游戏是否未进行
        activeUnits.removeAll(unit -> !unit.isAdded() || !unit.hasEffect(this)); // 移除不符合条件的单位
        if(activeUnits.isEmpty()) return;
        Groups.build.each(this::clearAutomaticTarget);
    }

    private void clearTargetsFor(Unit unit){
        if(!Vars.state.isGame()) return;
        Groups.build.each(building -> clearAutomaticTarget(building, unit)); // 遍历所有建筑并清除指定单位的目标
    }

    private void clearAutomaticTarget(Building building){
        if(building instanceof TurretBuild turret && !turret.controlled() && !turret.logicControlled()
                && turret.target instanceof Unit unit && unit.hasEffect(this)){
            turret.target = null;
        }else if(building instanceof TractorBeamBuild tractor && tractor.target != null
                && tractor.target.hasEffect(this)){
            tractor.target = null;
        }
    }

    private void clearAutomaticTarget(Building building, Unit unit){
        if(building instanceof TurretBuild turret && !turret.controlled() && !turret.logicControlled()
                && turret.target == unit){
            turret.target = null;
        }else if(building instanceof TractorBeamBuild tractor && tractor.target == unit){
            tractor.target = null;
        }
    }

    private void registerStealthDraws(){
        restoreNormalDraw();
        if(activeUnits.isEmpty()) return;
        DawnShaders.load();
        if(DawnShaders.stealthAlpha == null) return;
        DawnShaders.stealthAlpha.opacity = opacity;
        for(Unit unit : activeUnits){
            if(!unit.isAdded() || !unit.hasEffect(this)) continue;
            int index = findDrawIndex(unit);
            if(index < 0) continue;
            drawnUnits.add(unit);
            Groups.draw.removeIndex(unit, index);
            Draw.draw(drawLayer(unit), () -> {
                Draw.shader(DawnShaders.stealthAlpha);
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
        if(unit instanceof Segmentc segment){
            return unit.type.groundLayer + segment.segmentIndex() / 4000f
                    * (unit.type.segmentLayerOrder ? 1f : -1f) + (unit.type.segmentLayerOrder ? 0f : 0.01f);
        }
        return unit.type.groundLayer + Math.min(unit.type.hitSize / 4000f, 0.01f);
    }

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
