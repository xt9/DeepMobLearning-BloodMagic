package xt9.deepmoblearningbm.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xt9.deepmoblearning.common.items.ItemGlitchHeart;
import xt9.deepmoblearning.common.items.ItemLivingMatter;
import xt9.deepmoblearning.common.util.DataModel;
import xt9.deepmoblearningbm.ModConfig;

/**
 * Created by xt9 on 2018-07-01.
 */
public class EssenceHelper {

    public static int getFillAmount(ItemStack dataModel, double multiplier) {
        return (int) (getFluidBaseAmount(dataModel) * multiplier);
    }

    /* mB of fluid per tier as base */
    private static int getFluidBaseAmount(ItemStack dataModel) {
        int tier = DataModel.getTier(dataModel);
        return ModConfig.essenceAmountSubCat.getTierEssenceAmount(tier);
    }
}
