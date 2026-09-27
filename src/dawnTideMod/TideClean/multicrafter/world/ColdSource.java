package dawnTideMod.TideClean.multicrafter.world;

/** 冷量源：实现此接口的建筑可向相邻建筑提供冷量（与原版 heat 同量纲） */
public interface ColdSource {
    /** 当前输出的冷量，0 = 未工作 */
    float coldOutput();
}
