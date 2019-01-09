package com.grillo78.appsmod.util;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class SoundPlayer implements BasicPlayerListener{
	  private PrintStream out = null;
	  public BasicController control;
	  public float progress;
	  public long duration;
	  @SuppressWarnings("rawtypes")
	  public Map audioInfo;
	  public BasicPlayer player;
	  
	  public SoundPlayer()
	     {
	      out = System.out;
	     }

	  public void play(String filename)
	     {
	       // Instantiate BasicPlayer.
	      player = new BasicPlayer();
	      // BasicPlayer is a BasicController.
	      control = (BasicController) player;
	      // Register BasicPlayerTest to BasicPlayerListener events.
	      // It means that this object will be notified on BasicPlayer
	      // events such as : opened(...), progress(...), stateUpdated(...)
	      player.addBasicPlayerListener(this);

	  try
	     { 
	      // Open file, or URL or Stream (shoutcast, icecast) to play.
		  
	      control.open(new File(filename));

	      // control.open(new URL("http://yourshoutcastserver.com:8000"));

	      // Start playback in a thread.
	      control.play();

	      // If you want to pause/resume/pause the played file then
	      // write a Swing player and just call control.pause(),
	      // control.resume() or control.stop(). 
	      // Use control.seek(bytesToSkip) to seek file
	      // (i.e. fast forward and rewind). seek feature will
	      // work only if underlying JavaSound SPI implements
	      // skip(...). True for MP3SPI and SUN SPI's
	      // (WAVE, AU, AIFF).

	      // Set Volume (0 to 1.0).
	      control.setGain(0.85);
	      // Set Pan (-1.0 to 1.0).
	      control.setPan(0.0);
	    }
	    catch (BasicPlayerException e)
	    {
	      e.printStackTrace();
	    }
	  }

	  /**
	   * Open callback, stream is ready to play.
	   *
	   * properties map includes audio format dependant features such as
	   * bitrate, duration, frequency, channels, number of frames, vbr flag, ... 
	   *
	   * @param stream could be File, URL or InputStream
	   * @param properties audio stream properties.
	   */
	  @SuppressWarnings("rawtypes")
	public void opened(Object stream, Map properties)
	  {
	    // Pay attention to properties. It's useful to get duration, 
	    // bitrate, channels, even tag such as ID3v2.
	    display("opened : "+properties.toString()); 
	    duration = (long) properties.get("duration");
	    audioInfo = properties;
	  }

	  /**
	   * Progress callback while playing.
	   * 
	   * This method is called severals time per seconds while playing.
	   * properties map includes audio format features such as
	   * instant bitrate, microseconds position, current frame number, ... 
	   * 
	   * @param bytesread from encoded stream.
	   * @param microseconds elapsed (<b>reseted after a seek !</b>).
	   * @param pcmdata PCM samples.
	   * @param properties audio stream parameters.
	  */
	  @SuppressWarnings("rawtypes")
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
	  {
	    // Pay attention to properties. It depends on underlying JavaSound SPI
	    // MP3SPI provides mp3.equalizer.
		if (audioInfo.get("audio.type")=="MP3") {
		  long x = (long) properties.get("mp3.position.microseconds");
		  progress = (float)(((double)x/(double)duration));	
		}
//		System.out.println(properties.toString());
	  }

	  /**
	   * Notification callback for basicplayer events such as opened, eom ...
	   * 
	   * @param event
	   */
	  public void stateUpdated(BasicPlayerEvent event)
	  {
	    // Notification of BasicPlayer states (opened, playing, end of media, ...)
	    display("stateUpdated : "+event.toString());
	  }
	  /**
	   * A handle to the BasicPlayer, plugins may control the player through
	   * the controller (play, stop, ...)
	   * @param controller : a handle to the player
	   */ 
	  public void setController(BasicController controller)
	  {
	    display("setController : "+controller);
	  }

	  public void display(String msg)
	  {
	    if (out != null) out.println(msg);
	  }
	  
	  public void processSeek(double rate)
	    {
	        try
	        {
	            if ((audioInfo != null) && (audioInfo.containsKey("audio.type")))
	            {
	                String type = (String) audioInfo.get("audio.type");
	                // Seek support for MP3.
	                if ((type=="MP3") && (audioInfo.containsKey("audio.length.bytes")))
	                {
	                    long skipBytes = (long) Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue() * rate);
	                    out.println("Seek value (MP3) : " + skipBytes);
	                    control.seek(skipBytes);
	                }
	                // Seek support for WAV.
	                else if ((type=="WAVE") && (audioInfo.containsKey("audio.length.bytes")))
	                {
	                    long skipBytes = (long) Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue() * rate);
	                    out.println("Seek value (WAVE) : " + skipBytes);
	                    player.seek(skipBytes);
	                }
	            }
	        }
	        catch (BasicPlayerException ioe)
	        {
	            out.println("Cannot skip: "+ ioe);
	        }
	    }
}
