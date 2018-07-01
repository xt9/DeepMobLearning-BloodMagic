package xt9.deepmoblearningbm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import xt9.deepmoblearning.common.energy.DeepEnergyStorage;
import xt9.deepmoblearning.common.util.MathHelper;
import xt9.deepmoblearningbm.ModConfig;
import xt9.deepmoblearningbm.ModConstants;
import xt9.deepmoblearningbm.client.util.FluidRenderHelper;
import xt9.deepmoblearningbm.common.inventory.ContainerDigitalAgonizer;
import xt9.deepmoblearningbm.common.tile.TileEntityDigitalAgonizer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by xt9 on 2018-06-30.
 */
public class DigitalAgonizerGui extends GuiContainer {
    private static final ResourceLocation defaultGui = new ResourceLocation(ModConstants.MODID, "textures/gui/default_gui.png");
    private static final ResourceLocation base = new ResourceLocation(ModConstants.MODID, "textures/gui/digital_agonizer_gui.png");

    private TileEntityDigitalAgonizer tile;
    private static final int WIDTH =  200;
    private static final int HEIGHT = 178;
    private DeepEnergyStorage energyStorage;

    public DigitalAgonizerGui(TileEntityDigitalAgonizer tile, InventoryPlayer inventory, World world) {
        super(new ContainerDigitalAgonizer(tile, inventory, world));
        this.tile = tile;
        xSize = WIDTH;
        ySize = HEIGHT;
        energyStorage = (DeepEnergyStorage) tile.getCapability(CapabilityEnergy.ENERGY, null);
    }

    /* Needed on 1.12 to render tooltips */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(base);

        int x = mouseX - guiLeft;
        int y = mouseY - guiTop;


        NumberFormat f = NumberFormat.getNumberInstance(Locale.ENGLISH);
        List<String> energyTooltip = new ArrayList<>();
        List<String> liquidTooltip = new ArrayList<>();

        if(17 <= y && y < 66) {
            if(52 <= x && x < 59) {
                // Tooltip for energy
                energyTooltip.add(f.format(energyStorage.getEnergyStored()) + "/" + f.format(energyStorage.getMaxEnergyStored()) + " RF");
                energyTooltip.add("Operational cost: " + f.format(ModConfig.getAgonizerRFCost()) + " RF/t");
                drawHoveringText(energyTooltip, x - 16, y - 16);
            }
        }

        if(17 <= y && y < 66) {
            if(132 <= x && x < 148) {
                // todo altar information
                // Tooltip for energy
                //liquidTooltip.add(f.format(tile.getTank().getFluidAmount()) + "/" + f.format(tile.getTank().getCapacity()) + "mB");
                //liquidTooltip.add(tile.getEssenceFluid().getLocalizedName(tile.getTank().getFluid()));
                drawHoveringText(liquidTooltip, x - 16, y - 16);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int left = getGuiLeft();
        int top = getGuiTop();

        // Draw the main GUI
        Minecraft.getMinecraft().getTextureManager().bindTexture(base);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(left + 46, top + 12, 0, 0, 107, 59);

        // Draw crafting progress
        int craftingBarWidth = (int) (((float) tile.getProgress() / 60 * 37));
        drawTexturedModalRect(left + 88,  top + 39, 25, 59, craftingBarWidth, 6);

        // Draw current energy
        int energyBarHeight = MathHelper.ensureRange((int) ((float) energyStorage.getEnergyStored() / (energyStorage.getMaxEnergyStored() - ModConfig.getAgonizerRFCost()) * 49), 0, 49);
        int energyBarOffset = 49 - energyBarHeight;
        drawTexturedModalRect(left + 52,  top + 17 + energyBarOffset, 0, 59, 7, energyBarHeight);

        // Draw data model slot
        drawTexturedModalRect(left + 91, top + 78, 7, 59, 18, 18);

        // Draw current multiplier
        drawCenteredString(fontRenderer, "x" + tile.getMultiplier(), getGuiLeft() + 105, top + 28, 0xFFFFFF);

        if(tile.hasValidDataModel()) {
            drawCenteredString(fontRenderer,  tile.getFillAmount() + "mB", getGuiLeft() + 105, top + 49, 0xFFFFFF);
        }

        //FluidStack fluidStack = tile.getTank().getFluid();
        //if(fluidStack != null) {
            //TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill(fluidStack).toString());
            //int filledAmount = (int) (((float) tile.getTank().getFluidAmount() / tile.getTank().getCapacity() * 49));
            //int offset = 49 - filledAmount;
            //FluidRenderHelper.renderTiledTextureAtlas(left + 131, top + 17 + offset, 16, filledAmount, 100, sprite);
        //}

        // Draw player inventory
        Minecraft.getMinecraft().getTextureManager().bindTexture(defaultGui);
        drawTexturedModalRect( left + 12, top + 106, 0, 0, 176, 90);


        drawGradientRect(left + 66, top + 31, left + 82, top + 33, 0xFF424242, 0xFF333333);

        int catalystWidth = (int) (((float) tile.getCatalystOperations() / tile.getCatalystOperationsMax() * 16));
        drawGradientRect(left + 66, top + 31, left + 66 + catalystWidth, top + 33, 0xFFa142f4, 0xFF852cd3);
    }
}
