package improc;

import java.util.ArrayList;

public class Wolf {
	
	double xprev ;
	double yprev ; 
	double xpos ; 
	double ypos ; 
	short[][][] currentPelt ; 
	int seqcount = 0 ; 
	int nskins = 101;
	int totalmovecount = 0 ; 
	double starttime  = System.nanoTime(); 
	
	ArrayList<ArrayList<short[][][]>> pelts = new ArrayList<ArrayList<short[][][]>>() ; 
	
	public Wolf(int x, int y){
		this.xpos = x ; 
		this.ypos = y ; 
		
		
		
		
		
	}
	
	public void setPelt(){
		
		
	}
	
	public void getPelt(String location){
		pelts = PlayerTextures.getMovementTextures(location,101) ; 
		currentPelt = pelts.get(0).get(0) ; 
		
	}
	
	
	public void move(double xincr, double yincr){

		if(!allowed(xincr,yincr))
			return ; 
		xprev = this.xpos ; 
		yprev = this.ypos ; 
		this.xpos = xprev + xincr ;
		this.ypos = yprev + yincr ; 
		
		double elapsed = (System.nanoTime() - starttime)/1000000 ;
		if(elapsed > 20){
			starttime = System.nanoTime(); 
			totalmovecount++ ; 
		}	
		seqcount = totalmovecount % nskins ; 
		//System.out.println("elapsed time = " + (System.nanoTime()-starttime)/1000000) ; 
		setMovementSkin() ; 

	}		
			
	public boolean allowed(double xincr, double yincr){
		double newx = xpos + xincr ;
		double newy = ypos + yincr ; 
		//System.out.println("newx = " + (newx + currentskin.length) + " newy = " + (newy + currentskin[0].length)) ; 
		if(newx <0 || newy <0 || newx + currentPelt.length >= 5000 ||newy + currentPelt[0].length >= 5000){
			//System.out.println("not allowed, staying at x = " + (this.x +currentskin.length) + " y = " + (this.y + currentskin[0].length)) ; 
			return false ;
		}
		
		//for(int i=(int)newx;i<(int)newx+currentskin.length;i++)
		//	for(int j=(int)newy;j<(int)newy+currentskin[0].length;j++)
		//		if(occupied[i][j] )//&& currentskin[i-(int)newx][j-(int)newy][3] != 0)
		//			return false ;	
		
		return true ; 
	}
	
	
	public void setMovementSkin(){ // set the skin to the current movement direction
		double xdir = xpos-xprev ; 
		double ydir = ypos-yprev ; 
		System.out.println("xdir = " + xdir + " ydir = " + ydir ) ; 
		if(xdir >0 && ydir == 0){
			currentPelt = pelts.get(0).get(seqcount) ; 
		}
		else if(xdir <0 &&ydir==0){
			currentPelt = pelts.get(4).get(seqcount) ; 
		}	
		else if(xdir ==0 && ydir > 0){
			currentPelt = pelts.get(2).get(seqcount) ; 
		}
		else if(xdir ==0 && ydir < 0 ){
			currentPelt = pelts.get(6).get(seqcount) ; 
		}
		else if(xdir >0 && ydir > 0){
			currentPelt = pelts.get(1).get(seqcount) ; 		
		}
		else if(xdir >0 && ydir < 0){
			currentPelt = pelts.get(7).get(seqcount) ; 
		}
		else if(xdir <0 && ydir < 0){
			currentPelt = pelts.get(6).get(seqcount) ; 
		}
		else if(xdir <0 && ydir > 0){
			currentPelt = pelts.get(3).get(seqcount) ; 
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[]args){
		
		new Wolf(0,0).getPelt("C:\\Users\\Acer\\Pictures\\improc\\animals\\");
		
		
		
		
	}
	
	
	
	
	

}
