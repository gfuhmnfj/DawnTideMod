package dawnTideMod.segment;

import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.gen.EntityMapping;
import mindustry.gen.Groups;
import mindustry.gen.Segmentc;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;

public class DawnTideSegmentUnit extends UnitEntity implements Segmentc{

    public static int classId = -1;

    public static int register(){
        if(classId != -1) return classId;
        classId = EntityMapping.register("dawn-tide-segment-unit", DawnTideSegmentUnit::new);
        return classId;
    }

    @Override
    public int classId(){
        return classId;
    }

    public static DawnTideSegmentUnit create(){
        return new DawnTideSegmentUnit();
    }

    static{
        register();
    }

    protected transient Segmentc parentSegment;

    protected transient Segmentc childSegment;

    protected transient Segmentc headSegment;

    protected int parentId = -1;

    protected int segmentIndex = 0;

    @Override
    public Segmentc parentSegment(){
        return parentSegment;
    }

    @Override
    public void parentSegment(Segmentc seg){
        this.parentSegment = seg;
    }

    @Override
    public Segmentc childSegment(){
        return childSegment;
    }

    @Override
    public void childSegment(Segmentc seg){
        this.childSegment = seg;
    }

    @Override
    public Segmentc headSegment(){
        return headSegment;
    }

    @Override
    public void headSegment(Segmentc seg){
        this.headSegment = seg;
    }

    @Override
    public int parentId(){
        return parentId;
    }

    @Override
    public void parentId(int id){
        this.parentId = id;
    }

    @Override
    public int segmentIndex(){
        return segmentIndex;
    }

    @Override
    public void segmentIndex(int index){
        this.segmentIndex = index;
    }

    @Override
    public boolean isHead(){
        return segmentIndex == 0;
    }

    @Override
    public boolean moving(){
        return isHead() && super.moving();
    }

    @Override
    public boolean ignoreSolids(){
        return !isHead();
    }

    @Override
    public boolean playerControllable(){
        return isHead() && super.playerControllable();
    }

    @Override
    public boolean isCommandable(){
        return isHead() && super.isCommandable();
    }

    @Override
    public boolean shouldUpdateController(){
        return isHead() && super.shouldUpdateController();
    }

    @Override
    public int collisionLayer(){
        return 1;
    }

    @Override
    public void addChild(Unit child){
        if(!(child instanceof Segmentc next)) return;

        this.childSegment = next;
        next.parentSegment(this);
        next.parentId(this.id);
        next.segmentIndex(this.segmentIndex + 1);

        Segmentc head = this.isHead() ? this : this.headSegment;
        next.headSegment(head == null ? this : head);
    }

    @Override
    public void checkParent(){
        if(parentSegment instanceof Unit p && !p.isAdded()){
            if(p instanceof Segmentc seg && seg.childSegment() == this){
                seg.childSegment(null);
            }
            parentSegment = null;
            parentId = -1;
            segmentIndex = 0;
            headSegment = this;
        }

        if(childSegment instanceof Unit c && !c.isAdded()){
            childSegment = null;
        }

        if(headSegment instanceof Unit h && !h.isAdded()){
            headSegment = isHead() ? this : null;
        }
    }

    @Override
    public void updateSegment(Segmentc parent, Segmentc child, int index){
        if(parent instanceof Unit p && p.isAdded()){
            followParent(p);
        }

        this.parentSegment = parent;
        this.childSegment  = child;
        this.segmentIndex  = index;
    }

    protected void followParent(Unit prev){
        float spacing = segmentSpacing();
        float rotSpeed = segmentRotSpeed();
        float maxRot = segmentMaxRot();

        float targetRot = Mathf.angle(this.x - prev.x, this.y - prev.y);

        float cur = this.rotation;
        float dist = Angles.angleDist(cur, targetRot);
        float limited = cur + Mathf.clamp(dist, -maxRot, maxRot);

        this.rotation = Mathf.slerpDelta(cur, limited, rotSpeed);

        Tmp.v1.trns(this.rotation, spacing);
        this.x = prev.x - Tmp.v1.x;
        this.y = prev.y - Tmp.v1.y;

        Unit head = headSegment instanceof Unit h ? h : prev;
        this.vel.set(head.vel);
    }

    private SegmentUnitType segmentType(){
        return type instanceof SegmentUnitType s ? s : null;
    }

    protected float segmentSpacing(){
        SegmentUnitType s = segmentType();
        float base = s == null ? hitSize : s.segmentSpacing;
        return Math.max(base, hitSize);
    }

    protected float segmentRotSpeed(){
        SegmentUnitType s = segmentType();
        return s == null ? 1f : s.segmentRotSpeed;
    }

    protected float segmentMaxRot(){
        SegmentUnitType s = segmentType();
        return s == null ? 30f : s.segmentMaxRot;
    }

    @Override
    public void update(){

        super.update();

        checkParent();

        if(!isHead() && parentSegment instanceof Unit p && p.isAdded()){
            updateSegment(parentSegment, childSegment, segmentIndex);
        }
    }

    @Override
    public void beforeWrite(){

        parentId = parentSegment instanceof Unit u ? u.id : -1;
    }

    @Override
    public void afterReadAll(){

        rebuildFromIds();
    }

    @Override
    public void afterSync(){

        rebuildFromIds();
    }

    protected void rebuildFromIds(){
        if(parentId == -1){
            parentSegment = null;
            return;
        }

        Unit p = Groups.unit.getByID(parentId);
        if(p instanceof Segmentc seg){
            parentSegment = seg;
            if(seg.childSegment() != this){
                seg.childSegment(this);
            }
            headSegment = seg.headSegment();
        }else{

            parentSegment = null;
            parentId = -1;
            segmentIndex = 0;
            headSegment = this;
        }
    }
}
