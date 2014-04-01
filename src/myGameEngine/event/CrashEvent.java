/** CrashEvent.java is an extension of the AbstractGameEvent class in Sage. 
 *  It currently only contains two methods that return event type and name.
 */

package myGameEngine.event;

import sage.event.*; 

public class CrashEvent extends AbstractGameEvent { 
	 public String getName() { 
		 return new String("crash event"); 
	 } 
	 
	 
	 // Current Assignment does not require this to do anything
	 public CrashEvent() {
		 
	 } 	 
} 

