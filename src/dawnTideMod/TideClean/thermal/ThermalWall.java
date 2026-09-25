package dawnTideMod.TideClean.thermal;

import mindustry.world.meta.BlockGroup;

public class ThermalWall extends ThermalBlock{

    public ThermalWall(String name){
        super(name);
        solid = true;
        destructible = true;
        update = true;
        noUpdateDisabled = true;
        canOverdrive = false;
        group = BlockGroup.walls;
    }
}
