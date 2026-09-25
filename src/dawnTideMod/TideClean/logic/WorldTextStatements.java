package dawnTideMod.TideClean.logic;

import arc.Core;
import arc.scene.ui.layout.Table;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

/** 文本 / 本地化类指令 */
public final class WorldTextStatements{

    private WorldTextStatements(){
    }

    /** worldfmt a b c result → 拼接最多 3 段文本/数字 */
    public static class WorldFmtStatement extends WorldStatement{
        public String a = "text";
        public String b = "0";
        public String c = "";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, a, v -> a = v);
            input(table, b, v -> b = v);
            input(table, c, v -> c = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String sa = a, sb = b, sc = c;
            return inst(exec -> {
                StringBuilder sb2 = new StringBuilder();
                appendPart(sb2, sa);
                appendPart(sb2, sb);
                appendPart(sb2, sc);
                out.setobj(sb2.toString());
            });
        }

        private static void appendPart(StringBuilder sb, String part){
            if(part == null || part.isEmpty()) return;
            Object v = WorldStateStatements.parseValue(part);
            sb.append(v instanceof Double d && d == Math.floor(d) && !d.isInfinite()
                ? String.valueOf(d.longValue()) : String.valueOf(v));
        }
    }

    /** worldbundle key result → 读语言包（自动本地化） */
    public static class WorldBundleStatement extends WorldStatement{
        public String key = "mod.dawntide.name";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, key, v -> key = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String k = key;
            return inst(exec -> {
                try{
                    String v = Core.bundle.get(k, k);
                    out.setobj(v);
                }catch(Throwable e){
                    out.setobj(k);
                }
            });
        }

        @Override
        protected String chineseName(){
            return "读取语言包";
        }
    }
}
