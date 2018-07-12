package xt9.deepmoblearningbm.plugin.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import xt9.deepmoblearning.common.util.Color;
import xt9.deepmoblearning.common.util.DataModel;
import xt9.deepmoblearning.common.util.Tier;
import xt9.deepmoblearningbm.util.EssenceHelper;
import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by xt9 on 2018-07-05.
 */
public class DigitalAgonizerRecipeWrapper implements IRecipeWrapper {
    private int tier = 1;
    private long ticks = 0;
    private long lastWorldTime;
    private final NonNullList<ItemStack> dataModels;

    private final NonNullList<ItemStack> inputs = NonNullList.create();
    private final FluidStack output;


    public DigitalAgonizerRecipeWrapper(DigitalAgonizerRecipe recipe) {
        dataModels = recipe.dataModels;

        inputs.addAll(dataModels);
        output = recipe.essence;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutput(FluidStack.class, output);
    }

    private void cycleTierAndModel() {
        if(Tier.isMaxTier(tier)) {
            tier = 1;
        } else {
            tier = tier + 1;
        }
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        render(minecraft);

        if(lastWorldTime == minecraft.world.getTotalWorldTime()) {
            return;
        } else {
            ticks++;
            lastWorldTime = minecraft.world.getTotalWorldTime();
        }

        if(ticks % (20 * 2) == 0)  {
            cycleTierAndModel();
        }
    }

    public void render(Minecraft minecraft) {
        FontRenderer render = minecraft.fontRenderer;

        String tierName = Tier.getTierName(tier, false);
        render.drawStringWithShadow(tierName, 2, 27, Color.WHITE);

        NumberFormat f = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DataModel.setTier(dataModels.get(0), tier);

        String amount = f.format(EssenceHelper.getFillAmount(dataModels.get(0), 1.0)) + "mB";
        render.drawStringWithShadow(amount, 114 - render.getStringWidth(amount), 27, Color.WHITE);

       // render.drawStringWithShadow(catalyst.getMultiplier() + "x", 60, 4, Color.WHITE);
    }
}
