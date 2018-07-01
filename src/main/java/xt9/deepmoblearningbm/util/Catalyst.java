package xt9.deepmoblearningbm.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import xt9.deepmoblearning.common.Registry;
import xt9.deepmoblearningbm.ModConfig;
/**
 * Created by xt9 on 2018-07-01.
 */
public class Catalyst {
    private static NonNullList<Catalyst> catalysts = NonNullList.create();
    private ItemStack stack;
    private double multiplier;
    private int operations;

    private Catalyst(Item item, double multiplier, int operations) {
        this.stack = new ItemStack(item);
        this.multiplier = multiplier;
        this.operations = operations;
    }

    public static void init() {
        addCatalyst(Registry.glitchHeart, ModConfig.essenceMultiplierSubCat.getHeartCatalystMultiplier(), 100);
        addCatalyst(Registry.livingMatterOverworldian, ModConfig.essenceMultiplierSubCat.getOverworldianCatalystMultiplier(), 10);
        addCatalyst(Registry.livingMatterHellish, ModConfig.essenceMultiplierSubCat.getHellishCatalystMultiplier(), 10);
        addCatalyst(Registry.livingMatterExtraterrestrial, ModConfig.essenceMultiplierSubCat.getExtraterrestrialCatalystMultiplier(), 10);
        addCatalyst(Registry.livingMatterTwilight, ModConfig.essenceMultiplierSubCat.getTwilightCatalystMultiplier(), 10);
    }

    public static void addCatalyst(Item item, double multiplier, int operations) {
        catalysts.add(new Catalyst(item, multiplier, operations));
    }

    public int getOperations() {
        return operations;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public static Catalyst getCatalyst(ItemStack stack) {
        for (Catalyst catalyst : catalysts) {
            if(ItemStack.areItemsEqual(catalyst.stack, stack)) {
                return catalyst;
            }
        } return null;
    }

    public static boolean isValidCatalyst(ItemStack stack) {
        for (Catalyst catalyst : catalysts) {
            if(ItemStack.areItemsEqual(catalyst.stack, stack)) {
                return true;
            }
        } return false;
    }
}
