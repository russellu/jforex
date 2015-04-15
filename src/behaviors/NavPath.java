package behaviors;

import java.util.ArrayList;

/**
 * navigational path: has end point, current point, begin point.
 * the navigational path is updated every move, as the player gets more information.
 * 
 * key aspects: no path retracing, always try to go on new territory unless you've exhauseted the possibilities
 * 
 * dynamically updated...player's LOS is given to the navPath
 * 
 * a series of straight lines makes up a curve...break up the path into small segments?
 * 
 * directed flood fill? fiber tracking through a vector field of collisions?
 * 
 * path deviation proportional to field emitted by collidable object? use the mouse to test dynamic path updating
 * 
 * @author russ
 *
 */

public class NavPath {

	public int[][] occupied ; 
	public ArrayList<double[]> path ; //
	public double[] pathStart ; 
	public double[] pathEnd ; 
		
	public NavPath(int[][] occupied,double[]pathStart, double[]pathEnd){
		
		 this.occupied = occupied ; 
		 this.pathStart = pathStart ; 
		 this.pathEnd = pathEnd ; 
		 path = new ArrayList<double[]>() ; 
	     path.add(pathStart) ; 
		
	}
	
	
	//calculate the path in terms of X and Y moves
	public void calculatePath(){
		double[] currentLoc = path.get(0) ; 
		double[] dest = pathEnd ; 
		
		double totalX = currentLoc[0] - dest[0] ; 
		double totalY = currentLoc[1] - dest[1] ; 
		
		
		
		while(Math.abs(currentLoc[0]-dest[0])> .1 && Math.abs(currentLoc[1]-dest[1]) > .1){
			//a bunch of complicated shit here
			
			//try{Thread.sleep(100);}catch(Exception e){}
			
			System.out.println("looping, xloc =  " + currentLoc[0] + " yLoc = " + currentLoc[1] ) ; 
			
			
			
			double xdiff = dest[0]-currentLoc[0] ;
			double ydiff = dest[1]-currentLoc[1] ; 
			
			xdiff = (xdiff/Math.sqrt(xdiff*xdiff+ydiff*ydiff))*100 ; 
			ydiff = (ydiff/Math.sqrt(xdiff*xdiff+ydiff*ydiff))*100 ; 
			
			//int[] incrs = getDirection(xdiff,ydiff) ;
			
			//currentLoc[0] = currentLoc[0] + incrs[0] ; 
		//	currentLoc[1] = currentLoc[1] + incrs[1] ; 
			
			currentLoc[0] = currentLoc[0] + xdiff/100 ; //*(totalX-xdiff) ; 
			currentLoc[1] = currentLoc[1] + ydiff/100 ; //*(totalY-ydiff) ; 
			
			double[] newLoc = {currentLoc[0],currentLoc[1]} ;
			System.out.println("adding to path, x = " + currentLoc[0] + " y = " + currentLoc[1]) ; 
			path.add(newLoc) ; 

		}
	}
	
	
	// yay you drew the path
	public void drawSelf(double[][][] world){
		//System.out.println("drawing path, pathlength = " + path.size()) ; 
		for(int i=0;i<path.size();i++){
			
			double[] currentPoint = path.get(i) ; 
			//System.out.println("drawing path, currentx = " + currentPoint[0] + " currenty = " + currentPoint[1]) ; 
			world[(int)currentPoint[0]][(int)currentPoint[1]][0] = 255 ; 
			world[(int)currentPoint[0]][(int)currentPoint[1]][1] = 0 ; 
			world[(int)currentPoint[0]][(int)currentPoint[1]][2] = 0 ; 
			world[(int)currentPoint[0]][(int)currentPoint[1]][3] = 255 ; 
					
		}	
	}
		
	public int[] getDirection(double xdiff, double ydiff){		
		int xIncr = 0 ; 
		int yIncr = 0 ; 
			
		if(xdiff<0)
			xIncr = -1 ;
		else if (xdiff>0) 
			xIncr = 1 ; 
		else xIncr = 0 ;
		if(ydiff<0)
			yIncr = -1 ; 
		else if (ydiff>0)
			yIncr = 1 ; 
		else yIncr = 0 ; 
			
		int[] direction = {xIncr,yIncr} ;
		return direction ; 		
	}
		
	
	
	
	
	
}
