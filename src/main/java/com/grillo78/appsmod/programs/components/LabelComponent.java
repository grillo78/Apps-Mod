package com.grillo78.appsmod.programs.components;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;

import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;

public class LabelComponent extends Component{

	public static Button goBack;
	public static Button goForward;
	public static Button refreshWebSite;
	public static BrowserComponent browserComp;
	public static AddressBarComponent addressBar;
	public static IBrowser browser;
	public static int width, height;
	public static int left;
	public static int top;
	
	public LabelComponent(int left, int top, int width, int height) {
		super(left, top);
		LabelComponent.left=left;
		LabelComponent.top=top;
		browser = MCEFApi.getAPI().createBrowser(MCEF.HOME_PAGE, false);
		LabelComponent.width = width;
		LabelComponent.height = height;
	}

	@Override
	protected void init(Layout layout) {
		addressBar = new AddressBarComponent(51, LabelComponent.top, width-51);
		addressBar.setPlaceholder("Enter Address");
		addressBar.setKeyListener(c ->
        {
            if(c == '\r')
            {
                browser.loadURL(addressBar.getText());
                browserComp.setEnabled(true);
                return false;
            }
            browserComp.setEnabled(false);
            return true;
        });
		layout.addComponent(addressBar);
		refreshWebSite = new Button(17, LabelComponent.top, Icons.RELOAD);
		refreshWebSite.setToolTip("Refresh", "Loads the entered address.");
		refreshWebSite.setClickListener((mouseX, mouseY, mouseButton) -> browser.loadURL(browser.getURL()));
		layout.addComponent(refreshWebSite);
		goBack = new Button(0, LabelComponent.top, Icons.ARROW_LEFT);
		goBack.setToolTip("Back", "Loads the previous website.");
		goBack.setClickListener((mouseX, mouseY, mouseButton) -> browser.goBack());
		layout.addComponent(goBack);
		goForward = new Button(34, LabelComponent.top, Icons.ARROW_RIGHT);
		goForward.setToolTip("Forward", "Loads the next website.");
		goForward.setClickListener((mouseX, mouseY, mouseButton) -> browser.goForward());
		layout.addComponent(goForward);
		browserComp = new BrowserComponent(0, LabelComponent.top+16, browser, width, LabelComponent.height-92);
		layout.addComponent(browserComp);
	}
	
	@Override
	protected void handleTick() {
		if (!browser.isPageLoading()) {
			refreshWebSite.setEnabled(true);
		}
		else {
			addressBar.setText(browser.getURL());
			refreshWebSite.setEnabled(false);
		}
	}
}
