package dawnTideMod.content;

import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.world.blocks.distribution.ItemBridge;

public class dawnTweaks {
    public static void load(){
        Blocks.coreShard.itemCapacity = 5000;
        ((ItemBridge)Blocks.bridgeConduit).range = 5;
        UnitTypes.antumbra.health = 50;
    }
}
