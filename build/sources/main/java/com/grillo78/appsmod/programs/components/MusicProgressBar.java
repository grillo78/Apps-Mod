package com.grillo78.appsmod.programs.components;

import com.grillo78.appsmod.util.SoundPlayer;
import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.ProgressBar;

public class MusicProgressBar extends Component{

	public static ProgressBar progress;
	private static SoundPlayer soundPlayer;
	private static int width, height;
	
	public MusicProgressBar(int left, int top, int width, int height) {
		super(left, top);
		MusicProgressBar.width = width;
		MusicProgressBar.height = height;
	}

	@Override
	protected void init(Layout layout) {
		progress = new ProgressBar(left, top, width, height);
		layout.addComponent(progress);
		super.init(layout);
	}
	
	@Override
	protected void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		if(soundPlayer != null && enabled) {
			if (((mouseX >= xPosition) && (mouseY>=yPosition))&&((mouseX <= xPosition+MusicProgressBar.width)&&(mouseY <= yPosition+MusicProgressBar.height))) {
				progress.setProgress((int) (((float)(MusicProgressBar.width-(xPosition+width-mouseX))/(float)MusicProgressBar.width)*100));
				soundPlayer.processSeek(((double)(MusicProgressBar.width-(xPosition+width-mouseX))/(double)MusicProgressBar.width));
				System.out.println("Seeked");
			}
		}
		super.handleMouseDrag(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void handleMouseRelease(int mouseX, int mouseY, int mouseButton) {
		super.handleMouseRelease(mouseX, mouseY, mouseButton);
	}
	
	public void setProgress(int progressIn) {
		progress.setProgress(progressIn);
	}
	
	public void setSoundPlayer(SoundPlayer soundPlayerIn) {
		soundPlayer = soundPlayerIn;
	}
}
