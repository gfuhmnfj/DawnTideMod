package dawnTideMod.TideClean.ui.world;

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

    private static double worldTime = 0;

    private static boolean initialized = false;

    public static void init(){
        if(initialized) return;
        initialized = true;

        arc.Events.run(mindustry.game.EventType.Trigger.update, WorldState::update);
        arc.Events.on(mindustry.game.EventType.ResetEvent.class, e -> WorldState.clear());

        Log.info("[曙光潮涌] 世界状态管理器已就绪。");
    }

    public static void clear(){
        int n = values.size + messages.size + timers.size + onces.size;
        values.clear();
        timers.clear();
        onces.clear();
        messages.clear();
        listeners.clear();
        worldTime = 0;
        Log.info("[曙光潮涌] 世界状态已清空（释放 @ 项）。", n);
    }

    public static void update(){
        worldTime += Time.delta / (1000f / 60f);

        for(ObjectMap.Entry<String, Double> e : timers){
            double left = e.value - 1;
            if(left <= 0){
                e.value = 0d;
            }else{
                e.value = left;
            }
        }
    }

    public static void put(String key, Object value){
        if(key == null || key.isEmpty()) return;
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
        if(key != null) values.remove(key);
    }

    public static int size(){
        return values.size;
    }

    public static Seq<String> keys(){
        return values.keys().toSeq();
    }

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

    public static boolean once(String key){
        if(key == null || key.isEmpty()) return false;
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

    public static double time(){
        return worldTime;
    }

    public static double seconds(){
        return worldTime / 60.0;
    }

    public static void advance(double ticks){
        worldTime += ticks;
    }

    public static String dump(){
        return String.format(
            "世界状态：%d 项键值 / %d 个计时器 / %d 个一次性锁 / %d 组消息 / 时间 %.1f 秒",
            values.size, timers.size, onces.size, messages.size, seconds()
        );
    }

    public static boolean active(){
        return Vars.state != null && Vars.state.isGame();
    }
}
