package pro.mikey.xray.utils;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class SimpleEntityData {

    private String entityString;

    private int color;
    private boolean drawing;
    private int order;

    public SimpleEntityData(String entityString, int color, boolean drawing, int order) {
        this.entityString = entityString;
        this.color = color;
        this.drawing = drawing;
        this.order = order;
    }

    public String getEntityString() {
        return entityString;
    }

    public void setEntityString(String entityString) {
        this.entityString = entityString;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    public EntityData toEntityData(){
        for ( EntityType entityType : ForgeRegistries.ENTITY_TYPES ) {
            if (entityType.toShortString().equals(this.entityString)){
                return new EntityData(entityType,this.color,this.drawing,this.order);
            }
        }
        return null;
    }

}

