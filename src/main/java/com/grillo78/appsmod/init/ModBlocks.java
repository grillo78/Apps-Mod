package com.grillo78.appsmod.init;

import java.util.ArrayList;
import java.util.List;

import com.grillo78.appsmod.blocks.PrintedBlock;
import com.grillo78.appsmod.blocks.ThreeDPrinterDeviceBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block THREEDPRINTER = new ThreeDPrinterDeviceBlock("3DPrinter", Material.IRON);
	public static final Block PRINTEDBLOCK = new PrintedBlock(Material.GLASS, "PrintedBlock");
}
