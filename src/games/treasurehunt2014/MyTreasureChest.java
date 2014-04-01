/** MyTreasurechest.java is a class that extends the sage.scene.shape class
 *  Cube.java and implements sage's IEventListener. It creates a Cube and has 
 *  it listen for a collision event. Its event handler calls its private method
 *  Grow(), which scales the cube up slightly.
 */

package games.treasurehunt2014;
import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.shape.Cube;

public class MyTreasureChest extends Cube implements IEventListener{
    private MyTreasureChest rec;
    	
	public MyTreasureChest () {
		rec = this;
	}
	
	@Override
	public boolean handleEvent(IGameEvent event) {
		Grow();
		return false;
	}
	
	private void Grow() {
		rec.scale(1.15f, 1.15f, 1.15f);
	}
}
