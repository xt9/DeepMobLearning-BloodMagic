package xt9.deepmoblearningbm.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xt9.deepmoblearningbm.DeepMobLearningBM;

/**
 * Created by xt9 on 2018-06-30.
 */
public class ServerProxy {
    public static void openTileEntityGui(World world, EntityPlayer player, int id, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        // Notify on open so we trigger the TE's getUpdateTag
        world.notifyBlockUpdate(pos, state, state, 3);

        ItemStack mainHandItem = player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offHandItem = player.getHeldItem(EnumHand.OFF_HAND);
        if(mainHandItem.getItem().getToolClasses(mainHandItem).contains("wrench") || offHandItem.getItem().getToolClasses(offHandItem).contains("wrench")) {
            return;
        }

        player.openGui(DeepMobLearningBM.instance, id, player.world, pos.getX(), pos.getY(), pos.getZ());
    }

    public void registerItemRenderer(Item item, ResourceLocation location, int meta) {}
    public void spawnParticle(World world, double x, double y, double z, double mx, double my, double mz) {}
}
