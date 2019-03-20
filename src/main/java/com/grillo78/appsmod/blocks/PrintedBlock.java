package com.grillo78.appsmod.blocks;

import com.grillo78.appsmod.init.ModBlocks;
import com.grillo78.appsmod.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class PrintedBlock extends Block{

	public PrintedBlock(Material materialIn, String name) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(1);
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	
	
}
