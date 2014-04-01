package myGameEngine.input.action;

import games.treasurehunt2014.BatShip;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.SceneNode;

public class SetSpeedAction extends AbstractInputAction {
	private boolean running = false; 
	private BatShip avatar;

	public SetSpeedAction(SceneNode batShip) {
		avatar = (BatShip)batShip;
	}
	
	public boolean isRunning() { return running; } 
	 
	
	public void performAction(float arg0, Event arg1) {
		 
			 System.out.println("changed"); 
			 running = !running;
			 avatar.setRunning(running);
	}
}
