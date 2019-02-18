package com.grillo78.appsmod.renderers;

import com.mrcrayfish.device.api.app.renderer.ListItemRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class MessageListRenderer<E> extends ListItemRenderer<E>
{

	private final int height;
	
	public MessageListRenderer(int height) {
		super(height);
		this.height = height;
	}
	
	public abstract void render(E e, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected);
}
