package games; 
 
import sage.app.BaseGame; 
import sage.display.*;  
import sage.scene.SceneNode; 
import sage.scene.shape.Rectangle; 
import sage.scene.HUDString; 
 
import graphicslib3D.Point3D; 
 
import java.awt.event.*; 
import java.util.Random; 
import java.awt.Color; 
import java.text.DecimalFormat; 
 
// only initGame() and update() need to be provided. 
 
public class jitterbug extends BaseGame implements MouseListener 
{ 
 private int score = 0 ; 
 private float time = 0 ; // game elapsed time 
 private HUDString scoreString ; 
 private HUDString timeString ; 
 IDisplaySystem display; 
 
 //initGame() is called once at startup by BaseGame.start() 
 
 public void initGame() 
 { 
 display = getDisplaySystem(); 
 
 // add some game objects 
 
 Rectangle rect1 = new Rectangle(0.3f, 0.3f); // white by default 
 addGameWorldObject(rect1); 
 Rectangle rect2 = new Rectangle(0.3f, 0.3f); 
 addGameWorldObject(rect2); 
 
 // a HUD 
 
 timeString = new HUDString("Time = " + time); 
 timeString.setLocation(0,0.05); // (0,0) [lower-left] to (1,1) 
 addGameWorldObject(timeString); 
 scoreString = new HUDString ("Score = " + score); //default is (0,0) 
 addGameWorldObject(scoreString); 
 
 // configure game display 
 
 display.setTitle("MyGame"); 
 display.addMouseListener(this); 
 } 

 // update is called by BaseGame once each time around game loop 
 
 public void update(float elapsedTimeMS) 
 { 
 // add a small movement to each game world object 
 
 for (SceneNode s : getGameWorld()) 
 { Random rng = new Random(); 
 float tx = (rng.nextFloat()*2 -1) * 0.005f ; 
 float ty = (rng.nextFloat()*2 -1) * 0.005f ; 
 s.translate(tx, ty, 0); 
 } 
 
 // update the HUD 
 
 scoreString.setText("Score = " + score); 
 time += elapsedTimeMS; 
 DecimalFormat df = new DecimalFormat("0.0"); 
 timeString.setText("Time = " + df.format(time/1000)); 
 
 // tell BaseGame to update game world state 
 
 super.update(elapsedTimeMS); 
 } 
 
 
 public void mousePressed(MouseEvent e) 
 { 
 // get relevant screen parameters 
 
 Point3D scrClickPoint = new Point3D(e.getX(), e.getY(), 0); 
 double scrWidth = display.getWidth(); 
 double scrHeight = display.getHeight(); 
 
 // convert screen point to world coordinates 
 // based on a default JOGL world window of -1..1 
 
 Point3D worldClickPoint = new Point3D(); 
 worldClickPoint.setX((scrClickPoint.getX() / scrWidth) * 2.0 - .5); 
 worldClickPoint.setY(-((scrClickPoint.getY() / scrHeight) * 2.0 - .5)); 
 worldClickPoint.setZ(0.0); 
 
 //see if mouse click was on a gameworld object 
 
 for (SceneNode s : getGameWorld()) 
 { if (s instanceof Rectangle) 
 { Rectangle r = (Rectangle) s; 
 if (r.contains(worldClickPoint)) 
 { 
 // mouse hit target; change color and increase score 
 r.setColor(Color.red); 
 score++ ; 
 } } } 
 } 
 
 public void mouseClicked(MouseEvent e) {} 
 public void mouseEntered(MouseEvent e) {} 
 public void mouseExited(MouseEvent e) {} 
 public void mouseReleased(MouseEvent e) {} 
 
 
 public static void main (String [] args) 
 { new jitterbug().start(); } 
} 