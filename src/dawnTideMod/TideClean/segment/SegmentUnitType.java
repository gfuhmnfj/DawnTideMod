package dawnTideMod.TideClean.ui.segment;

import arc.util.Log;
import mindustry.type.UnitType;

public class SegmentUnitType extends UnitType{
    public int segmentCount = 4;
    public boolean shareWeaponCooldown = false;
    public float minSegmentSpacing = 0f;
    public SegmentUnitType(String name){
        super(name);
        this.constructor = DawnTideSegmentUnit::create;
    }

    @Override
    public void init(){
        applySegmentSettings();
        super.init();
    }

    public void applySegmentSettings(){
        if(segmentCount <= 1){
            segmentUnits = 1;
            return;
        }

        segmentUnits = segmentCount;
        if(segmentUnit == null) segmentUnit = this;
        if(segmentEndUnit == null) segmentEndUnit = segmentUnit;
        float spacing = Math.max(minSegmentSpacing, hitSize);
        segmentSpacing = Math.max(segmentSpacing, spacing);
    }

    public void setSegmentCount(int count){
        this.segmentCount = Math.max(1, count);
        applySegmentSettings();
        Log.info("[曙光潮涌] 单位 [@] 身体节数已设为 @（总实体数 @）。",
            name, segmentCount, segmentCount + 1);
    }
    public boolean isSegmented(){
        return segmentCount > 1 && segmentUnits > 1;
    }
}
