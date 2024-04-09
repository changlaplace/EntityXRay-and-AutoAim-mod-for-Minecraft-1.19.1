package pro.changlaplace.entity_xray;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import pro.mikey.xray.store.GameBlockStore;
import pro.mikey.xray.xray.Controller;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.TimeUnit;

public class test {

    public static void main(String[] args) throws InterruptedException, AWTException {
//        String name = "John";
//        System.out.println("Hello " + name);
//
//
//        Robot robot = new Robot();
//
//        for(int i = 0;i < 10; i++){
//            try {
//                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//
//                //robot.keyPress(KeyEvent.Rig);
//
//                //robot.keyRelease(KeyEvent.VK_A);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        for ( Item item : ForgeRegistries.ITEMS ) {
//           System.out.println(item.getClass().getSimpleName());

        for ( Item item : ForgeRegistries.ITEMS ) {
            if( !(item instanceof net.minecraft.world.item.BlockItem) )
                continue;

            Block block = Block.byItem(item);
            if ( item == Items.AIR || block == Blocks.AIR || Controller.blackList.contains(block) )
                continue; // avoids troubles

//            store.add(new GameBlockStore.BlockWithItemStack(block, new ItemStack(item)));

    }
}}
