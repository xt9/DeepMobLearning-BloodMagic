package xt9.deepmoblearningbm.plugin.jei;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import java.util.ArrayList;

/**
 * Created by xt9 on 2018-07-05.
 */
public class DigitalAgonizerRecipe {
    public static ArrayList<DigitalAgonizerRecipe> recipes = new ArrayList<>();
    public final NonNullList<ItemStack> dataModels;
    public final FluidStack essence;


    private DigitalAgonizerRecipe(NonNullList<ItemStack> dataModels) {
        this.dataModels = dataModels;
        this.essence = new FluidStack(FluidRegistry.getFluid("lifeessence"), 1000);
    }

    public static void addRecipe(NonNullList<ItemStack> dataModels) {
        recipes.add(new DigitalAgonizerRecipe(dataModels));
    }
}
