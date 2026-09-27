package dawnTideMod.content;

import arc.struct.Seq;
import dawnTideMod.content.Blocks.DawnTideDefense;
import dawnTideMod.content.Blocks.DawnTideUnits;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.Planets;
import mindustry.content.TechTree;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;
import mindustry.type.SectorPreset;

import static mindustry.content.SectorPresets.planetaryTerminal;

public class dawnTideTechTree {

    private static TechTree.TechNode context;

    public static Seq<TechTree.TechNode> roots = new Seq<>();

    public static void load(){

        addToNext(Blocks.multiplicativeReconstructor, () -> {
            node(DawnTideUnits.numberUpgradeUnitGenerator,
                Seq.with(new Objectives.SectorComplete(planetaryTerminal)), () -> {

            });
        });

        addToNext(Blocks.router, () -> {
            nodeProduce(DawnTideDefense.miniWarehouse, () -> {});
        });

        addToNext(Items.titanium, () -> {
            nodeProduce(dawnTideItems.iron, () -> {
                nodeProduce(dawnTideItems.quartz, () -> {});
            });
            nodeProduce(dawnTideItems.ceramicGlass, () -> {});
            nodeProduce(dawnTideItems.fibrousFat, () -> {});
        });
        addToNext(Items.thorium, () -> {
            nodeProduce(dawnTideItems.steel, () -> {
                nodeProduce(dawnTideItems.uranium, () -> {});
            });
        });
        addToNext(dawnTideItems.uranium, () -> {
            nodeProduce(dawnTideItems.boundaryBreakingAlloy, () -> {});
        });
        addToNext(Items.blastCompound, () -> {
            nodeProduce(dawnTideItems.highExplosive, () -> {});
        });
        addToNext(Liquids.water, () -> {
            nodeProduce(dawnTideLiquids.vulcanizing, () -> {});
        });
        addToNext(Planets.erekir, () -> {
            node(DawnTidePlanet.dawnSector, () -> {});
        });
    }

    public static void addToNext(UnlockableContent content, Runnable run){
        context = TechTree.all.find(node -> node.content == content);
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

        if(context != null && context.content instanceof SectorPreset preset
            && !node.objectives.contains(o -> o instanceof Objectives.SectorComplete sector && sector.preset == preset)){
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

    public static TechTree.TechNode nodeProduce(UnlockableContent content){return nodeProduce(content,() -> {});
    }
}
