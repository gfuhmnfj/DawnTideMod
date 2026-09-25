package dawnTideMod.TideClean.thermal;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.type.ItemStack;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class ThermalCrafter extends GenericCrafter{

    public ItemStack[] coldItems = {};

    public float maxOperatingTemp = -1f;

    public float minTempEfficiency = 0.1f;

    public float coldResist = 0f;

    public float heatResist = 0f;

    public float thermalMass = 1f;

    public float ambientTemp = ThermalTiers.COMFORT_CENTER;

    public ThermalCrafter(String name){
        super(name);

        noUpdateDisabled = true;
    }

    @Override
    public void setStats(){
        super.setStats();

        for(ItemStack stack : coldItems){
            stats.add(Stat.input, stack);
        }

        if(maxOperatingTemp >= 0f){
            stats.add(Stat.temperature, maxOperatingTemp * 100f, StatUnit.percent);
        }
    }

    @Override
    public void init(){
        super.init();

        if(coldItems.length > 0){
            consumeItems(coldItems);
        }
    }

    public class ThermalCrafterBuild extends GenericCrafterBuild{

        public float temperature;
        protected float lastHealthMul = 1f;

        public float tempEfficiency = 1f;

        @Override
        public void created(){
            super.created();
            temperature = ambientTemp;
            applyThermalHealth(true);
        }

        @Override
        public void updateTile(){

            updateTemperature();

            super.updateTile();
        }

        protected void updateTemperature(){
            float rate = (thermalMass <= 0f ? 1f : 1f / thermalMass) * 0.02f;
            temperature = Mathf.approachDelta(temperature, ambientTemp, rate * Time.delta);

            if(maxOperatingTemp >= 0f && temperature > maxOperatingTemp){
                float over = (temperature - maxOperatingTemp) / Math.max(0.0001f, 1f - maxOperatingTemp);
                tempEfficiency = Mathf.lerp(1f, minTempEfficiency, Mathf.clamp(over));
            }else{
                tempEfficiency = 1f;
            }

            applyThermalHealth(false);
        }

        @Override
        public void updateEfficiencyMultiplier(){
            super.updateEfficiencyMultiplier();
            efficiency *= tempEfficiency;
        }

        protected void applyThermalHealth(){
            applyThermalHealth(false);
        }

        protected void applyThermalHealth(boolean force){
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

        public boolean hasColdItems(){
            if(coldItems.length == 0) return true;
            for(ItemStack stack : coldItems){
                if(items.get(stack.item) < stack.amount) return false;
            }
            return true;
        }

        public boolean coldSatisfied(){
            return hasColdItems() && (maxOperatingTemp < 0f || temperature <= maxOperatingTemp);
        }

        public float temp(){
            return temperature;
        }

        @Override
        public void write(arc.util.io.Writes write){
            super.write(write);
            write.f(temperature);
        }

        @Override
        public void read(arc.util.io.Reads read, byte revision){
            super.read(read, revision);
            temperature = read.f();
            applyThermalHealth(true);
        }
    }
}
