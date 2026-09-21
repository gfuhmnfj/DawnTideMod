package dawnTideMod.logic;

import arc.scene.ui.layout.Table;
import dawnTideMod.world.WorldState;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

public final class WorldTimerStatements{

    private WorldTimerStatements(){
    }

    public static class WorldTimeStatement extends WorldStatement{
        public String result = "time";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(WorldState.time()));
        }

        @Override
        protected String chineseName(){
            return "读取世界时间";
        }
    }

    public static class WorldTimerStatement extends WorldStatement{
        public String key = "timer";
        public float ticks = 60f;

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
            input(table, String.valueOf(ticks), v -> ticks = parse(v, 60f));
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            return inst(exec -> WorldState.setTimer(key, ticks));
        }

        @Override
        protected String chineseName(){
            return "设置计时器";
        }
    }

    public static class WorldTimerResetStatement extends WorldStatement{
        public String key = "timer";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(WorldState.timerDone(key) ? 1 : 0));
        }

        @Override
        protected String chineseName(){
            return "计时器到期?";
        }
    }

    public static class WorldOnceStatement extends WorldStatement{
        public String key = "once";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(WorldState.once(key) ? 1 : 0));
        }

        @Override
        protected String chineseName(){
            return "仅执行一次";
        }
    }

    public static class WorldOnceResetStatement extends WorldStatement{
        public String key = "once";

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            return inst(exec -> WorldState.resetOnce(key));
        }

        @Override
        protected String chineseName(){
            return "重置一次性锁";
        }
    }

    static float parse(String text, float def){
        try{
            return Float.parseFloat(text.trim());
        }catch(Exception e){
            return def;
        }
    }
}
