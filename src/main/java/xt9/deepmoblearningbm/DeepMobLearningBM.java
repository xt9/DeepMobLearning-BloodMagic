package xt9.deepmoblearningbm;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xt9.deepmoblearningbm.common.Registry;
import xt9.deepmoblearningbm.common.ServerProxy;
import xt9.deepmoblearningbm.common.network.HighlightAltarMessage;
import xt9.deepmoblearningbm.common.tile.IContainerProvider;
import xt9.deepmoblearningbm.util.Catalyst;

/**
 * Created by xt9 on 2018-06-30.
 */
@Mod(modid = ModConstants.MODID, version = ModConstants.VERSION, dependencies = "required-after:deepmoblearning;required-after:bloodmagic;after:jei;after:twilightforest")
@Mod.EventBusSubscriber
public class DeepMobLearningBM {
    private int networkID = 0;

    @Mod.Instance(ModConstants.MODID)
    public static DeepMobLearningBM instance;

    @SidedProxy(
        clientSide="xt9.deepmoblearningbm.client.ClientProxy",
        serverSide="xt9.deepmoblearningbm.common.ServerProxy"
    )
    public static ServerProxy proxy;
    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModConstants.MODID);
        network.registerMessage(HighlightAltarMessage.Handler.class, HighlightAltarMessage.class, networkID++, Side.SERVER);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Registry.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Registry.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        Registry.registerItemModels();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Catalyst.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler() {
            public Object getServerGuiElement(int i, EntityPlayer player, World world, int x, int y, int z) {
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                return tile instanceof IContainerProvider ? ((IContainerProvider) tile).getContainer(tile, player, world, x, y, z) : null;
            }
            public Object getClientGuiElement(int i, EntityPlayer player, World world, int x, int y, int z) {
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                return tile instanceof IContainerProvider ? ((IContainerProvider) tile).getGui(tile, player, world, x, y, z) : null;
            }
        });
    }

    public static CreativeTabs creativeTab = new CreativeTabs(ModConstants.MODID) {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getTabIconItem() {
            return ItemStack.EMPTY;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(Registry.blockDigitalAgonizerItem);
        }
    };
}
