package com.grillo78.appsmod.gui;

import com.grillo78.appsmod.Reference;
import com.grillo78.appsmod.inventory.ContainerThreeDPrinter;
import com.grillo78.appsmod.tileentity.TileEntityThreeDPrinter;
import com.grillo78.appsmod.tileentity.TileEntityThreeDPrinterDevice;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ThreeDPrinterUI extends GuiContainer{

	private static final ResourceLocation PRINTER_GUI_TEXTURES = new ResourceLocation(Reference.MODID,"textures/gui/container/printer.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final TileEntityThreeDPrinterDevice tilePrinter;
    
	public ThreeDPrinterUI(InventoryPlayer playerInv, TileEntityThreeDPrinterDevice printerInv) {
		super(new ContainerThreeDPrinter(playerInv, printerInv));
		playerInventory = Minecraft.getMinecraft().player.inventory;
		tilePrinter = printerInv;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.tilePrinter.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString("Copy UUID:", 8, (ySize/2)-19, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(PRINTER_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        int k = this.getTempLeftScaled(13);
        this.drawTexturedModalRect(i + 56, j + 36 + 6 - k, 176, 0, 14, k + 1);
    }
	
	private int getTempLeftScaled(int pixels)
    {
        int i = this.tilePrinter.getField(0);

        if (i == 0)
        {
            i = 200;
        }

        return this.tilePrinter.getField(1) * pixels / i;
    }
}
