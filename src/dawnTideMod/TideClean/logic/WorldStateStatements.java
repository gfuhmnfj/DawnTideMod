package dawnTideMod.TideClean.logic;

import arc.scene.ui.layout.Table;
import dawnTideMod.TideClean.world.WorldState;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

public final class WorldStateStatements{

    private WorldStateStatements(){
    }

    public static Object parseValue(String text){
        if(text == null) return 0d;
        try{
            return Double.parseDouble(text.trim());
        }catch(NumberFormatException e){
            return text;
        }
    }

    /** 解析为数字，失败返回 0 */
    public static double parseNum(String text){
        if(text == null) return 0d;
        try{
            return Double.parseDouble(text.trim());
        }catch(NumberFormatException e){
            return 0d;
        }
    }

    public static class WorldWriteStatement extends WorldStatement{
        public String key = "key";
        public String value = "0";

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
            input(table, value, v -> value = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            return inst(exec -> WorldState.put(key, parseValue(value)));
        }

        @Override
        protected String chineseName(){
            return "写入世界状态";
        }
    }

    public static class WorldReadStatement extends WorldStatement{
        public String result = "result";
        public String key = "key";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, key, v -> key = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> {
                Object o = WorldState.get(key);
                if(o instanceof Number n){
                    out.setnum(n.doubleValue());
                }else{
                    out.setobj(o);
                }
            });
        }

        @Override
        protected String chineseName(){
            return "读取世界状态";
        }
    }

    public static class WorldHasStatement extends WorldStatement{
        public String result = "result";
        public String key = "key";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, key, v -> key = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(WorldState.has(key) ? 1 : 0));
        }

        @Override
        protected String chineseName(){
            return "世界状态存在?";
        }
    }

    public static class WorldDelStatement extends WorldStatement{
        public String key = "key";

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            return inst(exec -> WorldState.remove(key));
        }

        @Override
        protected String chineseName(){
            return "删除世界状态";
        }
    }
}
