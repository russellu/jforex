package improc;

import java.util.ArrayList;

public class SpriteHolder {

	public ArrayList<ArrayList<short[][][]>> allFrames ; 
	public int time ; 
	public int angleIndex ; 
	public short[][][] currentSprite ;
	public int timeCounter = 0 ;
	public int timeMod ; 
	static double pi = Math.PI ;
	static double[] allowedAngles = {0,pi/4,pi/2,(3*pi)/4,pi,(5*pi)/4,(6*pi)/4,(7*pi)/4,2*pi} ;
	
	int prevDirection = 0 ; 

	public SpriteHolder(ArrayList<ArrayList<short[][][]>> allFrames){
		this.allFrames = allFrames ; 
		this.timeMod = allFrames.size() ; 
		time = 0 ; 
		angleIndex = 0 ; 
		currentSprite = allFrames.get(0).get(0) ; 
	}
	
	public void updatePrevDirection(int angleIndex){ //only update if moved
		prevDirection = angleIndex ; 
		//System.out.println("updating previous direction") ; 

	}
	
	public void updateCurrent(int[] newDirection){
		timeCounter ++ ; 
		boolean update = true ; 
		
		if(newDirection[0]==0&&newDirection[1]==0){
			update = false ; 			
		}
		else if(newDirection[0]==-1&&newDirection[1]==-1)
			angleIndex = 7 ; 
		else if(newDirection[0]==0&&newDirection[1]==-1)
			angleIndex = 6 ; 
		else if(newDirection[0]==1&&newDirection[1]==-1)
			angleIndex = 5 ; 
		else if(newDirection[0]==1&&newDirection[1]==0)
			angleIndex = 4 ; 
		else if(newDirection[0]==1&&newDirection[1]==1)
			angleIndex = 3 ; 
		else if(newDirection[0]==0&&newDirection[1]==1)
			angleIndex = 2 ; 
		else if(newDirection[0]==-1&&newDirection[1]==1)
			angleIndex = 1 ; 
		else if(newDirection[0]==-1&&newDirection[1]==0)
			angleIndex = 0 ; 
		
		// update the sprite => movement => vulnerable, change the aura.
		if(update){
			
			updatePrevDirection(angleIndex) ; 
		}
		this.currentSprite = allFrames.get(timeCounter%timeMod).get(prevDirection) ; 
		
	}
	
	public short[][][] getCurrentSprite(){
		return currentSprite ; 
	}
		
}
