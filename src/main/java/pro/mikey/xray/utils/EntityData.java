package pro.mikey.xray.utils;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

public class EntityData {

    private EntityType entityType;

    private int color;
    private boolean drawing;
    private int order;

    public EntityData(EntityType entityType, int color, boolean drawing, int order) {
        this.entityType = entityType;
        this.color = color;
        this.drawing = drawing;
        this.order = order;
    }
    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    public EntityType getEntityType() {
        return entityType;
    }

    public int getColor() {
        return color;
    }

    public boolean isDrawing() {
        return drawing;
    }

    public int getOrder() {
        return order;
    }
    public SimpleEntityData toSimpleEntityData(){
        return new SimpleEntityData(this.entityType.toShortString(),this.color,this.drawing,this.order);
    }

}

