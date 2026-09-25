package dawnTideMod.TideClean.ui.logic;

import arc.func.Func;
import arc.func.Prov;
import arc.util.Log;
import mindustry.gen.LogicIO;
import mindustry.logic.LAssembler;
import mindustry.logic.LCategory;
import mindustry.logic.LStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class WorldLogicRegistry{

    private WorldLogicRegistry(){

    }

    private static boolean registered = false;

    public static LCategory dawnCategory;

    private static final List<String> names = new ArrayList<>();

    public static int register(){
        if(registered) return names.size();
        registered = true;

        dawnCategory = new LCategory("dawn-tide-world", arc.graphics.Color.valueOf("6ee2ff"));

        add("worldwrite",   WorldStateStatements.WorldWriteStatement::new);
        add("worldread",    WorldStateStatements.WorldReadStatement::new);
        add("worldhas",     WorldStateStatements.WorldHasStatement::new);
        add("worlddel",     WorldStateStatements.WorldDelStatement::new);

        add("worldtime",    WorldTimerStatements.WorldTimeStatement::new);
        add("worldtimer",   WorldTimerStatements.WorldTimerStatement::new);
        add("worldreset",   WorldTimerStatements.WorldTimerResetStatement::new);
        add("worldonce",    WorldTimerStatements.WorldOnceStatement::new);
        add("worldunonce",  WorldTimerStatements.WorldOnceResetStatement::new);

        add("worldsend",    WorldMessageStatements.WorldSendStatement::new);
        add("worldrecv",    WorldMessageStatements.WorldRecvStatement::new);
        add("worldcount",   WorldMessageStatements.WorldCountStatement::new);

        add("worldinfo",    WorldInfoStatements.WorldInfoStatement::new);
        add("sectorinfo",   WorldInfoStatements.SectorInfoStatement::new);
        add("sectornear",   WorldInfoStatements.SectorNearStatement::new);

        Log.info("[曙光潮涌] 已注册 @ 条世界逻辑指令：@", names.size(), String.join(", ", names));
        return names.size();
    }

    private static void add(String name, Supplier<? extends LStatement> prov){
        Prov<LStatement> p = prov::get;

        LogicIO.allStatements.add(p);

        Func<String[], LStatement> parser = args -> {
            LStatement st = prov.get();
            if(args != null){
                fillFields(st, args);
            }
            st.afterRead();
            return st;
        };
        LAssembler.customParsers.put(name, parser);

        names.add(name);
    }

    private static void fillFields(LStatement st, String[] args){
        java.lang.reflect.Field[] fields = st.getClass().getDeclaredFields();
        int argIdx = 1;

        for(java.lang.reflect.Field f : fields){
            int mod = f.getModifiers();
            if(java.lang.reflect.Modifier.isStatic(mod)) continue;
            if(!java.lang.reflect.Modifier.isPublic(mod)) continue;
            if(argIdx >= args.length) break;

            String raw = args[argIdx++];
            try{
                Class<?> t = f.getType();
                if(t == String.class){
                    f.set(st, raw);
                }else if(t == boolean.class || t == Boolean.class){
                    f.setBoolean(st, "true".equalsIgnoreCase(raw) || "1".equals(raw));
                }else if(t == int.class || t == Integer.class){
                    f.setInt(st, (int)Double.parseDouble(raw));
                }else if(t == float.class || t == Float.class){
                    f.setFloat(st, (float)Double.parseDouble(raw));
                }else if(t == double.class || t == Double.class){
                    f.setDouble(st, Double.parseDouble(raw));
                }

            }catch(Throwable ignored){

            }
        }
    }

    public static int count(){
        return names.size();
    }

    public static List<String> registeredNames(){
        return java.util.Collections.unmodifiableList(names);
    }

    public static boolean isRegistered(){
        return registered;
    }
}
