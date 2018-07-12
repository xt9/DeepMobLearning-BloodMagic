package xt9.deepmoblearningbm.plugin.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import xt9.deepmoblearning.DeepConstants;
import xt9.deepmoblearningbm.ModConstants;
import xt9.deepmoblearningbm.common.Registry;

/**
 * Created by xt9 on 2018-07-05.
 */
public class DigitalAgonizerRecipeCategory implements IRecipeCategory {
    private ItemStack catalyst;
    private IDrawable background;
    private IDrawableAnimated progress;

    public DigitalAgonizerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation base = new ResourceLocation(ModConstants.MODID, "textures/gui/jei/digital_agonizer.png");
        this.catalyst = new ItemStack(Registry.blockDigitalAgonizerItem);

        background = guiHelper.createDrawable(base, 0, 0, 116, 36, 0, 0, 0, 0);
        IDrawableStatic progress = guiHelper.createDrawable(base, 0, 43, 35, 6);
        this.progress = guiHelper.createAnimatedDrawable(progress, 120, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = layout.getFluidStacks();

        guiItemStacks.init(0, true, 12, 3);
        guiItemStacks.set(ingredients);

        guiFluidStacks.init(1, true, 96, 4);
        guiFluidStacks.set(1, ingredients.getOutputs(FluidStack.class).get(0));
    }

    public void addCatalysts(IModRegistry registry) {
        registry.addRecipeCatalyst(catalyst, getUid());
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        progress.draw(minecraft, 52, 9);
    }

    @Override
    public String getUid() {
        return ModConstants.MODID + ".digital_agonizer";
    }

    @Override
    public String getTitle() {
        return catalyst.getDisplayName();
    }

    @Override
    public String getModName() {
        return DeepConstants.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }
}
