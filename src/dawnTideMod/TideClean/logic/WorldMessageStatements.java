package dawnTideMod.TideClean.ui.logic;

import arc.scene.ui.layout.Table;
import dawnTideMod.TideClean.ui.world.WorldState;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

public final class WorldMessageStatements{

    private WorldMessageStatements(){
    }

    public static class WorldSendStatement extends WorldStatement{
        public String name = "channel";
        public String value = "0";

        @Override
        public void build(Table table){
            input(table, name, v -> name = v);
            input(table, value, v -> value = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            return inst(exec -> WorldState.send(name, WorldStateStatements.parseValue(value)));
        }

        @Override
        protected String chineseName(){
            return "发送消息";
        }
    }

    public static class WorldRecvStatement extends WorldStatement{
        public String result = "result";
        public String name = "channel";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, name, v -> name = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setobj(WorldState.recv(name)));
        }

        @Override
        protected String chineseName(){
            return "接收消息";
        }
    }

    public static class WorldCountStatement extends WorldStatement{
        public String result = "count";
        public String name = "channel";

        @Override
        public void build(Table table){
            input(table, result, v -> result = v);
            input(table, name, v -> name = v);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder){
            LVar out = builder.var(result);
            return inst(exec -> out.setnum(WorldState.pending(name)));
        }

        @Override
        protected String chineseName(){
            return "消息数量";
        }
    }
}
