package myGameEngine.sceneNode;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import sage.scene.SceneNode; 
import sage.scene.Group; 
 
public class MyGroup extends Group 
{ 
	 protected void updateWorldTransforms() 
	 {  	 
		 SceneNode p = this.getParent(); 
		 if ((p != null) && (p.getWorldTranslation() != null) && 
			 (p.getWorldScale() != null) && (p.getWorldRotation() != null)) { 
			 Matrix3D tempTrans = (Matrix3D) p.getWorldTranslation().clone(); 
			 tempTrans.concatenate((Matrix3D)this.getLocalTranslation().clone()); 
			 this.setWorldTranslation(tempTrans); 
			 
			 Matrix3D tempRotation = (Matrix3D) p.getWorldRotation().clone(); 
			 tempRotation.concatenate((Matrix3D)this.getLocalRotation().clone()); 
			 this.setWorldRotation(tempRotation); 
			 
			 Matrix3D tempScale = (Matrix3D) p.getWorldScale().clone(); 
			 tempScale.concatenate((Matrix3D)this.getLocalScale().clone()); 
			 this.setWorldScale(tempScale); 
			 
			 // ADDED -- update translation due to parent rot/scale (same as Leaf): 
			 
			 Point3D lc1 = new Point3D(); 
			 lc1 = lc1.mult(this.getLocalTranslation()); 
			 Point3D lc2 = (lc1.mult(p.getWorldRotation())).mult(p.getWorldScale()); 
			 lc2 = lc2.minus(lc1); 
			 Matrix3D twT = this.getWorldTranslation(); 
			 twT.translate((float)lc2.getX(), (float)lc2.getY(), (float)lc2.getZ()); 
		 } else { // this also is copied from original Group class: 
			 this.setWorldTranslation((Matrix3D)this.getLocalTranslation().clone()); 
			 this.setWorldRotation((Matrix3D)this.getLocalRotation().clone()); 
			 this.setWorldScale((Matrix3D)this.getLocalScale().clone()); 
		 } 
	 } 
} 

