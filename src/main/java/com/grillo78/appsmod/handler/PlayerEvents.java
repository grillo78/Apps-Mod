package com.grillo78.appsmod.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class PlayerEvents {

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent e)
    {
    	EntityPlayer player = e.player;
    	TextComponentString prefix = new TextComponentString("[Apps Mod] -> Join to my Discord server: ");
        prefix.getStyle().setColor(TextFormatting.GOLD);
        TextComponentString url = new TextComponentString("https://discord.gg/2PpbtFr");
        url.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/2PpbtFr"));
        url.getStyle().setColor(TextFormatting.GOLD);
        player.sendMessage(prefix);
        player.sendMessage(url);
    }
}
