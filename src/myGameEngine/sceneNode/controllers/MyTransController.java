/** SceneNode controller that translates it up and down on the y axis.
 *  Duration of transit and speed can be passed into constructor.
 */
package myGameEngine.sceneNode.controllers;

import sage.scene.Controller;
import sage.scene.SceneNode;
import graphicslib3D.Matrix3D;

public class MyTransController extends Controller 
{ 
	 private double translationRate = .002 ; // movement per second 
	 private double cycleTime = 4000.0; // default cycle time 
	 private double totalTime; 
	 private double direction = 1.0; 
	 
	 public MyTransController() {
		 
	 }
	 public MyTransController(double transRate, double cycles) {
		 translationRate = transRate;
		 cycleTime = cycles;
	 }
 
	 public void setCycleTime(double c) { 
		 cycleTime = c; 
	 } 
	 
	 public void update(double time) {
	  
		 totalTime += time; 
		 double transAmount = translationRate * time ; 
		 
		 if (totalTime > cycleTime) { 
			 direction = -direction; 
			 totalTime = 0.0; 
		 } 
		 
		 transAmount = direction * transAmount; 
		 
		 Matrix3D newTrans = new Matrix3D(); 
		 newTrans.translate(0,transAmount,0); 
		 
		 for (SceneNode node : controlledNodes) { 
			 Matrix3D curTrans = node.getLocalTranslation(); 
			 curTrans.concatenate(newTrans); 
			 node.setLocalTranslation(curTrans); 
		 } 
	 } 
}
