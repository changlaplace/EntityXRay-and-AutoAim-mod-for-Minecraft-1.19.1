package pro.mikey.xray.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.mikey.xray.XRay;
import pro.mikey.xray.utils.BlockData;
import pro.mikey.xray.utils.EntityData;
import pro.mikey.xray.utils.SimpleEntityData;
import pro.mikey.xray.xray.Controller;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class EntityStore {

//    private ArrayList<SimpleEntityData> SimpleStoreList =new ArrayList<>();
    private HashMap<UUID, EntityData> store = new HashMap<>();
    private HashMap<EntityType, UUID>   storeReference = new HashMap<>();
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Path STORE_FILE = Minecraft.getInstance().gameDirectory.toPath().resolve(String.format("config/%s/entity_store.json", XRay.MOD_ID));
    private static final Gson PRETTY_JSON = new GsonBuilder().setPrettyPrinting().create();
    /////autoaim
    public static ArrayList<EntityType> AutoAimEntityTypes = new ArrayList<>();

    public void put(EntityData data) {
        if( this.storeReference.containsKey(data.getEntityType()) )
            return;

        UUID uniqueId = UUID.randomUUID();
        this.store.put(uniqueId, data);
        this.storeReference.put(data.getEntityType(), uniqueId);
       // this.SimpleStoreList.add(data.toSimpleEntityData());
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
    public void setStorewithSimple(ArrayList<SimpleEntityData> store) {
        this.store.clear();
        this.storeReference.clear();

        store.forEach(simpleEntityData -> this.put(simpleEntityData.toEntityData()));
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

    public void toggleDrawing(EntityType data) {
        UUID uniqueId = storeReference.get(data);
        if( uniqueId == null )
            return;

        // We'd hope this never happens...
        EntityData entityData = this.store.get(uniqueId);
        if( entityData == null )
            return;

        entityData.setDrawing(!entityData.isDrawing());
    }

    public void populateGameEntities(){
        if(!this.store.isEmpty())
            return;
        int i = 1;
        for ( EntityType entityType : ForgeRegistries.ENTITY_TYPES ) {
            EntityData tobeadded = new EntityData(entityType,(RANDOM.nextInt(255) << 16) + (RANDOM.nextInt(255) << 8) + RANDOM.nextInt(255),
                    false, i++);
            this.put(tobeadded);
        }

    }

    public void write() {
        ArrayList<SimpleEntityData> SimpleStoreList = new ArrayList<>();
        for (EntityData entityData : store.values()){
            SimpleStoreList.add(entityData.toSimpleEntityData());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORE_FILE.toFile()))) {
            PRETTY_JSON.toJson(SimpleStoreList, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to write json data to {}", STORE_FILE);
        }
    }

    public ArrayList<SimpleEntityData> read() {
        if (!Files.exists(STORE_FILE))
            return new ArrayList<>();

        try {
            Type type = new TypeToken<ArrayList<SimpleEntityData>>() {
            }.getType();
            try (BufferedReader reader = new BufferedReader(new FileReader(STORE_FILE.toFile()))) {
                return PRETTY_JSON.fromJson(reader, type);
            } catch (JsonSyntaxException ex) {
                XRay.logger.log(Level.ERROR, "Failed to read json data from " + STORE_FILE);
            }
        } catch (IOException e) {
            XRay.logger.log(Level.ERROR, "Failed to read json data from " + STORE_FILE);
        }

        return new ArrayList<>();
    }
    private static final Path STORE_FILE_AUTOAIM = Minecraft.getInstance().gameDirectory.toPath().resolve(String.format("config/%s/AutoAimEntityTypes.json", XRay.MOD_ID));

    public ArrayList<EntityType> getAutoAimEntityTypes(){
        return AutoAimEntityTypes;
    }
    public void writeAutoAim(){
        ArrayList<String> SimpleAutoAimData = new ArrayList<>();
        AutoAimEntityTypes.add(EntityType.PIG);
        for (EntityType entityType : AutoAimEntityTypes){
            SimpleAutoAimData.add(entityType.toShortString());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORE_FILE_AUTOAIM.toFile()))) {
            PRETTY_JSON.toJson(SimpleAutoAimData, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to write json data to {}", STORE_FILE_AUTOAIM);
        }
    }
    public void readAutoAim(){

        if (!Files.exists(STORE_FILE_AUTOAIM))
            AutoAimEntityTypes = new ArrayList<>();
        else{
            try {
                ArrayList<String> SimpleAutoAimData = new ArrayList<>();
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                try (BufferedReader reader = new BufferedReader(new FileReader(STORE_FILE_AUTOAIM.toFile()))) {
                    SimpleAutoAimData = PRETTY_JSON.fromJson(reader, type);
                    for ( EntityType entityType : ForgeRegistries.ENTITY_TYPES ) {
                        if (SimpleAutoAimData.contains(entityType.toShortString())){
                            AutoAimEntityTypes.add(entityType);
                        }
                    }
                } catch (JsonSyntaxException ex) {
                    XRay.logger.log(Level.ERROR, "Failed to read json data from " + STORE_FILE_AUTOAIM);
                }
            } catch (IOException e) {
                XRay.logger.log(Level.ERROR, "Failed to read json data from " + STORE_FILE_AUTOAIM);
            }


        }



    }
}
