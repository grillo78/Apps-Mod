package com.grillo78.appsmod.tileentity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grillo78.appsmod.models.PrintedBlockModel;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPrintedBlock extends TileEntity{
	
	private String model;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("model", model);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("model")) {
			model = compound.getString("model");
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	public void setModel(String modelIn) {
		model = modelIn;
		World world = this.getWorld();
		if(world != null) {
			BlockPos pos = this.getPos();
			 world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			 world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
			 this.markDirty();
		}
	}
	
	public ModelRenderer[] getModelRenderer(PrintedBlockModel modelIn) {
		JsonParser jp = new JsonParser();
		JsonArray elements = jp.parse(model).getAsJsonObject().get("elements").getAsJsonArray();
		ModelRenderer[] modelRenderer = new ModelRenderer[elements.size()-1];
		for(int i = 0; i < modelRenderer.length; i++) {
			modelRenderer[i] = new ModelRenderer(modelIn);
			JsonObject actualElement = elements.get(i).getAsJsonObject();
			int width = (actualElement.get("to").getAsJsonArray().get(0).getAsInt())-(actualElement.get("from").getAsJsonArray().get(0).getAsInt());
			int height = (actualElement.get("to").getAsJsonArray().get(1).getAsInt())-(actualElement.get("from").getAsJsonArray().get(1).getAsInt());
			int depth = (actualElement.get("to").getAsJsonArray().get(2).getAsInt())-(actualElement.get("from").getAsJsonArray().get(2).getAsInt());
			modelRenderer[i].addBox(actualElement.get("from").getAsJsonArray().get(0).getAsInt(), actualElement.get("from").getAsJsonArray().get(1).getAsInt(), actualElement.get("from").getAsJsonArray().get(2).getAsInt(), width, height, depth);
		}
		
		return modelRenderer;
	}
	
	public String getModel() {
		return model;
	}
}
