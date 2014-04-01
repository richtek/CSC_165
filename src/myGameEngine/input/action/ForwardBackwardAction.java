package myGameEngine.input.action;

import net.java.games.input.Event;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class ForwardBackwardAction extends AbstractInputAction { 
	private ICamera camera; 
    private SetSpeedAction runAction; 
 
    public ForwardBackwardAction(ICamera c, SetSpeedAction r) { 
    	camera = c; 
    	runAction = r; 
    } 
 
    public void performAction(float time, Event e) { 
    	float moveAmount;
    	if (runAction.isRunning()) {
    		moveAmount = (float) 0.2 ; 
    	} 
    	else { 
    		moveAmount = (float) 0.025 ; 
    	} 
 
		Vector3D viewDir = camera.getViewDirection().normalize(); 
		Vector3D curLocVector = new Vector3D(camera.getLocation()); 
		Vector3D newLocVec = curLocVector.add(viewDir.mult(moveAmount)); 
		
		if (e.getValue() < -0.2 || e.getComponent().getName().startsWith("W")) { 
			newLocVec = curLocVector.add(viewDir.mult(moveAmount * time)); 
		} 
		else { 
			if (e.getValue() > 0.2 || e.getComponent().getName().startsWith("S")) { 
		    	 newLocVec = curLocVector.minus(viewDir.mult(moveAmount * time)); 
			} 
			else { 
				 newLocVec = curLocVector; 
			} 
		} 

		double newX = newLocVec.getX(); 
		double newY = newLocVec.getY(); 
		double newZ = newLocVec.getZ(); 
		Point3D newLoc = new Point3D(newX, newY, newZ); 
		camera.setLocation(newLoc); 
    } 
} 
