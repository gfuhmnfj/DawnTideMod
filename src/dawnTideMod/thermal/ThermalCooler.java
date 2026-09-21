package dawnTideMod.thermal;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class ThermalCooler extends ThermalBlock{

    public float targetTemp = 0.05f;

    public float coolRate = 0.03f;

    public float coolOutput = 15f;

    public ThermalCooler(String name){
        super(name);

        solid = true;
        destructible = true;
        group = BlockGroup.none;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.temperature, targetTemp * 100f, StatUnit.percent);
        stats.add(Stat.heatCapacity, coolOutput, StatUnit.none);
    }

    public class ThermalCoolerBuild extends ThermalBuild{

        @Override
        public void updateThermal(){
            float k = 1f - (float)Math.pow(1f - Mathf.clamp(coolRate), Time.delta / 16.666f);
            temperature = Mathf.lerp(temperature, targetTemp, Mathf.clamp(k));
            applyThermalHealth(false);
        }
    }
}
