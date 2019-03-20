package com.grillo78.appsmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.grillo78.appsmod.handler.PlayerEvents;
import com.grillo78.appsmod.programs.ApplicationDiscord;
import com.grillo78.appsmod.programs.ApplicationMusicPlayer;
import com.grillo78.appsmod.programs.ApplicationPrintIt;
import com.grillo78.appsmod.programs.ApplicationWeatherForecast;
import com.grillo78.appsmod.programs.ApplicationWebBrowser;
import com.grillo78.appsmod.proxy.CommonProxy;
import com.grillo78.appsmod.tab.AppsModTab;
import com.grillo78.appsmod.tileentity.TileEntityThreeDPrinter;
import com.mrcrayfish.device.api.ApplicationManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:cdm@[0.4.0,);required-after:mcef@[0.9.0,)")
public class AppsMod
{

	public static final CreativeTabs appsModTab = new AppsModTab("Apps Mod");
	public static final Logger log = LogManager.getLogger(Reference.MODID);
	
	@Instance(Reference.MODID)
    public static AppsMod instance;
    
    @SidedProxy(serverSide="com.grillo78.appsmod.proxy.CommonProxy",clientSide="com.grillo78.appsmod.proxy.ClientProxy")
    public static CommonProxy PROXY;

	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		GameRegistry.registerTileEntity(TileEntityThreeDPrinter.class, new ResourceLocation(Reference.MODID,"tileEntity3DPrinter"));
		PROXY.onPreInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new PlayerEvents());
        registerApplications();
    }
    
    @EventHandler
    public void stopping(FMLServerStoppingEvent event) {
    	
    }
    
    private void registerApplications()
	{
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "browser"), ApplicationWebBrowser.class);
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "minetunes"), ApplicationMusicPlayer.class);
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "weather"), ApplicationWeatherForecast.class);
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "discord"), ApplicationDiscord.class);
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "printit"), ApplicationPrintIt.class);
	}
}
