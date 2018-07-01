package xt9.deepmoblearningbm.common.items;

import WayofTime.bloodmagic.tile.TileAltar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import xt9.deepmoblearning.common.util.ItemStackNBTHelper;
import xt9.deepmoblearningbm.common.blocks.BlockDigitalAgonizer;
import xt9.deepmoblearningbm.common.tile.TileEntityDigitalAgonizer;

/**
 * Created by xt9 on 2018-07-01.
 */
public class ItemAgonizerLinker extends ItemBase {

    public ItemAgonizerLinker() {
        super("agonizer_linker", 1);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack agonizerLinker = player.getHeldItem(hand);
        if(!player.world.isRemote) {
            if(player.isSneaking()) {
                if(isBlockDigitalAgonizerAtPos(world, pos)) {
                    setTargetAgonizer(agonizerLinker, pos);
                    player.sendStatusMessage(new TextComponentString("§cSet Agonizer target!§r"), true);
                } else if(isValidAltar(world, pos)) {
                    if(hasTargetAgonizer(agonizerLinker)) {
                        BlockPos agonizerPos = BlockPos.fromLong(getTargetAgonizerPos(agonizerLinker));

                        if(isBlockDigitalAgonizerAtPos(world, agonizerPos)) {
                            TileEntityDigitalAgonizer tile = getAgonizerFromPos(world, agonizerPos);
                            tile.setAltarPos(pos);
                            player.sendStatusMessage(new TextComponentString("§cLinked Altar to target Agonizer!§r"), true);
                        }
                    }
                }
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    private boolean isBlockDigitalAgonizerAtPos(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockDigitalAgonizer;
    }

    private boolean isValidAltar(World world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof TileAltar;
    }

    private TileEntityDigitalAgonizer getAgonizerFromPos(World world, BlockPos pos) {
        return (TileEntityDigitalAgonizer) world.getTileEntity(pos);
    }

    private boolean hasTargetAgonizer(ItemStack stack) {
        return ItemStackNBTHelper.hasKey(stack, "targetAgonizer");
    }

    private long getTargetAgonizerPos(ItemStack stack) {
        return ItemStackNBTHelper.getLong(stack, "targetAgonizer", 0);
    }

    private void setTargetAgonizer(ItemStack stack, BlockPos pos) {
        ItemStackNBTHelper.setLong(stack, "targetAgonizer", pos.toLong());
    }
}
