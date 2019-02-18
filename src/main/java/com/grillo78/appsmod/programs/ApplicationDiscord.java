package com.grillo78.appsmod.programs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.grillo78.appsmod.handler.DiscordEventHandler;
import com.grillo78.appsmod.renderers.IconsListRenderer;
import com.grillo78.appsmod.renderers.MessageListRenderer;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.TextField;
import com.mrcrayfish.device.api.app.renderer.ListItemRenderer;
import com.mrcrayfish.device.api.utils.RenderUtil;
import com.mrcrayfish.device.core.Laptop;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationDiscord extends Application{

	public List<BufferedImage> icons = new ArrayList<>();
	public List<String> iconsId = new ArrayList<>();
	public static ItemList<Guild> serversItems;
	public static ItemList<PrivateChannel> directMessagesItems;
	public static ItemList<TextChannel> channelsItems;
	public static ItemList<Message> messages;
	public static Guild currentGuild;
	public static MessageChannel currentChannel;
	public static DiscordEventHandler discordEventHandler;
	
	@Override
	public void init(NBTTagCompound intent) {
		
		File tokens = new File("tokens.json");
		
		this.setDefaultWidth(362);
		this.setDefaultHeight(200);
		Layout login = new Layout(0, 0, 362, 164);
		Layout servers = new Layout(0, 0, 362, 164);
		Layout directMessages = new Layout(0,0,362,164);
		Layout channels = new Layout(0, 0, 362, 164);
		Layout chat = new Layout(0, 0, 362, 164);
		
		//Log-in Layout init
		TextField email = new TextField(50, 15, 100);
		email.setPlaceholder("Email");
		login.addComponent(email);
		TextField password = new TextField(50, 36, 100);
		password.setPlaceholder("Password");
		login.addComponent(password);
		Button loginBtn = new Button(100, 55, "Log-in");
		loginBtn.setClickListener((mouseX, mouseY, mouseButton)->{
			if(email.getText()!="" && password.getText()!="") {
				try {
					discordEventHandler.loginWithCredentiasl(email.getText(), password.getText(), icons, iconsId);
					this.setCurrentLayout(servers);
				} catch (LoginException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		login.addComponent(loginBtn);
		
		//Direct Messages Layout init
		Button goToServers = new Button(16, 0, Icons.CHAT);
		goToServers.setClickListener((mouseX, mouseY,mouseButton) -> {
			for(int i=0;i!=icons.size();i++) {
				icons.set(i, null);
			}
			icons.clear();
			iconsId.clear();
			for(int i=0;i!=discordEventHandler.client.getGuilds().size();i++) {
				try {
					URL url = new URL(discordEventHandler.client.getGuilds().get(i).getIconUrl());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	                BufferedImage bufferedImage = ImageIO.read(conn.getInputStream());
	                icons.add(bufferedImage);
	                iconsId.add(discordEventHandler.client.getGuilds().get(i).getIconId());
				} catch (IOException e1) {
				}
			}
			serversItems.setListItemRenderer(new IconsListRenderer<Guild>(16,icons, iconsId) {
				
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
			this.setCurrentLayout(servers);
		});
		directMessages.addComponent(goToServers);
		directMessagesItems = new ItemList<>(0, 20, servers.width, 8);
		directMessagesItems.setItemClickListener((mouseX, mouseY,mouseButton) -> {
			messages.removeAll();
			currentChannel = directMessagesItems.getSelectedItem();
			discordEventHandler.getMessages(directMessagesItems.getSelectedItem(), messages);
			this.setCurrentLayout(chat);
		});
		directMessages.addComponent(directMessagesItems);
		
		//Servers Layout init
		Button goToDM = new Button(16, 0, Icons.USER);
		goToDM.setEnabled(false);
		goToDM.setClickListener((mouseX, mouseY,mouseButton) -> {
			directMessagesItems.setItems(discordEventHandler.client.getPrivateChannels());
			this.setCurrentLayout(directMessages);
			for(int i=0;i!=icons.size();i++) {
				icons.set(i, null);
			}
			icons.clear();
			iconsId.clear();
			for(int i=0;i!=discordEventHandler.client.getPrivateChannels().size();i++) {
				try {
					URL url = new URL(discordEventHandler.client.getPrivateChannels().get(i).getUser().getAvatarUrl());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	                BufferedImage bufferedImage = ImageIO.read(conn.getInputStream());
	                icons.add(bufferedImage);
	                iconsId.add(discordEventHandler.client.getPrivateChannels().get(i).getUser().getAvatarId());
				} catch (IOException e1) {
				}
			}
			directMessagesItems.setListItemRenderer(new IconsListRenderer<PrivateChannel>(16, icons, iconsId) {
				@Override
				public void render(PrivateChannel e, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
					Gui.drawRect(x, y, x+width, y+height, Color.LIGHT_GRAY.getRGB());
					if(e.getUser().getAvatarUrl()!=null) {
						DynamicTexture texture = new DynamicTexture(iconsIMG.get(iconsIdList.indexOf(e.getUser().getAvatarId())));
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
		});
		servers.addComponent(goToDM);
		Button exitBtn = new Button(0, 0, Icons.LOGOUT);
		exitBtn.setClickListener((mouseX, mouseY,mouseButton) -> {
			this.setCurrentLayout(login);
			tokens.delete();
			discordEventHandler.client.shutdownNow();
		});
		servers.addComponent(exitBtn);
		serversItems = new ItemList<>(0, 20, servers.width, 8);
		serversItems.setLoading(true);
		serversItems.setItemClickListener((mouseX, mouseY,mouseButton) -> {
			currentGuild = serversItems.getSelectedItem();
			channelsItems.setItems(serversItems.getSelectedItem().getTextChannels());
			this.setCurrentLayout(channels);
		});
		servers.addComponent(serversItems);
		
		//Channels Layout init
		Button backBtn = new Button(0, 0, Icons.ARROW_LEFT);
		backBtn.setClickListener((mouseX, mouseY,mouseButton)->{
			if(this.getCurrentLayout()==channels) {
				for(int i=0;i!=icons.size();i++) {
					icons.set(i, null);
				}
				icons.clear();
				iconsId.clear();
				for(int i=0;i!=discordEventHandler.client.getGuilds().size();i++) {
					try {
						URL url = new URL(discordEventHandler.client.getGuilds().get(i).getIconUrl());
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		                BufferedImage bufferedImage = ImageIO.read(conn.getInputStream());
		                icons.add(bufferedImage);
		                iconsId.add(discordEventHandler.client.getGuilds().get(i).getIconId());
					} catch (IOException e1) {
					}
				}
				serversItems.setListItemRenderer(new IconsListRenderer<Guild>(16,icons, iconsId) {
					
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
				this.setCurrentLayout(servers);
			}
			if(this.getCurrentLayout()==chat) {
				if(currentChannel.getType()==ChannelType.PRIVATE) {
					directMessagesItems.setItems(discordEventHandler.client.getPrivateChannels());
					this.setCurrentLayout(directMessages);
					for(int i=0;i!=icons.size();i++) {
						icons.set(i, null);
					}
					icons.clear();
					iconsId.clear();
					for(int i=0;i!=discordEventHandler.client.getPrivateChannels().size();i++) {
						try {
							URL url = new URL(discordEventHandler.client.getPrivateChannels().get(i).getUser().getAvatarUrl());
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			                BufferedImage bufferedImage = ImageIO.read(conn.getInputStream());
			                icons.add(bufferedImage);
			                iconsId.add(discordEventHandler.client.getPrivateChannels().get(i).getUser().getAvatarId());
						} catch (IOException e1) {
						}
					}
					directMessagesItems.setListItemRenderer(new IconsListRenderer<PrivateChannel>(16, icons, iconsId) {
						@Override
						public void render(PrivateChannel e, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
							Gui.drawRect(x, y, x+width, y+height, Color.LIGHT_GRAY.getRGB());
							if(e.getUser().getAvatarUrl()!=null) {
								DynamicTexture texture = new DynamicTexture(iconsIMG.get(iconsIdList.indexOf(e.getUser().getAvatarId())));
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
				}
				else {
					this.setCurrentLayout(channels);
				}
			}
		});
		channels.addComponent(backBtn);
		channelsItems = new ItemList<>(0, 20, servers.width, 10);
		channelsItems.setItemClickListener((mouseX, mouseY,mouseButton) -> {
			currentChannel = channelsItems.getSelectedItem();
			discordEventHandler.getMessages(currentChannel, messages);
			this.setCurrentLayout(chat);
		});
		channelsItems.setListItemRenderer(new ListItemRenderer<TextChannel>(13) {
			
			@Override
			public void render(TextChannel e, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
				Gui.drawRect(x, y, x+width, y+height, Color.LIGHT_GRAY.getRGB());
				Laptop.fontRenderer.drawString(e.getName(), x, y, Color.GRAY.getRGB());
			}
		});
		channels.addComponent(channelsItems);
		
		
		
		//Chat Layout init
		messages = new ItemList<Message>(0, 21, chat.width, 2);
		messages.setListItemRenderer(new MessageListRenderer<Message>(50) {

			public void adjustString(String string, int width, int x, int y,int color) {
				List<String> formattedString = new ArrayList<>();
				FontRenderer fontRenderer = Laptop.fontRenderer;
				if(fontRenderer.getStringWidth(string) > width)
				{
					formattedString = fontRenderer.listFormattedStringToWidth(string, width-8);
					for (int i=0; i!=formattedString.size();i++) {
						fontRenderer.drawString(formattedString.get(i), x, y+i*9, color);
					}
				}
				else {
					fontRenderer.drawString(string, x, y, color);
				}
			}
			
			@Override
			public void render(Message e, Gui gui, Minecraft mc, int x, int y, int width, int height,
					boolean selected) {
				Gui.drawRect(x, y, x + width, y + height, Color.WHITE.getRGB());
				RenderUtil.drawStringClipped(e.getAuthor().getName()+"#"+e.getAuthor().getDiscriminator(), x + 5, y + 2, width - 20, Color.BLACK.getRGB(), false);
				if (e.isEdited()) {
					adjustString(e.getEditedTime().toZonedDateTime().toString(), width, x+245, y, Color.BLACK.getRGB());
				}
				else {
					adjustString(e.getCreationTime().toZonedDateTime().toString(), width, x+245, y, Color.BLACK.getRGB());
				}
				if(e.getContentDisplay()!="") {
					adjustString(e.getContentDisplay(), width-8, x+5, y+11, Color.BLACK.getRGB());
				}
				else {
					
				}
			}
		});
		chat.addComponent(messages);
		TextField messageBox = new TextField(0, chat.height-16, chat.width-16);
		chat.addComponent(messageBox);
		Button sendBtn = new Button(chat.width-16, chat.height-16, Icons.SEND);
		sendBtn.setClickListener((mouseX, mouseY, mouseButton)->{
			currentChannel.sendMessage(messageBox.getText().toString()).complete();
			messageBox.clear();
		});
		chat.addComponent(sendBtn);
		chat.addComponent(backBtn);
		discordEventHandler = new DiscordEventHandler(serversItems, channelsItems, goToDM);
		this.setCurrentLayout(login);
		if (tokens.exists()) {
			try {
				discordEventHandler.loginWithToken(icons, iconsId);
				this.setCurrentLayout(servers);
			} catch (JsonIOException | JsonSyntaxException | LoginException | FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	@Override
	public void load(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onClose() {
		if(discordEventHandler.client!=null) {
			discordEventHandler.client.shutdownNow();
		}
		super.onClose();
	}

}
