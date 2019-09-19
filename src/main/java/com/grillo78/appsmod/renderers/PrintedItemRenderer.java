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
	private JsonParser jp = new JsonParser();
	private JsonArray elements;
	private ModelRenderer[] modelRenderer;
	private JsonObject actualElement;
	private int width;
	private int height;
	private int depth;
	private static ResourceLocation texture = new ResourceLocation("minecraft", "textures/blocks/concrete_white.png");
	
	public PrintedItemRenderer() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void renderByItem(ItemStack stack) {
		GlStateManager.pushMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		renderModel(stack);
		Minecraft.getMinecraft().renderEngine.deleteTexture(texture);
		GlStateManager.popMatrix();
		super.renderByItem(stack);
	}
	
	public void renderModel(ItemStack stack) {
		if(stack.hasTagCompound()) {
			if(stack.getTagCompound().hasKey("model")) {
				elements = jp.parse(stack.getTagCompound().getString("model")).getAsJsonObject().get("elements").getAsJsonArray();
				modelRenderer = new ModelRenderer[elements.size()];
				for(int i = 0; i < modelRenderer.length; i++) {
					modelRenderer[i] = new ModelRenderer(model);
					actualElement = elements.get(i).getAsJsonObject();
					width = (int) ((actualElement.get("to").getAsJsonArray().get(0).getAsFloat())-(actualElement.get("from").getAsJsonArray().get(0).getAsFloat()));
					height = (int) ((actualElement.get("to").getAsJsonArray().get(1).getAsFloat())-(actualElement.get("from").getAsJsonArray().get(1).getAsFloat()));
					depth = (int) ((actualElement.get("to").getAsJsonArray().get(2).getAsFloat())-(actualElement.get("from").getAsJsonArray().get(2).getAsFloat()));
					modelRenderer[i].addBox(actualElement.get("from").getAsJsonArray().get(0).getAsFloat(), actualElement.get("from").getAsJsonArray().get(1).getAsFloat(), actualElement.get("from").getAsJsonArray().get(2).getAsFloat(), width, height, depth);
				}
				for(int i=0; i < modelRenderer.length; i++) {
					modelRenderer[i].render(0.0625F);
				}
			}
		}
	}
}
