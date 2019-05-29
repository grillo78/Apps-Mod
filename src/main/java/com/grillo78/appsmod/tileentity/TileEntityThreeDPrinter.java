package com.grillo78.appsmod.tileentity;

import java.util.UUID;

import com.grillo78.appsmod.Reference;
import com.grillo78.appsmod.init.ModBlocks;
import com.grillo78.appsmod.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityThreeDPrinter extends TileEntityLockable implements ITickable, ISidedInventory{

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
    private int actualTemperature = 100;
    private int maxTemperature = 200;
    private int printTime;
    private int totalPrintTime;
    private UUID uuid;
    
    public UUID getUuid() {
		return uuid;
	}
    
    public void setUuid(UUID uuid) {
		this.uuid = uuid;
		World world = this.getWorld();
		if(world != null) {
			BlockPos pos = this.getPos();
			 world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			 world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
			 this.markDirty();
		}
	}
    
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
                return this.maxTemperature;
            case 1:
                return this.actualTemperature;
            case 2:
                return this.printTime;
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
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
//		return new ContainerThreeDPrinter(playerInventory, this);
		return null;
	}

	@Override
	public String getGuiID() {
		return Reference.MODID+"threeDPrinter";
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
            isPrinting = true;
			if(isPrinting) {
				if(getStackInSlot(0).getItemDamage()!=1000) {
					getStackInSlot(0).setItemDamage(getStackInSlot(0).getItemDamage()+1);
				}
				else {
					getStackInSlot(0).shrink(1);
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("model", "{\r\n" + 
							"	\"credit\": \"Made with Blockbench\",\r\n" + 
							"	\"textures\": {\r\n" + 
							"		\"0\": \"blocks/anvil_base\",\r\n" + 
							"		\"1\": \"blocks/iron_block\",\r\n" + 
							"		\"2\": \"blocks/glass\",\r\n" + 
							"		\"particle\": \"blocks/anvil_base\"\r\n" + 
							"	},\r\n" + 
							"	\"elements\": [\r\n" + 
							"		{\r\n" + 
							"			\"from\": [0, 0, 0],\r\n" + 
							"			\"to\": [1, 1, 16],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#0\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#0\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [15, 0, 0],\r\n" + 
							"			\"to\": [16, 1, 16],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#0\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#0\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [0, 1, 0],\r\n" + 
							"			\"to\": [16, 2, 16],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#2\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#2\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#2\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#2\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 16, 16], \"texture\": \"#2\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 16, 16], \"texture\": \"#2\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [-1, 16, -2],\r\n" + 
							"			\"to\": [0, 17, 15],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [16, 16, -2],\r\n" + 
							"			\"to\": [17, 17, 15],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [0, 16, -2],\r\n" + 
							"			\"to\": [16, 18, 1],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 16, 2], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 3, 2], \"texture\": \"#1\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 16, 2], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 3, 2], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 16, 3], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 16, 3], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [7, 15, 1],\r\n" + 
							"			\"to\": [9, 32, 3],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 2, 16], \"texture\": \"#0\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 2, 16], \"texture\": \"#0\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 2, 16], \"texture\": \"#0\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 2, 16], \"texture\": \"#0\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 2, 2], \"texture\": \"#0\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 2, 2], \"texture\": \"#0\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [7.75, 14.5, 1.75],\r\n" + 
							"			\"to\": [8.25, 15, 2.25],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 0.5, 0.5], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 0.5, 0.5], \"texture\": \"#1\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 0.5, 0.5], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 0.5, 0.5], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 0.5, 0.5], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 0.5, 0.5], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [-1, 0, -2],\r\n" + 
							"			\"to\": [0, 16, 1],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 3, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 3, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 3], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 3], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [16, 0, -2],\r\n" + 
							"			\"to\": [17, 16, 1],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 3, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 3, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 3], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 3], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [16, 0, 12],\r\n" + 
							"			\"to\": [17, 16, 15],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 3, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 3, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 3], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 3], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [-1, 0, 12],\r\n" + 
							"			\"to\": [0, 16, 15],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 3, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 3, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 3], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 3], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [-1, 17, -2],\r\n" + 
							"			\"to\": [0, 18, 15],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#0\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		},\r\n" + 
							"		{\r\n" + 
							"			\"from\": [16, 17, -2],\r\n" + 
							"			\"to\": [17, 18, 15],\r\n" + 
							"			\"faces\": {\r\n" + 
							"				\"north\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"east\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"south\": {\"uv\": [0, 0, 1, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"west\": {\"uv\": [0, 0, 16, 1], \"texture\": \"#1\"},\r\n" + 
							"				\"up\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"},\r\n" + 
							"				\"down\": {\"uv\": [0, 0, 1, 16], \"texture\": \"#1\"}\r\n" + 
							"			}\r\n" + 
							"		}\r\n" + 
							"	]\r\n" + 
							"}");
					ItemStack stack = new ItemStack(ModBlocks.PRINTEDBLOCK);
					stack.setTagCompound(nbt);
					setInventorySlotContents(1, stack);
					
					isPrinting=false;
				}
				this.markDirty();
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if(uuid!=null) {
			compound.setUniqueId("uuid", uuid);
		}
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("uuid")) {
			uuid = compound.getUniqueId("uuid");
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	public int getTotalPrintTime() {
		return totalPrintTime;
	}

	public void setTotalPrintTime(int totalPrintTime) {
		this.totalPrintTime = totalPrintTime;
	}

	public int getPrintTime() {
		return printTime;
	}

	public void setPrintTime(int printTime) {
		this.printTime = printTime;
	}
}
