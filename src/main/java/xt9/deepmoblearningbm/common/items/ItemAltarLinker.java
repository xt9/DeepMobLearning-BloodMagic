package xt9.deepmoblearningbm.common.items;

import WayofTime.bloodmagic.tile.TileAltar;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xt9.deepmoblearning.common.util.ItemStackNBTHelper;
import xt9.deepmoblearningbm.ModConfig;
import xt9.deepmoblearningbm.common.blocks.BlockDigitalAgonizer;
import xt9.deepmoblearningbm.common.tile.TileEntityDigitalAgonizer;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by xt9 on 2018-07-01.
 */
public class ItemAltarLinker extends ItemBase {
    private final int linkingRange = 25;

    public ItemAltarLinker() {
        super("altar_linker", 1);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
        list.add("Max linking range: §r" + linkingRange + " blocks");

        if(hasTargetAgonizer(stack)) {
            BlockPos pos = BlockPos.fromLong(getTargetAgonizerPos(stack));
            list.add("Agonizer target: §f(x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ() + ")§r");
            list.add("§fSneak + Right-click§7 to link an altar");
            list.add("Clear target with §rRight-click");
        } else {
            list.add("No Agonizer target, link while sneaking");
            list.add("Clear target with §rRight-click");
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
        if(!player.world.isRemote && !player.isSneaking()) {
            removeTargetAccelerator(player.getHeldItem(handIn));
            player.sendStatusMessage(new TextComponentString("Cleared Target!"), true);
        }
        return super.onItemRightClick(worldIn, player, handIn);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack agonizerLinker = player.getHeldItem(hand);
        if(!player.world.isRemote) {
            if(player.isSneaking()) {
                if(isBlockDigitalAgonizerAtPos(world, pos)) {
                    setTargetAgonizer(agonizerLinker, pos);
                    player.sendStatusMessage(new TextComponentString("Set Agonizer target!"), true);
                } else if(isValidAltar(world, pos)) {
                    if(hasTargetAgonizer(agonizerLinker)) {
                        BlockPos agonizerPos = BlockPos.fromLong(getTargetAgonizerPos(agonizerLinker));

                        if(!ModConfig.isMultipleAgonizersAllowed) {
                            if(isAltarLinked(pos)) {
                                player.sendStatusMessage(new TextComponentString("Altar is already linked to an Agonizer."), true);
                                return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
                            }
                        }

                        if(getBlockDistance(pos, agonizerPos) <= linkingRange) {
                            if (isBlockDigitalAgonizerAtPos(world, agonizerPos)) {
                                TileEntityDigitalAgonizer tile = getAgonizerFromPos(world, agonizerPos);
                                tile.setAltarPos(pos);
                                tile.updateState(true);
                                player.sendStatusMessage(new TextComponentString("Linked Altar to target Agonizer!"), true);
                            }
                        } else {
                            player.sendStatusMessage(new TextComponentString("Altar too far away from Agonizer!"), true);
                        }
                    }
                }
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    private boolean isAltarLinked(BlockPos altarPos) {
        return TileEntityDigitalAgonizer.linkedPositions.values()
                .stream()
                .anyMatch(pos -> pos.equals(altarPos));
    }

    /* Maths: http://www.meracalculator.com/math/distance-between-2-points(3-dim).php */
    private double getBlockDistance(BlockPos pos, BlockPos pos2) {
        double x = Math.pow((pos.getX() - pos2.getX()), 2);
        double y = Math.pow((pos.getY() - pos2.getY()), 2);
        double z = Math.pow((pos.getZ() - pos2.getZ()), 2);

        return Math.sqrt(x + y + z);
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

    private void removeTargetAccelerator(ItemStack stack) {
        ItemStackNBTHelper.removeTag(stack, "targetAgonizer");
    }

    private long getTargetAgonizerPos(ItemStack stack) {
        return ItemStackNBTHelper.getLong(stack, "targetAgonizer", 0);
    }

    private void setTargetAgonizer(ItemStack stack, BlockPos pos) {
        ItemStackNBTHelper.setLong(stack, "targetAgonizer", pos.toLong());
    }
}
