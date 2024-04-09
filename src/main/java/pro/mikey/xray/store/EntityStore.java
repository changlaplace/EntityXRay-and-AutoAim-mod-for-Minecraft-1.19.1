package pro.mikey.xray.store;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import pro.mikey.xray.utils.BlockData;
import pro.mikey.xray.utils.EntityData;
import pro.mikey.xray.xray.Controller;

import java.util.*;

public class EntityStore {

    private ArrayList<EntityData> storeList =new ArrayList<>();
    private HashMap<UUID, EntityData> store = new HashMap<>();
    private HashMap<EntityType, UUID>    storeReference = new HashMap<>();

    public void put(EntityData data) {
        if( this.storeReference.containsKey(data.getEntityType()) )
            return;

        UUID uniqueId = UUID.randomUUID();
        this.store.put(uniqueId, data);
        this.storeReference.put(data.getEntityType(), uniqueId);
        this.storeList.add(data);
    }

    public void remove(EntityType entityType) {
        if( !this.storeReference.containsKey(entityType) )
            return;

        UUID uuid = this.storeReference.get(entityType);
        this.storeReference.remove(entityType);
        this.store.remove(uuid);
    }

    public HashMap<UUID, EntityData> getStore() {
        return store;
    }

    public void setStore(ArrayList<EntityData> store) {
        this.store.clear();
        this.storeReference.clear();

        store.forEach(this::put);
    }

    public Pair<EntityData, UUID> getStoreByReference(EntityType entityType) {
        UUID uniqueId = storeReference.get(entityType);
        if( uniqueId == null )
            return null;

        EntityData entityData = this.store.get(uniqueId);
        if( entityData == null )
            return null;

        return new ImmutablePair<>(entityData, uniqueId);
    }

    public void toggleDrawing(EntityData data) {
        UUID uniqueId = storeReference.get(data.getEntityType());
        if( uniqueId == null )
            return;

        // We'd hope this never happens...
        EntityData entityData = this.store.get(uniqueId);
        if( entityData == null )
            return;

        entityData.setDrawing(!entityData.isDrawing());
    }
    private static final Random RANDOM = new Random();
    public void populateGameEntities(){
        if(!this.store.isEmpty())
            return;
        int i = 1;
        for ( EntityType entityType : ForgeRegistries.ENTITY_TYPES ) {
            EntityData tobeadded = new EntityData(entityType,(RANDOM.nextInt(255) << 16) + (RANDOM.nextInt(255) << 8) + RANDOM.nextInt(255),
                    false, i++);
            this.put(tobeadded);
        }
//            if( !(item instanceof net.minecraft.world.item.BlockItem) )
//                continue;
//
//            Block block = Block.byItem(item);
//            if ( item == Items.AIR || block == Blocks.AIR || Controller.blackList.contains(block) )
//                continue; // avoids troubles
//
//            store.add(new GameBlockStore.BlockWithItemStack(block, new ItemStack(item)));
    }



//    public static ArrayList<BlockData> getFromSimpleBlockList(List<BlockData.SerializableBlockData> simpleList)
//    {
//        ArrayList<BlockData> blockData = new ArrayList<>();
//
//        for (BlockData.SerializableBlockData e : simpleList) {
//            if( e == null )
//                continue;
//
//            ResourceLocation location = null;
//            try {
//                location = new ResourceLocation(e.getBlockName());
//            } catch (Exception ignored) {};
//            if( location == null )
//                continue;
//
//            Block block = ForgeRegistries.BLOCKS.getValue(location);
//            if( block == null )
//                continue;
//
//            blockData.add(
//                    new BlockData(
//                            e.getName(),
//                            e.getBlockName(),
//                            e.getColor(),
//                            new ItemStack( block, 1),
//                            e.isDrawing(),
//                            e.getOrder()
//                    )
//            );
//        }
//
//        return blockData;
//    }
}
