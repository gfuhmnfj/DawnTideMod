package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.maps.planet.ErekirPlanetGenerator;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;
import mindustry.world.meta.Env;

/**
 * 「曙光潮涌」星球与区块注册类。
 *
 * <p>星球参数与数值全部对照原版埃里克尔（{@code Planets.java:46}）编写，仅色板换成青色系。</p>
 */
public class DawnTidePlanets{

    public static Planet dawnStar;
    public static SectorPreset dawnSector;

    /** 主色：云带 / 大气 / 列表图标共用的青色。 */
    private static final String THEME_COLOR = "45d5a5";
    /** 辅色：第二层云带。 */
    private static final String THEME_COLOR_2 = "66e0c0";

    public static void load(){
        dawnStar = new Planet("dawnStar", Planets.sun, 1f, 2){{
            // ===== 球体外观 =====
            generator = new ErekirPlanetGenerator();          // 复用埃里克尔地形（黑面 + 亮绿晶簇）
            meshLoader = () -> new HexMesh(this, 5);          // 第二参：球面细分度，越大越圆越卡
            hasAtmosphere = true;

            // ===== 大气光晕 =====
            atmosphereColor = Color.valueOf(THEME_COLOR);
            atmosphereRadIn = 0.02f;                          // 光晕向内收缩量（原版 0.02）
            atmosphereRadOut = 0.3f;                          // 光晕向外厚度（原版 0.3，调大会吞掉球体细节）
            landCloudColor = Color.valueOf(THEME_COLOR);      // 降落过场云色

            // ===== 云带环 =====
            // HexSkyMesh 构造实参顺序（对照 HexSkyMesh.java:16）：
            // planet, seed, speed, radius, divisions, color, octaves, persistence, scl, thresh
            // 其中 scl = 噪声缩放，thresh = 覆盖阈值：越小云越多，0 会变成整球包裹（环带消失）
            cloudMeshLoader = () -> new MultiMesh(
                new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf(THEME_COLOR).a(0.75f), 2, 0.42f, 1f, 0.43f),
                new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf(THEME_COLOR_2).a(0.75f), 2, 0.42f, 1.2f, 0.45f)
            );

            // ===== 定点光照（暗面 + 亮绿晶簇的来源，照抄埃里克尔）=====
            updateLighting = false;                           // 关动态光照，固定受光角度
            tidalLock = true;                                 // 潮汐锁定：朝镜头的一面保持黑夜
            orbitSpacing = 2f;                                // 轨道间距（影响相机与受光角度）
            totalRadius += 2.6f;
            lightSrcTo = 0.5f;                                // 边缘轮廓光
            lightDstFrom = 0.2f;                              // 暗面过渡

            // ===== 战役设定 =====
            alwaysUnlocked = true;                            // 无需解锁即可选
            startSector = 15;                                 // 初始登陆区块编号
            defaultCore = Blocks.coreBastion;
            defaultEnv = Env.scorching | Env.terrestrial;
            iconColor = Color.valueOf(THEME_COLOR);           // 星球列表小圆点颜色
            clearSectorOnLose = true;                         // 失守后区块重置
            allowLaunchToNumbered = false;

            ruleSetter = r -> {
                r.waveTeam = Team.malis;
                r.fog = true;
                r.staticFog = true;
                r.lighting = false;
                r.coreDestroyClear = true;
                r.onlyDepositCore = true;
            };
        }};

        // 构造参数：名字, 地图文件名(maps/xxx.msav), 所属星球, 区块编号(0~79)
        dawnSector = new SectorPreset("dawn-sector", "dawn-sector", dawnStar, 15){{
            difficulty = 3f;                                  // 敌人难度 0~10
            captureWave = 30;                                 // 守满 30 波获胜，0 = 无限生存
            requireUnlock = true;
            showSectorLandInfo = true;

            rules = r -> {
                r.winWave = captureWave;
                r.waveSpacing = 60f * 2f;                     // 波间隔 2 分钟
            };
        }};
    }
}
