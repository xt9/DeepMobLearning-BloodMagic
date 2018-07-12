package xt9.deepmoblearningbm.plugin.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import xt9.deepmoblearning.DeepConstants;
import xt9.deepmoblearningbm.ModConstants;
import xt9.deepmoblearningbm.common.Registry;

/**
 * Created by xt9 on 2018-07-08.
 */
public class DigitalAgonizerCatalystCategory implements IRecipeCategory {
    private ItemStack catalyst;
    private IDrawable background;
    private final IDrawable slotBackground;

    public DigitalAgonizerCatalystCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(114, 30);
        this.catalyst = new ItemStack(Registry.blockDigitalAgonizerItem);
        slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();

        guiItemStacks.init(0, true, 6, 6);
        guiItemStacks.setBackground(0, slotBackground);
        guiItemStacks.set(ingredients);
    }

    public void addCatalysts(IModRegistry registry) {
        registry.addRecipeCatalyst(catalyst, getUid());
    }

    @Override
    public String getUid() {
        return ModConstants.MODID + ".digital_agonizer_catalyst";
    }

    @Override
    public String getTitle() {
        return "Agonizer Catalysts";
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
