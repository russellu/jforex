package players;
import physics.Occupying;
import physics.PhysicalReality;
import physics.PhysicsFunctions;
import sprites.SpriteGetter;
import improc.SpriteHolder;


/**
 * ADD RESET BUTTON
 * extension of player class...shows the aura of the player which reflects it's current state...
 * blue is when the player is at it's least vulnerable, ie, not attacking (if you're attacking, you can't
 * dodge or block...red is when the player is most vulnerable. maybe use JET heatmap to indicate this?
 * 
 * the %chance of a succesful block is highest when you are least vulnerable ie most blue 
 * 
 * players can block if you are in the nearest neighbourhood of their current orientation otherwise they must turn (Takes time)
 * continuous time, where the player's react but might seem turn based due to discrete nature of environment.
 * 
 * line of sight for sneak attacks...sneaking 
 * 
 * projectile deflection physics
 * 
 * trigger the dodge automatically, but have it on a refresh timer so that you can't dodge everything
 * also orientation matters, if you aren't oriented to whats coming yo cannot dodge it
 * 
 * @author russ
 *
 */

public class Alex extends Player implements Occupying{

	SpriteHolder walkingSprites ; 
	SpriteHolder slashingSprites ; 
	SpriteHolder throwingSprites ; //to do
	SpriteHolder twirlingAxe ; //to do
	SpriteHolder idleSprites ; 
	SpriteHolder auraSprites ; 
	short[][][] currentSprite ; 
	short[][][] previousSprite ;
	int attackAnimationTimer = 0 ;
	double mass = 5000;
	PhysicalReality world ; 
	int immunity = -1 ; 
	int immunityCount = -1 ; 
	
	
		
	public Alex(SpriteHolder sprites, PhysicalReality world, int xloc, int yloc,int ID) {
		super(sprites, xloc, yloc,ID);
		this.world = world ; 
		setupSprites() ; 
		
	}
	
	public Alex(PhysicalReality world,int xloc, int yloc,int ID) {
		this.ID = ID ; 
		this.world = world ; 
		this.xloc = xloc ; 
		this.yloc = yloc ; 
		setupSprites() ; 
		
	}
	
	public boolean occupies(int x, int y) {
		if(x<xloc||y<yloc||x>xloc+currentSprite.length||y>yloc+currentSprite[0].length) //if outside of our bounding box, return false
			return false;
		else if (currentSprite[(int)(x-xloc)][(int)(y-yloc)][3]!=0) // within bounding box and occupied (check only alpha channel (no alpha=> invisible)
			return true ; 
		else return false ; 
		
		
	}
	
	public void velocityUpdate(){
		
		
	}
	
	public void identifierUpdate(){

		
	}
	
	public void updateMovement(PlayerControl pc){
		int[] mvmt = pc.get2dDirectionVector() ;

		previousSprite = currentSprite ; 		
		auraSprites.updateCurrent(mvmt);
		
		if(mvmt[0]==0 && mvmt[1]==0 && currentAttack == NONE){ //not moving or attacking
			idleSprites.updateCurrent(mvmt);
			currentSprite = idleSprites.getCurrentSprite() ; 
			idle = true ; moving = false ; attacking = false ; 
			xVel = 0 ; 
			yVel = 0 ; 
		}
		else if(currentAttack == NONE){ // moving
			walkingSprites.updateCurrent(mvmt);
			idleSprites.updateCurrent(mvmt);
			slashingSprites.updateCurrent(mvmt);	
			xVel = (double)mvmt[0]*5 ;
			yVel = (double)mvmt[1]*5 ; 
			currentSprite = walkingSprites.getCurrentSprite() ;

			idle = false ; moving = true ; attacking = false ;
		}
		else if(currentAttack == SLASH){ //attacking 
			
			slashingSprites.updateCurrent(mvmt);	
			currentSprite = slashingSprites.getCurrentSprite() ;
		//	idle = false ; moving = false ; attacking = true ; 
			if( ++attackAnimationTimer > slashingSprites.allFrames.size() && !attacking){
				currentAttack = NONE ; 
				attackAnimationTimer = 0 ; 
			}
		}
		
		checkCollisions();

	//change some aura colors
		if(immunityCount > 0){
			redScale = 2 ;
			greenScale = 0 ;
			blueScale = 0 ;
		}
		else if(attacking){
			redScale = 1 ; 
			greenScale = 2*((double)(slashingSprites.timeCounter%slashingSprites.timeMod)/(double)slashingSprites.timeMod)  ;
			blueScale = .25 ; 			
		}
		else if(moving){
			redScale = 0.0 ; 
			greenScale = 0.8 ; 
			blueScale = 0.8 ; 
		}
		else if(idle){
			redScale = 0.5 ;
			greenScale = 0.5 ;
			blueScale = 1 ; 
		}
		
		finalVelocityUpdate() ; 
	}
	
	public short[][][]getCurrentPic(){
		return currentSprite ; 
	}
	
	public void setupSprites(){
		String slashingSpritesPath = "C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexAttackingProc\\" ; 
		slashingSprites = new SpriteHolder(SpriteGetter.get(slashingSpritesPath)) ; 
		
		String walkingSpritesPath = "C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexWalking2Proc\\" ; 
		walkingSprites = new SpriteHolder(SpriteGetter.get(walkingSpritesPath)) ; 
		
		String idleSpritesPath = "C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexIdleProc\\" ; 
		idleSprites = new SpriteHolder(SpriteGetter.get(idleSpritesPath)) ; 

		String auraSpritesPath = "C:\\Users\\Acer\\Pictures\\improc\\auras\\" ;
		auraSprites = new SpriteHolder(SpriteGetter.get(auraSpritesPath)) ; 
	}
	
	public short[][][] getCurrentAura(){
		return auraSprites.getCurrentSprite() ; 
		
	}
	
	public void attack(int attackCode){
		//System.out.println("alex attacking") ; 
		currentAttack = SLASH ; 
		attacking = true ; 
		
	}
	
	public void velocityUpdate(double xIncr, double yIncr) {
		this.xVel = xIncr ; 
		this.yVel = yIncr ; 
	}

	public boolean checkCollisions() {

		immunityCount -- ;
		if(immunityCount < 0)
			immunity = -1 ; 
		
		int[][] idSpace = world.getIdentifiers();
		
		double xpos = (double)this.xloc ; 
		double ypos = (double)this.yloc ; 
		double width = this.currentSprite.length ;
		double height = this.currentSprite[0].length ;
		
	/*	
		if((int)Math.floor(xpos) <= 0 || (int)Math.ceil(xpos+width) >= idSpace.length){
		//	this.velocityUpdate(-xVel, yVel);
			return true ;
			
		}
		else if((int)Math.floor(ypos) <= 0 || (int)Math.ceil(ypos+height) >= idSpace[0].length){
		//	this.velocityUpdate(xVel, -yVel);
			return true ; 
		}		
		
	*/	
		
		for(int i=(int)Math.floor(xpos);i<Math.ceil(xpos+width);i++)
			for(int j=(int)Math.floor(ypos);j<Math.ceil(ypos+height);j++){
				if(
						!(i <= 0 || i >= idSpace[0].length)&&
						!(j <= 0 || j >= idSpace.length) &&
						idSpace[i][j] != 0 &&
						immunity != idSpace[i][j] &&
						idSpace[i][j] != ID &&
						currentSprite[(int)(i-xpos)][(int)(j-ypos)][0]!=0 
									
					){// if the pixel isn't occupied by this body
				//	System.out.println("collision detected " + idSpace[i][j] + " this ID = " + this.ID) ; 
					Occupying oc = world.getBodies().get(idSpace[i][j]) ; 
					double[][] v1v2 = PhysicsFunctions.calculateElasticCollision(this,oc) ; 
					//oc.checkCollisions() ;
		
					oc.velocityUpdate(v1v2[1][0], v1v2[1][1]) ;		
				//	System.out.println("acting on oc, x = " +v1v2[1][0]) ; 
					this.velocityUpdate(v1v2[0][0], v1v2[0][1]) ;
					this.xloc = prevXLoc ;
					this.yloc = prevYLoc ; 
					immunity = idSpace[i][j] ; 
					immunityCount = 3 ; 
					return true ; 
				}
			}
		
		return false ; 
	}
	
	public double getXPos(){
		return this.xloc ; 
	}
	public double getYPos(){
		return this.yloc ; 
	}
	
	public void finalVelocityUpdate(){
		
		double newX = xloc + xVel ;
		double newY = yloc + yVel ; 
		
	//	if(newX > 0 && newY > 0 && newX+currentSprite.length < world.getIdentifiers().length && newY+currentSprite[0].length < world.getIdentifiers()[0].length){
			this.prevXLoc = xloc ; 
			this.prevYLoc = yloc ; 
			this.xloc = (newX) ; 
			this.yloc = (newY) ;
		//	System.out.println("making new stop") ; 
	/*	}
		else {
			System.out.println("setting to prev x = " + prevXLoc + " newx = " +newX + 
					" y = " + prevYLoc + " newy = " + newY + 
					" newY += " + (newY+currentSprite[0].length) + " newX += " + (newX+currentSprite.length) + 
					" w = " + world.getIdentifiers().length + " h = " + world.getIdentifiers()[0].length + 
					" xvel = " + xVel + " yVel = " + yVel + 
					" s w =" + currentSprite.length + " s h = " + currentSprite[0].length
					
					) ; 
			this.xloc = prevXLoc ; 
			this.yloc = prevYLoc ; 
			
		}*/
	}
	
	
	public double getMass(){
		return mass  ; 
	}

	public double[] getVelocity(){
		double[] d = {xVel, yVel} ;
		return d ; 
	}
	
	public void actedUpon() {

		checkCollisions() ; 
	
	}

	public void erasePrevID(){
		if(previousSprite != null)
		for(int i=(int)prevXLoc;i<prevXLoc+previousSprite.length;i++)
			for(int j=(int)prevYLoc;j<prevYLoc+previousSprite[0].length;j++)
			if(i>0&&j>0&&i<world.getIdentifiers().length&&j<world.getIdentifiers()[0].length){				
				if(world.getIdentifiers()[i][j]==ID)
					world.getIdentifiers()[i][j] = 0 ; 			
			}		
	}
	
	public void drawSelf(double[][][] world, int[][] identifier) {
		int currentX = (int)getX() ;
		int currentY = (int)getY() ;
		
	//	System.out.println("getX = " + getX() + " getY = " + getY()) ; 
		
		erasePrevID() ; 
		
		short[][][] currentPic = getCurrentPic() ; 
		short[][][] currentAura = getCurrentAura() ;
		int auraW = currentAura.length ; 
		int auraH = currentAura[0].length ; 
		int currentW = currentPic.length ; 
		int currentH = currentPic[0].length ; 
		
		for(int i=currentX; i<currentX+currentW;i++)
			for(int j=currentY;j<currentY+currentH;j++)
				if(i>0&&j>0&&i<world.length&&j<world[0].length)
					if(currentPic[i-currentX][j-currentY][0]!=0){
						world[i][j][0] = currentPic[i-currentX][j-currentY][0] ;  				
						world[i][j][1] = currentPic[i-currentX][j-currentY][1] ;  
						world[i][j][2] = currentPic[i-currentX][j-currentY][2] ;  
						world[i][j][3] = 255 ; //currentPic[i-currentX][j-currentY][3] ;  						
						identifier[i][j] = 8 ; //update the identifier layer
					}
					else if(i-currentX < auraW && j-currentY < auraH && currentAura[i-currentX][j-currentY][0] !=0){
						world[i][j][0] =world[i][j][0]/2+  currentAura[i-currentX][j-currentY][0]*redScale ; 				
						world[i][j][1] =world[i][j][1]/2+ currentAura[i-currentX][j-currentY][1]*greenScale ; 
						world[i][j][2] =world[i][j][2]/2+ currentAura[i-currentX][j-currentY][2]*blueScale ; 
						world[i][j][3] =255 ; 
					}
	}
}