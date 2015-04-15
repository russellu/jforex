package physics;

import java.awt.Color;

/**
 * pad the sprite Battleground with bodies of infinite mass (boundary = infinite mass)
 * keep level in hashmap. hash the location you are traveling to...and see if it is associated to a body (collision detection)
 * 
 * color attraction...players can move faster on certain colors? keep it simple water = bue, grass = green, etc...player strengths druid = grass
 * 
 * make body out of arrows and rocks. body can be any arbitrary shape, specified by occupied. can even have disconnected parts...but better
 * to use composite bodies for this type of behavior. 
 * when the body collides with another body,it imparts it's momentum to that body. physics 101
 * 
 * give each attack a movement direction (velocity vector, similar to the water), for each attack animation frame
 * each attack has it's instantaneous direction and force.
 * 
 * need a better framework for integrating the physics
 * 
 * complete mastery of time and space : space check, time needs loop control
 * 
 * @author russ 
 *
 */

public class Body implements Occupying{

	
	public double mass ; 
	public double xpos ;
	public double ypos ; 
	public double prevxpos ; 
	public double prevypos ; 
	public double width ; 
	public double height ; 
	public double xVel ; 
	public double yVel ; 
	public double friction = 1.01 ; 
	boolean[][] occupied ;
	Color color = new Color(255,255,255) ;
	double elasticity = 0 ; //inelastic
	int ID ;
	PhysicalReality world ; 
	
	double maxVel = 10 ; 
	
	double tempXVel ; 
	double tempYVel ;
	
	int immunity = -1 ; //immune to collisions with said object
	int immunityCount = -1 ;
	
	
	
	public Body(){}
	
	public Body(PhysicalReality world, int xpos, int ypos, int width,int height, double mass,int ID){
		this.world = world ; 
		this.xpos = xpos ; 
		this.ypos = ypos ; 
		this.width = width ; 
		this.height = height ; 
		this.mass = mass ; 
		this.ID = ID ; 
	}
	public Body(int xpos,int ypos,int width,int height){
		this.xpos = xpos ; 
		this.ypos = ypos ; 
		this.mass = Double.MAX_VALUE ; 
	} // no mass=> unmoveable = infinite mass

	
	
	public boolean occupies(int x, int y) {
		if(x<xpos||y<ypos||x>xpos+width||y>ypos+height) //if outside of our bounding box, return false
			return false;
		else if (occupied[x-(int)xpos][y-(int)ypos]) // within bounding box and occupied
			return true ; 
		else return false ; 
	}
	
	public void finalVelocityUpdate(){
		this.xVel = tempXVel ; 
		this.yVel = tempYVel ; 
		if(xVel > maxVel)
			xVel = maxVel ;
		if(yVel > maxVel)
			yVel = maxVel ; 
		
	//	double newX = xpos + xVel ;
	//	double newY=  ypos + yVel ; 
		
		//if(newX>0 && newY>0 && newX + width < world.getIdentifiers().length && newY + height < world.getIdentifiers()[0].length) {
			this.xpos = (this.xpos + xVel ); 
			this.ypos = (this.ypos + yVel); 
	//	}
		xVel = xVel/friction ;
		yVel = yVel/friction ; 
		tempXVel = tempXVel/friction ; 
		tempYVel = tempYVel/friction ;	
	}

	
	public void velocityUpdate(double xIncr, double yIncr){
		this.tempXVel = xIncr ;
		this.tempYVel = yIncr ; 
		
	//	xpos = xpos + this.xVel ; 
	//	ypos = ypos + this.yVel ; 
		
	}
	
	public void identifierUpdate(){
		
	}
	
	public void drawSelf(double[][][] world,int[][]identifier){//draw the body in the world
		
//		System.out.println("drawing self with id" + ID) ;
		for(int i=(int)prevxpos;i<prevxpos+width;i++)
			for(int j=(int)prevypos;j<prevypos+height;j++)
				if(i>0&&j>0&&i<world.length&&j<world[0].length){
				identifier[i][j] = 0 ; 
			}
		
		
		int boxColor = 123 ; 
		if(immunityCount>0)
			boxColor = 255 ; 
		
		
		
		//System.out.println("Drawing self, xpos = " + this.xpos +  " ypos = " + this.ypos) ; 
		for(int i=(int)xpos;i<(int)xpos+width;i++)
			for(int j=(int)ypos;j<(int)ypos+height;j++)
				if(i>0&&j>0&&i<world.length&&j<world[0].length){
					world[i][j][0] = boxColor ;				
					world[i][j][1] = boxColor ;
					world[i][j][2] = boxColor ;
					world[i][j][3] = 255 ;	
					identifier[i][j] = ID ; 
			}
	}

	public boolean checkCollisions() {
		
	//	System.out.println("checking CoLLISIONS, this ID = " + this.ID + " x = " + this.xpos + " y = " + this.ypos + " x+w = " + (this.xpos+this.width) + " y + h = " + (this.ypos + this.height)) ;
	//	System.out.println("checking CoLLISIONS, this ID = " + .ID + " x = " + this.xpos + " y = " + this.ypos + " x+w = " + (this.xpos+this.width) + " y + h = " + (this.ypos + this.height)) ;

		int[][] idSpace = world.getIdentifiers();
		
		if((int)Math.floor(xpos) <= 0 || (int)Math.ceil(xpos+width) >= idSpace.length){
			this.velocityUpdate(-xVel, yVel);
			xpos = xpos + -xVel ;
			return true ;
			
		}
		else if((int)Math.floor(ypos) <= 0 || (int)Math.ceil(ypos+height) >= idSpace[0].length){
			this.velocityUpdate(xVel, -yVel);
			ypos = prevypos + -yVel ;
			return true ; 
		}		
		
		for(int i=(int)Math.floor(xpos);i<Math.ceil(xpos+width);i++)
			for(int j=(int)Math.floor(ypos);j<Math.ceil(ypos+height);j++){
				if(idSpace[i][j] != 0 && idSpace[i][j] != ID && immunity!=idSpace[i][j]){// if the pixel isn't occupied by this body
				//	System.out.println("collision detected " + idSpace[i][j] + " this ID = " + this.ID) ; 
					Occupying oc = world.getBodies().get(idSpace[i][j]) ; 
					double[][] v1v2 = PhysicsFunctions.calculateElasticCollision(this,oc) ; 
					//oc.checkCollisions() ;
					oc.velocityUpdate(v1v2[1][0], v1v2[1][1]) ;		
					this.velocityUpdate(v1v2[0][0], v1v2[0][1]) ;
					immunity = idSpace[i][j] ; 
					immunityCount = 3 ; 
					this.ypos = this.prevypos ;
					this.xpos = this.prevxpos ; 
					return true ; 
				}
			}
		
		return false ; 
		
	}
	


	public double getMass(){		
		return mass ; 
	}
	
	public double[]getVelocity(){
		double[] d = {xVel,yVel} ;
		return d ; 
	}
	
	public void actedUpon() {

//		System.out.println("acting on body xpos" + this.xpos) ; 
		
		immunityCount -- ;
		if(immunityCount <0)
			immunity = -1 ; 
		
		prevxpos = xpos ; 
		prevypos = ypos ; 
		
		checkCollisions() ; 

	}

	public double getXPos() {
		return this.xpos ; 
	}

	public double getYPos() {
		return this.ypos ; 
	}	
}
