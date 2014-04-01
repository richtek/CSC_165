/** BatShip is an extension to Sage TriMesh. It can be displayed two different 
 *  colors for the ship. 
 */
package games.treasurehunt2014;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import sage.scene.TriMesh;

public class BatShip extends TriMesh{
	
	private boolean isJumping;
	private boolean isRunning;
	private float elapsedTime;
	private float jumpAmount; // Incrementally raises and lowers avatar
	private float jumpSpeed;
	private float degrees = 0; // Keeps track of the degrees the bat has been rotated locally
	private static float gravity= -32f;
	
	private static float[] vrts = new float[] {0,.38f,-1.75f, .3f,0,-1.5f, -.3f,0,-1.5f, 0,.28f,-1.35f, 1.0f,0,-1.5f, .75f,.5f,-1.25f,
											   -1.0f,0,-1.5f, -.75f,.5f,-1.25f, 1.0f,0,1.5f, .75f,.5f,1.25f, -.75f,.5f,1.25f, -1f,0,1.5f,
											   0,1.1f,2.5f, 1.0f,0,-1.5f, .75f,.5f,-1.25f, 2.25f,1.5f,-.5f, 2.0f,1.5f,-.5f, 2.25f,1.5f,1.0f,
											   2.0f,1.5f,2.0f, 1.0f,0,0, .75f,.5f,0, -1.0f,0,-1.5f, -.75f,.5f,-1.25f, -2.25f,1.5f,-.5f,
											   -2.0f,1.5f,-.5f, -2.25f,1.5f,1.0f, -2f,1.5f,1.0f, -1.0f,0,0, -.75f,.5f,0, -.7f,.28f,-1.35f,
											   -.3f,.28f,-1.35f, -.5f,.14f,-1.45f, .7f,.28f,-1.35f, .3f,.28f,-1.35f, .5f,.14f,-1.45f}; 		
	private static float[] cl1 = new float[] {0,0,0,1f, 0,0,0,1f, 0,0,0,1f, 0,0,0,1f, .816f,.086f,.086f,1f, .816f,.086f,.086f,1f, 
											  .816f,.086f,.086f,1f, .816f,.086f,.086f,1f, .816f,.086f,.086f,1f, .816f,.086f,.086f,1f, 
											  .816f,.086f,.086f,1f, .816f,.086f,.086f,1f, 0,0,0,1f, .59f,.58f,.52f,1f, .59f,.58f,.52f,1f, 
											  .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, .59f,.58f,.52f,1f, 
											  .59f,.58f,.52f,1f, .59f,.58f,.52f,1f, .59f,.58f,.52f,1f, .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, 
											  .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, .59f,.58f,.52f,1f, .59f,.58f,.52f,1f, 1,1,1,1, 1,1,1,1, 
											  1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1}; 
	private static float[] cl2 = new float[] {0,0,0,1f, 0,0,0,1f, 0,0,0,1f, .38f,.38f,.28f,1f, .675f,.478f,.078f,1f, .675f,.478f,.078f,1f, 
											  .675f,.478f,.078f,1f, .675f,.478f,.078f,1f, .675f,.478f,.078f,1f, .675f,.478f,.078f,1f,
											  .675f,.478f,.078f,1f, .675f,.478f,.078f,1f, 0,0,0,1f, .59f,.58f,.52f,1f, .59f,.58f,.52f,1f,  
											  .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, .59f,.58f,.52f,1f, 
											  .59f,.58f,.52f,1f, .59f,.58f,.52f,1f, .59f,.58f,.52f,1f, .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, 
											  .38f,.38f,.28f,1f, .38f,.38f,.28f,1f, .59f,.58f,.52f,1f, .59f,.58f,.52f,1f, 1,1,1,1, 1,1,1,1, 
											  1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1}; 
	private static int[] triangles = new int[] {0,1,2, 0,1,3, 0,2,3, 4,5,6, 5,6,7, 5,7,9, 7,9,10, 4,8,9, 4,5,9, 9,10,12, 8,11,12, 8,9,12, 
												10,11,12, 7,10,11, 6,7,11, 13,14,15, 13,15,16, 17,19,20, 17,18,19, 15,16,17, 16,17,18, 
												14,18,20, 14,16,18, 13,17,19, 13,15,17, 21,22,23, 22,23,24,23,24,25, 24,25,26, 25,26,27, 
												26,27,28, 22,24,28,	24,26,28, 21,25,27, 21,23,25, 29,30,31, 32,33,34}; 
	
	@SuppressWarnings("unused")
	private FloatBuffer colorBuf1;
	
	public BatShip(int color) {
		 	FloatBuffer vertBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts); 
		 	if (color == 1) {
		 		FloatBuffer colorBuf1 = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl1);
		 		this.setColorBuffer(colorBuf1); 
		 	}
		 	else {
		 		FloatBuffer colorBuf1 = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl2);
		 		this.setColorBuffer(colorBuf1); 
		 	}
	        IntBuffer triangleBuf = com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles); 
	        this.setVertexBuffer(vertBuf); 	        
	        this.setIndexBuffer(triangleBuf); 
	        setIsJumping(false); 
	        this.elapsedTime = 0;
	        this.jumpAmount = 0;
			this.jumpSpeed = 6f;	        
    }

	public Boolean getIsJumping() {
		return isJumping;
	}

	public void setIsJumping(Boolean isJumping) {
		this.isJumping = isJumping;
	}
	
	public void jump() {	
		
		if (isRunning) jumpSpeed *= 1.5;
		elapsedTime += .02;
		// y = ut + .05 * gt^2, where u = speed and g = -32ft/sec/sec
		jumpAmount = (float) ((jumpSpeed * elapsedTime) + (.5 * gravity * elapsedTime * elapsedTime));				
		this.translate(0, jumpAmount, 0);
		
		// 
		if (this.getLocalTranslation().getCol(3).getY() <= 1) {
			elapsedTime = 0;
			jumpAmount = 0;
			this.translate(0, (float) (1- this.getLocalTranslation().getCol(3).getY()), 0);
			this.setIsJumping(false);
		}
		jumpSpeed = 6f;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}	 
		
	public void rotate(float degrees) {
		this.rotate(degrees);
		this.degrees += degrees;
		this.degrees = (this.degrees % 360);
	}
	
	public float getDegrees() {
		return this.degrees;
	}
	
	public void setDegrees(float degrees) {
		this.degrees += degrees;
	}
}
