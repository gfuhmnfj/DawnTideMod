package dawnTideMod.TideClean.logic;

import arc.scene.ui.layout.Table;
import dawnTideMod.TideClean.world.WorldState;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

/** 数据结构类指令：列表 / 交换 / 类型转换 */
public final class WorldDataStatements{

    private WorldDataStatements(){
    }

    /** worldpush name value mode → 列表压入（mode: 0=FIFO 1=LIFO） */
    public static class WorldPushStatement extends WorldStatement{
        public String name = "queue";
        public String value = "0";
        public String mode = "1";

        @Override
        public void build(Table table){
            input(table, name, v -> name = v);
            input(table, value, v -> value = v);
            input(table, mode, v -> mode = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String n = name;
            double v = WorldStateStatements.parseNum(value);
            boolean lifo = WorldStateStatements.parseNum(mode) > 0;
            return inst(exec -> WorldState.push(n, v, lifo));
        }

        @Override
        protected String chineseName(){
            return "列表压入";
        }
    }

    /** worldpop name result → 列表弹出（空返回 NaN→0） */
    public static class WorldPopStatement extends WorldStatement{
        public String name = "queue";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, name, v -> name = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String n = name;
            return inst(exec -> {
                double v = WorldState.pop(n);
                out.setnum(Double.isNaN(v) ? 0 : v);
            });
        }

        @Override
        protected String chineseName(){
            return "列表弹出";
        }
    }

    /** worldlistsize name result → 列表长度 */
    public static class WorldListSizeStatement extends WorldStatement{
        public String name = "queue";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, name, v -> name = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String n = name;
            return inst(exec -> out.setnum(WorldState.listSize(n)));
        }

        @Override
        protected String chineseName(){
            return "列表长度";
        }
    }

    /** worldswap keyA keyB → 交换两键值 */
    public static class WorldSwapStatement extends WorldStatement{
        public String keyA = "a";
        public String keyB = "b";

        @Override
        public void build(Table table){
            input(table, keyA, v -> keyA = v);
            input(table, keyB, v -> keyB = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String a = keyA, b = keyB;
            return inst(exec -> {
                Object va = WorldState.get(a);
                Object vb = WorldState.get(b);
                WorldState.put(a, vb);
                WorldState.put(b, va);
            });
        }

        @Override
        protected String chineseName(){
            return "交换键值";
        }
    }

    /** worldtype key mode result → 读值并转换（mode: 0=转数字 1=转字符串） */
    public static class WorldTypeStatement extends WorldStatement{
        public String key = "key";
        public String mode = "0";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
            input(table, mode, v -> mode = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String k = key;
            int m = (int)WorldStateStatements.parseNum(mode);
            return inst(exec -> {
                Object o = WorldState.get(k);
                if(o == null){
                    out.setnum(0);
                }else if(m == 0){
                    out.setnum(o instanceof Number n ? n.doubleValue() : WorldStateStatements.parseNum(o.toString()));
                }else{
                    out.setobj(o.toString());
                }
            });
        }

        @Override
        protected String chineseName(){
            return "类型转换";
        }
    }
}
