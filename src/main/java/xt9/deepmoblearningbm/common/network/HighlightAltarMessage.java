package xt9.deepmoblearningbm.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xt9.deepmoblearningbm.common.inventory.ContainerDigitalAgonizer;

/**
 * Created by xt9 on 2018-07-02.
 */
public class HighlightAltarMessage implements IMessage {
    public HighlightAltarMessage() {}

    @Override
    public void fromBytes(ByteBuf byteBuf) {}

    @Override
    public void toBytes(ByteBuf byteBuf) {}

    public static class Handler implements IMessageHandler<HighlightAltarMessage, IMessage> {

        @Override
        public IMessage onMessage(HighlightAltarMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;

            player.getServerWorld().addScheduledTask(() -> {
                if(player.openContainer instanceof ContainerDigitalAgonizer) {
                    ContainerDigitalAgonizer container = (ContainerDigitalAgonizer) player.openContainer;
                    container.tile.setHighlightingTicks(20 * 20);
                    player.sendStatusMessage(new TextComponentString("Displaying altar position for 20 seconds!"), true);
                }
            });
            return null;
        }
    }
}
