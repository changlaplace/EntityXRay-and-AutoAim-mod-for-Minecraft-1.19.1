package pro.mikey.xray.autoaim;

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
import pro.mikey.xray.XRay;
import pro.mikey.xray.autoaim.utils.Wrapper;


// The value here should match an entry in the META-INF/mods.toml file
@Mod.EventBusSubscriber(modid = XRay.MOD_ID, value = Dist.CLIENT)
public class AutoAimEntity
{

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (Wrapper.playerPlaying()) {
            System.out.println("player tick");
//            aimAssistance.analyseBehaviour();
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent clientTickEvent) {
        if (Wrapper.playerPlaying()) {
//            MouseUtils.checkForMouseMove();
//            aimAssistance.analyseEnvironment();
            System.out.println("client tick");
        }
    }

    @SubscribeEvent
    public static void onRender(TickEvent.RenderTickEvent renderTickEvent) {
        if (Wrapper.playerPlaying()) {
//            aimAssistance.assistIfPossible();
            System.out.println("player tick");
        }
    }
}
