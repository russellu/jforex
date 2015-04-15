package physics;

/**
 * interface occupying: methods for collision detection, see if there is something in the way
 * 
 * have a separate integer array holding an identifier to all objects ... all occupying objects will have to
 * be in the same array? or do they automatically update it when they move?
 * 
 * checkCollisions before every move, check to see if the move will cause a collision. if so, retract the move (forward model)
 * if there is a collision, impart the momentum you had to the other object. 
 * how to get the angle right? if collision was from an x-direction move, reverse the x and keep y the same, and vice versa.
 * 
 * you can use the pixel by pixel for loop checking of boundaries to your advantage...by having shields and swords be independent objects
 * referenced separately through the player object, hashMap style
 * if the players reflexes are good enough, he can block shots by moving his shield also (reflex attribute)
 * 
 * implements collidable
 * 
 * @author russ
 *
 */

public interface Occupying {

	public boolean occupies(int x, int y) ; // 
	public void identifierUpdate() ; // update the identifier array
	public void velocityUpdate(double xIncr, double yIncr) ; // 
	
	
	//check all collisions in the new vicinity, act upon those bodies and self
	// but you should not be allowed to pass through another body...(unless you halt time)
	public boolean checkCollisions() ; 
	
	 //acted upon by a body of velocity and mass
	// update only the velocities in the acted upon method, only change position in the move method
	public void actedUpon() ;		
	
	void finalVelocityUpdate() ; 
		
	public double getMass();
	public double[] getVelocity() ; 
	public double getXPos() ; 
	public double getYPos() ; 
	public void drawSelf(double[][][] world,int[][]identifier) ;
	
}

