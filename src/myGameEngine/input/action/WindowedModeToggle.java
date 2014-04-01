package myGameEngine.input.action;

import myGameEngine.display.MyDisplaySystem;
import net.java.games.input.Event;
import sage.display.IDisplaySystem;
import sage.input.action.AbstractInputAction;

public class WindowedModeToggle extends AbstractInputAction{

	private IDisplaySystem display;
	
	public WindowedModeToggle(IDisplaySystem d) {
		display = d;
	}
	@Override
	public void performAction(float time, Event evt) {
		// Allows player to toggle between windowed mode and full screen
		if(display.isFullScreen()) {
			((MyDisplaySystem) display).setIsFullScreen(false);
		}
		else
			((MyDisplaySystem) display).setIsFullScreen(true);
	}

}
