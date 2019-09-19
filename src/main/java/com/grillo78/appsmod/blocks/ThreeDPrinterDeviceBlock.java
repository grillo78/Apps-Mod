package com.grillo78.appsmod.blocks;

import com.grillo78.appsmod.AppsMod;
import com.grillo78.appsmod.init.ModBlocks;
import com.grillo78.appsmod.init.ModItems;
import com.grillo78.appsmod.items.ItemColoredDevicesCustom;
import com.grillo78.appsmod.tileentity.TileEntityThreeDPrinterDevice;
import com.mrcrayfish.device.block.BlockDevice;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThreeDPrinterDeviceBlock extends BlockDevice.Colored{

	
	public ThreeDPrinterDeviceBlock(String name, Material materialIn) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(1);
		setCreativeTab(AppsMod.appsModTab);

		ModBlocks.BLOCKS.add(this);
		ItemColoredDevicesCustom item = new ItemColoredDevicesCustom(this);
		item.setRegistryName(this.getRegistryName());
		ModItems.ITEMS.add(item);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityThreeDPrinterDevice)
            {
            	playerIn.openGui(AppsMod.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
                AppsMod.log.debug("Opening GUI");
                return true;
            }
        }
		return false;
	}
	
	public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityThreeDPrinterDevice();
    }
}
