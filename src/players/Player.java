package players;

import javax.swing.JFrame;

import improc.SpriteHolder;

/**
 * Player class : contains the coordinates of the player and a reference to it's sprites
 * 
 * 
 * @author russ
 *
 */

public class Player implements ControllablePlayer {

	SpriteHolder sprites ; 
	short[][][] currentPic ;
	public boolean idle ;
	public boolean attacking ; 
	public boolean moving ; 
	//attacks
	int NONE = 0 ; 
	int SLASH = 1 ; 
	int CRUSH = 2 ; 
	int PIERCE = 3 ; 
	int[] attackCodes = {SLASH,CRUSH,PIERCE} ; 
	int currentAttack = NONE ; 
	double xloc ; 
	double yloc ; 
	double prevXLoc ; 
	double prevYLoc ; 
	
	double xVel ; 
	double yVel ; 
	int ID ;
	
	public double redScale = 1 ;
	public double greenScale = 1 ; 
	public double blueScale = 1 ; 

	public Player(){}
	
	public Player(SpriteHolder sprites, int xloc, int yloc,int ID){
		this.sprites = sprites ; 
		this.ID = ID ; 
		this.xloc = xloc ;
		this.yloc = yloc ;
	}
	
	public double getX(){
		return this.xloc ; 
	}
	
	public double getY(){
		return this.yloc ;	
	}
	
	public int getW(){
		return sprites.getCurrentSprite().length ; 
	}
	
	public int getH(){
		return sprites.getCurrentSprite().length ; 
	}
	
	public short[][][]getCurrentPic(){
		return sprites.getCurrentSprite() ; 
	}
	
	public void updateMovement(PlayerControl pc){
		int[] mvmt = pc.get2dDirectionVector() ;
		sprites.updateCurrent(mvmt);
		moveStep(mvmt[0]*4,mvmt[1]*4) ; 
	}

	public void moveStep(double xIncr, double yIncr) {
		this.prevXLoc = xloc ; 
		this.prevYLoc = yloc ; 
		this.xloc += xIncr ; 
		this.yloc += yIncr ;
	}

	public void moveJump(int newX, int newY) {
		this.xloc = newX ; 
		this.yloc = newY ; 
	}
	public void attack(int attackCode){
		
		System.out.println("attacking " + attackCode) ; 
		
	}
	public int getAttackCode(){
		return currentAttack ; 
	}

	public void haltAttack() {
		attacking = false ;		
	}
	
}
