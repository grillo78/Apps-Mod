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
		JsonArray elements = jp.parse(te.model).getAsJsonObject().get("elements").getAsJsonArray();
		ModelRenderer[] modelRenderer = new ModelRenderer[elements.size()-1];
		for(int i = 0; i < modelRenderer.length; i++) {
			modelRenderer[i] = new ModelRenderer(model);
			JsonObject actualElement = elements.get(i).getAsJsonObject();
			int width = (actualElement.get("to").getAsJsonArray().get(0).getAsInt())-(actualElement.get("from").getAsJsonArray().get(0).getAsInt());
			int height = (actualElement.get("to").getAsJsonArray().get(1).getAsInt())-(actualElement.get("from").getAsJsonArray().get(1).getAsInt());
			int depth = (actualElement.get("to").getAsJsonArray().get(2).getAsInt())-(actualElement.get("from").getAsJsonArray().get(2).getAsInt());
			modelRenderer[i].addBox(actualElement.get("from").getAsJsonArray().get(0).getAsInt(), actualElement.get("from").getAsJsonArray().get(1).getAsInt(), actualElement.get("from").getAsJsonArray().get(2).getAsInt(), width, height, depth);
		}
		for(int i=0; i < modelRenderer.length; i++) {
			modelRenderer[i].render(0.0625F);
		}
	}
}
