package xt9.deepmoblearningbm.plugin.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import xt9.deepmoblearning.common.util.Color;
import xt9.deepmoblearningbm.util.Catalyst;

/**
 * Created by xt9 on 2018-07-08.
 */
public class DigitalAgonizerCatalystWrapper implements IRecipeWrapper {
    private final Catalyst catalyst;
    private final NonNullList<ItemStack> inputs = NonNullList.create();

    public DigitalAgonizerCatalystWrapper(Catalyst catalyst) {
        this.catalyst = catalyst;
        inputs.add(catalyst.getStack());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        FontRenderer render = minecraft.fontRenderer;

        render.drawStringWithShadow("Multiplier: §b" + catalyst.getMultiplier() + "x", 32, 3, Color.WHITE);
        render.drawStringWithShadow("Operations: §b"+ catalyst.getOperations(), 32, 15, Color.WHITE);
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInput(ItemStack.class, inputs.get(0));
    }
}
