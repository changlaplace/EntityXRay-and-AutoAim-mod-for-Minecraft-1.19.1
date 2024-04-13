package pro.mikey.xray;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final General general = new General();
    public static final Store store = new Store();

    public static class General {
        public final ForgeConfigSpec.BooleanValue showOverlay;
        public final ForgeConfigSpec.DoubleValue outlineThickness;

        General() {
            BUILDER.push("general");

            showOverlay = BUILDER
                    .comment("This allows you hide or show the overlay in the top right of the screen when XRay is enabled")
                    .define("showOverlay", true);

            outlineThickness = BUILDER
                    .comment("This allows you to set your own outline thickness, I find that 1.0 is perfect but others my",
                            "think differently. The max is 5.0")
                    .defineInRange("outlineThickness", 1.0, 1.0, 5.0);

            BUILDER.pop();
        }
    }

    public static class Store {
        public final ForgeConfigSpec.IntValue radius;
        public final ForgeConfigSpec.BooleanValue lavaActive;
        ///////////////////////////
        public ForgeConfigSpec.IntValue EntityRadius;
        public ForgeConfigSpec.BooleanValue EntityOutlineMode;

        //////////////////////////
        //AutoAim

        public ForgeConfigSpec.DoubleValue scrollingspeed;
        public ForgeConfigSpec.DoubleValue angleofsearch;
        public ForgeConfigSpec.IntValue delayframe;

        public ForgeConfigSpec.DoubleValue eyerefactor;


//        public static float scrollingspeed = 0.3F;
//        public static float AngleOfSearch = 25.0F;
//        public static float DelayedFrame = 3;

        Store() {
            BUILDER.comment("DO NOT TOUCH!").push("store");

            radius = BUILDER
                    .comment("DO NOT TOUCH!", "This is not for you.")
                    .defineInRange("radius", 2, 0, 5);

            lavaActive = BUILDER
                    .comment("Memory value for if you're currently wanting Lava to be rendered into the mix")
                    .define("lavaActive", false);
            ///////
            EntityRadius = BUILDER
                    .comment("DO NOT TOUCH!", "This is not for you.")
                    .defineInRange("EntityRadius", 2, 0, 5);
            EntityOutlineMode = BUILDER
                    .comment("Toggle if u want show outline or BoundBox")
                    .define("EntityOutlineMode", false);

            scrollingspeed = BUILDER
                    .comment("This allows you to set your own outline thickness, I find that 1.0 is perfect but others my",
                            "think differently. The max is 5.0")
                    .defineInRange("scrolspeed", 0.3, 0, 2);

            angleofsearch = (ForgeConfigSpec.DoubleValue) BUILDER
                    .comment("Toggle if u want show outline or BoundBox")
                    .defineInRange("angleofsearch", 25.0,0,180);
            delayframe = (ForgeConfigSpec.IntValue) BUILDER
                    .comment("Toggle if u want show outline or BoundBox")
                    .defineInRange("delayframe", 0,0,200);

            eyerefactor = (ForgeConfigSpec.DoubleValue) BUILDER
                    .comment("Toggle if u want show outline or BoundBox")
                    .defineInRange("eyerefactor", 0.2,0,2);

            BUILDER.pop();
        }

    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();


    //////XRayEntity


}
