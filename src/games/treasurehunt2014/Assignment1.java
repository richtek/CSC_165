/** Assignment1.java extends Sage's BaseGame class and contains the
 *  methods necessary to play the Treasure Hunt 2014 game.  
 */
package games.treasurehunt2014;

import sage.app.BaseGame; 
import sage.camera.ICamera;
import sage.display.*;  
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.input.IInputManager;
import sage.scene.HUDString;
import sage.scene.SceneNode;
import sage.scene.shape.Cylinder;
import sage.scene.shape.Line;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Sphere;
import sage.scene.shape.Teapot;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D; 
 
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import myGameEngine.event.CrashEvent;
import myGameEngine.input.action.ForwardBackwardAction;
import myGameEngine.input.action.LeftRightAction;
import myGameEngine.input.action.PitchAction;
import myGameEngine.input.action.QuitGameAction;
import myGameEngine.input.action.SetSpeedAction;
import myGameEngine.input.action.YawAction;

public class Assignment1 extends BaseGame {
	 IDisplaySystem display; 
	 ICamera camera; // One camera in this game
	 
	 //Shapes that are used in the game
	 Pyramid myPyr;
	 Cylinder myCyl;
	 Sphere mySph;
	 Diamond myDia; 
	 Teapot teap;
	 MyTreasureChest myTreasure;
	 IEventManager eventMgr;	
	 private int score = 0 ; // holds game score for HUD
	 private float time = 0 ; // game elapsed time for HUD
	 private HUDString scoreString ; 
	 private HUDString timeString ;

	 
	 protected void initGame() {
		 eventMgr = EventManager.getInstance();
		 initGameObjects(); 
		 IInputManager im = getInputManager(); 
		 String gpName = im.getFirstGamepadName(); // For Gamepad input
		 String kbName = im.getKeyboardName();	// For keyboard input
		 
		 SetSpeedAction setSpeed = new SetSpeedAction(myCyl); // Used to increase forward, backward, left and right movement
		 
		 ForwardBackwardAction mvFrontBack = new ForwardBackwardAction(camera, setSpeed); 
		 LeftRightAction mvLeftRight = new LeftRightAction(camera, setSpeed);
		 YawAction mvYaw = new YawAction(camera);
		 PitchAction mvPitch = new PitchAction(camera);
		 QuitGameAction quitGame= new QuitGameAction(this);
		 
		 // Associate input controls with their respective classes
		 im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._4, 
				 			setSpeed, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY); 
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.W, 
							mvFrontBack, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.S, 
							mvFrontBack, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.Y, 
							mvFrontBack, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.X, 
							mvLeftRight, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A, 
							mvLeftRight, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.D, 
							mvLeftRight, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.ESCAPE, 
				 			quitGame, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		 im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RX, 
					        mvYaw, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.RIGHT, 
			        		mvYaw, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.LEFT, 
				 			mvYaw, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RY, 
			                mvPitch, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.UP, 
	                		mvPitch, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.DOWN, 
	                		mvPitch, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 
		 timeString = new HUDString("Time = " + time); 
		 timeString.setLocation(0,0.05); // (0,0) [lower-left] to (1,1) 
		 addGameWorldObject(timeString); 
		 scoreString = new HUDString ("Score = " + score); //default is (0,0) 
		 scoreString.setLocation(0.12, 0.05);
		 addGameWorldObject(scoreString); 
		 super.update(0.0f);
	 } 
	 
	 public void update(float elapsedTimeMS) { 
		 Iterable<SceneNode> it = getGameWorld(); // Get iterable collection
		 Iterator<SceneNode> iter = it.iterator(); // Get iterator for collection
		 ArrayList<SceneNode> deleteList = new ArrayList<SceneNode>(); // Create list for objects that have 
		 															   //been collided with to be added to.
		 while(iter.hasNext()) { // Iterate through collection and check for collisions
			 SceneNode node = (SceneNode) iter.next();
			 if (node instanceof Teapot || node instanceof Diamond || node instanceof Pyramid
					 || node instanceof Cylinder || node instanceof Sphere) {
				 if (node.getWorldBound().contains(camera.getLocation())) { 
					 deleteList.add(node); // If collided, add that object to deleteList array
					 score += 10; // Score 10 points for all objects collected
					 if(node instanceof Teapot) {
						 score += 20; // Score total of 30 for a teapot!
					 }					 
					 CrashEvent newCrash = new CrashEvent(); 
					 eventMgr.triggerEvent(newCrash); // Trigger event to let treasure chest know event has happened
				 } 
			 }
		 }	
		 
		 // Loop through deleteList and have any of those objects removed from the game world
		 for(int i = 0; i < deleteList.size(); i++) {
			 removeGameWorldObject(deleteList.get(i));			 
		 }
		 		 
		 deleteList.clear(); // Clear list before next update
		 scoreString.setText("Score = " + score); // Update score on HUD
		 time += elapsedTimeMS; 
		 DecimalFormat df = new DecimalFormat("0.0"); 
		 timeString.setText("Time = " + df.format(time/1000)); // Update time on HUD
		 super.update(elapsedTimeMS); 
	 } 
 
	 
	 private void initGameObjects() { 
		 IDisplaySystem display = getDisplaySystem(); 
		 display.setTitle("Treasure Hunt 2014"); // Sets frame title
		 Random rand = new Random(); // Used for generating random locations for world objects
		 camera = display.getRenderer().getCamera(); 
		 camera.setPerspectiveFrustum(45, 1, 0.01, 1000); 
		 camera.setLocation(new Point3D(1, 1, 20)); 
		 
		 // Populate the world with the five different objects
		 for (int i = 2; i < 52; i++){
			 myDia = new Diamond();
			 Matrix3D objM = myDia.getLocalTranslation(); 
			 objM.translate(rand.nextInt(i*5) - (i*2.5),rand.nextInt(i*5) - (i*2.5),rand.nextInt(i*5) - (i*2.5)); 
			 myDia.setLocalTranslation(objM); 			 
			 addGameWorldObject(myDia); 
		 }
		 
		 for (int i = 2; i < 42; i++){
			 myPyr = new Pyramid();
			 Matrix3D objM = myPyr.getLocalTranslation(); 
			 objM.translate(rand.nextInt(i*6) - (i*3),rand.nextInt(i*6) - (i*3),rand.nextInt(i*6) - (i*3)); 
			 myPyr.setLocalTranslation(objM); 			 
			 addGameWorldObject(myPyr); 
		 }
		 
		 for (int i = 2; i < 32; i++){
			 mySph = new Sphere();
			 Matrix3D objM = mySph.getLocalTranslation(); 
			 objM.translate(rand.nextInt(i*8) - (i*4),rand.nextInt(i*8) - (i*4),rand.nextInt(i*8) - (i*4)); 
			 mySph.setLocalTranslation(objM); 			 
			 addGameWorldObject(mySph); 
		 }
		 
		 for (int i = 2; i < 22; i++){
			 myCyl = new Cylinder();
			 Matrix3D objM = myCyl.getLocalTranslation(); 
			 objM.translate(rand.nextInt(i*12) - (i*6),rand.nextInt(i*12) - (i*6),rand.nextInt(i*12) - (i*6)); 
			 myCyl.setLocalTranslation(objM); 
			 myCyl.setSolid(true);			 
			 addGameWorldObject(myCyl); 
		 }
		 
		 for (int i = 2; i < 12; i++){
			 teap = new Teapot();
			 Matrix3D objM = teap.getLocalTranslation(); 
			 objM.translate(rand.nextInt(i*24) - (i*12),rand.nextInt(i*24) - (i*12),rand.nextInt(i*24) - (i*12)); 
			 teap.setLocalTranslation(objM);			
			 addGameWorldObject(teap); 
		 }
		 
		 // Instantiate and position a treasure chest at the origin
		 myTreasure = new MyTreasureChest(); 
		 Matrix3D treasureS = myTreasure.getLocalScale(); 
		 treasureS.scale(1.1,1.1,2.2);
		 myTreasure.setLocalScale(treasureS);
		 Matrix3D treasureM = myTreasure.getLocalTranslation(); 
		 treasureM.translate(0,0,0); 
		 myTreasure.setLocalTranslation(treasureM); 
		 addGameWorldObject(myTreasure); 
		 
		 // Generate a three colored x, y, z set of axis
		 Point3D origin = new Point3D(0,0,0); 
		 Point3D xEnd = new Point3D(100,0,0); 
		 Point3D yEnd = new Point3D(0,100,0); 
		 Point3D zEnd = new Point3D(0,0,100); 
		 Line xAxis = new Line (origin, xEnd, Color.red, 2); 
		 Line yAxis = new Line (origin, yEnd, Color.green, 2); 
		 Line zAxis = new Line (origin, zEnd, Color.blue, 2); 
		 addGameWorldObject(xAxis); addGameWorldObject(yAxis); 
		 addGameWorldObject(zAxis); 
		 
		 // The treasure chest is the only listener. Grows when it detects collisions
		 eventMgr.addListener( myTreasure, CrashEvent.class);
	 } 
} 

	