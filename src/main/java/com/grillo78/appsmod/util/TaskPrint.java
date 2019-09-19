package com.grillo78.appsmod.util;

import java.util.UUID;

import com.grillo78.appsmod.tileentity.TileEntityThreeDPrinterDevice;
import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.core.network.NetworkDevice;
import com.mrcrayfish.device.core.network.Router;
import com.mrcrayfish.device.tileentity.TileEntityNetworkDevice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaskPrint extends Task{

	private BlockPos devicePos;
	private UUID printerId;
	private String print;
	
	public TaskPrint() {
		super("Print");
		}
	
	public TaskPrint(BlockPos devicePos, NetworkDevice printer, String print) {
		this();
		this.devicePos = devicePos;
		this.printerId = printer.getId();
		this.print = print;
	}

	@Override
	public void prepareRequest(NBTTagCompound nbt) {
		nbt.setLong("devicePos", devicePos.toLong());
		nbt.setUniqueId("printerId", printerId);
		nbt.setString("print", print);
		
	}

	@Override
	public void processRequest(NBTTagCompound nbt, World world, EntityPlayer player) {
		TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("devicePos")));
        if(tileEntity instanceof TileEntityNetworkDevice)
        {
            TileEntityNetworkDevice device = (TileEntityNetworkDevice) tileEntity;
            Router router = device.getRouter();
            if(router != null)
            {
                TileEntityNetworkDevice printer = router.getDevice(world, nbt.getUniqueId("printerId"));
                if(printer != null && printer instanceof TileEntityThreeDPrinterDevice)
                {
                	
                    ((TileEntityThreeDPrinterDevice) printer).print(nbt.getString("print"));
                    this.setSuccessful();
                }
            }
}
		
	}

	@Override
	public void prepareResponse(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processResponse(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

}
