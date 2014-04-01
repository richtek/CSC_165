/** This class is made to control a Sage ICamera and a Sage SceneNode
 *  as an avatar, by implementing an orbital camera that focuses on the 
 *  avatar.
 */
package myGameEngine.camera.controllers;
import myGameEngine.input.action.SetSpeedAction;
import net.java.games.input.Component.Identifier.Button;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Event;
import net.java.games.input.Component.Identifier.Axis;
import games.treasurehunt2014.Assignment2;
import games.treasurehunt2014.BatShip;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.camera.ICamera;
import sage.input.IInputManager;
import sage.input.IInputManager.INPUT_ACTION_TYPE;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.scene.SceneNode;
import sage.util.MathUtils;

public class Camera3Pcontroller {
	 private ICamera cam; //the camera being controlled 
	 private SceneNode target; //the target the camera looks at 
	 private float cameraAzimuth; //rotation of camera around target Y axis 
	 private float cameraElevation; //elevation of camera above target 
	 private float cameraDistanceFromTarget; 	
	 private Point3D targetPos; // avatar’s position in the world 
	 private SetSpeedAction setSpeed;
	 private Vector3D worldUpVec; 
	 private Assignment2 game; // needed for a couple actions to smooth gamepad movement
	 
	 public Camera3Pcontroller(ICamera cam, SceneNode target, 
			 				IInputManager inputMgr, String controllerName, Assignment2 game) { 
		 this.game = game;		
		 this.cam = cam; 	
		 this.target = target;
		 worldUpVec = new Vector3D(0,1,0); 
		 cameraDistanceFromTarget = 5.0f; 
		 cameraAzimuth = 180; // start from BEHIND and ABOVE the target 
		 cameraElevation = 20.0f; // elevation is in degrees 
		 update(0.0f); // initialize camera state 
		 setupInput(inputMgr, controllerName);
	 }

	 public void update(float time) { 
		 updateTarget(); 
		 updateCameraPosition(); 
		 cam.lookAt(targetPos, worldUpVec); // SAGE built-in function 
	 } 
	 
	 private void updateTarget() { 
		 targetPos = new Point3D(target.getWorldTranslation().getCol(3)); 
	} 
	 
	 private void updateCameraPosition() { 
		 double theta = cameraAzimuth; 
		 double phi = cameraElevation ; 
		 double r = cameraDistanceFromTarget; 
		 
		 // calculate new camera position in Cartesian coords 
		 Point3D relativePosition = MathUtils.sphericalToCartesian(theta, phi, r); 
		 Point3D desiredCameraLoc = relativePosition.add(targetPos); 
		 cam.setLocation(desiredCameraLoc); 
	 } 
	 
	 // Setup all the inputs
	 private void setupInput(IInputManager im, String cn) { 
		 IAction orbitAction = new OrbitAroundAction(); 
		 im.associateAction(cn, Axis.RX, orbitAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN); 
		 im.associateAction(cn, Key.LEFT, orbitAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(cn, Key.RIGHT, orbitAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 IAction zoomAction = new ZoomAction();
		 im.associateAction(cn, Axis.RY, zoomAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(cn, Key.UP, zoomAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(cn, Key.DOWN, zoomAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 IAction mvFrontBack = new ForwardBackwardAction();
		 im.associateAction(cn, Key.W, mvFrontBack, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(cn, Key.S, mvFrontBack, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(cn, Axis.Y, mvFrontBack, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 IAction mvLeftRight = new LeftRightAction();
		 im.associateAction(cn, Axis.X, mvLeftRight, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(cn, Key.A, mvLeftRight, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(cn, Key.D, mvLeftRight, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 IAction jump = new JumpAction();
		 im.associateAction(cn, Key.SPACE, jump, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		 im.associateAction(cn, Button._0, jump, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		 setSpeed = new SetSpeedAction(target); // Used to increase forward, backward, left and right movement
		 im.associateAction(cn, Button._4, setSpeed, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY); 
		 im.associateAction(cn, Key.X, setSpeed, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY); 
		 MoveBehindAvatar moveBehind = new MoveBehindAvatar();
		 im.associateAction(cn, Key.V, moveBehind, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		 im.associateAction(cn, Button._1, moveBehind, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	 }
	 
	 public class OrbitAroundAction extends AbstractInputAction {
		  	// Moves the camera in a 360 degree orbit about the target
			@Override
			public void performAction(float time, Event evt) { 
				 float rotAmount = 0; 
				 
				 if (evt.getComponent().toString().startsWith("Left")) {
					 rotAmount = -0.1f * time;
				 }
				 else if (evt.getComponent().toString().startsWith("Right")) {
					 rotAmount = 0.1f * time;
				 }
				 else if (evt.getValue() < -0.2 && game.isUpdating()) { 
					 rotAmount = -0.1f * time;
					 game.setUpdating(false); // Limits gamepad generated events to one per BaseGame update()
				 } 
				 else if (evt.getValue() > 0.2 && game.isUpdating()) { 
					 rotAmount = 0.1f * time; 
					 game.setUpdating(false); // Limits gamepad generated events to one per BaseGame update()
				 }
				
				 
				 cameraAzimuth += rotAmount ; 
				 cameraAzimuth = cameraAzimuth % 360 ; 
			} 
	}
	 
	 public class ZoomAction extends AbstractInputAction {
		 // Zooms camera in and out on target
		@Override
		public void performAction(float time, Event evt) {
			float zoomAmount = 0;
			
			if (evt.getComponent().toString().startsWith("Down")) {
				zoomAmount += .03f * time;
			}
			else if (evt.getComponent().toString().startsWith("Up")) {
				zoomAmount -= .03f * time;
			}
			else if (evt.getValue() < -0.3) { 
				zoomAmount -= .03f * time;
			}
			else if	(evt.getValue() > .3) {			
				zoomAmount += .03f * time;	
			}
				
			cameraDistanceFromTarget += zoomAmount;
			
			if (cameraDistanceFromTarget <= 4) {
				cameraDistanceFromTarget = 4.0f;
			}
			if (cameraDistanceFromTarget >= 80) {
				cameraDistanceFromTarget = 80.0f;
			}
	    } 
	 }
	 
	 public class ForwardBackwardAction extends AbstractInputAction { 
		 // Moves the avatar forward and backward
		 private float speed; 
		 
		 public void performAction(float time, Event e) 
		 { 		
			 if (setSpeed.isRunning()){
				 speed = .06f;
			 }
			 else
				 speed = .02f;
			 
			 Matrix3D rot = target.getLocalRotation(); 
			 Vector3D dir = new Vector3D(0,0,1); 
			 dir = dir.mult(rot); 
			 dir.scale((double)(speed * (time)));
			 
			 if (e.getComponent().toString().startsWith("S")){
				 target.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
			 }
			 else if (e.getComponent().toString().startsWith("W")){		 
				 target.translate((float)-dir.getX(),(float)-dir.getY(),(float)-dir.getZ());
			 }
			 else if (e.getValue() > .1 && game.isUpdating()){
				 target.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
				 game.setUpdating(false); // Limits gamepad generated events to one per BaseGame update()
			 }
			 else if (e.getValue() < -.1 && game.isUpdating()){
				 target.translate((float)-dir.getX(),(float)-dir.getY(),(float)-dir.getZ());
				 game.setUpdating(false); // Limits gamepad generated events to one per BaseGame update()
			 }
		 }
	 }
	 
	 public class LeftRightAction extends AbstractInputAction {
		 // Rotates target left and right
		@Override
		public void performAction(float time, Event evt) {
			  
			 float rotAmount = .4f;
			 Vector3D dir = new Vector3D(0,1,0); 
			 
			 if (evt.getComponent().toString().startsWith("D")) {
				 target.rotate(-(rotAmount * time)/10,  dir);
				 ((BatShip) target).setDegrees(-(rotAmount * time)/10);
			 }
			 else if (evt.getComponent().toString().startsWith("A")) {
				 target.rotate((rotAmount * time)/10,  dir);
				 ((BatShip) target).setDegrees((rotAmount * time)/10);
			 }
			 else if (evt.getValue() > .5) {
				 target.rotate((-rotAmount * time)/10,  dir);
				 ((BatShip) target).setDegrees(-(rotAmount * time)/10);
			 }
			 else if (evt.getValue() < - .5) {
				 target.rotate((rotAmount * time)/10,  dir);
				 ((BatShip) target).setDegrees((rotAmount * time)/10);
			 }			
		}		 
	 } 
	 
	 public class JumpAction extends AbstractInputAction {
		 // Makes target jump
		@Override
		public void performAction(float time, Event evt) {
			((BatShip) target).setIsJumping(true);
			((BatShip) target).jump();			
		}
		 
	 }
	 
	 public class MoveBehindAvatar extends AbstractInputAction {
		 // Moves camera directly behind target
		@Override
		public void performAction(float arg0, Event arg1) {
			cameraAzimuth = ((BatShip) target).getDegrees();
		}		 
	 }
	 
}
