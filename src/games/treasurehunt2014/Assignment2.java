/** Assignment1.java extends Sage's BaseGame class and contains the
 *  methods necessary to play the Treasure Hunt 2014 game.  
 */
package games.treasurehunt2014;

import sage.app.BaseGame; 
import sage.camera.ICamera;
import sage.camera.JOGLCamera;
import sage.display.*;  
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.input.IInputManager;
import sage.renderer.*; 
import sage.scene.HUDString;
import sage.scene.RotationController;
import sage.scene.SceneNode;
import sage.scene.shape.Cylinder;
import sage.scene.shape.Line;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Rectangle;
import sage.scene.shape.Sphere;
import sage.scene.shape.Teapot;
import sage.texture.Texture;
import sage.texture.TextureManager;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D; 
import graphicslib3D.Vector3D;
 
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import myGameEngine.camera.controllers.Camera3Pcontroller;
import myGameEngine.display.MyDisplaySystem;
import myGameEngine.event.CrashEvent;
import myGameEngine.input.action.QuitGameAction;
import myGameEngine.input.action.WindowedModeToggle;
import myGameEngine.sceneNode.MyGroup;
import myGameEngine.sceneNode.controllers.MyTransController;
import myGameEngine.sceneNode.controllers.MyVanishController;

public class Assignment2 extends BaseGame {
	 IDisplaySystem display; 
	 ICamera camera1, camera2; // Two cameras, one for each player
	 Camera3Pcontroller cc1, cc2; // Controller for each camera
	 
	 //Shapes that are used in the game
	 Pyramid myPyr;
	 SceneNode player1, player2;
	 Cylinder myCyl;
	 Sphere mySph;
	 Diamond myDia; 
	 Teapot teap;
	 MyGroup group1, group2, group3;
	 MyTreasureChest myTreasure;
	 
	 private boolean isUpdating = false;
	 IRenderer renderer;
	 IEventManager eventMgr;	
	 private int score1 = 0, score2 = 0 ; // holds game score for HUD
	 private float time = 0 ; // game elapsed time for HUD
	 private HUDString scoreString1, scoreString2 ; 
	 private HUDString timeString1, timeString2 ;
	 ArrayList<SceneNode> deleteList = new ArrayList<SceneNode>(); // Create list for objects that have 
	   															//been collided with to be added to.

	 
	 protected void initGame() {
		 eventMgr = EventManager.getInstance();
		 display = createDisplaySystem();
		 display.setTitle("2 Player Treasure Hunt");
		 initGameObjects(); 
		 renderer = display.getRenderer(); 
		 createPlayers();
		 IInputManager im = getInputManager(); 
		 String gpName = im.getFirstGamepadName(); // For Gamepad input
		 String kbName = im.getKeyboardName();	// For keyboard input
		 cc1 = new Camera3Pcontroller(camera1, player1, im, gpName, this);
		 cc2 = new Camera3Pcontroller(camera2, player2, im, kbName, this);
		 
		 QuitGameAction quitGame= new QuitGameAction(this);
		 WindowedModeToggle toggleAction = new WindowedModeToggle(display);
		 		 
		 // Associate input controls with their respective classes
		 im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._6, 
		 					quitGame, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		 im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.ESCAPE, 
				 			quitGame, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		 im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._5, 
							toggleAction, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	     im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.TAB, 
				 			toggleAction, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);		 
		 super.update(0.0f);
	 } 
	 
	 public void update(float elapsedTimeMS) { 
		
		 Iterable<SceneNode> it = getGameWorld(); // Get iterable collection
		 Iterator<SceneNode> iter = it.iterator(); // Get iterator for collection
		 
		 while(iter.hasNext()) { // Iterate through collection and check for collisions
			 SceneNode node = (SceneNode) iter.next();
			 if (node instanceof Teapot || node instanceof Diamond || node instanceof Pyramid
					 || node instanceof Cylinder || node instanceof Sphere || node instanceof MyGroup) {
				 if (node.getWorldBound().intersects(player1.getWorldBound())) { 
					 deleteList.add(node); // If collided, add that object to deleteList array
					 score1 += 10; // Score 10 points for all objects collected
					 if(node instanceof Teapot) {
						 score1 += 20; // Score total of 30 for a teapot!
					 }					 
					 CrashEvent newCrash = new CrashEvent(); 
					 eventMgr.triggerEvent(newCrash); // Trigger event to let treasure chest know event has happened
				 } 
				 if (node.getWorldBound().intersects(player2.getWorldBound())) { 
					 deleteList.add(node); // If collided, add that object to deleteList array
					 score2 += 10; // Score 10 points for all objects collected
					 if(node instanceof Teapot) {
						 score2 += 20; // Score total of 30 for a teapot!
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
		 
		 // Jumping takes place over time, so each update needs to check if a player is currently jumping
		 if(((BatShip) player1).getIsJumping()) ((BatShip) player1).jump();
		 if(((BatShip) player2).getIsJumping()) ((BatShip) player2).jump();
		 
		 deleteList.clear(); // Clear list before next update
		 scoreString1.setText("Score = " + score1); // Update score on HUD player 1
		 scoreString2.setText("Score = " + score2); // Update score on HUD player 2
		 time += elapsedTimeMS; 
		 DecimalFormat df = new DecimalFormat("0.0"); 
		 timeString1.setText("Time = " + df.format(time/1000)); // Update time on HUD1
		 timeString2.setText("Time = " + df.format(time/1000)); // Update time on HUD2
		 setUpdating(true);
		 cc1.update(elapsedTimeMS); 
		 cc2.update(elapsedTimeMS); 		 
		 super.update(elapsedTimeMS); 
	 } 
 
	 @Override
	 protected void render() { 
		 renderer.setCamera(camera1); 
		 super.render(); 
		 renderer.setCamera(camera2); 
		 super.render(); 
	 } 

	 private IDisplaySystem createDisplaySystem() { 
		 // Creates a display system in FSEM
		 IDisplaySystem display = new MyDisplaySystem(1010, 720, 24, 20, true, "sage.renderer.jogl.JOGLRenderer"); 
		 System.out.print("\nWaiting for display creation..."); 
		 int count = 0; 
		 
		 // wait until display creation completes or a timeout occurs 
		 while (!display.isCreated()) { 
			 try { 
				 Thread.sleep(10); 
			 } catch (InterruptedException e) { 
				 throw new RuntimeException("Display creation interrupted"); 
			 } 
			 
			 count++; 
			 System.out.print("+"); 
			 if (count % 80 == 0) { System.out.println(); } 
			 
			 if (count > 2000)  {// 20 seconds (approx.) 
				 throw new RuntimeException("Unable to create display"); 
			 } 
		 } 
		 System.out.println(); 
		 return display ; 
	 } 

	 private void initGameObjects() { 
		 IDisplaySystem display = getDisplaySystem(); 
		 display.setTitle("Treasure Hunt 2014"); // Sets frame title
		 Random rand = new Random(); // Used for generating random locations for world objects
		 
		 // This generates floating cylinders with diamonds that revolve around them
		 for (int i = 2; i < 104; i++) {		 
			 group1 = new MyGroup();
			 group2 = new MyGroup();
			 group3 = new MyGroup();
			 
			 myCyl = new Cylinder();
			 Vector3D xAxis = new Vector3D(1,0,0);
			 myCyl.rotate(90, xAxis);
			 myCyl.setSolid(true);
			 
			 // Randomize color of each cylinder
			 Color myColor = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
			 myCyl.setColor(myColor);
			 myDia = new Diamond();
			 
			 group1.addChild(myCyl);
			 group1.addChild(group2);
			 group2.addChild(group3);
			 group3.addChild(myDia);
			 
			 // MyTransController has constructor that takes an increment amount and number of increments per full translation
			 MyTransController upAndDown = new MyTransController(rand.nextInt(5)/1000 + .002, rand.nextInt(3000) + 2000);
			 upAndDown.addControlledNode(group1);
			 group1.addController(upAndDown);
			 group1.translate(rand.nextInt(i * 10) - (i* 5), rand.nextInt(6) + 5, rand.nextInt(i * 10) - (i* 5));
			 
			 Vector3D diamondRevV = new Vector3D(0,1,0); 
			 RotationController diamondRev = new RotationController(rand.nextInt(400) - 200,diamondRevV); 
			 diamondRev.addControlledNode(group2); 
			 group2.addController(diamondRev); 
	
			 group3.translate(2, 0, 0);
			 addGameWorldObject(group1);
			 i++;
		 }
		 
		 // Populate the world with the five different objects
		 for (int i = 2; i < 52; i++){
			 myDia = new Diamond();
			 Matrix3D objM = myDia.getLocalTranslation(); 
			 objM.translate(rand.nextInt(i*5) - (i*2.5),1,rand.nextInt(i*5) - (i*2.5)); 
			 myDia.setLocalTranslation(objM);
			 
			 // MyVanishController gets a random number of seconds before it is deleted from game
			 MyVanishController vanish = new MyVanishController(this, rand.nextInt(100) + 10);
			 vanish.addControlledNode(myDia);
			 myDia.addController(vanish);
			 addGameWorldObject(myDia); 
		 }
		 
		 for (int i = 2; i < 20; i++){
			 teap = new Teapot();
			 Matrix3D objM = teap.getLocalTranslation(); 
			 objM.translate(rand.nextInt(i*50) - (i*25),1,rand.nextInt(i*50) - (i*25)); 
			 teap.setLocalTranslation(objM);			
			 addGameWorldObject(teap); 
		 }
		 
		 // Instantiate and position a treasure chest at the origin
		 myTreasure = new MyTreasureChest(); 
		 Matrix3D treasureS = myTreasure.getLocalScale(); 
		 treasureS.scale(1.05,1.05,1.05);
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
		 
		 // Create a flat playing surface
		 Texture mars = TextureManager.loadTexture2D("mars.jpg");
		 Rectangle myRect = new Rectangle();
		 Color myColor = new Color(110,63,16);
		 myRect.setColor(myColor);
		 myRect.setTexture(mars);
		 Matrix3D scaleM = new Matrix3D();
		 scaleM = myRect.getLocalScale();
		 scaleM.scale(700, 700, 700);
		 myRect.setLocalScale(scaleM);
		 Matrix3D rotateM = new Matrix3D();
		 rotateM = myRect.getLocalRotation();
		 rotateM.rotateX(90);
		 myRect.setLocalRotation(rotateM);
		 Matrix3D transM = new Matrix3D();
		 transM = myRect.getLocalTranslation();
		 transM.translate(0, 0, 0);
		 myRect.setLocalTranslation(transM);
		 addGameWorldObject(myRect); 
		 
		 // The treasure chest is the only listener. Grows when it detects collisions
		 eventMgr.addListener( myTreasure, CrashEvent.class);		 
	 } 
	 
	 private void createPlayers() { 
		 player1 = new BatShip(1); 
		 player1.translate(0, 1, 50); 
		 player1.rotate(-90, new Vector3D(0, 1, 0)); 
		 ((BatShip) player1).setDegrees(-90);
		 addGameWorldObject(player1); 
		 
		 camera1 = new JOGLCamera(renderer); 
		 camera1.setPerspectiveFrustum(60, 2, 1, 1000); 
		 camera1.setViewport(0.0, 1.0, 0.0, 0.45); 
		 
		 player2 = new BatShip(2); 
		 player2.translate(50, 1, 0); 
		 player2.rotate(75, new Vector3D(0, 1, 0)); 
		 ((BatShip) player2).setDegrees(75);
		 addGameWorldObject(player2); 
		 
		 camera2 = new JOGLCamera(renderer); 
		 camera2.setPerspectiveFrustum(60, 2, 1, 1000); 
		 camera2.setViewport(0.0, 1.0, 0.55, 1.0); 
		 createPlayerHUDs(); 
	 } 

	 
	 private void createPlayerHUDs() 
	 { 
		 HUDString player1ID = new HUDString("Player 1"); 
		 player1ID.setName("Player1ID"); 
		 player1ID.setLocation(0.01, 0.15); 
		 player1ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO); 
		 player1ID.setColor(Color.red); 
		 player1ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER); 
		 timeString1 = new HUDString("Time = " + time); 
		 timeString1.setLocation(0.01,0.05); // (0,0) [lower-left] to (1,1) 
		 timeString1.setColor(Color.red);
		 camera1.addToHUD(timeString1); 
		 scoreString1 = new HUDString ("Score = " + score1); //default is (0,0) 
		 scoreString1.setLocation(0.12, 0.05);
		 scoreString1.setColor(Color.red);
		 camera1.addToHUD(scoreString1); 
		 camera1.addToHUD(player1ID); 
		 
		 HUDString player2ID = new HUDString("Player 2"); 
		 player2ID.setName("Player2ID"); 
		 player2ID.setLocation(0.01, 0.15); 
		 player2ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO); 
		 player2ID.setColor(Color.yellow); 
		 player2ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER); 
		 timeString2 = new HUDString("Time = " + time); 
		 timeString2.setLocation(0.01,0.05); // (0,0) [lower-left] to (1,1) 
		 timeString2.setColor(Color.yellow);
		 camera2.addToHUD(timeString2); 
		 scoreString2 = new HUDString ("Score = " + score2); //default is (0,0) 
		 scoreString2.setLocation(0.12, 0.05);
		 scoreString2.setColor(Color.yellow);		 
		 camera2.addToHUD(scoreString2); 
		 camera2.addToHUD(player2ID); 
	 } 

	 public void addToDelete(SceneNode s) {
		 deleteList.add(s);
	 }

	public boolean isUpdating() {
		return isUpdating;
	}

	public void setUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}
} 

	