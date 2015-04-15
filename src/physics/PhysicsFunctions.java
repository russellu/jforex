package physics;

public class PhysicsFunctions {


	/**
	 * returns velocities of two objects as a function of their current velocities and mass
	 * does not take into account direction of collision ie if body a slams body b from behind,
	 * body b's forward velocity is imparted...this should only work for head-on collisions
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[][] calculateElasticCollision(Occupying a, Occupying b){
		double m1 = a.getMass() ; 
		double m2 = b.getMass() ; 
		double[] v1 = a.getVelocity() ; 
		double[] v2 = b.getVelocity() ; 
		
		/**
		 *  * v1 = (u1(m1-m2)+2m2u2) / (m1+m2) => wikipedia elastic collision
		 */
		
		double v1x = (v1[0]*(m1-m2)+2*m2*v2[0]) / (m1+m2) ; 
		double v1y = (v1[1]*(m1-m2)+2*m2*v2[1]) / (m1+m2) ; 
		double v2x = (v2[0]*(m2-m1)+2*m1*v1[0]) / (m1+m2) ; 
		double v2y = (v2[1]*(m2-m1)+2*m1*v1[1]) / (m1+m2) ; 
		
		//System.out.println("v1x = " + v1x + " v2x = " + v2x + " v1y = " + v1y + " v2y = " + v2y) ; 
		//System.out.println("v1xpos = " + a.getXPos() + " v2xpos = " + b.getXPos() + " v1ypos = " + a.getYPos() + " v2ypos = " + b.getYPos()) ; 

		
		double[] newV1 = {v1x,v1y} ;
		double[] newV2 = {v2x,v2y} ;
		double[][] newVelocities = {newV1,newV2} ;
		
		return newVelocities ; 
		
		
	}
}
