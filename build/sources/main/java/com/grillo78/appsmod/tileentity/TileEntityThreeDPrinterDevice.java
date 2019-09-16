package com.grillo78.appsmod.tileentity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.grillo78.appsmod.init.ModBlocks;
import com.grillo78.appsmod.init.ModItems;
import com.mrcrayfish.device.tileentity.TileEntityNetworkDevice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class TileEntityThreeDPrinterDevice extends TileEntityNetworkDevice.Colored implements ISidedInventory{

	public enum slotEnum 
    {
        INPUT_SLOT, OUTPUT_SLOT
    }
	
	private static final int[] slotsTop = new int[] {

          slotEnum.INPUT_SLOT.ordinal()};
    private static final int[] slotsBottom = new int[] {

          slotEnum.OUTPUT_SLOT.ordinal()};
    private NonNullList<ItemStack>  printerItemStackArray = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    public boolean isPrinting = false;
    public boolean isHot = false;
    public boolean isHeating = false;
    public boolean isCooling = false;
    private String modelString;
    private float actualTemperature = 20;
    private float maxTemperature = 160;
    private int printTime;
    private int totalPrintTime = 50;
    
	@Override
	public int getSizeInventory() {
		return printerItemStackArray.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.printerItemStackArray)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return printerItemStackArray.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		
		return ItemStackHelper.getAndSplit(this.printerItemStackArray, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.printerItemStackArray, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.printerItemStackArray.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.printerItemStackArray.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
		
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 1)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = this.printerItemStackArray.get(1);
            return itemstack.getItem() == ModItems.FILAMENT;
        }
	}

	@Override
	public int getField(int id) {
		switch (id)
        {
            case 0:
                return (int) this.maxTemperature;
            case 1:
                return (int) this.actualTemperature;
            case 2:
                return (int) this.printTime;
            case 3:
                return this.totalPrintTime;
            default:
                return 0;
        }
	}

	@Override
	public void setField(int id, int value) {
		switch (id)
        {
            case 0:
                this.maxTemperature = value;
                break;
            case 1:
                this.actualTemperature = value;
                break;
            case 2:
                this.printTime = value;
                break;
            case 3:
                this.totalPrintTime = value;
        }
		
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public void clear() {
		this.printerItemStackArray.clear();
		
	}

	@Override
	public String getName() {
		return "PrusaCraft";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update() {
		if (getStackInSlot(0).getItem()==ModItems.FILAMENT) {
			if(isPrinting) {
				if(getStackInSlot(0).getItemDamage()!=1000) {
					getStackInSlot(0).setItemDamage(getStackInSlot(0).getItemDamage()+1);
				}
				else {
					getStackInSlot(0).shrink(1);
				}
				if (printTime != totalPrintTime) {
					printTime++;
				}
				else {
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("model", modelString);
					ItemStack stack = new ItemStack(ModBlocks.PRINTEDBLOCK);
					stack.setTagCompound(nbt);
					setInventorySlotContents(1, stack);
					
					modelString = null;
					printTime = 0;
					isPrinting=false;
				}
				this.markDirty();
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if(modelString != null) {
			compound.setString("model", modelString);
		}
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("model")) {
			modelString = compound.getString("model");
		}
	}
	
	public float getTotalPrintTime() {
		return totalPrintTime;
	}

	public void setTotalPrintTime(int totalPrintTime) {
		this.totalPrintTime = totalPrintTime;
	}

	@Override
	public String getDeviceName() {
		return "3D Printer";
	}

	public void print(String print) {
		try {
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new FileReader(new File(print)));
			JsonObject rootobj = root.getAsJsonObject();
			modelString = rootobj.toString();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isPrinting = true;
	}

}
