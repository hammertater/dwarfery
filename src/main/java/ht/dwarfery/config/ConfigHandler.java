package ht.dwarfery.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHandler {

    private static Integer theNumber;

    public static void onConfigLoad() {
        ConfigHandler.bakeConfig();
    }

    public static void bakeConfig() {
        theNumber = COMMON.theNumber.get();
    }

    public static class Common {
        private final ForgeConfigSpec.IntValue theNumber;

        public Common(ForgeConfigSpec.Builder builder) {
            theNumber = builder
                    .comment("Some number")
                    .defineInRange("theNumber", 1, 0, 64);
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

}
