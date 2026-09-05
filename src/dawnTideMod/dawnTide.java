package dawnTideMod;

import dawnTideMod.content.dawnTideItems;
import dawnTideMod.content.dawnTideLiquids;
import dawnTideMod.content.dawnTideTechTree;
import mindustry.mod.*;
import dawnTideMod.content.dawnTideBlocks;


public class dawnTide extends Mod {

    public dawnTide(){
    }

    @Override
    public void loadContent(){
        dawnTideItems.load();
        dawnTideBlocks.load();
        super.loadContent();
        dawnTideLiquids.load();
        dawnTideTechTree.load();
    }
}
