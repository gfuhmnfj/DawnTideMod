package dawnTideMod.content;

import arc.graphics.Color;
import dawnTideMod.TideClean.planets.DawnTidePalette;
import dawnTideMod.TideClean.planets.DawnTideRing;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.graphics.g3d.PlanetGrid;
import mindustry.maps.planet.ErekirPlanetGenerator;
import mindustry.type.Planet;
import mindustry.type.Sector;
import mindustry.type.SectorPreset;

public class DawnTidePlanet{

    public static Planet dawn;

    public static Planet dawnMoon;

    public static SectorPreset dawnSector;

    public static void load() {

        dawn = new Planet("dawn-tide", Planets.sun, 1f, 2) {{
            localizedName = "什么星球";
            generator = new ErekirPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 5);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 2, 0.15f, 0.14f, 5,
                            DawnTidePalette.CLOUD_1.a(0.75f), 2, 0.42f, 1f, 0.43f),
                    new HexSkyMesh(this, 3, 0.6f, 0.15f, 5,
                            DawnTidePalette.CLOUD_2.a(0.75f), 2, 0.42f, 1.2f, 0.45f)
            );
            alwaysUnlocked = true;
            accessible = true;
            startSector = 10;
            atmosphereColor = DawnTidePalette.ATMOSPHERE;
            iconColor = DawnTidePalette.ICON;
            landCloudColor = DawnTidePalette.LAND_CLOUD;
            lightColor = Color.white.cpy();
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            defaultEnv = mindustry.world.meta.Env.terrestrial
                    | mindustry.world.meta.Env.oxygen
                    | mindustry.world.meta.Env.spores
                    | mindustry.world.meta.Env.groundWater;
            orbitSpacing = 2f;
            updateLighting = false;
            tidalLock = true;
            totalRadius += 2.6f;
            lightSrcTo = 0.5f;
            lightDstFrom = 0.2f;
            ruleSetter = r -> {
                r.waveTeam = Team.crux;
                r.placeRangeCheck = false;
                r.coreDestroyClear = true;
            };
        }};
        dawnMoon = new Planet("dawn-tide-moon", dawn, 0.22f) {{
            hasAtmosphere = false;
            updateLighting = false;
            accessible = false;
            drawOrbit = true;
            orbitSpacing = 0.9f;
            meshLoader = () -> new mindustry.graphics.g3d.NoiseMesh(
                    this, 13, 3, radius,
                    2, 0.6f, 20f, 0.06f,
                    DawnTidePalette.MOON_BASE, DawnTidePalette.MOON_TINT,
                    3, 0.6f, 0.38f, 0.5f
            );
            sectors.add(new Sector(this, PlanetGrid.Ptile.empty));
            icon = "commandRally";
        }};
        DawnTideRing.attach(dawn, () -> new MultiMesh(
                new HexSkyMesh(dawn, 2, 0.15f, 0.14f, 5,
                        DawnTidePalette.CLOUD_1.a(0.75f), 2, 0.42f, 1f, 0.43f),
                new HexSkyMesh(dawn, 3, 0.6f, 0.15f, 5,
                        DawnTidePalette.CLOUD_2.a(0.75f), 2, 0.42f, 1.2f, 0.45f)
        ));

        dawnSector = new SectorPreset("dawn-sector", "dawn-sector", dawn, 10) {{
            localizedName = "第一个地区";
            difficulty = 3f;
            captureWave = 30;
            requireUnlock = true;
            showSectorLandInfo = true;
            rules = r -> {
                r.winWave = captureWave;
                r.waveSpacing = 60f * 2f;
            };
        }};
    }
}
