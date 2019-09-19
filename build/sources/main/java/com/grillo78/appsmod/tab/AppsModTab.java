package com.grillo78.appsmod.tab;

import com.grillo78.appsmod.init.ModBlocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AppsModTab extends CreativeTabs{

	public AppsModTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModBlocks.THREEDPRINTER);
	}
}
