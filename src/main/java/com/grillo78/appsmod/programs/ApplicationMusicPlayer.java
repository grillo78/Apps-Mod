package com.grillo78.appsmod.programs;

import java.io.File;
import java.util.Arrays;

import com.grillo78.appsmod.programs.components.MusicProgressBar;
import com.grillo78.appsmod.util.SoundPlayer;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Slider;
import com.mrcrayfish.device.api.app.component.Text;
import com.mrcrayfish.device.api.app.listener.SlideListener;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationMusicPlayer extends Application{
	
	private static File dir;
	private static ItemList<String> soundsList;
	private static SoundPlayer soundPlayer;
	private static boolean paused;
	private static MusicProgressBar progress;
	private static Button playBtn;
	private static Button stopBtn;
	
	@Override
	public void init(NBTTagCompound intent) {
		this.setDefaultHeight(240);
		this.setDefaultWidth(362);
		dir = new File("Sounds");
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (dir.listFiles().length != 0) {
			paused = true;
			playBtn = new Button(0, 0, Icons.PLAY);
			playBtn.setClickListener((mouseX, mouseY, mouseButton)->{
				if(paused){
					paused = false;
					playBtn.setIcon(Icons.PAUSE);
					try {
						soundPlayer.control.resume();
					} catch (BasicPlayerException e) {
						e.printStackTrace();
					}
				}
				else {
					paused = true;
					playBtn.setIcon(Icons.PLAY);
					try {
						soundPlayer.control.pause();
					} catch (BasicPlayerException e) {
						e.printStackTrace();
					}
				}
			});
			playBtn.setEnabled(false);
			this.addComponent(playBtn);
			this.addComponent(new Text("Volume", 0, 20, 50));
			Slider volume = new Slider(0, 28, 50);
			volume.setPercentage((float) 0.85);
			volume.setSlideListener(new SlideListener()
			{
				@Override
				public void onSlide(float percentage)
				{
					if (soundPlayer != null){
						try {
							soundPlayer.control.setGain(percentage);
						} catch (BasicPlayerException e) {
							e.printStackTrace();
						}
					}
				}
			});
			this.addComponent(volume);
			progress = new MusicProgressBar(0, 150, 200, 16);
			this.addComponent(progress);
			stopBtn = new Button(16, 0, Icons.STOP);
			stopBtn.setClickListener((mouseX, mouseY, mouseButton)->{
				try {
					soundPlayer.control.stop();
				} catch (BasicPlayerException e) {
					e.printStackTrace();
				}
				stopBtn.setEnabled(false);
				playBtn.setEnabled(false);
				playBtn.setIcon(Icons.PLAY);
				progress.setProgress(0);
			});
			stopBtn.setEnabled(false);
			this.addComponent(stopBtn);
			soundsList = new ItemList<String>(100, 0, 262, 15, false);
			for (int i = 0; i!=dir.listFiles().length; i++) {
				soundsList.addItem(Arrays.asList(dir.list()).get(i).toString());
			}
			soundsList.setItemClickListener((mouseX, mouseY,mouseButton) -> {
				if (soundPlayer != null) {
					try {
						soundPlayer.control.stop();
					} catch (BasicPlayerException e1) {
						e1.printStackTrace();
					}
				}
				soundPlayer = new SoundPlayer();
				soundPlayer.play(Arrays.asList(dir.listFiles()).get(soundsList.getSelectedIndex()).toString());
				try {
					soundPlayer.control.setGain(volume.getPercentage());
				} catch (BasicPlayerException e) {
					e.printStackTrace();
				}
				paused = false;
				playBtn.setEnabled(true);
				stopBtn.setEnabled(true);
				playBtn.setIcon(Icons.PAUSE);
				progress.setSoundPlayer(soundPlayer);
			});
			this.addComponent(soundsList);
		}
		else {
			this.addComponent(new Text("Put audio files on: \"Minecraft installation folder\"/"+dir.getPath(), 100, 1, 262));
		}
	}

	@Override
	public void load(NBTTagCompound tagCompound) {
		
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		
	}
	
	@Override
	public void onClose() {
		if(soundPlayer != null) {
			try {
				soundPlayer.control.stop();
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
		super.onClose();
	}
	
	@Override
	public void onTick() {
		if (soundPlayer != null) {
			if (!paused) {
				progress.setProgress((int) (soundPlayer.progress*100));
				if (soundPlayer.player.getStatus() == 2) {
					paused=true;
					progress.setProgress(0);
					playBtn.setIcon(Icons.PLAY);
					playBtn.setEnabled(false);
					stopBtn.setEnabled(false);
				}
			}
		}
		super.onTick();
	}
}
