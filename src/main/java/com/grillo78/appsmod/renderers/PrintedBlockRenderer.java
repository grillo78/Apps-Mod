package com.grillo78.appsmod.renderers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
		renderModel(te);
		GlStateManager.popMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
	
	public static void renderModel(TileEntityPrintedBlock te) {
		JsonParser jp = new JsonParser();
		if(te.getModel()!=null) {
			JsonArray elements = jp.parse(te.getModel()).getAsJsonObject().get("elements").getAsJsonArray();
			ModelRenderer[] modelRenderer = new ModelRenderer[elements.size()];
			for(int i = 0; i < modelRenderer.length; i++) {
				modelRenderer[i] = new ModelRenderer(model);
				JsonObject actualElement = elements.get(i).getAsJsonObject();
				int width = (int) ((actualElement.get("to").getAsJsonArray().get(0).getAsFloat())-(actualElement.get("from").getAsJsonArray().get(0).getAsFloat()));
				int height = (int) ((actualElement.get("to").getAsJsonArray().get(1).getAsFloat())-(actualElement.get("from").getAsJsonArray().get(1).getAsFloat()));
				int depth = (int) ((actualElement.get("to").getAsJsonArray().get(2).getAsFloat())-(actualElement.get("from").getAsJsonArray().get(2).getAsFloat()));
				modelRenderer[i].addBox(actualElement.get("from").getAsJsonArray().get(0).getAsFloat(), actualElement.get("from").getAsJsonArray().get(1).getAsFloat(), actualElement.get("from").getAsJsonArray().get(2).getAsFloat(), width, height, depth);
			}
			for(int i=0; i < modelRenderer.length; i++) {
				modelRenderer[i].render(0.0625F);
			}
		}
	}
}
