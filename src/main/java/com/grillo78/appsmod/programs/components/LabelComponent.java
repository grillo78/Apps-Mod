package com.grillo78.appsmod.programs.components;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;

import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;

public class LabelComponent extends Component{

	public Button goBack;
	public Button goForward;
	public Button refreshWebSite;
	public BrowserComponent browserComp;
	public AddressBarComponent addressBar;
	public IBrowser browser;
	public int width, height;
	public int left;
	public int top;
	
	public LabelComponent(int left, int top, int width, int height) {
		super(left, top);
		this.left=left;
		this.top=top;
		browser = MCEFApi.getAPI().createBrowser(MCEF.HOME_PAGE, false);
		this.width = width;
		this.height = height;
	}

	@Override
	protected void init(Layout layout) {
		addressBar = new AddressBarComponent(51, top, width-67);
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
		browserComp = new BrowserComponent(0, top+16, browser, width, height-92);
		layout.addComponent(browserComp);
		BookmarksComponent BookmakrsComp = new BookmarksComponent(width-50, 17, 50, browser);
		layout.addComponent(BookmakrsComp);
		Button BookmarksBtn = new Button(width-16, 0, Icons.BOOKMARK_ON);
		BookmarksBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (BookmakrsComp.isEnabled()) {
				BookmakrsComp.setEnabled(false);
				BookmakrsComp.setVisible(false);
				browserComp.setEnabled(true);
			}
			else {
				BookmakrsComp.setEnabled(true);
				BookmakrsComp.setVisible(true);
				browserComp.setEnabled(false);
			}
		});
		layout.addComponent(BookmarksBtn);
		refreshWebSite = new Button(17, top, Icons.RELOAD);
		refreshWebSite.setToolTip("Refresh", "Loads the entered address.");
		refreshWebSite.setClickListener((mouseX, mouseY, mouseButton) -> browser.loadURL(browser.getURL()));
		layout.addComponent(refreshWebSite);
		goBack = new Button(0, top, Icons.ARROW_LEFT);
		goBack.setToolTip("Back", "Loads the previous website.");
		goBack.setClickListener((mouseX, mouseY, mouseButton) -> browser.goBack());
		layout.addComponent(goBack);
		goForward = new Button(34, top, Icons.ARROW_RIGHT);
		goForward.setToolTip("Forward", "Loads the next website.");
		goForward.setClickListener((mouseX, mouseY, mouseButton) -> browser.goForward());
		layout.addComponent(goForward);
		super.init(layout);
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
