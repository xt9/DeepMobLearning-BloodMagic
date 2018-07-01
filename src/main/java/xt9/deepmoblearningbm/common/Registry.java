package xt9.deepmoblearningbm.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import xt9.deepmoblearningbm.ModConstants;
import xt9.deepmoblearningbm.common.blocks.BlockDigitalAgonizer;
import xt9.deepmoblearningbm.common.items.ItemAgonizerLinker;
import xt9.deepmoblearningbm.common.tile.TileEntityDigitalAgonizer;

/**
 * Created by xt9 on 2018-06-30.
 */
public class Registry {
    private static BlockDigitalAgonizer blockDigitalAgonizer = new BlockDigitalAgonizer();
    public static Item blockDigitalAgonizerItem = new ItemBlock(blockDigitalAgonizer).setRegistryName(blockDigitalAgonizer.getRegistryName());
    public static ItemAgonizerLinker itemAgonizerLinker = new ItemAgonizerLinker();

    private static NonNullList<Block> blocks = NonNullList.create();
    private static NonNullList<Item> itemBlocks = NonNullList.create();
    private static NonNullList<Item> items = NonNullList.create();

    @SuppressWarnings("unchecked")
    public static void registerBlocks(IForgeRegistry registry) {
        if(ModConstants.MOD_BM_LOADED) {
            blocks.add(blockDigitalAgonizer);
        }
        blocks.forEach(registry::register);
        registerTileEntities();
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public static void registerItems(IForgeRegistry registry) {
        if(ModConstants.MOD_BM_LOADED) {
            itemBlocks.add(blockDigitalAgonizerItem);
            items.add(itemAgonizerLinker);
        }
        itemBlocks.forEach(registry::register);
        items.forEach(registry::register);
    }

    private static void registerTileEntities() {
        if(ModConstants.MOD_BM_LOADED) {
            GameRegistry.registerTileEntity(TileEntityDigitalAgonizer.class, new ResourceLocation(ModConstants.MODID, "digital_agonizer"));
        }
    }
}
