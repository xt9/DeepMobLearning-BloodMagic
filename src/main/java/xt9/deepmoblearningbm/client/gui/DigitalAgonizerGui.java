package xt9.deepmoblearningbm.client.gui;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import org.lwjgl.opengl.GL11;
import xt9.deepmoblearning.common.energy.DeepEnergyStorage;
import xt9.deepmoblearning.common.util.MathHelper;
import xt9.deepmoblearningbm.DeepMobLearningBM;
import xt9.deepmoblearningbm.ModConfig;
import xt9.deepmoblearningbm.ModConstants;
import xt9.deepmoblearningbm.client.gui.buttons.AlertInformationZone;
import xt9.deepmoblearningbm.client.gui.buttons.ClickableZoneButton;
import xt9.deepmoblearningbm.client.gui.buttons.ZoneButton;
import xt9.deepmoblearningbm.common.inventory.ContainerDigitalAgonizer;
import xt9.deepmoblearningbm.common.network.HighlightAltarMessage;
import xt9.deepmoblearningbm.common.tile.TileEntityDigitalAgonizer;

import java.io.IOException;
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
    private ZoneButton sacRuneZone;
    private ClickableZoneButton altarButton;
    private AlertInformationZone alertButton;
    private NumberFormat f = NumberFormat.getNumberInstance(Locale.ENGLISH);

    public DigitalAgonizerGui(TileEntityDigitalAgonizer tile, InventoryPlayer inventory, World world) {
        super(new ContainerDigitalAgonizer(tile, inventory, world));
        this.tile = tile;
        xSize = WIDTH;
        ySize = HEIGHT;
        energyStorage = (DeepEnergyStorage) tile.getCapability(CapabilityEnergy.ENERGY, null);
    }

    @Override
    public void initGui() {
        super.initGui();
        sacRuneZone = new ZoneButton(0, getGuiLeft() + 120, getGuiTop() - 8, 16, 16, this.width, this.height);
        altarButton = new ClickableZoneButton(1, getGuiLeft() + 130, getGuiTop() + 35, 16, 16, this.width, this.height);
        alertButton = new AlertInformationZone(2, getGuiLeft() + 114, getGuiTop() + 79, 16, 16, this.width, this.height);
        buttonList.add(sacRuneZone);
        buttonList.add(altarButton);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        buttonList.forEach(guiButton -> {
            if(guiButton.isMouseOver()) {
                handleButtonClick(guiButton);
            }
        });
    }

    private void handleButtonClick(GuiButton guiButton) {
        if(guiButton.id == altarButton.id) {
            if(tile.getAltarTank() != null) {
                DeepMobLearningBM.network.sendToServer(new HighlightAltarMessage());
            }
        }
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

        List<String> energyTooltip = new ArrayList<>();
        if(17 <= y && y < 66) {
            if(52 <= x && x < 59) {
                // Tooltip for energy
                energyTooltip.add(f.format(energyStorage.getEnergyStored()) + "/" + f.format(energyStorage.getMaxEnergyStored()) + " RF");
                energyTooltip.add("Operational cost: " + f.format(ModConfig.getAgonizerRFCost()) + " RF/t");
                drawHoveringText(energyTooltip, x, y);
            }
        }

        List<String> runeTooltips = new ArrayList<>();
        List<String> altarTooltip = new ArrayList<>();
        List<String> alertTooltips = new ArrayList<>();

        if(tile.getAltarTank() != null) {
            BloodAltar altar = tile.getAltarTank();
            runeTooltips.add("Altar has " + tile.getNumOfSacrificeRunes() + " sacrifice runes.");
            runeTooltips.add("Rune multiplier: " + altar.getSacrificeMultiplier() + "x");
            runeTooltips.add("Catalyst multiplier: " + tile.getMultiplier() + "x");
            runeTooltips.add("Final multiplier: " + f.format(tile.getMultiplier() + tile.getSacrificeMultiplier()) + "x");

            altarTooltip.add("Linked to altar at position");
            altarTooltip.add("x: " + tile.getAltarPos().getX() + ", y: " + tile.getAltarPos().getY() + ", z: " + tile.getAltarPos().getZ() + "");
            altarTooltip.add("Tank: " + f.format(altar.getFluidAmount()) + "/" + f.format(altar.getCapacity()) + "mB");
            altarTooltip.add("");
            altarTooltip.add("§oClick icon to reveal altar§r");
            altarTooltip.add("§oposition for 20 seconds.§r");
        } else {
            altarTooltip.add("No linked altar!");
            altarTooltip.add("Use the Altar Linker to link this");
            altarTooltip.add("machine to an Altar.");
        }

        alertTooltips.add("§cIssues§r");
        if(!tile.hasDataModel()) {
            alertTooltips.add("- No Data Model");
        }
        if(tile.hasDataModel() && !tile.isValidDataModelTier()) {
            alertTooltips.add("- Data Model Tier too low");
        }
        if(tile.getAltarTank() == null) {
            alertTooltips.add("- No linked Altar");
        }

        sacRuneZone.setTooltip(runeTooltips);
        altarButton.setTooltip(altarTooltip);
        alertButton.setTooltip(alertTooltips);
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
        int craftingBarWidth = (int) (((float) tile.getProgress() / 60 * 36));
        drawTexturedModalRect(left + 88,  top + 39, 25, 59, craftingBarWidth, 6);

        // Draw current energy
        int energyBarHeight = MathHelper.ensureRange((int) ((float) energyStorage.getEnergyStored() / (energyStorage.getMaxEnergyStored() - ModConfig.getAgonizerRFCost()) * 49), 0, 49);
        int energyBarOffset = 49 - energyBarHeight;
        drawTexturedModalRect(left + 52,  top + 17 + energyBarOffset, 0, 59, 7, energyBarHeight);

        // Draw data model slot
        drawTexturedModalRect(left + 91, top + 78, 7, 59, 18, 18);

        // Draw Error icon or Altar
        if(tile.getAltarTank() == null) {
            drawTexturedModalRect(left + 129, top + 35, 60, 59, 15, 15);
        } else {
            drawItemStack(left + 130, top + 34, new ItemStack(RegistrarBloodMagicBlocks.ALTAR));
            // Draw sac runes
            drawItemStack(left + 120, top - 8, new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 3));
            drawString(fontRenderer,  tile.getNumOfSacrificeRunes() + "", left + 140, top - 4, 0xFFFFFF);
        }

        // Draw current multiplier
        drawCenteredString(fontRenderer, f.format(tile.getMultiplier() + tile.getSacrificeMultiplier()) + "x", left + 105, top + 28, 0xFFFFFF);

        if(tile.hasDataModel() && tile.isValidDataModelTier()) {
            drawCenteredString(fontRenderer,  tile.getFillAmount() + "mB", left + 105, top + 49, 0xFFFFFF);
        }

        // Draw player inventory
        Minecraft.getMinecraft().getTextureManager().bindTexture(defaultGui);
        drawTexturedModalRect( left + 12, top + 106, 0, 0, 176, 90);

        // Draw catalyst operations
        drawGradientRect(left + 66, top + 31, left + 82, top + 33, 0xFF424242, 0xFF333333);
        int catalystWidth = (int) (((float) tile.getCatalystOperations() / tile.getCatalystOperationsMax() * 16));
        drawGradientRect(left + 66, top + 31, left + 66 + catalystWidth, top + 33, 0xFFa142f4, 0xFF852cd3);

        buttonList.forEach(guiButton -> guiButton.drawButton(mc, mouseX, mouseY, mc.getRenderPartialTicks()));
        if(!tile.hasDataModel() || (tile.hasDataModel() && !tile.isValidDataModelTier()) || tile.getAltarTank() == null) {
            alertButton.drawButton(mc, mouseX, mouseY, mc.getRenderPartialTicks());
        }
    }

    private void drawItemStack(int x, int y, ItemStack stack) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        this.zLevel = 1.0F;
        itemRender.zLevel = 1.0F;
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();

        RenderHelper.disableStandardItemLighting();
    }
}
