package dawnTideMod.TideClean.world;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Log;
import mindustry.Vars;

public class WorldState{
    private static final ObjectMap<String, Object> values = new ObjectMap<>();
    private static final ObjectMap<String, Double> timers = new ObjectMap<>();
    private static final ObjectMap<String, Boolean> onces = new ObjectMap<>();
    private static final ObjectMap<String, Seq<Object>> messages = new ObjectMap<>();
    private static final ObjectMap<String, Seq<Integer>> listeners = new ObjectMap<>();
    private static final ObjectMap<String, Double> counters = new ObjectMap<>();
    private static final ObjectMap<String, Seq<Double>> lists = new ObjectMap<>();
    private static final ObjectMap<String, double[]> timedEvents = new ObjectMap<>();
    private static final ObjectMap<String, Income> incomes = new ObjectMap<>();
    private static final ObjectMap<String, Quest> quests = new ObjectMap<>();
    private static final Seq<String> insertOrder = new Seq<>();
    private static double worldTime = 0;
    private static boolean initialized = false;
    private static boolean writesLocked = false;
    private static int frameBudget = -1;
    private static int frameSpent = 0;
    private static long frameId = 0;
    private static final int MAX_ENTRIES = 512;

    /** 周期性资源收入任务 */
    public static class Income{
        public String itemName;
        public double interval, timer, amount;
        public Income(String item, double interval, double amount){
            this.itemName = item; this.interval = interval; this.amount = amount; this.timer = interval;
        }
    }

    /** 任务链：目标击杀数 + 奖励 */
    public static class Quest{
        public String questId;
        public double target, count;
        public String rewardItem;
        public double rewardAmount;
        public boolean done;
        public Quest(String id, double target, String rewardItem, double rewardAmount){
            this.questId = id; this.target = target; this.rewardItem = rewardItem; this.rewardAmount = rewardAmount;
        }
    }

    public static void init(){
        if(initialized) return;
        initialized = true;
        arc.Events.run(mindustry.game.EventType.Trigger.update, WorldState::update);
        arc.Events.on(mindustry.game.EventType.ResetEvent.class, e -> WorldState.clear());
        arc.Events.on(mindustry.game.EventType.SaveLoadEvent.class, e -> WorldState.save());
        arc.Events.on(mindustry.game.EventType.WorldLoadEvent.class, e -> WorldState.load());
        arc.Events.on(mindustry.game.EventType.WaveEvent.class, e -> {
            if(Vars.state != null) fire("wave", (double)Vars.state.wave);
        });
        arc.Events.on(mindustry.game.EventType.UnitDestroyEvent.class, e -> {
            if(e.unit == null || e.unit.type == null) return;
            String name = e.unit.type.name;
            fire("unitdestroy", name);
            notifyKill(name);
        });
        arc.Events.on(mindustry.game.EventType.BlockDestroyEvent.class, e -> {
            if(e.tile == null || e.tile.build == null) return;
            fire("blockdestroy", e.tile.build.block.name);
        });
        arc.Events.on(mindustry.game.EventType.SectorCaptureEvent.class, e -> {
            if(e.sector != null && e.sector.preset != null) fire("capture", e.sector.preset.name);
        });
        Log.info("");
    }

    public static void clear(){
        int n = values.size + messages.size + timers.size + onces.size;
        values.clear();
        timers.clear();
        onces.clear();
        messages.clear();
        listeners.clear();
        counters.clear();
        lists.clear();
        timedEvents.clear();
        incomes.clear();
        quests.clear();
        insertOrder.clear();
        worldTime = 0;
        writesLocked = false;
        Log.info("[曙光潮涌] 世界状态已清空（释放 @ 项）。", n);
    }

    public static void update(){
        frameId++;
        frameSpent = 0;
        if(!active() || Vars.state.isPaused()) return;

        worldTime += Time.delta / (1000f / 60f);

        for(ObjectMap.Entry<String, Double> e : timers){
            double left = e.value - 1;
            e.value = left <= 0 ? 0d : left;
        }

        // 定时广播
        for(ObjectMap.Entry<String, double[]> e : timedEvents){
            double[] t = e.value;
            t[1] -= 1;
            if(t[1] <= 0){
                t[1] = t[0];
                fire(e.key, worldTime);
            }
        }

        // 经济收入
        for(ObjectMap.Entry<String, Income> e : incomes){
            Income in = e.value;
            in.timer -= 1;
            if(in.timer <= 0){
                in.timer = in.interval;
                payCore(in.itemName, in.amount);
            }
        }
    }

    private static void payCore(String itemName, double amount){
        try{
            mindustry.ctype.ContentType type = mindustry.ctype.ContentType.item;
            mindustry.ctype.Content c = Vars.content.getByName(type, itemName);
            if(c instanceof mindustry.type.Item item){
                var team = Vars.state.rules.defaultTeam;
                var core = team.core();
                if(core != null) core.items.add(item, (int)amount);
            }
        }catch(Throwable ignored){
        }
    }

    // ===== 事件总线 =====

    /** 向事件的所有监听处理器派发值（塞进处理器收件箱 "p<id>"） */
    public static void fire(String event, Object value){
        if(event == null) return;
        Seq<Integer> s = listeners.get(event);
        if(s == null) return;
        for(Integer id : s){
            messages.get("p" + id, Seq::new).add(value);
        }
    }

    /** 注册周期性广播：每 interval tick 向事件名广播一次 */
    public static void setInterval(String event, double intervalTicks){
        if(event == null || event.isEmpty() || intervalTicks < 1) return;
        timedEvents.put(event, new double[]{intervalTicks, intervalTicks});
    }

    public static void removeInterval(String event){
        if(event != null) timedEvents.remove(event);
    }

    public static void onUnitKilled(String typeName){
        // 兼容入口（quest 计数在 UnitDestroyEvent 钩子里走 notifyKill）
        notifyKill(typeName);
    }

    private static void notifyKill(String typeName){
        for(ObjectMap.Entry<String, Quest> e : quests){
            Quest q = e.value;
            if(q.done) continue;
            q.count++;
            if(q.count >= q.target){
                q.done = true;
                onces.put("quest:" + q.questId, Boolean.TRUE);
                put("quest:" + q.questId + ":done", 1d);
                if(q.rewardItem != null && !q.rewardItem.isEmpty()) payCore(q.rewardItem, q.rewardAmount);
                fire("quest:" + q.questId, (double)q.count);
                if(Vars.ui != null && Vars.ui.hudfrag != null && !Vars.headless){
                    try{
                        Vars.ui.hudfrag.showToast("[accent]任务完成[]：" + q.questId);
                    }catch(Throwable ignored){
                    }
                }
            }
        }
    }

    public static void registerQuest(String id, double target, String rewardItem, double rewardAmount){
        if(id == null || id.isEmpty() || target < 1) return;
        Quest q = quests.get(id);
        if(q == null){
            quests.put(id, new Quest(id, target, rewardItem, rewardAmount));
        }else{
            q.target = target;
            q.rewardItem = rewardItem;
            q.rewardAmount = rewardAmount;
            q.done = false;
            q.count = 0;
        }
    }

    public static double questCount(String id){
        Quest q = id == null ? null : quests.get(id);
        return q == null ? 0 : q.count;
    }

    public static boolean questDone(String id){
        Quest q = id == null ? null : quests.get(id);
        return q != null && q.done;
    }

    public static void removeQuest(String id){
        if(id != null) quests.remove(id);
    }

    // ===== 权限 / 预算 =====

    public static void lockWrites(boolean lock){
        writesLocked = lock;
    }

    public static boolean writesLocked(){
        return writesLocked;
    }

    public static void setFrameBudget(int perFrame){
        frameBudget = perFrame;
    }

    public static boolean trySpend(){
        if(frameBudget <= 0) return true;
        if(frameSpent >= frameBudget) return false;
        frameSpent++;
        return true;
    }

    // ===== 键值存储（带 LRU 上限 + 锁写）=====

    public static void put(String key, Object value){
        if(key == null || key.isEmpty() || writesLocked) return;
        if(!values.containsKey(key)){
            insertOrder.add(key);
            while(insertOrder.size > MAX_ENTRIES){
                String evict = insertOrder.remove(0);
                values.remove(evict);
            }
        }
        values.put(key, value);
    }

    public static Object get(String key){
        return key == null ? null : values.get(key);
    }

    public static double getNum(String key, double def){
        Object o = get(key);
        if(o instanceof Number n) return n.doubleValue();
        return def;
    }

    public static boolean has(String key){
        return key != null && values.containsKey(key);
    }

    public static void remove(String key){
        if(key == null || writesLocked) return;
        values.remove(key);
        insertOrder.remove(key);
    }

    public static int size(){
        return values.size;
    }

    public static Seq<String> keys(){
        return values.keys().toSeq();
    }

    // ===== 计数器 =====

    /** op: 0=+1 1=-1 2=+amount 3=置零 */
    public static double counter(String key, int op, double amount){
        if(key == null || key.isEmpty() || writesLocked) return getCounter(key);
        double v = getCounter(key);
        if(op == 0) v += 1;
        else if(op == 1) v -= 1;
        else if(op == 2) v += amount;
        else if(op == 3) v = 0;
        counters.put(key, v);
        return v;
    }

    /** 计数器增量（返回新值） */
    public static double counter(String key, double delta){
        if(key == null || key.isEmpty() || writesLocked) return getCounter(key);
        double v = getCounter(key) + delta;
        counters.put(key, v);
        return v;
    }

    public static void putNum(String key, double value){
        put(key, value);
    }

    public static double getCounter(String key){
        Double d = key == null ? null : counters.get(key);
        return d == null ? 0 : d;
    }

    // ===== 列表（FIFO/LIFO + 索引访问）=====

    public static void push(String name, double value, boolean lifo){
        if(name == null || name.isEmpty() || writesLocked) return;
        Seq<Double> s = lists.get(name, Seq::new);
        if(lifo) s.add(value);
        else s.insert(0, value);
    }

    /** 弹出（默认取尾部）；空返回 NaN */
    public static double pop(String name){
        Seq<Double> s = name == null ? null : lists.get(name);
        if(s == null || s.isEmpty()) return Double.NaN;
        return s.remove(s.size - 1);
    }

    public static int listSize(String name){
        Seq<Double> s = name == null ? null : lists.get(name);
        return s == null ? 0 : s.size;
    }

    public static void listAdd(String name, double value){
        if(name == null || name.isEmpty() || writesLocked) return;
        lists.get(name, Seq::new).add(value);
    }

    public static double listGet(String name, int index, double def){
        Seq<Double> s = name == null ? null : lists.get(name);
        if(s == null || index < 0 || index >= s.size) return def;
        Double v = s.get(index);
        return v == null ? def : v;
    }

    public static boolean listSet(String name, int index, double value){
        if(name == null || writesLocked) return false;
        Seq<Double> s = lists.get(name, Seq::new);
        if(index < 0 || index > s.size) return false;
        if(index == s.size) s.add(value);
        else s.set(index, value);
        return true;
    }

    public static void listClear(String name){
        if(name != null){
            Seq<Double> s = lists.get(name);
            if(s != null) s.clear();
        }
    }

    // ===== 计时器 =====

    public static void setTimer(String key, double ticks){
        if(key == null || key.isEmpty()) return;
        timers.put(key, ticks);
    }

    public static boolean timerDone(String key){
        if(key == null) return false;
        Double d = timers.get(key);
        return d != null && d <= 0;
    }

    public static double timerLeft(String key){
        if(key == null) return -1;
        Double d = timers.get(key);
        return d == null ? -1 : d;
    }

    public static void removeTimer(String key){
        if(key != null) timers.remove(key);
    }

    // ===== 一次性锁 =====

    public static boolean once(String key){
        if(key == null || key.isEmpty() || writesLocked) return false;
        if(onces.containsKey(key)) return false;
        onces.put(key, Boolean.TRUE);
        return true;
    }

    public static boolean isOnce(String key){
        return key != null && onces.containsKey(key);
    }

    public static void resetOnce(String key){
        if(key != null) onces.remove(key);
    }

    // ===== 消息 =====

    public static void send(String name, Object value){
        if(name == null || name.isEmpty()) return;
        messages.get(name, Seq::new).add(value);
    }

    public static Object recv(String name){
        if(name == null) return null;
        Seq<Object> q = messages.get(name);
        if(q == null || q.isEmpty()) return null;
        return q.remove(0);
    }

    public static int pending(String name){
        if(name == null) return 0;
        Seq<Object> q = messages.get(name);
        return q == null ? 0 : q.size;
    }

    // ===== 监听者 =====

    public static void listen(String event, int processorId){
        if(event == null || event.isEmpty()) return;
        Seq<Integer> s = listeners.get(event, Seq::new);
        if(!s.contains(processorId)) s.add(processorId);
    }

    public static void unlisten(String event, int processorId){
        if(event == null) return;
        Seq<Integer> s = listeners.get(event);
        if(s != null) s.remove((Integer)processorId, false);
    }

    public static int listenerCount(String event){
        if(event == null) return 0;
        Seq<Integer> s = listeners.get(event);
        return s == null ? 0 : s.size;
    }

    public static void clearListeners(String event){
        if(event != null) listeners.remove(event);
    }

    // ===== 收入注册 =====

    public static void addIncome(String id, double intervalTicks, String itemName, double amount){
        if(id == null || id.isEmpty() || intervalTicks < 1) return;
        incomes.put(id, new Income(itemName, intervalTicks, amount));
    }

    public static void removeIncome(String id){
        if(id != null) incomes.remove(id);
    }

    // ===== 世界时间 =====

    public static double time(){
        return worldTime;
    }

    public static double seconds(){
        return worldTime / 60.0;
    }

    public static void advance(double ticks){
        worldTime += ticks;
    }

    // ===== 持久化（挂 state.rules.tags，随存档保存）=====

    private static final String TAG_KEY = "dawntide.worldstate";
    private static final char SEP_ENTRY = '\u0002', SEP_KV = '\u0001';

    public static void save(){
        try{
            if(Vars.state == null || Vars.state.rules == null || !active()) return;
            StringBuilder sb = new StringBuilder();
            appendSection(sb, 'V', values, true);
            appendSection(sb, 'T', timers, true);
            appendSection(sb, 'C', counters, true);
            appendSection(sb, 'O', onces, false);
            sb.append('W').append(worldTime);
            Vars.state.rules.tags.put(TAG_KEY, sb.toString());
        }catch(Throwable error){
            Log.err(error);
        }
    }

    private static void appendSection(StringBuilder sb, char tag, ObjectMap<String, ?> map, boolean withValue){
        for(ObjectMap.Entry<String, ?> e : map){
            sb.append(tag).append(e.key).append(SEP_KV);
            if(withValue){
                Object v = e.value;
                if(v instanceof Number n) sb.append('n').append(n.doubleValue());
                else if(v instanceof Boolean b) sb.append('b').append(b ? 1 : 0);
                else sb.append('s').append(String.valueOf(v));
            }
            sb.append(SEP_ENTRY);
        }
    }

    public static void load(){
        try{
            if(Vars.state == null || Vars.state.rules == null || !active()) return;
            String data = Vars.state.rules.tags.get(TAG_KEY, "");
            if(data == null || data.isEmpty()) return;
            for(String entry : data.split(String.valueOf(SEP_ENTRY))){
                if(entry.isEmpty()) continue;
                char tag = entry.charAt(0);
                int sep = entry.indexOf(SEP_KV);
                if(sep < 0) continue;
                String key = entry.substring(1, sep);
                String rest = entry.substring(sep + 1);
                switch(tag){
                    case 'V' -> {
                        char t = rest.charAt(0);
                        String raw = rest.substring(1);
                        values.put(key, t == 'n' ? (Object)Double.parseDouble(raw)
                            : t == 'b' ? (Object)(raw.equals("1"))
                            : (Object)raw);
                    }
                    case 'T' -> timers.put(key, Double.parseDouble(rest));
                    case 'C' -> counters.put(key, Double.parseDouble(rest));
                    case 'O' -> onces.put(key, Boolean.TRUE);
                    case 'W' -> worldTime = Double.parseDouble(rest);
                }
            }
        }catch(Throwable error){
            Log.err(error);
        }
    }

    public static String dump(){
        return String.format(
            "世界状态：%d 项键值 / %d 个计时器 / %d 个一次性锁 / %d 组消息 / %d 计数器 / %d 列表 / 时间 %.1f 秒%s",
            values.size, timers.size, onces.size, messages.size, counters.size, lists.size, seconds(),
            writesLocked ? "（写锁定）" : ""
        );
    }

    public static boolean active(){
        return Vars.state != null && Vars.state.isGame();
    }
}
