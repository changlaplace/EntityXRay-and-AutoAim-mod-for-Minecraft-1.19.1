package pro.changlaplace.entity_xray;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.TimeUnit;

public class test {

    public static void main(String[] args) throws InterruptedException, AWTException {
        String name = "John";
        System.out.println("Hello " + name);


        Robot robot = new Robot();

        for(int i = 0;i < 10; i++){
            try {
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                //robot.keyPress(KeyEvent.Rig);

                //robot.keyRelease(KeyEvent.VK_A);
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
