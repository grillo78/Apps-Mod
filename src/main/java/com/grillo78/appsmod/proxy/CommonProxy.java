package com.grillo78.appsmod.proxy;

import com.grillo78.appsmod.AppsMod;
import com.grillo78.appsmod.gui.ThreeDPrinterUI;
import com.grillo78.appsmod.inventory.ContainerThreeDPrinter;
import com.grillo78.appsmod.tileentity.TileEntityThreeDPrinter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy implements IGuiHandler{

	public void onPreInit() {
		NetworkRegistry.INSTANCE.registerGuiHandler(AppsMod.instance, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity != null) {
			if(ID==0) {
				if (tileentity instanceof TileEntityThreeDPrinter) {
					return new ContainerThreeDPrinter(player.inventory, (TileEntityThreeDPrinter) tileentity);
				}
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity != null) {
			if(ID==0) {
				if (tileentity instanceof TileEntityThreeDPrinter) {
					return new ThreeDPrinterUI(player.inventory, (TileEntityThreeDPrinter) tileentity);
				}
			}
		}
		return null;
	}

	public void initRenderers() {}

}
