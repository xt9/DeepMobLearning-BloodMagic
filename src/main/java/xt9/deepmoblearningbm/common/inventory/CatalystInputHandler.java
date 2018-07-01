package xt9.deepmoblearningbm.common.inventory;

import net.minecraft.item.ItemStack;
import xt9.deepmoblearning.common.handlers.BaseItemHandler;
import xt9.deepmoblearningbm.util.Catalyst;

import javax.annotation.Nonnull;

/**
 * Created by xt9 on 2018-06-30.
 */
public class CatalystInputHandler extends BaseItemHandler {
    public CatalystInputHandler() {
        super();
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(Catalyst.isValidCatalyst(stack)) {
            return super.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }
}
