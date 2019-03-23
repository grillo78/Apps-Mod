package com.grillo78.appsmod.renderers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grillo78.appsmod.models.PrintedBlockModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PrintedItemRenderer extends TileEntityItemStackRenderer{

	private static PrintedBlockModel model = new PrintedBlockModel();
	
	public PrintedItemRenderer() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void renderByItem(ItemStack stack) {
		GlStateManager.pushMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/blocks/iron_block.png"));
		renderModel(stack);
		GlStateManager.popMatrix();
		super.renderByItem(stack);
	}
	
	public static void renderModel(ItemStack stack) {
		JsonParser jp = new JsonParser();
		JsonArray elements = jp.parse(stack.getTagCompound().getString("model")).getAsJsonObject().get("elements").getAsJsonArray();
		ModelRenderer[] modelRenderer = new ModelRenderer[elements.size()];
		for(int i = 0; i < modelRenderer.length; i++) {
			modelRenderer[i] = new ModelRenderer(model);
			JsonObject actualElement = elements.get(i).getAsJsonObject();
			int width = (actualElement.get("to").getAsJsonArray().get(0).getAsInt())-(actualElement.get("from").getAsJsonArray().get(0).getAsInt());
			int height = (actualElement.get("to").getAsJsonArray().get(1).getAsInt())-(actualElement.get("from").getAsJsonArray().get(1).getAsInt());
			int depth = (actualElement.get("to").getAsJsonArray().get(2).getAsInt())-(actualElement.get("from").getAsJsonArray().get(2).getAsInt());
			modelRenderer[i].addBox(actualElement.get("from").getAsJsonArray().get(0).getAsFloat(), actualElement.get("from").getAsJsonArray().get(1).getAsFloat(), actualElement.get("from").getAsJsonArray().get(2).getAsFloat(), width, height, depth);
		}
		for(int i=0; i < modelRenderer.length; i++) {
			modelRenderer[i].render(0.0625F);
		}
	}
}
