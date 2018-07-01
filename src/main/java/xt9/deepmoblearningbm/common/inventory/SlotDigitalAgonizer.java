package xt9.deepmoblearningbm.common.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import xt9.deepmoblearning.common.items.ItemDataModel;
import xt9.deepmoblearningbm.util.Catalyst;

/**
 * Created by xt9 on 2018-06-30.
 */
public class SlotDigitalAgonizer extends SlotItemHandler {
    public SlotDigitalAgonizer(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        Item item = stack.getItem();
        switch(getSlotIndex()) {
            case ContainerDigitalAgonizer.DATA_MODEL_SLOT:
                return !stack.isEmpty() && item instanceof ItemDataModel;
            case ContainerDigitalAgonizer.CATALYST_SLOT:
                return !stack.isEmpty() && Catalyst.isValidCatalyst(stack);
            default:
                return false;
        }
    }

    @Override
    public int getSlotStackLimit() {
        return 64;
    }
}
