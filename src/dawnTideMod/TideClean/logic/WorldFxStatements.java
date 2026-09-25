package dawnTideMod.TideClean.logic;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.Core;
import arc.scene.ui.layout.Table;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Sounds;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

/** 视觉 / 音频反馈类指令 */
public final class WorldFxStatements{

    private WorldFxStatements(){
    }

    private static final mindustry.entities.Effect[] LIGHT_STEPS = new mindustry.entities.Effect[]{
        lightEffect(120), lightEffect(300), lightEffect(600)
    };

    /** 静态光效：data = float[]{radius, colorRGBA, opacity} */
    private static mindustry.entities.Effect lightEffect(int lifetime){
        return new mindustry.entities.Effect(lifetime, 1000f, e -> {
            if(e.data instanceof float[] d){
                Color c = Tmp.c1.set((int)d[1]);
                float alpha = d[2] * e.fout();
                mindustry.graphics.Drawf.light(e.x, e.y, d[0], c, Mathf.clamp(alpha));
            }
        });
    }

    private static mindustry.entities.Effect lightFor(double seconds){
        double s = Math.max(1, seconds);
        if(s <= 2.5) return LIGHT_STEPS[0];
        if(s <= 6) return LIGHT_STEPS[1];
        return LIGHT_STEPS[2];
    }

    /** worldfx name x y → 放原版特效（Fx 反射按名查找），世界像素坐标 */
    public static class WorldFxStatement extends WorldStatement{
        public String name = "explosion";
        public String x = "0";
        public String y = "0";

        @Override
        public void build(Table table){
            input(table, name, v -> name = v);
            input(table, x, v -> x = v);
            input(table, y, v -> y = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String n = name;
            double px = WorldStateStatements.parseNum(x), py = WorldStateStatements.parseNum(y);
            return inst(exec -> {
                try{
                    var f = Fx.class.getField(n);
                    if(f.get(null) instanceof mindustry.entities.Effect fx){
                        fx.at((float)px, (float)py);
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "播放特效";
        }
    }

    /** worldsound name volume → 播放音效（Sounds 反射按名查找） */
    public static class WorldSoundStatement extends WorldStatement{
        public String name = "explosion";
        public String volume = "1";

        @Override
        public void build(Table table){
            input(table, name, v -> name = v);
            input(table, volume, v -> volume = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String n = name;
            double vol = WorldStateStatements.parseNum(volume);
            return inst(exec -> {
                try{
                    var f = Sounds.class.getField(n);
                    if(f.get(null) instanceof arc.audio.Sound s && !Vars.headless){
                        s.at(Core.camera.position.x, Core.camera.position.y, 1f, Mathf.clamp((float)vol));
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "播放音效";
        }
    }

    /** worldlight x y radius color duration → 动态光点（hex 颜色，秒） */
    public static class WorldLightStatement extends WorldStatement{
        public String x = "0";
        public String y = "0";
        public String radius = "80";
        public String color = "ffd59e";
        public String duration = "3";

        @Override
        public void build(Table table){
            input(table, x, v -> x = v);
            input(table, y, v -> y = v);
            input(table, radius, v -> radius = v);
            input(table, color, v -> color = v);
            input(table, duration, v -> duration = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            double px = WorldStateStatements.parseNum(x), py = WorldStateStatements.parseNum(y);
            double r = WorldStateStatements.parseNum(radius);
            double dur = WorldStateStatements.parseNum(duration);
            int rgba = Color.valueOf(color.replace("#", "")).rgba();
            return inst(exec -> {
                try{
                    if(!Vars.headless){
                        lightFor(dur).at((float)px, (float)py, 0f,
                            new float[]{(float)Math.max(8, r), rgba, 0.85f});
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "动态光点";
        }
    }

    /** worldspawnfx type team x y → 在坐标生成单位 + 出场特效 */
    public static class WorldSpawnFxStatement extends WorldStatement{
        public String type = "dagger";
        public String team = "0";
        public String x = "0";
        public String y = "0";

        @Override
        public void build(Table table){
            input(table, type, v -> type = v);
            input(table, team, v -> team = v);
            input(table, x, v -> x = v);
            input(table, y, v -> y = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String n = type;
            int teamId = (int)WorldStateStatements.parseNum(team);
            double px = WorldStateStatements.parseNum(x), py = WorldStateStatements.parseNum(y);
            return inst(exec -> {
                try{
                    var t = Vars.content.getByName(mindustry.ctype.ContentType.unit, n);
                    if(t instanceof mindustry.type.UnitType ut){
                        ut.spawn(mindustry.game.Team.get(teamId), (float)px, (float)py);
                        Fx.spawn.at((float)px, (float)py);
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "生成单位+特效";
        }
    }
}
