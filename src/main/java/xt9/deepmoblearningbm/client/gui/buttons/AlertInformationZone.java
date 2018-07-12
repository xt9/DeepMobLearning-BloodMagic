package xt9.deepmoblearningbm.client.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import xt9.deepmoblearningbm.ModConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xt9 on 2018-07-06.
 */
public class AlertInformationZone extends GuiButton {
    private List<String> tooltip = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;
    protected static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MODID,"textures/gui/buttons/buttons.png");

    public AlertInformationZone(int buttonId, int x, int y, int widthIn, int heightIn, int screenWidth, int screenHeight) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void setTooltip(List<String> tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(TEXTURE);
        RenderHelper.disableStandardItemLighting();
        drawTexturedModalRect(x, y, 0, 0, width, height);

        hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        if(hovered) {
            GuiUtils.drawHoveringText(tooltip, mouseX, mouseY, screenWidth, screenHeight, -1, mc.fontRenderer);
        }
    }
}
