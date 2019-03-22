package com.grillo78.appsmod.proxy;

import com.grillo78.appsmod.Reference;
import com.grillo78.appsmod.init.ModBlocks;
import com.grillo78.appsmod.renderers.PrintedBlockRenderer;
import com.grillo78.appsmod.renderers.PrintedItemRenderer;
import com.grillo78.appsmod.tileentity.TileEntityPrintedBlock;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
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
		Item.getItemFromBlock(ModBlocks.PRINTEDBLOCK).setTileEntityItemStackRenderer(new PrintedItemRenderer());
	}
	
	@Override
	public void initRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrintedBlock.class, new PrintedBlockRenderer());
	}
	
}
