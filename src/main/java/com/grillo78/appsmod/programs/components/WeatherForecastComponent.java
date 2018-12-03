package com.grillo78.appsmod.programs.components;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ComboBox;
import com.mrcrayfish.device.api.app.component.Image;
import com.mrcrayfish.device.api.app.component.Text;
import com.mrcrayfish.device.api.app.component.TextField;

public class WeatherForecastComponent extends Component{

	private Text temp;
	private int width;
	private TextField searchCity;
	private Image weatherIMG;
	
	public WeatherForecastComponent(int left, int top, int width) {
		super(left, top);
		this.width = width;
	}
	
	@Override
	protected void init(Layout layout) {
		searchCity = new TextField(left, top, width-51);
		layout.addComponent(searchCity);
		ComboBox.List<String> units = new ComboBox.List<String>( width-51, 0, 41, new String[] {"Celsius", "Farenheit", "Kelvin"});
		layout.addComponent(units);
		Button searchBtn = new Button(width-10, top, Icons.SEARCH);
		searchBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
			getInfo(searchCity.getText(), units.getSelectedItem());
			
		});
		layout.addComponent(searchBtn);
		weatherIMG = new Image(left, top+17, 50, 50, Icons.HELP);
		layout.addComponent(weatherIMG);
		temp = new Text("", left, top+84, width);
		layout.addComponent(temp);
		super.init(layout);
	}

	public void getInfo(String city, String units) {
		String sURL;
		switch (units) {
		case "Celsius":
			sURL = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&apikey=dc2462e85d80b1c4897c69880f0e8541&units=metric";
			break;

		case "Farenheit":
			sURL = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&apikey=dc2462e85d80b1c4897c69880f0e8541&units=imperial";
			break;
		case "Kelvin":
			sURL = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&apikey=dc2462e85d80b1c4897c69880f0e8541";
			break;
		default:
			sURL = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&apikey=dc2462e85d80b1c4897c69880f0e8541";
			break;
		}
	    try {
	      URL url = new URL(sURL);
	      URLConnection connection = (HttpURLConnection) url.openConnection();
	      JsonParser jp = new JsonParser();
	      JsonElement root = jp.parse(new InputStreamReader((InputStream) connection.getContent())); //Convert the input stream to a json element
	      JsonObject rootobj = root.getAsJsonObject();
	      JsonObject main = rootobj.get("main").getAsJsonObject();
	      JsonArray weatherKey = rootobj.get("weather").getAsJsonArray();
	      JsonObject weather = weatherKey.get(0).getAsJsonObject();
	      this.weatherIMG.setImage("https://openweathermap.org/themes/openweathermap/assets/vendor/owm/img/widgets/"+weather.get("icon").getAsString()+".png");
	      System.out.println(weather.get("main").getAsString());
	      switch (units) {
			case "Celsius":
			    this.temp.setText("Temperature:"+main.get("temp").getAsString()+"º");
				break;

			case "Farenheit":
			    this.temp.setText("Temperature:"+main.get("temp").getAsString()+"F");
				break;
			case "Kelvin":
			    this.temp.setText("Temperature:"+main.get("temp").getAsString()+"K");
				break;
			default:
			    this.temp.setText("Temperature:"+main.get("temp").getAsString()+"K");
				break;
			}
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }
	}
	
}
