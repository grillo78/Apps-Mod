package com.grillo78.appsmod.programs.components;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;

import net.montoyo.mcef.api.IBrowser;

public class BookmarksComponent extends Component{

	private static File dir;
	private static IBrowser Browser;
	private static ItemList<String> Bookmarks;
	private int width;
	private static Button removeBtn;
	private static Button addBtn;
	private static Button goBtn;
	
	public BookmarksComponent(int left, int top, int width, IBrowser browser2) {
		super(left, top);
		Browser = browser2;
		this.width = width;
	}
	
	@Override
	public void init(Layout layout) {
		Bookmarks = new ItemList<>(left, top, width, 3);
		dir = new File("Bookmarks.json");
		if (!dir.exists()) {
			try {
				FileUtils.writeStringToFile(dir, "{\"bookmarks_names\":[\"Google\",\"Youtube\"],\"bookmarks_URL\":[\"http://www.google.com\",\"http://www.youtube.com\"]}", "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		dir.setWritable(true);
		resfreshBookmarks();
	    layout.addComponent(Bookmarks);
	    removeBtn = new Button(left, top+43, Icons.CROSS);
	    removeBtn.setClickListener((mouseX, mouseY, mouseButton) ->{
	    	if(Bookmarks.getSelectedIndex() != -1) {
		    	try {
			    	JsonParser jp = new JsonParser();
					JsonElement root = jp.parse(new FileReader(dir));
					JsonObject rootobj = root.getAsJsonObject();
					if (rootobj.get("bookmarks_names").getAsJsonArray().size() != 0) {
						rootobj.get("bookmarks_names").getAsJsonArray().remove(Bookmarks.getSelectedIndex());
						rootobj.get("bookmarks_URL").getAsJsonArray().remove(Bookmarks.getSelectedIndex());
						try (Writer writer = new FileWriter(dir)) {
						    Gson gson = new GsonBuilder().create();
						    gson.toJson(root, writer);
						}
						resfreshBookmarks();
					}
				} catch (IOException ex) {
				      ex.printStackTrace();
				}
	    	}
	    });
	    layout.addComponent(removeBtn);
	    addBtn = new Button(left+16, top+43, Icons.PLUS);
	    addBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    	try {
		        JsonParser jp = new JsonParser();
				JsonElement root = jp.parse(new FileReader(dir));
				JsonObject rootobj = root.getAsJsonObject();
				Document doc = Jsoup.connect(Browser.getURL()).get();
				if(doc.title()!=null) { 
					rootobj.get("bookmarks_names").getAsJsonArray().add(doc.title());
				}
				else {
					rootobj.get("bookmarks_names").getAsJsonArray().add("Nameless");
				}
				rootobj.get("bookmarks_URL").getAsJsonArray().add(Browser.getURL());
				try (Writer writer = new FileWriter(dir)) {
				    Gson gson = new GsonBuilder().create();
				    gson.toJson(root, writer);
				}
			} catch (IOException ex) {
			      ex.printStackTrace();
			}
	    	
	    	resfreshBookmarks();
	    });
	    layout.addComponent(addBtn);
	    goBtn = new Button(left+32, top+43, Icons.ARROW_RIGHT);
	    goBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    	try {
		    	JsonParser jp = new JsonParser();
				JsonElement root = jp.parse(new FileReader(dir));
				JsonObject rootobj = root.getAsJsonObject();
				Browser.loadURL(rootobj.get("bookmarks_URL").getAsJsonArray().get(Bookmarks.getSelectedIndex()).getAsString());
			} catch (IOException ex) {
			      ex.printStackTrace();
			}
	    	setEnabled(false);
	    });
	    layout.addComponent(goBtn);
	    setEnabled(false);
		super.init(layout);
	}
	
	private void resfreshBookmarks() {
		Bookmarks.removeAll();
		try {
	    	JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new FileReader(dir));
			JsonObject rootobj = root.getAsJsonObject();
			if (rootobj.get("bookmarks_names").getAsJsonArray().size() != 0) {
				for (int i = 0; i!=rootobj.get("bookmarks_names").getAsJsonArray().size(); i++) {
					Bookmarks.addItem(rootobj.get("bookmarks_names").getAsJsonArray().get(i).getAsString());
				}
			}
		} catch (IOException ex) {
		      ex.printStackTrace();
		}
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		removeBtn.setEnabled(enabled);
		removeBtn.setVisible(enabled);
		addBtn.setEnabled(enabled);
		addBtn.setVisible(enabled);
		goBtn.setEnabled(enabled);
		goBtn.setVisible(enabled);
		Bookmarks.setEnabled(enabled);
		Bookmarks.setVisible(enabled);
	}
}
