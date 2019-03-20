package com.grillo78.appsmod.gui;

import com.grillo78.appsmod.AppsMod;
import com.grillo78.appsmod.Reference;
import com.grillo78.appsmod.inventory.ContainerThreeDPrinter;
import com.grillo78.appsmod.tileentity.TileEntityThreeDPrinter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ThreeDPrinterUI extends GuiContainer{

	private static final ResourceLocation PRINTER_GUI_TEXTURES = new ResourceLocation(Reference.MODID,"textures/gui/container/printer.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final IInventory tilePrinter;
    
	public ThreeDPrinterUI(InventoryPlayer playerInv, TileEntityThreeDPrinter printerInv) {
		super(new ContainerThreeDPrinter(playerInv, printerInv));
		AppsMod.log.debug("Open GUI");
		playerInventory = Minecraft.getMinecraft().player.inventory;
		tilePrinter = new TileEntityThreeDPrinter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	protected void drawGuiContainerForegroundLayer(float partialTicks, int mouseX, int mouseY) {
		String s = this.tilePrinter.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(PRINTER_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
	
}
