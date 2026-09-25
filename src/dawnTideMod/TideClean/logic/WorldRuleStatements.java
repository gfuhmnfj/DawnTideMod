package dawnTideMod.TideClean.logic;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.content.Weathers;
import mindustry.gen.Sounds;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;
import dawnTideMod.TideClean.ui.UnitEditorDialog.DawnControlPanel;
import dawnTideMod.TideClean.world.WorldState;

/** 规则与玩法控制类指令 */
public final class WorldRuleStatements{

    private WorldRuleStatements(){
    }

    /** worldrule key value → 写游戏规则（白名单） */
    public static class WorldRuleStatement extends WorldStatement{
        public String key = "waveSpacing";
        public String value = "120";

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
            input(table, value, v -> value = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String k = key;
            double v = WorldStateStatements.parseNum(value);
            return inst(exec -> {
                try{
                    var rules = Vars.state.rules;
                    switch(k){
                        case "waves" -> rules.waves = v > 0;
                        case "waveSpacing" -> rules.waveSpacing = (float)(v * 60f);
                        case "wave" -> Vars.state.wave = (int)v;
                        case "infiniteResources" -> rules.infiniteResources = v > 0;
                        case "unitBuildSpeedMultiplier" -> rules.unitBuildSpeedMultiplier = (float)v;
                        case "unitDamageMultiplier" -> rules.unitDamageMultiplier = (float)v;
                        case "blockDamageMultiplier" -> rules.blockDamageMultiplier = (float)v;
                        case "buildSpeedMultiplier" -> rules.buildSpeedMultiplier = (float)v;
                        case "attackMode" -> rules.attackMode = v > 0;
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "修改游戏规则";
        }
    }

    /** worldweather name duration intensity → 触发天气（秒） */
    public static class WorldWeatherStatement extends WorldStatement{
        public String name = "sandstorm";
        public String duration = "60";
        public String intensity = "1";

        @Override
        public void build(Table table){
            input(table, name, v -> name = v);
            input(table, duration, v -> duration = v);
            input(table, intensity, v -> intensity = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String n = name;
            double dur = WorldStateStatements.parseNum(duration) * 60f;
            double inten = WorldStateStatements.parseNum(intensity);
            return inst(exec -> {
                try{
                    var field = Weathers.class.getField(n);
                    if(field.get(null) instanceof mindustry.type.Weather w){
                        mindustry.type.Weather.createWeather(w, (float)inten, (float)dur, 0f, 0f);
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "触发天气";
        }
    }

    /** worldspeed mult → 全局变速 */
    public static class WorldSpeedStatement extends WorldStatement{
        public String mult = "1";

        @Override
        public void build(Table table){
            input(table, mult, v -> mult = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            double m = WorldStateStatements.parseNum(mult);
            return inst(exec -> {
                try{
                    DawnControlPanel.setSpeed((float)m);
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "设置游戏速度";
        }
    }

    /** worldtoast message → 屏幕消息 */
    public static class WorldToastStatement extends WorldStatement{
        public String message = "message";

        @Override
        public void build(Table table){
            input(table, message, v -> message = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String msg = message;
            return inst(exec -> {
                try{
                    if(!Vars.headless && Vars.ui != null){
                        Vars.ui.showInfoToast(msg, 3f);
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "屏幕消息";
        }
    }

    /** worldsector action name → 解锁区块（action: 0=解锁 1=查询） */
    public static class WorldSectorStatement extends WorldStatement{
        public String action = "0";
        public String name = "groundZero";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, action, v -> action = v);
            input(table, name, v -> name = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            int act = (int)WorldStateStatements.parseNum(action);
            String n = name;
            return inst(exec -> {
                try{
                    for(var preset : Vars.content.sectors()){
                        if(preset.name.equals(n)){
                            if(act == 0){
                                preset.alwaysUnlocked = true;
                                preset.quietUnlock();
                                out.setnum(1);
                            }else{
                                out.setnum(preset.sector != null && preset.sector.unlocked() ? 1 : 0);
                            }
                            return;
                        }
                    }
                    out.setnum(0);
                }catch(Throwable e){
                    out.setnum(-1);
                }
            });
        }

        @Override
        protected String chineseName(){
            return "解锁区块";
        }
    }

    /** worldresearch name → 解锁科技内容 */
    public static class WorldResearchStatement extends WorldStatement{
        public String name = "copper";

        @Override
        public void build(Table table){
            input(table, name, v -> name = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String n = name;
            return inst(exec -> {
                try{
                    mindustry.ctype.ContentType[] types = {
                        mindustry.ctype.ContentType.item, mindustry.ctype.ContentType.block,
                        mindustry.ctype.ContentType.unit, mindustry.ctype.ContentType.liquid,
                        mindustry.ctype.ContentType.status
                    };
                    for(var t : types){
                        var c = Vars.content.getByName(t, n);
                        if(c instanceof mindustry.ctype.UnlockableContent u){
                            u.unlock();
                            return;
                        }
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "解锁科技";
        }
    }

    /** worldincome id interval item amount → 周期给默认队核心发资源（秒） */
    public static class WorldIncomeStatement extends WorldStatement{
        public String id = "income1";
        public String interval = "30";
        public String item = "copper";
        public String amount = "10";

        @Override
        public void build(Table table){
            input(table, id, v -> id = v);
            input(table, interval, v -> interval = v);
            input(table, item, v -> item = v);
            input(table, amount, v -> amount = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String i = id, it = item;
            double sec = WorldStateStatements.parseNum(interval);
            double amt = WorldStateStatements.parseNum(amount);
            return inst(exec -> WorldState.addIncome(i, Math.max(1, sec * 60), it, amt));
        }

        @Override
        protected String chineseName(){
            return "周期资源收入";
        }
    }

    /** worldincomeoff id → 停止周期收入 */
    public static class WorldIncomeOffStatement extends WorldStatement{
        public String id = "income1";

        @Override
        public void build(Table table){
            input(table, id, v -> id = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String i = id;
            return inst(exec -> WorldState.removeIncome(i));
        }

        @Override
        protected String chineseName(){
            return "停止资源收入";
        }
    }
}
