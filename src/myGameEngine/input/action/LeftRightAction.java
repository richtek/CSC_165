/** LeftRightAction.java is a camera control class for Sage ICamera.
 *  Passing the camera instance to this class will allow for
 *  the camera to be move left and right along its right axis.
 */

package myGameEngine.input.action;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class LeftRightAction  extends AbstractInputAction{
	private ICamera camera; 
    private SetSpeedAction runAction; // Used in the event a speed modifier is needed
 
        
    public LeftRightAction(ICamera c, SetSpeedAction r) { 
    	camera = c; 
    	runAction = r; 
    } 
 
    // Sets the move amount based on whether camera is running or not.
    public void performAction(float time, Event e) { 
    	System.out.println(e.getComponent().getName());
    	float moveAmount; 
    	if (runAction.isRunning()) {
    		moveAmount = (float) 0.05 ; 
    	} 
    	else { 
    		moveAmount = (float) 0.015 ; 
    	} 
 
    	Vector3D rightDir = camera.getRightAxis().normalize(); 
		Vector3D curLocVector = new Vector3D(camera.getLocation()); 
		Vector3D newLocVec = curLocVector.add(rightDir.mult(moveAmount)); 
		
		 // Get the value from the input device and set
		 // new position for the camera to move to.
		if (e.getValue() > 0.2 || e.getComponent().getName().startsWith("D")) { 
			newLocVec = curLocVector.add(rightDir.mult(moveAmount * time)); 
		} 
		else if (e.getValue() < -0.2 || e.getComponent().getName().startsWith("A")) { 
		    newLocVec = curLocVector.minus(rightDir.mult(moveAmount * time)); 
		} 
		else { 
			newLocVec = curLocVector; 
		} 		 
		
		// Need to get the X,Y, and Z from the new vector in order 
		// to create a new point that the camera can use to set its location
		double newX = newLocVec.getX(); 
		double newY = newLocVec.getY(); 
		double newZ = newLocVec.getZ(); 
		Point3D newLoc = new Point3D(newX, newY, newZ); 
		
		// Apply new position to camera
		camera.setLocation(newLoc); 
    } 
}
