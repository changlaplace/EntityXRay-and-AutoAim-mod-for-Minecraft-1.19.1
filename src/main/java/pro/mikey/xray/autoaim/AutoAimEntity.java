package pro.mikey.xray.autoaim;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import pro.mikey.xray.ClientController;
import pro.mikey.xray.Configuration;
import pro.mikey.xray.XRay;
import pro.mikey.xray.autoaim.utils.Wrapper;
import pro.mikey.xray.utils.EntityData;
import pro.mikey.xray.utils.RenderEntityProps;
import pro.mikey.xray.xray.Controller;
import pro.mikey.xray.xray.Render;

import java.util.ArrayList;
import java.util.List;

import static pro.mikey.xray.ClientController.entityStore;


// The value here should match an entry in the META-INF/mods.toml file
@Mod.EventBusSubscriber(modid = XRay.MOD_ID, value = Dist.CLIENT)
public class AutoAimEntity
{
    public static List<? extends Entity> EntityFoundList = new ArrayList<>();
    public static Entity targetEntity;
    @SubscribeEvent
    public static void tickEndFindAimEntity( TickEvent.ClientTickEvent event ) {
        if (Wrapper.playerPlaying() && Controller.isAutoAimActive()) {
            EntityFoundList.clear();
            AABB searchAABB = Wrapper.MC.player.getBoundingBox().inflate(Configuration.store.EntityRadius.get()*3.0*16).setMinY(-100).setMaxY(320);

            for (EntityType entityType : entityStore.getAutoAimEntityTypes()){
                EntityFoundList.addAll(Wrapper.MC.level.getEntities(entityType,searchAABB, Entity::isAlive));
            }


            System.out.println("AutoAim founded entity numbers"+EntityFoundList.size());

            targetEntity = Wrapper.getClosestEntityToCrosshair((List<Entity>) EntityFoundList);

            //System.out.println(targetEntity.toString());

            System.out.println("if is null targetentity");
            System.out.println(targetEntity == null);

            }
    }

    @SubscribeEvent
    public static void onWorldRenderAimEntity( RenderLevelStageEvent event ) // Called when drawing the world.
    {
        if (targetEntity != null){

            float[] yawPitch = Wrapper.getYawPitchBetween(
                    Wrapper.MC.player.getX(),
                    Wrapper.MC.player.getY() + Wrapper.MC.player.getEyeHeight(),
                    Wrapper.MC.player.getZ(),
                    // target
                    targetEntity.getX(),
                    targetEntity.getY() + targetEntity.getEyeHeight()*Configuration.store.eyerefactor.get(),
                    targetEntity.getZ()
            );

            // Compute the distance from the player's crosshair
            float vectorYaw = Mth.wrapDegrees(yawPitch[0] - Wrapper.MC.player.getRotationVector().y);
            float vectorPitch = Mth.wrapDegrees(yawPitch[1] - Wrapper.MC.player.getRotationVector().x);
            float dist = Mth.sqrt(vectorYaw*vectorYaw + vectorPitch*vectorPitch);


            float yawArg = (float) (Wrapper.MC.player.getRotationVector().y+Configuration.store.scrollingspeed.get()*vectorYaw/dist);
            float pitchArg = (float) (Wrapper.MC.player.getRotationVector().x+Configuration.store.scrollingspeed.get()*vectorPitch/dist);
            Wrapper.setRotations(yawArg, pitchArg
            );
        }

//    @SubscribeEvent
//    public static void onClientTick(TickEvent.ClientTickEvent clientTickEvent) {
//        if (Wrapper.playerPlaying() && Controller.isAutoAimActive()) {
//            EntityFoundList.clear();
//            AABB searchAABB = Wrapper.MC.player.getBoundingBox().inflate(Configuration.store.EntityRadius.get()*3.0*16).setMinY(-100).setMaxY(320);
//
//            for (EntityType entityType : entityStore.getAutoAimEntityTypes()){
//                EntityFoundList.addAll(Wrapper.MC.level.getEntities(entityType,searchAABB, Entity::isAlive));
//            }
//
//
//            System.out.println("AutoAim founded entity numbers"+EntityFoundList.size());
//
//            targetEntity = Wrapper.getClosestEntityToCrosshair((List<Entity>) EntityFoundList);
//
//            //System.out.println(targetEntity.toString());
//
//            System.out.println("if is null targetentity");
//            System.out.println(targetEntity == null);
//            if (targetEntity != null){
//
//                float[] yawPitch = Wrapper.getYawPitchBetween(
//                        Wrapper.MC.player.getX(),
//                        Wrapper.MC.player.getY() + Wrapper.MC.player.getEyeHeight(),
//                        Wrapper.MC.player.getZ(),
//                        // target
//                        targetEntity.getX(),
//                        targetEntity.getY() + targetEntity.getEyeHeight()*Configuration.store.eyerefactor.get(),
//                        targetEntity.getZ()
//                );
//
//                // Compute the distance from the player's crosshair
//                float vectorYaw = Mth.wrapDegrees(yawPitch[0] - Wrapper.MC.player.getRotationVector().y);
//                float vectorPitch = Mth.wrapDegrees(yawPitch[1] - Wrapper.MC.player.getRotationVector().x);
//                float dist = Mth.sqrt(vectorYaw*vectorYaw + vectorPitch*vectorPitch);
//
//
//                float yawArg = (float) (Wrapper.MC.player.getRotationVector().y+Configuration.store.scrollingspeed.get()*vectorYaw/dist);
//                float pitchArg = (float) (Wrapper.MC.player.getRotationVector().x+Configuration.store.scrollingspeed.get()*vectorPitch/dist);
//                Wrapper.setRotations(yawArg, pitchArg
//                        );
//
//
//            }
//
//
//        }
    }

//    @SubscribeEvent
//    public static void onRender(TickEvent.RenderTickEvent renderTickEvent) {
//        if (Wrapper.playerPlaying() && Controller.isAutoAimActive()) {
////            aimAssistance.assistIfPossible();
//            System.out.println("render tick");
//        }
//    }
}
