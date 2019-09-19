package com.grillo78.appsmod.proxy;

import com.grillo78.appsmod.Reference;
import com.grillo78.appsmod.init.ModBlocks;
import com.grillo78.appsmod.init.ModItems;
import com.grillo78.appsmod.renderers.PrintedBlockRenderer;
import com.grillo78.appsmod.renderers.PrintedItemRenderer;
import com.grillo78.appsmod.tileentity.TileEntityPrintedBlock;
import com.mrcrayfish.device.item.SubItems;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Reference.MODID, value = {Side.CLIENT})
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModItems.ITEMS.forEach(item -> registerRenders(item));
	}
	
	@Override
	public void initRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrintedBlock.class, new PrintedBlockRenderer());
	}
	
	
	public static void registerRenders(Item item){
        if(item instanceof SubItems)
        {
            NonNullList<ResourceLocation> modelLocations = ((SubItems) item).getModels();
            for(int i = 0; i < modelLocations.size(); i++)
            {
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(modelLocations.get(i), "inventory"));
            }
        }
        else
        {
        	if(item == Item.getItemFromBlock(ModBlocks.PRINTEDBLOCK)) {
        		Item.getItemFromBlock(ModBlocks.PRINTEDBLOCK).setTileEntityItemStackRenderer(new PrintedItemRenderer());
        	}else {
        		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        	}
        }
	}
}
