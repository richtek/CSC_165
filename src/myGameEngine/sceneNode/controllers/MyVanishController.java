/** SceneNode controller that causes SceneNode to be removed from game
 *  after a specified period of time. 
 */
package myGameEngine.sceneNode.controllers;

import games.treasurehunt2014.Assignment2;
import sage.scene.Controller;
import sage.scene.SceneNode;

public class MyVanishController extends Controller{
	private double timeRemaining;
	private Assignment2 game;

	public MyVanishController(Assignment2 game, int timeTilGone) {
		timeRemaining = timeTilGone;
		this.game = game;
	}
	
	@Override
	public void update(double time) {
		timeRemaining -= .04;
		
		for (SceneNode node : controlledNodes) { 
			if (timeRemaining <= 0) {
				game.addToDelete(node);
			}
		}
		
	}

}
