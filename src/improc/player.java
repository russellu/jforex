package improc;

import java.util.ArrayList;

public class player {

	double[][][]currentskin = null ; 
	DirectionSkins[] dskins = new DirectionSkins[7]; 
	boolean[][] occupied ;
	double x = 0 ;
	double y = 0 ;
	double xprev = 0 ; 
	double yprev = 0 ; 
	double xvel = 0 ;
	double yvel = 0 ;
	int seqcount = 0 ; 
	int nskins = 7 ;
	int totalmovecount = 0 ; 
	double starttime  = System.nanoTime(); 
			
	
	public player(double x,double y,boolean[][]occupied){
		this.occupied = occupied ;  
		initSkin() ; 
		this.x = x ; 
		this.y = y ; 
		this.xprev = x-1 ;
		this.yprev = y-1 ;
		initSkins() ; 
	}
	
	public void initSkins(){
		for(int i=0;i<nskins;i++){
			dskins[i] = new DirectionSkins() ; 	
		}	
	}
	
	
	public void passiveUpdate(){ // for when the user is not pressing the key
		move(xvel,yvel) ; 
		
		
	}
	
	public void move(double xincr, double yincr){

		if(!allowed(xincr,yincr))
			return ; 
		xprev = this.x ; 
		yprev = this.y ; 
		this.x = xprev + xincr ;
		this.y = yprev + yincr ; 
		double elapsed = (System.nanoTime() - starttime)/1000000 ;
		if(elapsed > 90){
			starttime = System.nanoTime(); 
			totalmovecount++ ; 
		}	
		seqcount = totalmovecount % nskins ; 
		//System.out.println("elapsed time = " + (System.nanoTime()-starttime)/1000000) ; 
		setMovementSkin() ; 

	}		
			
	public boolean allowed(double xincr, double yincr){
		double newx = x + xincr ;
		double newy = y + yincr ; 
		//System.out.println("newx = " + (newx + currentskin.length) + " newy = " + (newy + currentskin[0].length)) ; 
		if(newx <0 || newy <0 || newx + currentskin.length >= occupied.length ||newy + currentskin[0].length >= occupied[0].length){
			//System.out.println("not allowed, staying at x = " + (this.x +currentskin.length) + " y = " + (this.y + currentskin[0].length)) ; 
			return false ;
		}
		
		for(int i=(int)newx;i<(int)newx+currentskin.length;i++)
			for(int j=(int)newy;j<(int)newy+currentskin[0].length;j++)
				if(occupied[i][j] )//&& currentskin[i-(int)newx][j-(int)newy][3] != 0)
					return false ;	
		
		return true ; 
	}
	
	public void addMovementSkin(double[][][]newskin,int direction,int seq){
		dskins[seq].addDirectionSkin(newskin,direction) ; 		
	}
	
	public void setMovementSkin(){ // set the skin to the current movement direction
		double xdir = x-xprev ; 
		double ydir = y-yprev ; 
		System.out.println("xdir = " + xdir + " ydir = " + ydir ) ; 
		if(xdir >0 && ydir == 0){
			currentskin = dskins[seqcount].getDirectionSkin(270) ; 
		}
		else if(xdir <0 &&ydir==0){
			currentskin = dskins[seqcount].getDirectionSkin(90) ; 
		}	
		else if(xdir ==0 && ydir > 0){
			currentskin = dskins[seqcount].getDirectionSkin(180) ; 
		}
		else if(xdir ==0 && ydir < 0 ){
			currentskin = dskins[seqcount].getDirectionSkin(0) ; 
		}
		else if(xdir >0 && ydir > 0){
			currentskin = dskins[seqcount].getDirectionSkin(225) ; 		
		}
		else if(xdir >0 && ydir < 0){
			currentskin = dskins[seqcount].getDirectionSkin(315) ; 
		}
		else if(xdir <0 && ydir < 0){
			currentskin = dskins[seqcount].getDirectionSkin(45) ; 
		}
		else if(xdir <0 && ydir > 0){
			currentskin = dskins[seqcount].getDirectionSkin(135) ; 
		}
	}
	
	public void initSkin(){
		int skinw = 20  ;
		int skinh = 20  ; 
		currentskin = new double[skinw][skinh][4] ; 
		for(int i=0;i<skinw;i++)
			for(int j=0;j<skinh;j++){
				double[] currentpixel = {i*j*2.55,i*j*2.55,i*j*2.55,255} ;
				currentskin[i][j] = currentpixel ; 
			}	
	}
			
}
