package xt9.deepmoblearningbm;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xt9.deepmoblearning.common.items.ItemGlitchHeart;
import xt9.deepmoblearning.common.items.ItemLivingMatter;
import xt9.deepmoblearningbm.util.MathHelper;
import net.minecraftforge.common.config.*;

/**
 * Created by xt9 on 2018-06-30.
 */
@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber
@Config(modid = ModConstants.MODID)
public class ModConfig {
    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ModConstants.MODID)) {
            ConfigManager.sync(ModConstants.MODID, Config.Type.INSTANCE);
        }
    }

    @Config.Name("Essence Multiplier Settings")
    public static CatalystMultiplierSubCategory essenceMultiplierSubCat = new CatalystMultiplierSubCategory();

    @Config.Name("Essence Base Amounts")
    public static EssenceBaseAmountsSubCategory essenceAmountSubCat = new EssenceBaseAmountsSubCategory();

    @Config.Comment({
        "Max: 10,000",
        "Default: 128"
    })
    @Config.Name("RF tick cost of the Digital Agonizer")
    @Config.RangeInt(min = 1, max = 10000)
    public static int agonizerRFCost = 128;
    public static int getAgonizerRFCost() {
        return MathHelper.ensureRange(agonizerRFCost, 1, 10000);
    }

    public static class EssenceBaseAmountsSubCategory {
        @Config.Comment({"Default: 100"})
        @Config.Name("Tier 1 Data Model - Essence per operation")
        @Config.RangeInt(min = 1, max = 2000)
        public int tierOneEssenceAmount = 100;

        @Config.Comment({"Default: 200"})
        @Config.Name("Tier 2 Data Model - Essence per operation")
        @Config.RangeInt(min = 1, max = 2000)
        public int tierTwoEssenceAmount = 200;

        @Config.Comment({"Default: 350"})
        @Config.Name("Tier 3 Data Model - Essence per operation")
        @Config.RangeInt(min = 1, max = 2000)
        public int tierThreeEssenceAmount = 350;

        @Config.Comment({"Default: 350"})
        @Config.Name("Tier 4 Data Model - Essence per operation")
        @Config.RangeInt(min = 1, max = 2000)
        public int tierFourEssenceAmount = 600;

        public int getTierEssenceAmount(int tier) {
            switch (tier) {
                case 1: return MathHelper.ensureRange(tierOneEssenceAmount, 1, 2000);
                case 2: return MathHelper.ensureRange(tierTwoEssenceAmount, 1, 2000);
                case 3: return MathHelper.ensureRange(tierThreeEssenceAmount, 1, 2000);
                case 4: return MathHelper.ensureRange(tierFourEssenceAmount, 1, 2000);
                default: return 1;
            }
        }
    }

    public static class CatalystMultiplierSubCategory {
        @Config.Comment({"Default: 5.0"})
        @Config.Name("#1 Multiplier of Corrupted Glitch Heart")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double heartCatalystMultiplier = 5.0;
        public double getHeartCatalystMultiplier() {
            return MathHelper.ensureRange(heartCatalystMultiplier, 1.0, 10.0);
        }

        @Config.Comment({"Default: 1.2"})
        @Config.Name("#2 Multiplier of Overworldian Living matter")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double overworldianCatalystMultiplier = 1.2;
        public double getOverworldianCatalystMultiplier() {
            return MathHelper.ensureRange(overworldianCatalystMultiplier, 1.0, 10.0);
        }

        @Config.Comment({"Default: 1.4"})
        @Config.Name("#3 Multiplier of Hellish Living matter")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double hellishCatalystMultiplier = 1.4;
        public double getHellishCatalystMultiplier() {
            return MathHelper.ensureRange(hellishCatalystMultiplier, 1.0, 10.0);
        }

        @Config.Comment({"Default: 1.7"})
        @Config.Name("#4 Multiplier of Extraterrestrial Living matter")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double extraterrestrialCatalystMultiplier = 1.7;
        public double getExtraterrestrialCatalystMultiplier() {
            return MathHelper.ensureRange(extraterrestrialCatalystMultiplier, 1.0, 10.0);
        }

        @Config.Comment({"Default: 1.5"})
        @Config.Name("#5 Multiplier of Twilight Living matter")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double twilightCatalystMultiplier = 1.5;
        public double getTwilightCatalystMultiplier() {
            return MathHelper.ensureRange(twilightCatalystMultiplier, 1.0, 10.0);
        }
    }


}
