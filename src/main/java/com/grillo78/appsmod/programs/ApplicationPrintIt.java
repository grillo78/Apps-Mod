package com.grillo78.appsmod.programs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.grillo78.appsmod.AppsMod;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Text;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationPrintIt extends Application{
	
	private static File selectedFile;
	private Button printBtn;
//	private Laptop laptop;
	private static File actualFolder;
	
	@Override
	public void init(NBTTagCompound nbt) {
		this.setDefaultWidth(362);
		this.setDefaultHeight(240);
		Layout main = new Layout(0, 0, 362, 164);
		Layout load = new Layout(0, 0, 362, 164);
		selectedFile = null;
		
		//Main init
		printBtn = new Button(362-34, 164-16, "Print");
		printBtn.setEnabled(false);
		main.addComponent(printBtn);
		Button loadBtn = new Button(0, 0, Icons.IMPORT);
		loadBtn.setClickListener((mouseX, mouseY, mouseButton) ->
		{
			this.setCurrentLayout(load);
		});
		main.addComponent(loadBtn);
		Text fileName = new Text("Select a model", 0, 16, 200);
		main.addComponent(fileName);
		
		//Load init
		Button backBtn = new Button(0, 0, Icons.ARROW_LEFT);
		load.addComponent(backBtn);
		ItemList<String> fileList = new ItemList<String>(0, 16, 362, 10);
		fileList.setItemClickListener((mouseX, mouseY, mouseButton) ->{
			if(!fileList.getSelectedItem().endsWith(".json")) {
				actualFolder = new File(actualFolder.getAbsolutePath().substring(0, actualFolder.getAbsolutePath().length()-1)+fileList.getSelectedItem()+"\\.");
				updateFileList(fileList);
			}else {
				selectedFile = new File(actualFolder.getAbsolutePath().substring(0, actualFolder.getAbsolutePath().length()-1)+fileList.getSelectedItem());
				fileName.setText(fileList.getSelectedItem());
				this.setCurrentLayout(main);
			}
		});
		actualFolder = Minecraft.getMinecraft().mcDataDir;
		updateFileList(fileList);
		load.addComponent(fileList);
		Button upBtn = new Button(16, 0, Icons.ARROW_UP);
		upBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
			String filePath = actualFolder.getAbsolutePath().substring(0, actualFolder.getAbsolutePath().lastIndexOf("\\"));
			filePath = filePath.substring(0, filePath.lastIndexOf("\\"))+"\\.";
			actualFolder = new File(filePath);
			updateFileList(fileList);
		});
		load.addComponent(upBtn);
//		if (Minecraft.getMinecraft().currentScreen instanceof Laptop) {
//			AppsMod.log.info("funciona");
//			laptop = (Laptop) Minecraft.getMinecraft().currentScreen;
//		}
		
		this.setCurrentLayout(main);
	}

	@Override
	public void onTick() {
		if(selectedFile==null) {
			printBtn.setEnabled(false);
		}else {
			printBtn.setEnabled(true);
		}
		super.onTick();
	}
	
	@Override
	public void load(NBTTagCompound tagCompound) {
		
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		
	}
	
	public static void updateFileList(ItemList<String> fileList) {
		fileList.removeAll();
		String[] fileListStr = actualFolder.list();
		for(int i = 0; i < fileListStr.length; i++) {
			if (actualFolder.listFiles()[i].isDirectory()) {
				fileList.addItem(fileListStr[i]);
			}
		}
		for(int i = 0; i < fileListStr.length; i++) {
			if (fileListStr[i].endsWith(".json")) {
				JsonParser jp = new JsonParser();
				try {
					if (jp.parse(new FileReader(new File(actualFolder.getAbsolutePath().substring(0, actualFolder.getAbsolutePath().length()-1)+fileListStr[i]))).isJsonObject()) {
						if(jp.parse(new FileReader(new File(actualFolder.getAbsolutePath().substring(0, actualFolder.getAbsolutePath().length()-1)+fileListStr[i]))).getAsJsonObject().has("elements")) {
							fileList.addItem(fileListStr[i]);
						}
					}
				} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		AppsMod.log.info(actualFolder.getAbsolutePath());
	}

}
