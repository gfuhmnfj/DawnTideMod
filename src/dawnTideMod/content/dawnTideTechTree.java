package dawnTideMod.content;

import arc.struct.Seq;
import mindustry.content.*;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;
import mindustry.type.SectorPreset;
import static mindustry.content.SectorPresets.planetaryTerminal;


public class dawnTideTechTree {
    private static TechTree.TechNode context = null;
    public static Seq<TechTree.TechNode> roots = new Seq<>();
    public static void load(){
        // 物品，液体，辅助，电力，单位工厂，墙

        // 工厂
        addToNext(Blocks.multiplicativeReconstructor,() -> {
            node(dawnTideBlocks.NumberUpgradeUnitGenerator, Seq.with(new Objectives.SectorComplete(planetaryTerminal)), () -> {});
        });
        addToNext(Blocks.router,() ->{
            nodeProduce(dawnTideBlocks.miniWarehouse,() ->{});
        });


        //物品
        addToNext(Items.titanium,() ->{
            nodeProduce(dawnTideItems.Iron,() ->{
                nodeProduce(dawnTideItems.Quartz,() ->{});
            });
            nodeProduce(dawnTideItems.CeramicGlass,() ->{});
            nodeProduce(dawnTideItems.fibrousFat,() ->{});
        });
        addToNext(Items.thorium,() ->{
            nodeProduce(dawnTideItems.Steel,() ->{
                nodeProduce(dawnTideItems.Uranium,() ->{});
            });
        });
        addToNext(dawnTideItems.Uranium,() ->{
            nodeProduce(dawnTideItems.BoundaryBreakingAlloy,() ->{});
        });
        addToNext(Items.blastCompound,() ->{
            nodeProduce(dawnTideItems.HighExplosive,() ->{});
        });

        //液体
        addToNext(Liquids.water,() ->{
            nodeProduce(dawnTideLiquids.vulcanizing,() ->{});
        });

        //墙

    }

    public static void addToNext(UnlockableContent content,Runnable run){
        context = TechTree.all.find(t -> t.content == content);
        run.run();
    }

    public static TechTree.TechNode nodeRoot(String name, UnlockableContent content, Runnable children){
        return nodeRoot(name, content, false, children);
    }

    public static TechTree.TechNode nodeRoot(String name, UnlockableContent content, boolean requireUnlock, Runnable children){
        var root = node(content, content.researchRequirements(), children);
        root.name = name;
        root.requiresUnlock = requireUnlock;
        roots.add(root);
        return root;
    }

    public static TechTree.TechNode node(UnlockableContent content, Runnable children){
        return node(content, content.researchRequirements(), children);
    }

    public static TechTree.TechNode node(UnlockableContent content, ItemStack[] requirements, Runnable children){
        return node(content, requirements, null, children);
    }

    public static TechTree.TechNode node(UnlockableContent content, ItemStack[] requirements, Seq<Objectives.Objective> objectives, Runnable children){
        TechTree.TechNode node = new TechTree.TechNode(context, content, requirements);
        if(objectives != null){
            node.objectives.addAll(objectives);
        }

        //Java 11 兼容写法：不使用 instanceof 模式匹配（该语法需 Java 16+）
        SectorPreset preset = (context != null && context.content instanceof SectorPreset) ? (SectorPreset)context.content : null;
        if(preset != null && !node.objectives.contains(o -> o instanceof Objectives.SectorComplete && ((Objectives.SectorComplete)o).preset == preset)){
            node.objectives.insert(0, new Objectives.SectorComplete(preset));
        }

        TechTree.TechNode prev = context;
        context = node;
        children.run();
        context = prev;

        return node;
    }

    public static void node(UnlockableContent content, Seq<Objectives.Objective> objectives, Runnable children){
        node(content, content.researchRequirements(), objectives, children);
    }

    public static TechTree.TechNode node(UnlockableContent block){
        return node(block, () -> {});
    }

    public static TechTree.TechNode nodeProduce(UnlockableContent content, Seq<Objectives.Objective> objectives, Runnable children){
        return node(content, content.researchRequirements(), objectives.add(new Objectives.Produce(content)), children);
    }

    public static TechTree.TechNode nodeProduce(UnlockableContent content, Runnable children){
        return nodeProduce(content, new Seq<>(), children);
    }

    public static TechTree.TechNode nodeProduce(UnlockableContent content){
        return nodeProduce(content,() -> {});
    }
}
