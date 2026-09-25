package dawnTideMod.TideClean.ui.logic;

import arc.scene.ui.layout.Table;
import dawnTideMod.TideClean.ui.world.WorldState;
import mindustry.Vars;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;
import mindustry.type.Sector;

public final class WorldInfoStatements{

    private WorldInfoStatements(){
    }

    public static class WorldInfoStatement extends WorldStatement{
        public String result = "result";
        public String type = "wave";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, type, v -> type = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(read()));
        }

        private double read(){
            boolean game = WorldState.active();
            return switch(type == null ? "" : type.trim()){
                case "wave" -> game ? Vars.state.wave : 0;
                case "enemies" -> game ? Vars.state.enemies : 0;
                case "tick" -> game ? Vars.state.tick : 0;
                case "time" -> WorldState.time();
                case "seconds" -> WorldState.seconds();
                case "keys" -> WorldState.size();
                case "mapwidth" -> Vars.world == null ? 0 : Vars.world.width();
                case "mapheight" -> Vars.world == null ? 0 : Vars.world.height();
                case "game" -> game ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        protected String chineseName(){
            return "世界信息";
        }
    }

    public static class SectorInfoStatement extends WorldStatement{
        public String result = "result";
        public String type = "id";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, type, v -> type = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(read()));
        }

        private double read(){
            Sector sector = current();
            if(sector == null) return 0;
            return switch(type == null ? "" : type.trim()){
                case "id" -> sector.id;
                case "threat" -> sector.threat;
                case "captured" -> sector.isCaptured() ? 1 : 0;
                case "unlocked" -> sector.unlocked() ? 1 : 0;
                case "enemybase" -> sector.hasEnemyBase() ? 1 : 0;
                case "attacked" -> sector.isAttacked() ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        protected String chineseName(){
            return "区块信息";
        }
    }

    public static class SectorNearStatement extends WorldStatement{
        public String result = "result";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(countNearby()));
        }

        private double countNearby(){
            Sector sector = current();
            if(sector == null) return 0;

            int count = 0;
            for(Sector other : sector.near()){
                if(other != null && other.unlocked()) count++;
            }
            return count;
        }

        @Override
        protected String chineseName(){
            return "邻近区块数";
        }
    }

    private static Sector current(){
        if(!WorldState.active()) return null;
        return Vars.state.rules.sector;
    }
}
