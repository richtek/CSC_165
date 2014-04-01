/** YawAction.java is a camera control class for Sage ICamera.
 *  Passing the camera instance to this class will allow for
 *  the camera to be rotated about its up axis.
 */
		

package myGameEngine.input.action;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class YawAction extends AbstractInputAction {
	private ICamera camera;

	public YawAction(ICamera c) {		
		camera = c;
	}
	
	@Override
	public void performAction(float time, Event evt) {
		 Matrix3D rotationAmt = new Matrix3D(); 
		 Vector3D vd = camera.getViewDirection();
		 Vector3D rd = camera.getRightAxis(); 
		 Vector3D wud= camera.getUpAxis();
		 
		 System.out.println(evt.getComponent().getName());
		 // Get the value from the input device and set
		 // rotational amount to be applied to the right axis 
		 // and view direction axis vectors.
		 if (evt.getValue() < -0.1 || evt.getComponent().getName().startsWith("L")) 
			 rotationAmt.rotate(.35, wud); 
		 else if (evt.getValue() > 0.1 || evt.getComponent().getName().startsWith("R"))
			 rotationAmt.rotate(-.35, wud); 
		 
		 rd = rd.mult(rotationAmt); 
		 vd = vd.mult(rotationAmt); 
		 
		 // Apply these changes to the camera
		 camera.setRightAxis(rd.normalize()); 
		 camera.setViewDirection(vd.normalize()); 		
	}

}
