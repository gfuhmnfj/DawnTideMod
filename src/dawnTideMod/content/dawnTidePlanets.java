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

public class dawnTidePlanets {
    public static Planet dawnStar;
    public static SectorPreset dawnSector;

    public static void load(){
        dawnStar = new Planet("dawnStar", Planets.sun, 1f, 2){{
            generator = new ErekirPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 5);
            hasAtmosphere = true;
            atmosphereColor = Color.valueOf("45d5a5"); // ★大气颜色改这里
            atmosphereRadIn = 0.9f;                   // 光晕向内收缩量
            atmosphereRadOut = 1.4f;                   // ★光晕向外扩散厚度，调大更膨胀
            landCloudColor = Color.valueOf("45d5a5");  // 降落时的云颜色

            // HexSkyMesh 参数依次：种子, 速度, 变化幅度, 八度, 颜色(带透明度), 颜色缩放, 缩放, 系数, 透明系数
            cloudMeshLoader = () -> new MultiMesh(
                new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("45d5a5").a(0.75f), 2, 0.42f, 1f, 0f),
                new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("66e0c0").a(0.75f), 2, 0.42f, 1.2f, 0f)
            );

            alwaysUnlocked = true;//不用解锁直接能选
            startSector = 15;//初始登陆区块编号
            defaultCore = Blocks.coreBastion;//默认核心
            defaultEnv = Env.scorching | Env.terrestrial;
            iconColor = Color.valueOf("45d5a5");//星球列表小圆点颜色
            clearSectorOnLose = true;//失守后区块重置
            updateLighting = false;//关动态光照
            allowLaunchToNumbered = false;

            tidalLock = true;                   // 潮汐锁定：自转面恒定朝太阳 → 朝镜头的一面是黑夜
            orbitSpacing = 1f;                  // 轨道间距（影响相机/受光角度）
            totalRadius += 2.6f;                // 总半径加成（同埃里克尔）
            lightSrcTo = 0.5f;                  // 光源偏移 → 边缘轮廓光
            lightDstFrom = 0.2f;                // 暗面过渡 → 球体大部分压暗，亮绿晶簇区块才会"刺"出来
            ruleSetter = r -> {
                r.waveTeam = Team.malis;//进攻队伍
                r.fog = true;
                r.staticFog = true;
                r.lighting = false;
                r.coreDestroyClear = true;
                r.onlyDepositCore = true;
            };
        }};

        dawnSector = new SectorPreset("dawn-sector", "dawn-sector", dawnStar, 15){{
            difficulty = 3f;//难度
            captureWave = 30;//波次
            requireUnlock = true;       // 需科技解锁才能降落
            showSectorLandInfo = true;

            rules = r -> {
                r.winWave = captureWave;
                r.waveSpacing = 60f * 2f; // 波间隔 2 分钟
            };
        }};
    }
}
