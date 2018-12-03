package com.grillo78.appsmod.programs;

import com.grillo78.appsmod.programs.components.WeatherForecastComponent;
import com.mrcrayfish.device.api.app.Application;

import net.minecraft.nbt.NBTTagCompound;

public class ApplicationWeatherForecast extends Application{
	
	private WeatherForecastComponent weatherComp;
	
	@Override
	public void init(NBTTagCompound intent) {
		weatherComp = new WeatherForecastComponent(0, 0, 190);
		this.addComponent(weatherComp);
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
