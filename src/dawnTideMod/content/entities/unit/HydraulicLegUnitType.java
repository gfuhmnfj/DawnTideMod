package dawnTideMod.content.entities.unit;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.gen.LegsUnit;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

/**
 * 「液压机」腿单位类型 —— 腿像液压缸一样按固定节奏自由伸缩：
 * 快速伸出 -> 保压 -> 快速缩回 -> 停顿，循环往复；移动时步幅也随之变化。
 *
 * 原理（源码依据）：
 * - 腿单位的全部参数（legLength/legCount 等）直接定义在 {@link UnitType} 上；
 * - {@link UnitType#update(Unit)} 由 UnitComp 每帧调用（UnitComp.java: type.update(self())），
 *   在这里按液压节奏改写 legLength（腿总长）即可驱动整条腿伸缩；
 * - 腿单位必须 constructor = LegsUnit::create，否则腿部实体不生效（参考原版 EntityMapping 的 "legs" 映射）。
 *
 * 注意：legLength 是类型级（UnitType）字段，同类单位共享，因此所有同类单位按同一节奏同步伸缩；
 * 伸缩相位取全局时间，多单位并存时数值一致、不会互相冲突。
 *
 * 用法示例（在 content 里定义单位时）：
 *   hydraulicWalker = new HydraulicLegUnitType("hydraulic-walker"){{
 *       legCount = 6;
 *       legGroupSize = 3;
 *       minLength = 6f;
 *       maxLength = 20f;
 *       cycleTime = 4f;
 *       speed = 0.6f;
 *       health = 900;
 * }};
 */
public class HydraulicLegUnitType extends UnitType{

    /** 一个液压周期总时长（秒） */
    public float cycleTime = 4f;
    /** 快速伸出时长占比 */
    public float extendFrac = 0.2f;
    /** 保压（伸到头停留）时长占比 */
    public float holdFrac = 0.3f;
    /** 快速缩回时长占比（剩余为停顿） */
    public float retractFrac = 0.2f;
    /** 缩到底时的腿长 */
    public float minLength = 6f;
    /** 伸到头时的腿长 */
    public float maxLength = 16f;

    public HydraulicLegUnitType(String name){
        super(name);

        //腿单位必须使用 LegsUnit 实体，否则腿不工作（与原版 "legs" 类型一致）
        constructor = LegsUnit::create;

        //默认腿部参数，外部可再覆盖
        legCount = 4;
        legGroupSize = 2;
        legLength = minLength;
        legSpeed = 0.3f;
        legContinuousMove = true; //不移动时腿也活动，液压伸缩的节奏看得更清楚
        legBaseOffset = 0f;
    }

    @Override
    public void update(Unit unit){
        super.update(unit);

        //把一个周期分成 4 段：伸出(梯形上升沿) -> 保压 -> 缩回(下降沿) -> 停
        float t = (Time.time % cycleTime) / cycleTime;
        float e1 = extendFrac;
        float h1 = extendFrac + holdFrac;
        float r1 = h1 + retractFrac;

        float scl;
        if(t < e1){
            scl = t / e1;                            //液压缸快速伸出
        }else if(t < h1){
            scl = 1f;                                //保压：保持伸长
        }else if(t < r1){
            scl = 1f - (t - h1) / retractFrac;       //液压缸快速缩回
        }else{
            scl = 0f;                                //停顿：保持缩回
        }

        legLength = Mathf.clamp(scl) * (maxLength - minLength) + minLength;
    }
}
