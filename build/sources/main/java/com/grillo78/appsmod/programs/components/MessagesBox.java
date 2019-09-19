package com.grillo78.appsmod.programs.components;

import com.mrcrayfish.device.api.app.component.ItemList;

public class MessagesBox extends ItemList<String>{

	public MessagesBox(int left, int top, int width, int visibleItems) {
		super(left, top, width, visibleItems);
		
	}

	public void setScroll() {
		this.offset = this.items.size()-this.visibleItems;
		updateComponents(left, top);
	}
}
