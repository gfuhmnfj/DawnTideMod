package dawnTideMod.logic;

import arc.func.Cons;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.logic.LCategory;
import mindustry.logic.LExecutor;
import mindustry.logic.LStatement;
import mindustry.logic.LCanvas;

import java.lang.reflect.Field;

public abstract class WorldStatement extends LStatement{

    protected static float fieldWidth(){
        return LCanvas.useRows() ? 100f : 190f;
    }

    protected Cell<?> input(Table table, String value, Cons<String> setter){
        return fields(table, value, setter).width(fieldWidth());
    }

    protected void label(Table table, String text){
        table.add(text);
    }

    @Override
    public LCategory category(){
        return WorldLogicRegistry.dawnCategory != null
            ? WorldLogicRegistry.dawnCategory
            : LCategory.world;
    }

    @Override
    public boolean privileged(){
        return false;
    }

    @Override
    public void write(StringBuilder out){
        out.append(typeName());

        for(Field f : serialFields(getClass())){
            out.append(' ');
            try{
                Object v = f.get(this);
                out.append(formatValue(v));
            }catch(IllegalAccessException e){

                out.append('0');
            }
        }
    }

    private static String formatValue(Object v){
        if(v == null) return "0";
        if(v instanceof Number n){
            double d = n.doubleValue();
            if(d == Math.floor(d) && !Double.isInfinite(d)){
                return String.valueOf((long)d);
            }
            return String.valueOf(d);
        }
        return String.valueOf(v);
    }

    private static java.util.List<Field> serialFields(Class<?> type){
        java.util.List<Field> list = new java.util.ArrayList<>();
        for(Field f : type.getDeclaredFields()){
            int mod = f.getModifiers();
            if(java.lang.reflect.Modifier.isStatic(mod)) continue;
            if(!java.lang.reflect.Modifier.isPublic(mod)) continue;
            list.add(f);
        }
        return list;
    }

    @Override
    public String name(){

        try{
            String key = "logic." + typeName() + ".name";
            String v = arc.Core.bundle.get(key);

            if(v != null && !v.startsWith("???") && !v.equals(key)){
                return v;
            }
        }catch(Throwable ignored){

        }

        return chineseName();
    }

    protected String chineseName(){
        return super.name();
    }

    protected static LExecutor.LInstruction inst(Cons<LExecutor> body){
        return body::get;
    }

}
