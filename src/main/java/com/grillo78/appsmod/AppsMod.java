package com.grillo78.appsmod;

import com.grillo78.appsmod.handler.PlayerEvents;
import com.grillo78.appsmod.programs.ApplicationDiscord;
import com.grillo78.appsmod.programs.ApplicationMusicPlayer;
import com.grillo78.appsmod.programs.ApplicationWeatherForecast;
import com.grillo78.appsmod.programs.ApplicationWebBrowser;
import com.mrcrayfish.device.api.ApplicationManager;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:cdm@[0.4.0,);required-after:mcef@[0.9.0,)")
public class AppsMod
{

    public static AppsMod instance;

	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new PlayerEvents());
        registerApplications();
    }
    
    private void registerApplications()
	{
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "browser"), ApplicationWebBrowser.class);
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "minetunes"), ApplicationMusicPlayer.class);
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "weather"), ApplicationWeatherForecast.class);
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "discord"), ApplicationDiscord.class);
	}
}
