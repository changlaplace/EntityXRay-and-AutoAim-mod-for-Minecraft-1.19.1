package pro.mikey.xray.autoaim;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
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

import java.util.ArrayList;
import java.util.List;

import static pro.mikey.xray.ClientController.entityStore;


// The value here should match an entry in the META-INF/mods.toml file
@Mod.EventBusSubscriber(modid = XRay.MOD_ID, value = Dist.CLIENT)
public class AutoAimEntity
{


        //    public static boolean isEntityBeingAimedAt(Entity entity){
//        if (Wrapper.MC.crosshairPickEntity.equals(entity)){
//            return true;
//        }
//    }

    //onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent)
//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent){
//        if (Wrapper.playerPlaying() && Controller.isAutoAimActive()) {
//            ArrayList<Entity> LookingDirerctionEntities =new ArrayList<>();
//
//            Controller.EntitiesNeededRender.forEach(individual -> {
//                if (ClientController.entityStore.AutoAimEntityTypes.contains(individual.getType())){
//                    System.out.println("finded aimed entity");
//                    float[] bestYawPitch = Wrapper.getClosestYawPitchBetween(Wrapper.MC.player,individual);
//                    if (true){
//                        System.out.println("Added to list");
//                        LookingDirerctionEntities.add(individual);
//                    }
//                }
//            });
//
//            if (LookingDirerctionEntities.isEmpty()){
//                //Wrapper.MC.player.displayClientMessage(Component.literal("not aiming"),true);
//            }
//            else{
//                Wrapper.MC.player.displayClientMessage(Component.literal("you are aiming"),true);
//            }
//
//
//            System.out.println("player tick");
////            aimAssistance.analyseBehaviour();
//        }
//    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent clientTickEvent) {
        if (Wrapper.playerPlaying() && Controller.isAutoAimActive()) {

            List<? extends Entity> EntityFoundList = new ArrayList<>();

            AABB searchAABB = Wrapper.MC.player.getBoundingBox().inflate(Configuration.store.EntityRadius.get()*3.0*16).setMinY(-100).setMaxY(320);

            for (EntityType entityType : entityStore.getAutoAimEntityTypes()){
                EntityFoundList.addAll(Wrapper.MC.level.getEntities(entityType,searchAABB, Entity::isAlive));
//                EntityFoundList.forEach(individual -> {
//
//                    if(individual != Wrapper.MC.player){
////                        Controller.EntitiesNeededRender.add(individual);
////                        renderEntityQueue.add(new RenderEntityProps(individual.getBoundingBox(),entityData.getColor()));
//                        float[] closestYawPitchBetween = Wrapper.getClosestYawPitchBetween(Wrapper.MC.player,individual);
////                        if((Mth.abs(closestYawPitchBetween[0])+Mth.abs(closestYawPitchBetween[1]))<1){
////                            LookingDirerctionEntities.add(individual);
////                        }
//                        LookingDirerctionEntities.add(individual);
//                        System.out.println(closestYawPitchBetween[0]);
//                        System.out.println(closestYawPitchBetween[1]);
//                        System.out.println("tuple");
//                    }
//
//                    /////for debug
//                    //player.displayClientMessage(Component.literal(renderEntityQueue.toString()),true);
//                    //System.out.println(Component.literal(renderEntityQueue.toString()));
//                });
            }


            System.out.println("AutoAim founded entity numbers"+EntityFoundList.size());

            Entity targetEntity = Wrapper.getClosestEntityToCrosshair((List<Entity>) EntityFoundList);

            //System.out.println(targetEntity.toString());

            System.out.println("if is null targetentity");
            System.out.println(targetEntity == null);
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


//            if (!LookingDirerctionEntities.isEmpty()){
//                System.out.println("finally we are not empty");
//                System.out.println(LookingDirerctionEntities.size());
//            }
//
//            System.out.println(LookingDirerctionEntities.size());
//            System.out.println("client tick");
//            Wrapper.MC.player.fac
//            Controller.EntitiesNeededRender.forEach(individual -> {
//                if (ClientController.entityStore.AutoAimEntityTypes.contains(individual.getType())){
//                    System.out.println("finded aimed entity");
//                    float[] bestYawPitch = Wrapper.getClosestYawPitchBetween(Wrapper.MC.player,individual);
//                    if (true){
//                        System.out.println("Added to list");
//                        LookingDirerctionEntities.add(individual);
//                    }
//                }
//            });

//            if (LookingDirerctionEntities.isEmpty()){
//                //Wrapper.MC.player.displayClientMessage(Component.literal("not aiming"),true);
//            }
//            else{
//                Wrapper.MC.player.displayClientMessage(Component.literal("you are aiming"),true);
//            }

        }
    }

//    @SubscribeEvent
//    public static void onRender(TickEvent.RenderTickEvent renderTickEvent) {
//        if (Wrapper.playerPlaying() && Controller.isAutoAimActive()) {
////            aimAssistance.assistIfPossible();
//            System.out.println("render tick");
//        }
//    }
}
