package com.grillo78.appsmod.handler;

import com.grillo78.appsmod.AppsMod;
import com.grillo78.appsmod.init.ModBlocks;
import com.grillo78.appsmod.init.ModItems;
import com.grillo78.appsmod.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {
	@SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
		AppsMod.log.info("Registring Items from Apps Mod");
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event)
    {
    	AppsMod.log.info("Registring Blocks from Apps Mod");
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(Item item : ModItems.ITEMS) {
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
		
		for(Block block: ModBlocks.BLOCKS) {
			if(block instanceof IHasModel) {
				((IHasModel)block).registerModels();
			}
		}
	}
}
