/** Diamond.java is a class that extends the Sage TriMesh class.
 *  The shape this class generates is that of an 8 sided diamond.
 */


package games.treasurehunt2014;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import sage.scene.TriMesh;

public class Diamond extends TriMesh
{ 
	private double opacity = 1;
	private static float[] vrts = new float[] {0,0,1, 0,-1,0, 1,0,0, 1,0,0, 0,-1,0, 0,0,-1, 0,0,-1, -1,0,0, -1,0,0, 
		0,0,1, 0,1,0, 0,1,0}; 
	private float[] cl1 = new float[] {.1f,.5f,.1f,(float) opacity, .1f,.5f,.1f,(float) opacity, .1f,.5f,.1f,(float) opacity, 1,1,1,(float) opacity, 
										1,1,1,(float) opacity, 1,1,1,(float) opacity, .1f,.5f,.1f,(float) opacity, .1f,.1f,.1f,(float) opacity, 
										1,1,1,(float) opacity, 1,1,1,(float) opacity, 1,1,1,(float) opacity, .1f,.5f,.1f,(float) opacity}; 
//	private static float[] cl2 = new float[] {.5f,.5f,.5f,1, .5f,.5f,.5f,1, .5f,.5f,.5f,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, .5f,.5f,.5f,1, 
//		.5f,.5f,.5f,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, .5f,.5f,.5f,1};
	private static int[] triangles = new int[] {0,1,2, 3,4,5, 6,7,1, 8,9,5, 9,3,10, 2,6,11, 5,8,10, 7,0,11}; 
	
	@SuppressWarnings("unused")
	private FloatBuffer colorBuf1;
	 
    public Diamond() { 
        FloatBuffer vertBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts); 
        FloatBuffer colorBuf1 = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl1);         
//        FloatBuffer colorBuf2 = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl2); 
        IntBuffer triangleBuf = com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles); 
        this.setVertexBuffer(vertBuf); 
        this.setColorBuffer(colorBuf1); 
        this.setIndexBuffer(triangleBuf); 
    }

    public void updateTransforms(double time) 
    { 
	    super.updateTransforms(time); 
	  
   }
	
}
