package dawnTideMod.TideClean.logic;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import dawnTideMod.TideClean.world.WorldState;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

/** 世界处理器扩展指令：随机 / 计数器 / 列表 / 事件监听与派发 */
public final class WorldExtStatements{

    private WorldExtStatements(){
    }

    public static class WorldRandStatement extends WorldStatement{
        public String result = "result";
        public String min = "0";
        public String max = "10";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, min, v -> min = v);
            input(table, max, v -> max = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            double lo = WorldStateStatements.parseNum(min);
            double hi = WorldStateStatements.parseNum(max);
            return inst(exec -> out.setnum(Mathf.random((float)lo, (float)hi)));
        }

        @Override
        protected String chineseName(){
            return "世界随机数";
        }
    }

    public static class WorldCounterStatement extends WorldStatement{
        public String result = "result";
        public String key = "key";
        public String op = "add"; // add / sub / set / get / reset
        public String value = "1";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, key, v -> key = v);
            input(table, op, v -> op = v);
            input(table, value, v -> value = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String k = key;
            String o = op == null ? "add" : op.trim().toLowerCase();
            double v = WorldStateStatements.parseNum(value);
            return inst(exec -> {
                double r;
                switch(o){
                    case "sub" -> r = WorldState.counter(k, -v);
                    case "set" -> { WorldState.putNum(k, v); r = v; }
                    case "get" -> r = WorldState.getNum(k, 0d);
                    case "reset" -> { WorldState.remove(k); r = 0; }
                    default -> r = WorldState.counter(k, v);
                }
                out.setnum(r);
            });
        }

        @Override
        protected String chineseName(){
            return "世界计数器";
        }
    }

    public static class WorldListStatement extends WorldStatement{
        public String result = "result";
        public String key = "list";
        public String op = "add"; // add / get / set / size / clear
        public String index = "0";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, key, v -> key = v);
            input(table, op, v -> op = v);
            input(table, index, v -> index = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String k = key;
            String o = op == null ? "add" : op.trim().toLowerCase();
            double v = WorldStateStatements.parseNum(index);
            return inst(exec -> {
                double r;
                switch(o){
                    case "get" -> r = WorldState.listGet(k, (int)v, 0d);
                    case "set" -> { r = WorldState.listSet(k, (int)v, v) ? v : 0; }
                    case "size" -> r = WorldState.listSize(k);
                    case "clear" -> { WorldState.listClear(k); r = 0; }
                    default -> { WorldState.listAdd(k, v); r = WorldState.listSize(k); }
                }
                out.setnum(r);
            });
        }

        @Override
        protected String chineseName(){
            return "世界列表";
        }
    }

    public static class WorldListenStatement extends WorldStatement{
        public String event = "wave";
        public String channel = "1";

        @Override
        public void build(Table table){
            input(table, event, v -> event = v);
            input(table, channel, v -> channel = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String ev = event;
            int ch = (int)WorldStateStatements.parseNum(channel);
            return inst(exec -> WorldState.listen(ev, ch));
        }

        @Override
        protected String chineseName(){
            return "监听世界事件";
        }
    }

    public static class WorldFireStatement extends WorldStatement{
        public String event = "custom";
        public String value = "1";

        @Override
        public void build(Table table){
            input(table, event, v -> event = v);
            input(table, value, v -> value = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String ev = event;
            Object val = WorldStateStatements.parseValue(value);
            return inst(exec -> WorldState.fire(ev, val));
        }

        @Override
        protected String chineseName(){
            return "派发世界事件";
        }
    }
}
