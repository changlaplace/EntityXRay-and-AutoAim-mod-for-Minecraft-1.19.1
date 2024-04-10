package pro.mikey.xray.xray;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import pro.mikey.xray.Configuration;
import pro.mikey.xray.store.BlockStore;
import pro.mikey.xray.utils.RenderBlockProps;
import pro.mikey.xray.utils.RenderEntityProps;

import java.util.*;

public class Controller {
    private static final int maxStepsToScan = 5;

    private static boolean isSearching = false;

    // Block blackList
    // Todo: move this to a configurable thing
    public static ArrayList<Block> blackList = new ArrayList<>() {{
        add(Blocks.AIR);
        add(Blocks.BEDROCK);
        add(Blocks.STONE);
        add(Blocks.GRASS);
        add(Blocks.DIRT);
    }};

    private static ChunkPos lastChunkPos = null;

    public static final Set<RenderBlockProps> syncRenderList = Collections.synchronizedSet(new HashSet<>()); // this is accessed by threads

    /**
     * Global blockStore used for:
     * [Rendering, GUI, Configuration Handling]
     */
    private static BlockStore blockStore = new BlockStore();

    // Thread management

    // Draw states
    private static boolean xrayActive = false; // Off by default
    private static boolean lavaActive = Configuration.store.lavaActive.get();

    public static BlockStore getBlockStore() {
        return blockStore;
    }

    // Public accessors
    public static boolean isXRayActive() {
        return xrayActive && Minecraft.getInstance().level != null && Minecraft.getInstance().player != null;
    }

    public static void toggleXRay() {
        if (!xrayActive) // enable drawing
        {
            syncRenderList.clear(); // first, clear the buffer
            xrayActive = true; // then, enable drawing
            requestBlockFinder(true); // finally, force a refresh

            if (!Configuration.general.showOverlay.get() && Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("xray.toggle.activated"), false);
        } else // disable drawing
        {
            if (!Configuration.general.showOverlay.get() && Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("xray.toggle.deactivated"), false);

            xrayActive = false;
        }
    }

    public static boolean isLavaActive() {
        return lavaActive;
    }

    public static void toggleLava() {
        lavaActive = !lavaActive;
        Configuration.store.lavaActive.set(lavaActive);
    }

    public static int getRadius() {
        return Mth.clamp(Configuration.store.radius.get(), 0, maxStepsToScan) * 3;
    }

    public static int getHalfRange() {
        return Math.max(0, getRadius() / 2);
    }

    public static int getVisualRadius() {
        return Math.max(1, getRadius());
    }

    ///////EXRAY
    public static int getEntityRadius(){
        return Mth.clamp(Configuration.store.EntityRadius.get(), 0, maxStepsToScan) * 3;
    }
    public static int getEntityVisualRadius(){
        return Math.max(1, getRadius());
    }

    ///////////

    public static void incrementCurrentDist() {
        if (Configuration.store.radius.get() < maxStepsToScan)
            Configuration.store.radius.set(Configuration.store.radius.get() + 1);
        else
            Configuration.store.radius.set(0);
    }

    public static void decrementCurrentDist() {
        if (Configuration.store.radius.get() > 0)
            Configuration.store.radius.set(Configuration.store.radius.get() - 1);
        else
            Configuration.store.radius.set(maxStepsToScan);
    }
    //////EXRay
    public static void decrementEntityCurrentDist() {
        if (Configuration.store.EntityRadius.get() > 0)
            Configuration.store.EntityRadius.set(Configuration.store.EntityRadius.get() - 1);
        else
            Configuration.store.EntityRadius.set(maxStepsToScan);
    }
    /////////
    /**
     * Precondition: world and player must be not null
     * Has player moved since the last region scan?
     * This method does not update the last player location so consecutive
     * calls yield the same result.
     *
     * @return true if the player has moved since the last blockFinder call
     */
    private static boolean playerHasMoved() {
        if (Minecraft.getInstance().player == null)
            return false;

        ChunkPos plyChunkPos = Minecraft.getInstance().player.chunkPosition();
        int range = getHalfRange();

        return lastChunkPos == null ||
                plyChunkPos.x > lastChunkPos.x + range || plyChunkPos.x < lastChunkPos.x - range ||
                plyChunkPos.z > lastChunkPos.z + range || plyChunkPos.z < lastChunkPos.z - range;
    }

    private static void updatePlayerPosition() {
        lastChunkPos = Minecraft.getInstance().player.chunkPosition();
    }

    /**
     * Starts a region scan thread if possible, that is if:
     * - we actually want to draw syncRenderList
     * - we are not already scanning an area
     * - either the player has moved since the last call
     * - or we want to (and can) force a scan
     *
     * @param force should we force a block scan even if the player hasn't moved?
     */
    public static synchronized void requestBlockFinder(boolean force) {
        if (isXRayActive() && (force || playerHasMoved()) && !isSearching) // world/player check done by xrayActive()
        {
            updatePlayerPosition(); // since we're about to run, update the last known position
            Util.backgroundExecutor().execute(() -> {
                isSearching = true;
                // Scan for the blocks
                Set<RenderBlockProps> c = RenderEnqueue.blockFinder();
                syncRenderList.clear();
                syncRenderList.addAll(c);
                isSearching = false;

                // Tell the render to update
                Render.requestedRefresh = true;
            });
        }
    }




    //Down are the methods for entities finding########################################
    public static boolean EntityxrayActive = false;
    public static Set<Entity> EntitiesNeededRender = Collections.synchronizedSet(new HashSet<>());
    public static Set<RenderEntityProps> EntitysyncRenderList = Collections.synchronizedSet(new HashSet<>());
    public static void toggleEntityXRay() {
        System.out.println("active toggleEntityXray function");
        if (!EntityxrayActive) // enable drawing
        {
            EntitysyncRenderList.clear(); // first, clear the buffer
            EntityxrayActive = true; // then, enable drawing
            requestEntityFinder(true); // finally, force a refresh

            if (!Configuration.general.showOverlay.get() && Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.displayClientMessage(Component.literal("entity xray active"),true);
        } else // disable drawing
        {
            if (!Configuration.general.showOverlay.get() && Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.displayClientMessage(Component.literal("entity xray deactivate"),true);

            EntityxrayActive = false;
        }
    }
    //here force always equals true,i dont dare to change lol
    public static synchronized void requestEntityFinder(boolean force) {
        if (EntityxrayActive && (force) ) // world/player check done by xrayActive()
        {
            System.out.println("active requestEntityFinder function");
            //updatePlayerPosition(); // since we're about to run, update the last known position
            Util.backgroundExecutor().execute(() -> {
                //isSearching = true;
                // Scan for the blocks
                Set<RenderEntityProps> c = RenderEnqueue.EntityFinder();
                EntitysyncRenderList.clear();
                EntitysyncRenderList.addAll(c);
                //isSearching = false;

                // Tell the render to update
                Render.EntityrequestedRefresh = true;
                if (!EntitysyncRenderList.isEmpty()){
                    System.out.println("added into controler set successfully");
                }
            });
        }
    }
//    public static void finde() {
//        if (!Configuration.general.showOverlay.get() && Minecraft.getInstance().player != null)
//            Minecraft.getInstance().player.displayClientMessage(Component.translatable("xray.toggle.finde"), false);
//        RenderEnqueue.entityFinder();
//    }
}
