/** PitchAction.java is a camera control class for Sage ICamera.
 *  Passing the camera instance to this class will allow for
 *  the camera to be rotated about its right axis.
 */

package myGameEngine.input.action;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class PitchAction extends AbstractInputAction {
	ICamera camera;

	public PitchAction(ICamera c) {
		camera = c;
	}
	
	@Override
	public void performAction(float time, Event evt) {
		 Matrix3D rotationAmt = new Matrix3D(); 
		 Vector3D vd = camera.getViewDirection(); 
		 Vector3D ud = camera.getUpAxis(); 
		 Vector3D rd = camera.getRightAxis(); 
		 
		// Get the value from the input device and set
		// rotational amount to be applied to the up axis 
		// and view direction axis vectors.
		 if (evt.getValue() < -0.1 || evt.getComponent().getName().startsWith("U")) 
			 rotationAmt.rotate(.35,rd); 
		 else if (evt.getValue() > 0.1 || evt.getComponent().getName().startsWith("D"))
			 rotationAmt.rotate(-.35,rd); 
		 
		 vd = vd.mult(rotationAmt); 
		 ud = ud.mult(rotationAmt); 
		 
		 // Apply above changes to the camera
		 camera.setUpAxis(ud.normalize()); 
		 camera.setViewDirection(vd.normalize()); 
	
	}

}

