package dawnTideMod.TideClean.logic;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.Pathfinder;
import mindustry.game.EventType.Trigger;
import mindustry.game.EventType.WorldLoadEvent;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.ui.Fonts;
import mindustry.world.Tile;

public class EnemyPathEstimate{

    public static boolean enabled = false;

    private static final Seq<Seq<Tile>> paths = new Seq<>();
    private static final Color routeColor = Color.valueOf("ff6b5e");
    private static final Color spawnColor = Color.valueOf("ffd37f");
    private static final Color targetColor = Color.valueOf("ff4d4d");

    private static final int maxRoutes = 4;
    private static final int maxSteps = 700;

    private static float timer = 60f;

    public static void register(){
        Events.run(Trigger.draw, EnemyPathEstimate::draw);
        Events.on(WorldLoadEvent.class, e -> paths.clear());
    }

    public static void toggle(){
        enabled = !enabled;
        timer = 60f;
        if(!enabled) paths.clear();
    }

    public static Seq<Seq<Tile>> routes(){
        return paths;
    }

    private static void draw(){
        if(Vars.headless || !enabled) return;

        timer += Time.delta;
        if(timer >= 60f){
            timer = 0f;
            recompute();
        }

        if(paths.isEmpty()) return;

        Draw.draw(Layer.overlayUI, () -> {
            for(Seq<Tile> route : paths){
                if(route.size < 2) continue;

                Draw.color(routeColor, 0.9f);
                Lines.stroke(3f);
                for(int i = 0; i < route.size - 1; i++){
                    Tile a = route.get(i), b = route.get(i + 1);
                    Lines.line(a.worldx(), a.worldy(), b.worldx(), b.worldy());
                }

                Tile start = route.first(), end = route.peek();

                Draw.color(spawnColor);
                Fill.circle(start.worldx(), start.worldy(), 7f);

                Draw.color(targetColor);
                Fill.circle(end.worldx(), end.worldy(), 9f);
                Lines.stroke(2f);
                Lines.circle(end.worldx(), end.worldy(), 18f);
            }

            float scale = Fonts.outline.getScaleX();
            Fonts.outline.getData().setScale(0.6f);
            Draw.color(routeColor);
            Tile end = paths.first().peek();
            Fonts.outline.draw("[scarlet]预计进攻路线[]", end.worldx(), end.worldy() + 30f, Align.center);
            Fonts.outline.getData().setScale(scale);
            Draw.reset();
        });
    }

    private static void recompute(){
        paths.clear();
        if(!Vars.state.isGame() || Vars.world == null) return;

        Team enemy = Vars.state.rules.waveTeam;
        Pathfinder.Flowfield field = Vars.pathfinder.getField(enemy, Pathfinder.costGround, Pathfinder.fieldCore);
        if(field == null || !field.hasCompleteWeights()) return;

        Seq<Tile> starts = new Seq<>();
        for(Tile spawn : Vars.spawner.getSpawns()){
            if(spawn != null) starts.add(spawn);
        }

        for(Unit unit : Groups.unit){
            if(unit.team != enemy) continue;
            Tile tile = unit.tileOn();
            if(tile != null) starts.add(tile);
        }

        for(Tile start : starts){
            if(paths.size >= maxRoutes) break;
            Seq<Tile> route = trace(field, start);
            if(route.size > 1) paths.add(route);
        }
    }

    private static Seq<Tile> trace(Pathfinder.Flowfield field, Tile start){
        Seq<Tile> out = new Seq<>();
        Tile current = start, previous = null;

        for(int i = 0; i < maxSteps && current != null; i++){
            out.add(current);

            Tile next = field.getNextTile(current, true);
            if(next == null || next == current || next == previous) break;

            previous = current;
            current = next;
        }

        return out;
    }
}
