package com.grillo78.appsmod.renderers;

import java.awt.image.BufferedImage;
import java.util.List;

import com.mrcrayfish.device.api.app.renderer.ListItemRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class IconsListRenderer<E> extends ListItemRenderer<E> {

	public final List<BufferedImage> iconsIMG;
	public final List<String> iconsIdList;
	
	public IconsListRenderer(int height, List<BufferedImage> iconsIn, List<String> iconsIdIn) {
		super(height);
		iconsIMG = iconsIn;
		iconsIdList = iconsIdIn;
	}



	@Override
	public void render(E e, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
		
		
	}
}
