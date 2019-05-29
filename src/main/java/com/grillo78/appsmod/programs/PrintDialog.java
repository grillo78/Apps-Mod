package com.grillo78.appsmod.programs;

import com.grillo78.appsmod.tileentity.TileEntityThreeDPrinterDevice;
import com.grillo78.appsmod.util.TaskPrint;
import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.api.app.renderer.ListItemRenderer;
import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.api.task.TaskManager;
import com.mrcrayfish.device.api.utils.RenderUtil;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.core.network.NetworkDevice;
import com.mrcrayfish.device.core.network.task.TaskGetDevices;
import com.mrcrayfish.device.programs.system.object.ColorScheme;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class PrintDialog extends Dialog{
	private Layout layoutMain;
	private Label labelMessage;
	private Button buttonRefresh;
	private ItemList<NetworkDevice> itemListPrinters;
	private Button buttonPrint;
	private Button buttonCancel;
	private String modelString;
	
	public PrintDialog(String model) {
		modelString = model;
	}
	
	@Override
	public void init(NBTTagCompound intent) {
			super.init(intent);

			layoutMain = new Layout(150, 132);

			labelMessage = new Label("Select a Printer", 5, 5);
			layoutMain.addComponent(labelMessage);

			buttonRefresh = new Button(131, 2, Icons.RELOAD);
			buttonRefresh.setPadding(2);
			buttonRefresh.setToolTip("Refresh", "Retrieve an updated list of printers");
			buttonRefresh.setClickListener((mouseX, mouseY, mouseButton) ->
			{
                if(mouseButton == 0)
				{
					itemListPrinters.setSelectedIndex(-1);
					getPrinters(itemListPrinters);
				}
            });
			layoutMain.addComponent(buttonRefresh);

			itemListPrinters = new ItemList<>(5, 18, 140, 5);
			itemListPrinters.setListItemRenderer(new ListItemRenderer<NetworkDevice>(16)
			{
				@Override
				public void render(NetworkDevice networkDevice, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected)
				{
					ColorScheme colorScheme = Laptop.getSystem().getSettings().getColorScheme();
					Gui.drawRect(x, y, x + width, y + height, selected ? colorScheme.getItemHighlightColor() : colorScheme.getItemBackgroundColor());
					Icons.PRINTER.draw(mc, x + 3, y + 3);
					RenderUtil.drawStringClipped(networkDevice.getName(), x + 18, y + 4, 118, Laptop.getSystem().getSettings().getColorScheme().getTextColor(), true);
				}
			});
			itemListPrinters.setItemClickListener((blockPos, index, mouseButton) ->
			{
                if(mouseButton == 0)
				{
					buttonPrint.setEnabled(true);
				}
            });
			itemListPrinters.sortBy((o1, o2) ->
			{
				BlockPos laptopPos = Laptop.getPos();

				BlockPos pos1 = o1.getPos();
				double distance1 = laptopPos.distanceSqToCenter(pos1.getX() + 0.5, pos1.getY() + 0.5, pos1.getZ() + 0.5);

				BlockPos pos2 = o2.getPos();
				double distance2 = laptopPos.distanceSqToCenter(pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5);

				return distance2 < distance1 ? 1 : (distance1 == distance2) ? 0 : -1;
			});
			layoutMain.addComponent(itemListPrinters);

			buttonPrint = new Button(98, 108, "Print", Icons.CHECK);
			buttonPrint.setPadding(5);
			buttonPrint.setEnabled(false);
			buttonPrint.setClickListener((mouseX, mouseY, mouseButton) ->
			{
				if(mouseButton == 0)
				{
					NetworkDevice networkDevice = itemListPrinters.getSelectedItem();
					if(networkDevice != null)
					{
						TaskPrint task = new TaskPrint(Laptop.getPos(), networkDevice, modelString);
						TaskManager.sendTask(task);
						this.close();
					}
				}
			});
			layoutMain.addComponent(buttonPrint);

			buttonCancel = new Button(74, 108, Icons.CROSS);
			buttonCancel.setPadding(5);
			buttonCancel.setClickListener((mouseX, mouseY, mouseButton) ->
			{
				if(mouseButton == 0)
				{
					close();
				}
			});
			layoutMain.addComponent(buttonCancel);

			setLayout(layoutMain);

			getPrinters(itemListPrinters);
	}

	private void getPrinters(ItemList<NetworkDevice> itemList)
	{
		itemList.removeAll();
		itemList.setLoading(true);
		Task task = new TaskGetDevices(Laptop.getPos(), TileEntityThreeDPrinterDevice.class);
		task.setCallback((tagCompound, success) ->
		{
			if(success)
			{
				NBTTagList tagList = tagCompound.getTagList("network_devices", Constants.NBT.TAG_COMPOUND);
				for(int i = 0; i < tagList.tagCount(); i++)
				{
					itemList.addItem(NetworkDevice.fromTag(tagList.getCompoundTagAt(i)));
				}
				itemList.setLoading(false);
			}
		});
		TaskManager.sendTask(task);
	}
	
}
