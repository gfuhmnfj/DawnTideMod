package dawnTideMod.TideClean.logic;

import arc.scene.ui.layout.Table;
import dawnTideMod.TideClean.world.WorldState;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

/** 世界感知类指令：单位 / 方块 / 全局统计 / 波次 */
public final class WorldQueryStatements{

    private WorldQueryStatements(){
    }

    /** worldunits team result → 该队伍当前单位数 */
    public static class WorldUnitsStatement extends WorldStatement{
        public String team = "0";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, team, v -> team = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            int teamId = (int)WorldStateStatements.parseNum(team);
            return inst(exec -> {
                try{
                    Team t = Team.get(teamId);
                    var data = Vars.state.teams.get(t);
                    out.setnum(data == null ? 0 : data.units.size);
                }catch(Throwable e){
                    out.setnum(0);
                }
            });
        }

        @Override
        protected String chineseName(){
            return "查询队伍单位数";
        }
    }

    /** worldblock x y result → 世界像素坐标处的方块内部名（无则 0） */
    public static class WorldBlockStatement extends WorldStatement{
        public String x = "0";
        public String y = "0";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, x, v -> x = v);
            input(table, y, v -> y = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            double px = WorldStateStatements.parseNum(x), py = WorldStateStatements.parseNum(y);
            return inst(exec -> {
                try{
                    var tile = Vars.world.tile((int)(px / 8), (int)(py / 8));
                    if(tile == null){
                        out.setobj("0");
                        return;
                    }
                    if(tile.build != null){
                        out.setobj(tile.build.block.name);
                    }else if(tile.block() != null && tile.block() != mindustry.content.Blocks.air){
                        out.setobj(tile.block().name);
                    }else{
                        out.setobj("0");
                    }
                }catch(Throwable e){
                    out.setnum(0);
                }
            });
        }

        @Override
        protected String chineseName(){
            return "查询坐标方块";
        }
    }

    /** worldblockhp x y result → 该坐标建筑血量（无建筑返回 -1） */
    public static class WorldBlockHpStatement extends WorldStatement{
        public String x = "0";
        public String y = "0";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, x, v -> x = v);
            input(table, y, v -> y = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            double px = WorldStateStatements.parseNum(x), py = WorldStateStatements.parseNum(y);
            return inst(exec -> {
                try{
                    var tile = Vars.world.tile((int)(px / 8), (int)(py / 8));
                    if(tile != null && tile.build != null){
                        out.setnum(tile.build.health);
                    }else{
                        out.setnum(-1);
                    }
                }catch(Throwable e){
                    out.setnum(-1);
                }
            });
        }

        @Override
        protected String chineseName(){
            return "查询坐标建筑血量";
        }
    }

    /** worldwave result → 当前波次 */
    public static class WorldWaveStatement extends WorldStatement{
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(Vars.state != null ? Vars.state.wave : 0));
        }

        @Override
        protected String chineseName(){
            return "查询当前波次";
        }
    }

    /** worldstat kind team item result → 全局统计
     * kind: 0=队伍单位数 1=队伍核心数 2=核心物品库存 3=当前波次 4=距下波秒数 5=世界时间秒 */
    public static class WorldStatStatement extends WorldStatement{
        public String kind = "0";
        public String team = "0";
        public String item = "copper";
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, kind, v -> kind = v);
            input(table, team, v -> team = v);
            input(table, item, v -> item = v);
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            int kindN = (int)WorldStateStatements.parseNum(kind);
            int teamN = (int)WorldStateStatements.parseNum(team);
            String itemName = item;
            return inst(exec -> {
                try{
                    switch(kindN){
                        case 0 -> {
                            var data = Vars.state.teams.get(Team.get(teamN));
                            out.setnum(data == null ? 0 : data.units.size);
                        }
                        case 1 -> {
                            var data = Vars.state.teams.get(Team.get(teamN));
                            out.setnum(data == null ? 0 : data.cores.size);
                        }
                        case 2 -> {
                            var core = Team.get(teamN).core();
                            var it = Vars.content.getByName(mindustry.ctype.ContentType.item, itemName);
                            out.setnum(core != null && it instanceof mindustry.type.Item i ? core.items.get(i) : -1);
                        }
                        case 3 -> out.setnum(Vars.state.wave);
                        case 4 -> out.setnum(Vars.state.wavetime / 60f);
                        case 5 -> out.setnum(WorldState.seconds());
                        default -> out.setnum(0);
                    }
                }catch(Throwable e){
                    out.setnum(-1);
                }
            });
        }

        @Override
        protected String chineseName(){
            return "全局统计";
        }
    }
}
