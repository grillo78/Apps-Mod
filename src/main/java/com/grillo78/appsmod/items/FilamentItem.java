package com.grillo78.appsmod.items;

import com.grillo78.appsmod.AppsMod;
import com.grillo78.appsmod.init.ModItems;

import net.minecraft.item.Item;

public class FilamentItem extends Item{

	public FilamentItem(String name) {
		this.setCreativeTab(AppsMod.appsModTab);
		this.setMaxStackSize(1);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setNoRepair();
		this.setMaxDamage(10);
		
		ModItems.ITEMS.add(this);
	}

}
