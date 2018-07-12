package xt9.deepmoblearningbm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import xt9.deepmoblearningbm.client.particles.ParticleIndicator;
import xt9.deepmoblearningbm.common.ServerProxy;

/**
 * Created by xt9 on 2018-06-30.
 */
public class ClientProxy extends ServerProxy {
    public void registerItemRenderer(Item item, ResourceLocation location, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, "inventory"));
    }

    public void spawnParticle(World world, double x, double y, double z, double mx, double my, double mz) {
        Particle particle = new ParticleIndicator(world, x, y, z, mx, my, mz, 1.3F);
        particle.setRBGColorF(1.0F, 0.3F, 0.3F);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }
}
