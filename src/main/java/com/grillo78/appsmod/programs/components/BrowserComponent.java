package com.grillo78.appsmod.programs.components;

import org.lwjgl.input.Keyboard;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.core.Laptop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.montoyo.mcef.api.IBrowser;

public class BrowserComponent extends Component{

	public static IBrowser browser;
	public static int width;
	public static int height;
	
	public BrowserComponent(int left, int top, IBrowser browserIn, int width, int height) {
		super(left, top);
		BrowserComponent.browser = browserIn;
		BrowserComponent.width = width;
		BrowserComponent.height = height;
		browser.resize(width*4, height*4);
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	protected void handleKeyTyped(char character, int code) {
		if (enabled) {
			switch (code)
			{
				case Keyboard.KEY_BACK:
					browser.injectKeyTyped(Keyboard.getEventCharacter(), 0);
					break;
				case Keyboard.KEY_TAB:
					browser.injectKeyTyped(Keyboard.getEventCharacter(), 0);
					break;
				case Keyboard.KEY_LCONTROL:
					browser.injectKeyTyped(Keyboard.getEventCharacter(), 0);
					break;
				case Keyboard.KEY_RCONTROL:
					browser.injectKeyTyped(Keyboard.getEventCharacter(), 0);
					break;
				default:
					browser.injectKeyTyped(character, 0);
					super.handleKeyTyped(character, code);
			}
		}
	}
	
	@Override
	protected void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		super.handleMouseClick(mouseX, mouseY, mouseButton);
		if (((mouseX >= xPosition) && (mouseY>=yPosition))&&((mouseX <= xPosition+BrowserComponent.width)&&(mouseY <= yPosition+BrowserComponent.height))) {
			browser.injectMouseButton(4*(mouseX-xPosition), 4*(mouseY-yPosition), 0 ,mouseButton+1, true, 1);
			this.setEnabled(true);
		}
	}
	
	@Override
	protected void handleMouseRelease(int mouseX, int mouseY, int mouseButton) {
		super.handleMouseRelease(mouseX, mouseY, mouseButton);
		if (((mouseX >= xPosition) && (mouseY>=yPosition))&&((mouseX <= xPosition+BrowserComponent.width)&&(mouseY <= yPosition+BrowserComponent.height))) {
			browser.injectMouseButton(4*(mouseX-xPosition), 4*(mouseY-yPosition), 0 ,mouseButton+1, false, 1);
			this.setEnabled(true);
		}
	}
	
	@Override
	protected void handleMouseScroll(int mouseX, int mouseY, boolean direction) {
		if (direction) {
			browser.injectMouseWheel(4*(mouseX-xPosition), 4*(mouseY-yPosition), 0, 10, 1);
		}
		else {
			browser.injectMouseWheel(mouseX-xPosition, mouseY-yPosition, 0, -10, 1);
		}
	}
	@Override
	protected void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive,
			float partialTicks) {
		GlStateManager.disableDepth();
	    GlStateManager.enableTexture2D();
	    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        browser.draw(xPosition, yPosition + BrowserComponent.height, xPosition + BrowserComponent.width, yPosition);
        GlStateManager.enableDepth();
		super.render(laptop, mc, x, y, mouseX, mouseY, windowActive, partialTicks);
	}
	
}
