package dawnTideMod.TideClean.logic;

import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ai.types.CommandAI;
import mindustry.game.Team;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

/** 单位操控类指令 */
public final class WorldUnitStatements{

    private WorldUnitStatements(){
    }

    /** worldunitctrl team x y → 该队伍全部单位移动到目标坐标 */
    public static class WorldUnitCtrlStatement extends WorldStatement{
        public String team = "1";
        public String x = "0";
        public String y = "0";

        @Override
        public void build(Table table){
            input(table, team, v -> team = v);
            input(table, x, v -> x = v);
            input(table, y, v -> y = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            int teamId = (int)WorldStateStatements.parseNum(team);
            double px = WorldStateStatements.parseNum(x), py = WorldStateStatements.parseNum(y);
            return inst(exec -> {
                try{
                    Team t = Team.get(teamId);
                    var data = Vars.state.teams.get(t);
                    if(data == null) return;
                    Vec2 target = new Vec2((float)px, (float)py);
                    for(var unit : data.units){
                        CommandAI ai;
                        if(unit.controller() instanceof CommandAI existing){
                            ai = existing;
                        }else{
                            ai = new CommandAI();
                            unit.controller(ai);
                        }
                        ai.commandPosition(target);
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "指挥队伍移动";
        }
    }

    /** worldunitstat team mode value → 改队伍单位属性
     * mode: 0=血量倍乘 1=血量回复 2=速度设定(改单位类型速度) */
    public static class WorldUnitStatStatement extends WorldStatement{
        public String team = "1";
        public String mode = "0";
        public String value = "1.5";

        @Override
        public void build(Table table){
            input(table, team, v -> team = v);
            input(table, mode, v -> mode = v);
            input(table, value, v -> value = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            int teamId = (int)WorldStateStatements.parseNum(team);
            int m = (int)WorldStateStatements.parseNum(mode);
            double v = WorldStateStatements.parseNum(value);
            return inst(exec -> {
                try{
                    var data = Vars.state.teams.get(Team.get(teamId));
                    if(data == null) return;
                    for(var unit : data.units){
                        if(m == 0){
                            float nh = unit.maxHealth() * (float)v;
                            unit.maxHealth(nh);
                            unit.health(Math.min(unit.health, nh));
                        }else if(m == 1){
                            unit.heal((float)v);
                        }else if(m == 2 && unit.type != null){
                            unit.type.speed = (float)v;
                        }
                    }
                }catch(Throwable ignored){
                }
            });
        }

        @Override
        protected String chineseName(){
            return "修改队伍单位属性";
        }
    }
}
