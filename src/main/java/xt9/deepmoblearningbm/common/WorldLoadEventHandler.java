package xt9.deepmoblearningbm.common;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xt9.deepmoblearningbm.common.tile.TileEntityDigitalAgonizer;

@Mod.EventBusSubscriber
public class WorldLoadEventHandler {

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        // Only run on server-side worlds
        if (event.getWorld().isRemote) {
            return;
        }

        MinecraftServer server = event.getWorld().getMinecraftServer();
        if (server != null && server.isSinglePlayer()) {
            TileEntityDigitalAgonizer.linkedPositions.clear();
        }
    }
}
