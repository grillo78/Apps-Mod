package com.grillo78.appsmod.items;

import com.grillo78.appsmod.Reference;
import com.mrcrayfish.device.item.ItemColoredDevice;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ItemColoredDevicesCustom extends ItemColoredDevice{

	public ItemColoredDevicesCustom(Block block) {
		super(block);
	}

	@Override
	public NonNullList<ResourceLocation> getModels() {
		NonNullList<ResourceLocation> modelLocations = NonNullList.create();
        for(EnumDyeColor color : EnumDyeColor.values())
        {
            modelLocations.add(new ResourceLocation(Reference.MODID, getUnlocalizedName().substring(5) + "/" + color.getName()));
        }
        return modelLocations;
	}
}
