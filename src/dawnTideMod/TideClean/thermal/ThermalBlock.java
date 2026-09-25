package dawnTideMod.TideClean.ui.thermal;

import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class ThermalBlock extends Block{
    public float ambientTemp = ThermalTiers.COMFORT_CENTER;

    public float baseTemp = -1f;
    public float thermalMass = 1f;
    public float heatResist = 0f;
    public float coldResist = 0f;
    public boolean thermalHealth = true;
    public boolean showThermalBar = true;
    public float conductivity = 0.35f;
    public float neighborInfluence = 0.5f;

    public ThermalBlock(String name){
        super(name);
        update = true;
        noUpdateDisabled = true;
    }
    @Override
    public void setStats(){
        super.setStats();

        if(heatResist > 0f){
            stats.addPercent(Stat.temperature, heatResist);
        }

        stats.add(Stat.heatCapacity, thermalMass, StatUnit.none);
    }

    public class ThermalBuild extends Building{
        public float temperature;
        public float thermalEfficiency = 1f;
        protected float lastHealthMul = 1f;

        @Override
        public void created(){
            super.created();
            temperature = baseTemp >= 0f ? baseTemp : ambientTemp;
            applyThermalHealth(true);
        }

        @Override
        public void updateTile(){
            updateThermal();
            super.updateTile();
        }

        public void updateThermal(){
            float dt = Time.delta;
            float rate = (thermalMass <= 0f ? 1f : 1f / thermalMass) * 0.02f;
            float base = ambientTemp;

            float neighborTemp = computeNeighborTemp();
            if(!Float.isNaN(neighborTemp)){
                base = Mathf.lerp(base, neighborTemp, Mathf.clamp(conductivity) * Mathf.clamp(neighborInfluence));
            }

            temperature = Mathf.approachDelta(temperature, base, rate * dt);

            applyThermalHealth(false);
        }

        protected float computeNeighborTemp(){
            if(conductivity <= 0f || proximity == null || proximity.isEmpty()) return Float.NaN;

            float sum = 0f;
            int count = 0;
            for(Building other : proximity){
                if(other == null || other.dead) continue;
                if(other instanceof ThermalBuild tb){
                    sum += tb.temperature;
                    count++;
                }
            }
            return count == 0 ? Float.NaN : sum / count;
        }

        public void applyThermalHealth(boolean force){
            if(!thermalHealth) return;

            float mul = ThermalTiers.healthMultiplier(temperature, heatResist, coldResist);
            if(Mathf.equal(mul, lastHealthMul, 0.0001f) && !force) return;

            float newMax = block.health * mul;

            if(force || maxHealth <= 0f){
                maxHealth = newMax;
                health = newMax;
            }else{
                float frac = Mathf.clamp(health / maxHealth);
                maxHealth = newMax;
                health = Mathf.clamp(frac * newMax, 0f, newMax);
            }

            lastHealthMul = mul;
        }

        public float temp(){
            return temperature;
        }
        public float tempDeviation(){
            return ThermalTiers.deviation(temperature);
        }
        public boolean isOverheated(){
            return ThermalTiers.tier(temperature) == ThermalTiers.Tier.hot;
        }
        public boolean isFrozen(){
            return ThermalTiers.tier(temperature) == ThermalTiers.Tier.cold;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(temperature);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            applyThermalHealth(true);
        }
    }
}
