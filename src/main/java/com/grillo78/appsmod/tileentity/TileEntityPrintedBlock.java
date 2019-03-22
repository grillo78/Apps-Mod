package com.grillo78.appsmod.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPrintedBlock extends TileEntity{
	
	public String model;
	
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
	
	public String getModel() {
		return model;
	}
}
