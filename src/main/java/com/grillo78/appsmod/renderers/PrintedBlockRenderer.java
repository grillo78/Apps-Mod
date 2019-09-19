package com.grillo78.appsmod.renderers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grillo78.appsmod.models.PrintedBlockModel;
import com.grillo78.appsmod.tileentity.TileEntityPrintedBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PrintedBlockRenderer extends TileEntitySpecialRenderer<TileEntityPrintedBlock>{

	private PrintedBlockModel model = new PrintedBlockModel();
	private JsonParser jp = new JsonParser();
	private JsonArray elements;
	private ModelRenderer[] modelRenderer;
	private JsonObject actualElement;
	private int width;
	private int height;
	private int depth;
//	private IBlockState state;
//	private EnumFacing facing;
	private static ResourceLocation texture = new ResourceLocation("minecraft", "textures/blocks/concrete_white.png");
	
	public PrintedBlockRenderer() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void render(TileEntityPrintedBlock te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
//		state = te.getWorld().getBlockState(te.getPos());
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
//		if(facing == EnumFacing.SOUTH) {
		GlStateManager.translate(x, y, z);
//		}
//		if (facing == EnumFacing.EAST) {
//	        GlStateManager.translate(x + 1F, y, z - 1F);
//        }
//        if (facing == EnumFacing.NORTH) {
//            GlStateManager.translate(x + 1F, y, z + 1F);
//        }
//        if (facing == EnumFacing.WEST) {
//            GlStateManager.translate(x - 1F, y, z + 1F);
//        }
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
//		facing = state.getValue(PrintedBlock.FACING);
//		if (facing == EnumFacing.EAST) {
//            GlStateManager.rotate(90, 0, 1, 0);
//        }
//        if (facing == EnumFacing.NORTH) {
//            GlStateManager.rotate(180, 0, 1, 0);
//        }
//        if (facing == EnumFacing.WEST) {
//            GlStateManager.rotate(-90F, 0, 1, 0);
//        }
        GlStateManager.scale(1, 1, 1);
		renderModel(te);
		Minecraft.getMinecraft().renderEngine.deleteTexture(texture);
		GlStateManager.disableBlend();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
	
	public void renderModel(TileEntityPrintedBlock te) {
		if(te.model!=null) {
			elements = jp.parse(te.model).getAsJsonObject().get("elements").getAsJsonArray();
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
