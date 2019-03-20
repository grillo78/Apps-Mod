package com.grillo78.appsmod.programs;

import java.io.File;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;

import net.minecraft.nbt.NBTTagCompound;

public class ApplicationPrintIt extends Application{
	
	private static File selectedFile;
	private Button printBtn;
	@Override
	public void init(NBTTagCompound nbt) {
		this.setDefaultWidth(362);
		this.setDefaultHeight(240);
		Layout main = new Layout(0, 0, 362, 164);
		//Main init
		printBtn = new Button(362-30, 164-16, "Print");
		printBtn.setEnabled(false);
		main.addComponent(printBtn);
		this.setCurrentLayout(main);
	}

	@Override
	public void onTick() {
		if(selectedFile==null) {
			printBtn.setEnabled(false);
		}
		super.onTick();
	}
	
	@Override
	public void load(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub
		
	}

}
