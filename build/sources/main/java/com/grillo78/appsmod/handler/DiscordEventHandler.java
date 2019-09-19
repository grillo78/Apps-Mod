package com.grillo78.appsmod.handler;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.grillo78.appsmod.renderers.IconsListRenderer;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.utils.RenderUtil;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class DiscordEventHandler extends ListenerAdapter implements EventListener{
	
	public JDA client;
	public static ItemList<Guild> servers;
	public static ItemList<TextChannel> channels;
	public List<BufferedImage> icons;
	public List<String> iconsId;
	public static MessageChannel  currentChannel;
	public static ItemList<Message> messages;
	public File tokens = new File("tokens.json");
	public static Button goToDM;
	
	public DiscordEventHandler(ItemList<Guild> serversIn, ItemList<TextChannel> channelsIn, Button goToDMIn) {
		servers = serversIn;
		channels = channelsIn;
		goToDM = goToDMIn;
		
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		for(int i=0;i!=icons.size();i++) {
			icons.set(i, null);
		}
		icons.clear();
		iconsId.clear();
		for(int i=0;i!=client.getGuilds().size();i++) {
			try {
				URL url = new URL(client.getGuilds().get(i).getIconUrl());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedImage bufferedImage = ImageIO.read(conn.getInputStream());
                icons.add(bufferedImage);
                iconsId.add(client.getGuilds().get(i).getIconId());
			} catch (IOException e1) {
			}
		}
		servers.setListItemRenderer(new IconsListRenderer<Guild>(16,icons,iconsId) {
			
			@Override
			public void render(Guild e, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
				Gui.drawRect(x, y, x+width, y+height, Color.LIGHT_GRAY.getRGB());
				if(e.getIconUrl()!=null) {
					DynamicTexture texture = new DynamicTexture(iconsIMG.get(iconsIdList.indexOf(e.getIconId())));
					GlStateManager.enableAlpha();
					GlStateManager.enableBlend();
					GlStateManager.bindTexture(texture.getGlTextureId());
	                RenderUtil.drawRectWithFullTexture(x, y, x+16, y+16, 16, 16);
	                GlStateManager.deleteTexture(texture.getGlTextureId());
	                texture = null;
				}
				else {
					RenderUtil.drawStringClipped(e.getName(), x, y, width, Color.BLACK.getRGB(), false);
				}
			}
		});
		servers.setItems(client.getGuilds());
		servers.setLoading(false);
		goToDM.setEnabled(true);
		super.onReady(event);
	}
	
	public void getMessages(MessageChannel  currentChannelIn,ItemList<Message> messagesIn) {
		messages = messagesIn;
		currentChannel = currentChannelIn;
		List<Message> messagesList = new ArrayList<>();
		if(currentChannelIn.getType()==ChannelType.TEXT) {
			TextChannel textChannel = (TextChannel) currentChannelIn;
			if(textChannel.getGuild().getSelfMember().hasPermission(textChannel, Permission.MESSAGE_HISTORY)) {
				 int i = 100;
				    for (Message message : textChannel.getIterableHistory().cache(false))
				    {
				        messagesList.add(message);
				        if (--i <= 0) break;
				    }
				messages.setItems(messagesList);
			}
			else {
				messages.removeAll();
			}
		}
		if(currentChannelIn.getType()==ChannelType.PRIVATE) {
			PrivateChannel privateChannel = (PrivateChannel) currentChannelIn;
			 int i = 100;
			    for (Message message : privateChannel.getIterableHistory().cache(false))
			    {
			        messagesList.add(message);
			        if (--i <= 0) break;
			    }
			    messages.setItems(messagesList);
		}
	}
	
	public String getToken(String email, String password) throws IOException {
		String token;
		JSONObject req = new JSONObject();
		req.put("email", email);
		req.put("password", password);
		Connection con = Jsoup.connect("https://discordapp.com/api/auth/login").data("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0").header("Content-Type", "application/json").method(Method.POST);
		con.ignoreContentType(true);
		con.requestBody(req.toString())
		.execute();
		JsonObject res = new JsonParser().parse(con.response().body()).getAsJsonObject();
		try (Writer writer = new FileWriter(new File("tokens.json"))) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(res, writer);
		}
		String raw_token = res.get("token").toString();
		token = raw_token.substring(1, raw_token.length()-1);
		return token;
	}
	
	public void loginWithCredentiasl(String email, String password, List<BufferedImage> iconsIn, List<String> iconsIdIn) throws LoginException, IOException {
		client = new JDABuilder(AccountType.CLIENT).setToken(getToken(email, password)).build();
		client.addEventListener(this);
		icons = iconsIn;
		iconsId = iconsIdIn;
	}
	
	public void loginWithToken(List<BufferedImage> iconsIn, List<String> iconsIdIn) throws LoginException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonParser jp = new JsonParser();
		JsonObject token = jp.parse(new FileReader(tokens)).getAsJsonObject();
		client = new JDABuilder(AccountType.CLIENT).setToken(token.get("token").getAsString()).build();
		client.addEventListener(this);
		icons = iconsIn;
		iconsId = iconsIdIn;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getChannel().equals(currentChannel)) {
			getMessages(currentChannel, messages);
		}
		super.onMessageReceived(event);
	}

}
