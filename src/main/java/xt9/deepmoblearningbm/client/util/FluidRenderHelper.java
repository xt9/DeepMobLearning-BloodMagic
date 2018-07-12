package xt9.deepmoblearningbm.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;


public class FluidRenderHelper {
    private static Minecraft mc = Minecraft.getMinecraft();

    //FluidStack fluidStack = tile.getTank().getFluid();
    //if(fluidStack != null) {
    //TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill(fluidStack).toString());
    //int filledAmount = (int) (((float) tile.getTank().getFluidAmount() / tile.getTank().getCapacity() * 49));
    //int offset = 49 - filledAmount;
    //FluidRenderHelper.renderTiledTextureAtlas(left + 131, top + 17 + offset, 16, filledAmount, 100, sprite);
    //}

    /*
     * The following code is a slightly altered version of the renderTiledTextureAtlas method in Tinkers Construct
     * https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/slimeknights/tconstruct/library/client/GuiUtil.java#L45-L54
     * The MIT License (MIT)
     * Copyright (c) 2018 SlimeKnights/KnightMiner
     */
    public static void renderTiledTextureAtlas(int x, int y, int width, int height, float depth, TextureAtlasSprite sprite) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        putTiledTextureQuads(worldrenderer, x, y, width, height, depth, sprite);
        tessellator.draw();
    }

    /*
     * The following code is a slightly altered version of the putTiledTextureQuads method in Tinkers Construct
     * https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/slimeknights/tconstruct/library/client/GuiUtil.java#L63-L101
     * The MIT License (MIT)
     * Copyright (c) 2018 SlimeKnights/KnightMiner
     */
    private static void putTiledTextureQuads(BufferBuilder buffer, int x, int y, int width, int height, float depth, TextureAtlasSprite sprite) {
        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();

        // tile vertically
        do {
            int renderHeight = Math.min(sprite.getIconHeight(), height);
            height -= renderHeight;

            float v2 = sprite.getInterpolatedV((16f * renderHeight) / (float) sprite.getIconHeight());

            // we need to draw the quads per width too
            int x2 = x;
            int width2 = width;
            // tile horizontally
            do {
                int renderWidth = Math.min(sprite.getIconWidth(), width2);
                width2 -= renderWidth;
                float u2 = sprite.getInterpolatedU((16f * renderWidth) / (float) sprite.getIconWidth());

                buffer.pos(x2, y, depth).tex(u1, v1).endVertex();
                buffer.pos(x2, y + renderHeight, depth).tex(u1, v2).endVertex();
                buffer.pos(x2 + renderWidth, y + renderHeight, depth).tex(u2, v2).endVertex();
                buffer.pos(x2 + renderWidth, y, depth).tex(u2, v1).endVertex();

                x2 += renderWidth;
            } while(width2 > 0);

            y += renderHeight;
        } while(height > 0);
    }
}
