package pro.mikey.xray.xray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.mikey.xray.ClientController;
import pro.mikey.xray.Configuration;
import pro.mikey.xray.XRay;
import pro.mikey.xray.utils.BlockData;
import pro.mikey.xray.utils.EntityData;
import pro.mikey.xray.utils.RenderBlockProps;
import pro.mikey.xray.utils.RenderEntityProps;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static pro.mikey.xray.ClientController.entityStore;

public class RenderEnqueue {
	/**
	 * Use Controller.requestBlockFinder() to trigger a scan.
	 */
	public static Set<RenderBlockProps> blockFinder() {
        HashMap<UUID, BlockData> blocks = Controller.getBlockStore().getStore();
        if ( blocks.isEmpty() ) {
            return new HashSet<>(); // no need to scan the region if there's nothing to find
        }

		final Level world = Minecraft.getInstance().level;
        final Player player = Minecraft.getInstance().player;

		// Something is fatally wrong
        if( world == null || player == null ) {
			return new HashSet<>();
		}

		final Set<RenderBlockProps> renderQueue = new HashSet<>();

		int range = Controller.getHalfRange();

		int cX = player.chunkPosition().x;
		int cZ = player.chunkPosition().z;

		BlockState currentState;
		FluidState currentFluid;

		Pair<BlockData, UUID> dataWithUUID;
		ResourceLocation block;

		for (int i = cX - range; i <= cX + range; i++) {
			int chunkStartX = i << 4;
			for (int j = cZ - range; j <= cZ + range; j++) {
				int chunkStartZ = j << 4;

				int height =
						Arrays.stream(world.getChunk(i, j).getSections())
								.filter(Objects::nonNull)
								.mapToInt(LevelChunkSection::bottomBlockY)
								.max()
								.orElse(0);

				for (int k = chunkStartX; k < chunkStartX + 16; k++) {
					for (int l = chunkStartZ; l < chunkStartZ + 16; l++) {
						for (int m = world.getMinBuildHeight(); m < height + (1 << 4); m++) {
							BlockPos pos = new BlockPos(k, m, l);

							currentState = world.getBlockState(pos);
							currentFluid = currentState.getFluidState();



							if( (currentFluid.getType() == Fluids.LAVA || currentFluid.getType() == Fluids.FLOWING_LAVA) && Controller.isLavaActive() ) {
								renderQueue.add(new RenderBlockProps(pos.getX(), pos.getY(), pos.getZ(), 0xff0000));
								continue;
							}

							// Reject blacklisted blocks
							if( Controller.blackList.contains(currentState.getBlock()) )
								continue;

							block = ForgeRegistries.BLOCKS.getKey(currentState.getBlock());
							if( block == null )
								continue;

							dataWithUUID = Controller.getBlockStore().getStoreByReference(block.toString());
							if( dataWithUUID == null )
								continue;

							if( dataWithUUID.getKey() == null || !dataWithUUID.getKey().isDrawing() ) // fail safe
								continue;

							// Push the block to the render queue
							renderQueue.add(new RenderBlockProps(pos.getX(), pos.getY(), pos.getZ(), dataWithUUID.getKey().getColor()));
						}
					}
				}
			}
		}

		return renderQueue;
	}

	/**
	 * Single-block version of blockFinder. Can safely be called directly
	 * for quick block check.
	 * @param pos the BlockPos to check
	 * @param state the current state of the block
	 * @param add true if the block was added to world, false if it was removed
	 */
	public static void checkBlock(BlockPos pos, BlockState state, boolean add )
	{
		if ( !Controller.isXRayActive() || Controller.getBlockStore().getStore().isEmpty() )
		    return; // just pass

		// If we're removing then remove :D
		if( !add ) {
			boolean removed = Controller.syncRenderList.remove(new RenderBlockProps(pos, 0));
			if (removed) {
				Render.requestedRefresh = true;
			}
			return;
		}

		ResourceLocation block = ForgeRegistries.BLOCKS.getKey(state.getBlock());
		if( block == null )
			return;

		Pair<BlockData, UUID> dataWithUUID = Controller.getBlockStore().getStoreByReference(block.toString());
		if( dataWithUUID == null || dataWithUUID.getKey() == null || !dataWithUUID.getKey().isDrawing() )
			return;

		// the block was added to the world, let's add it to the drawing buffer
		Controller.syncRenderList.add(new RenderBlockProps(pos, dataWithUUID.getKey().getColor()) );
		Render.requestedRefresh = true;
	}





	public static Set<RenderEntityProps> EntityFinder(){

		//System.out.println("u pressed me in function entityfinder");
		final Set<RenderEntityProps> renderEntityQueue = new HashSet<>();
		final Level world = Minecraft.getInstance().level;
		final Player player = Minecraft.getInstance().player;

		final AABB searchAABB = player.getBoundingBox().inflate(Configuration.store.EntityRadius.get()*3.0*16).setMinY(-100).setMaxY(320);
		for (EntityData entityData : entityStore.getStore().values()){
			if (entityData.isDrawing()){
				List<? extends Entity> EntityFoundList = new ArrayList<>();

				//EntityFoundList.get(1).getBoundingBox()

				EntityFoundList = world.getEntities(entityData.getEntityType(),searchAABB, Entity::isAlive);
				if (!EntityFoundList.isEmpty()){
					EntityFoundList.forEach(individual -> {

						if(individual != player){
							Controller.EntitiesNeededRender.add(individual);
							renderEntityQueue.add(new RenderEntityProps(individual.getBoundingBox(),entityData.getColor()));
						}
						/////for debug
						//player.displayClientMessage(Component.literal(renderEntityQueue.toString()),true);
						//System.out.println(Component.literal(renderEntityQueue.toString()));
					});
				}
			}
		}
		return renderEntityQueue;

//		if (world != null) {
//			if (player != null) {
//				EntityFoundList = world.getEntities(EntityType.PIG,player.getBoundingBox().inflate(8.0D), Entity::isAlive);
//			}
//		}
//		else{
//			player.displayClientMessage(Component.literal("world pointer null"),true);
//		}
//
//		if (EntityFoundList != null) {
////			AABB pig_1_BoundBox;
////			pig_1_BoundBox = finde.get(1).getBoundingBox();
//			if (!EntityFoundList.isEmpty()){
//				EntityFoundList.forEach(individual -> {
//					Controller.EntitiesNeededRender.add(individual);
//					renderEntityQueue.add(new RenderEntityProps(individual.getBoundingBox(),11045301));
//				});
//			}
//
//			player.displayClientMessage(Component.literal(renderEntityQueue.toString()),true);
//			System.out.println(Component.literal(renderEntityQueue.toString()));
//		}
	}
}
