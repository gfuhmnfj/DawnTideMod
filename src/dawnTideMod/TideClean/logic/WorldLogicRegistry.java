package dawnTideMod.TideClean.logic;

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

        // ===== 世界状态 =====
        add("worldwrite",   WorldStateStatements.WorldWriteStatement::new);
        add("worldread",    WorldStateStatements.WorldReadStatement::new);
        add("worldhas",     WorldStateStatements.WorldHasStatement::new);
        add("worlddel",     WorldStateStatements.WorldDelStatement::new);

        // ===== 计时 / 一次性 =====
        add("worldtime",    WorldTimerStatements.WorldTimeStatement::new);
        add("worldtimer",   WorldTimerStatements.WorldTimerStatement::new);
        add("worldreset",   WorldTimerStatements.WorldTimerResetStatement::new);
        add("worldonce",    WorldTimerStatements.WorldOnceStatement::new);
        add("worldunonce",  WorldTimerStatements.WorldOnceResetStatement::new);

        // ===== 消息 =====
        add("worldsend",    WorldMessageStatements.WorldSendStatement::new);
        add("worldrecv",    WorldMessageStatements.WorldRecvStatement::new);
        add("worldcount",   WorldMessageStatements.WorldCountStatement::new);

        // ===== 信息 =====
        add("worldinfo",    WorldInfoStatements.WorldInfoStatement::new);
        add("sectorinfo",   WorldInfoStatements.SectorInfoStatement::new);
        add("sectornear",   WorldInfoStatements.SectorNearStatement::new);

        // ===== 世界感知 =====
        add("worldunits",   WorldQueryStatements.WorldUnitsStatement::new);
        add("worldblock",   WorldQueryStatements.WorldBlockStatement::new);
        add("worldblockhp", WorldQueryStatements.WorldBlockHpStatement::new);
        add("worldwave",    WorldQueryStatements.WorldWaveStatement::new);
        add("worldstat",    WorldQueryStatements.WorldStatStatement::new);

        // ===== 规则 / 玩法控制 =====
        add("worldrule",    WorldRuleStatements.WorldRuleStatement::new);
        add("worldweather", WorldRuleStatements.WorldWeatherStatement::new);
        add("worldspeed",   WorldRuleStatements.WorldSpeedStatement::new);
        add("worldtoast",   WorldRuleStatements.WorldToastStatement::new);
        add("worldsector",  WorldRuleStatements.WorldSectorStatement::new);
        add("worldresearch",WorldRuleStatements.WorldResearchStatement::new);
        add("worldincome",  WorldRuleStatements.WorldIncomeStatement::new);
        add("worldincomeoff", WorldRuleStatements.WorldIncomeOffStatement::new);

        // ===== 视觉 / 音频 =====
        add("worldfx",      WorldFxStatements.WorldFxStatement::new);
        add("worldsound",   WorldFxStatements.WorldSoundStatement::new);
        add("worldlight",   WorldFxStatements.WorldLightStatement::new);
        add("worldspawnfx", WorldFxStatements.WorldSpawnFxStatement::new);

        // ===== 单位操控 =====
        add("worldunitctrl", WorldUnitStatements.WorldUnitCtrlStatement::new);
        add("worldunitstat", WorldUnitStatements.WorldUnitStatStatement::new);

        // ===== 数据结构 =====
        add("worldrand",    WorldExtStatements.WorldRandStatement::new);
        add("worldcounter", WorldExtStatements.WorldCounterStatement::new);
        add("worldlist",    WorldExtStatements.WorldListStatement::new);
        add("worldlisten",  WorldExtStatements.WorldListenStatement::new);
        add("worldfire",    WorldExtStatements.WorldFireStatement::new);
        add("worldpush",    WorldDataStatements.WorldPushStatement::new);
        add("worldpop",     WorldDataStatements.WorldPopStatement::new);
        add("worldlistsize", WorldDataStatements.WorldListSizeStatement::new);
        add("worldswap",    WorldDataStatements.WorldSwapStatement::new);
        add("worldtype",    WorldDataStatements.WorldTypeStatement::new);

        // ===== 流程 / 调试 / 持久化 / 任务 =====
        add("worldcall",    WorldFlowStatements.WorldCallStatement::new);
        add("worldwait",    WorldFlowStatements.WorldWaitStatement::new);
        add("worlddump",    WorldFlowStatements.WorldDumpStatement::new);
        add("worldquota",   WorldFlowStatements.WorldQuotaStatement::new);
        add("worldlock",    WorldFlowStatements.WorldLockStatement::new);
        add("worldtick",    WorldFlowStatements.WorldTickStatement::new);
        add("worldtickoff", WorldFlowStatements.WorldTickOffStatement::new);
        add("worldsave",    WorldFlowStatements.WorldSaveStatement::new);
        add("worldload",    WorldFlowStatements.WorldLoadStatement::new);
        add("worldsync",    WorldFlowStatements.WorldSyncStatement::new);
        add("worldquest",   WorldFlowStatements.WorldQuestStatement::new);
        add("worldquestinfo", WorldFlowStatements.WorldQuestInfoStatement::new);

        // ===== 文本 / 本地化 =====
        add("worldfmt",     WorldTextStatements.WorldFmtStatement::new);
        add("worldbundle",  WorldTextStatements.WorldBundleStatement::new);

        loadAliases();

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

    /** 从数据目录 dawn-aliases.json 加载指令别名（{"别名": "指令名"}），文件缺失跳过 */
    @SuppressWarnings("unchecked")
    private static void loadAliases(){
        try{
            var file = arc.Core.files.local("dawn-aliases.json");
            if(!file.exists()) return;
            var json = new arc.util.serialization.Json();
            arc.struct.ObjectMap<String, String> map = json.fromJson(arc.struct.ObjectMap.class, file.readString());
            for(var e : map.entries()){
                String alias = e.key.trim(), target = e.value.trim();
                Func<String[], LStatement> parser = LAssembler.customParsers.get(target);
                if(parser != null && !LAssembler.customParsers.containsKey(alias)){
                    LAssembler.customParsers.put(alias, parser);
                    names.add(alias + "→" + target);
                }
            }
        }catch(Throwable error){
            Log.err("[曙光潮涌] 别名加载失败（可忽略）", error);
        }
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
