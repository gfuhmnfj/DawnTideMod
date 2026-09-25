package dawnTideMod.TideClean.ui.thermal;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class ThermalProducer extends ThermalBlock{

    public float targetTemp = 0.95f;

    public float warmupRate = 0.03f;

    public float heatOutput = 15f;

    public ThermalProducer(String name){
        super(name);

        solid = true;
        destructible = true;
        group = BlockGroup.none;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.temperature, targetTemp * 100f, StatUnit.percent);
        stats.add(Stat.heatCapacity, heatOutput, StatUnit.none);
    }

    public class ThermalProducerBuild extends ThermalBuild{

        @Override
        public void updateThermal(){

            float k = 1f - (float)Math.pow(1f - Mathf.clamp(warmupRate), Time.delta / 16.666f);
            temperature = Mathf.lerp(temperature, targetTemp, Mathf.clamp(k));
            applyThermalHealth(false);
        }
    }
}
