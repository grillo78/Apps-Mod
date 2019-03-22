package com.grillo78.appsmod.renderers;

import com.grillo78.appsmod.models.PrintedBlockModel;
import com.grillo78.appsmod.tileentity.TileEntityPrintedBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PrintedBlockRenderer extends TileEntitySpecialRenderer<TileEntityPrintedBlock>{

	private static PrintedBlockModel model = new PrintedBlockModel();
	
	public PrintedBlockRenderer() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void render(TileEntityPrintedBlock te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x , y , z);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/blocks/iron_block.png"));
		ModelRenderer[] modelRenderer = te.getModelRenderer(model);
		for(int i=0; i < modelRenderer.length; i++) {
			modelRenderer[i].render(0.0625F);
		}
		GlStateManager.popMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
}
