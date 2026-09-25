package dawnTideMod.TideClean.logic;

import arc.scene.ui.layout.Table;
import arc.util.Log;
import dawnTideMod.TideClean.world.WorldState;
import mindustry.Vars;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

/** 流程 / 调试 / 持久化类指令 */
public final class WorldFlowStatements{

    private WorldFlowStatements(){
    }

    /** worldcall queue value → 向指定队列发"调用"消息（配合 worldrecv） */
    public static class WorldCallStatement extends WorldStatement{
        public String queue = "handler";
        public String value = "0";

        @Override
        public void build(Table table){
            input(table, queue, v -> queue = v);
            input(table, value, v -> value = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String q = queue;
            Object v = WorldStateStatements.parseValue(value);
            return inst(exec -> WorldState.send("call:" + q, v));
        }

        @Override
        protected String chineseName(){
            return "调用处理器(发消息)";
        }
    }

    /** worldwait key result → 非阻塞等待：输出指定计时器剩余 tick（0=已到） */
    public static class WorldWaitStatement extends WorldStatement{
        public String key = "wait1";
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
            return inst(exec -> out.setnum(WorldState.timerLeft(k)));
        }

        @Override
        protected String chineseName(){
            return "等待剩余时间";
        }
    }

    /** worlddump result → 世界状态全量信息 */
    public static class WorldDumpStatement extends WorldStatement{
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> {
                String s = WorldState.dump();
                Log.info(s);
                out.setobj(s);
            });
        }

        @Override
        protected String chineseName(){
            return "输出世界状态";
        }
    }

    /** worldquota value → 设置每处理器每帧指令预算（0=不限） */
    public static class WorldQuotaStatement extends WorldStatement{
        public String value = "5000";

        @Override
        public void build(Table table){
            input(table, value, v -> value = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            int v = (int)WorldStateStatements.parseNum(value);
            return inst(exec -> WorldState.setFrameBudget(v));
        }

        @Override
        protected String chineseName(){
            return "设置指令预算";
        }
    }

    /** worldlock mode → 锁定/解锁世界状态写入（mode: 0=解锁 1=锁定） */
    public static class WorldLockStatement extends WorldStatement{
        public String mode = "0";

        @Override
        public void build(Table table){
            input(table, mode, v -> mode = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            boolean lock = WorldStateStatements.parseNum(mode) > 0;
            return inst(exec -> WorldState.lockWrites(lock));
        }

        @Override
        protected String chineseName(){
            return "锁定状态写入";
        }
    }

    /** worldtick event interval → 周期性向事件广播（秒） */
    public static class WorldTickStatement extends WorldStatement{
        public String event = "tick";
        public String interval = "5";

        @Override
        public void build(Table table){
            input(table, event, v -> event = v);
            input(table, interval, v -> interval = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String e = event;
            double sec = WorldStateStatements.parseNum(interval);
            return inst(exec -> WorldState.setInterval(e, Math.max(1, sec * 60)));
        }

        @Override
        protected String chineseName(){
            return "周期广播";
        }
    }

    /** worldtickoff event → 停止周期广播 */
    public static class WorldTickOffStatement extends WorldStatement{
        public String event = "tick";

        @Override
        public void build(Table table){
            input(table, event, v -> event = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            String e = event;
            return inst(exec -> WorldState.removeInterval(e));
        }

        @Override
        protected String chineseName(){
            return "停止周期广播";
        }
    }

    /** worldsave → 状态立即持久化到存档规则标签 */
    public static class WorldSaveStatement extends WorldStatement{
        @Override
        public void build(Table table){
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            return inst(exec -> WorldState.save());
        }

        @Override
        protected String chineseName(){
            return "保存世界状态";
        }
    }

    /** worldload → 从存档规则标签恢复状态 */
    public static class WorldLoadStatement extends WorldStatement{
        @Override
        public void build(Table table){
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            return inst(exec -> WorldState.load());
        }

        @Override
        protected String chineseName(){
            return "恢复世界状态";
        }
    }

    /** worldsync → 同步快照（服务端权威：保存状态到存档标签并广播提示） */
    public static class WorldSyncStatement extends WorldStatement{
        @Override
        public void build(Table table){
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            return inst(exec -> {
                WorldState.save();
                if(!Vars.headless && Vars.ui != null){
                    try{
                        Vars.ui.showInfoToast("世界状态已同步", 1.5f);
                    }catch(Throwable ignored){
                    }
                }
            });
        }

        @Override
        protected String chineseName(){
            return "同步世界状态";
        }
    }

    /** worldquest id target rewardItem rewardAmount → 注册任务（击杀 target 个任意单位后奖励） */
    public static class WorldQuestStatement extends WorldStatement{
        public String id = "quest1";
        public String target = "10";
        public String rewardItem = "copper";
        public String rewardAmount = "100";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, id, v -> id = v);
            input(table, target, v -> target = v);
            input(table, rewardItem, v -> rewardItem = v);
            input(table, rewardAmount, v -> rewardAmount = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String i = id, ri = rewardItem;
            double t = WorldStateStatements.parseNum(target);
            double ra = WorldStateStatements.parseNum(rewardAmount);
            return inst(exec -> {
                WorldState.registerQuest(i, t, ri, ra);
                out.setnum(WorldState.questDone(i) ? 1 : WorldState.questCount(i));
            });
        }

        @Override
        protected String chineseName(){
            return "注册任务";
        }
    }

    /** worldquestinfo id result → 查询任务进度（-1=已完成） */
    public static class WorldQuestInfoStatement extends WorldStatement{
        public String id = "quest1";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, id, v -> id = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            String i = id;
            return inst(exec -> {
                if(WorldState.questDone(i)){
                    out.setnum(-1);
                }else{
                    out.setnum(WorldState.questCount(i));
                }
            });
        }

        @Override
        protected String chineseName(){
            return "查询任务进度";
        }
    }
}
