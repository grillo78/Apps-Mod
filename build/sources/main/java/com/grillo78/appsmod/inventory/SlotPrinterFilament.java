package com.grillo78.appsmod.inventory;

import com.grillo78.appsmod.init.ModItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPrinterFilament extends Slot{

	public SlotPrinterFilament(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() ==ModItems.FILAMENT;
	}
}
