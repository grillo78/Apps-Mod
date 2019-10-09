package com.grillo78.appsmod.programs;



import com.grillo78.appsmod.programs.components.LabelComponent;
import com.mrcrayfish.device.api.app.Application;

import net.minecraft.nbt.NBTTagCompound;

public class ApplicationWebBrowser extends Application {

	private LabelComponent labelComp;
	@Override
	public void init(NBTTagCompound nbt) {
		this.setDefaultWidth(362);
		this.setDefaultHeight(240);
		
		labelComp = new LabelComponent(0, 0, 362, 240);
		this.addComponent(labelComp);
	}

	@Override
	public void onClose() {
		super.onClose();
		labelComp.browser.close();
	}

	@Override
	public void load(NBTTagCompound tagCompound) {
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
}
