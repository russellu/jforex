package improc;

public class Viewport {

	int xloc = 0 ;
	int yloc = 0 ; 
	int xspan = 400 ; 
	int yspan = 400 ;
	player pfix = null ; 
	
	public Viewport(){
		
		
	}
	
	public void move(int xincr, int yincr){
		int newx = xloc + xincr ;  
		int newy = yloc + yincr ; 
		if(newx >=0 && newy >= 0 && newx < 5000-xspan && newy<5000-yspan){
			this.xloc = newx ; 
			this.yloc = newy ; 
		}
		
		
	}
	public void updateZoom(int xspan, int yspan){
		this.xspan = xspan ; 
		this.yspan = yspan ; 
		
	}
	
	public void randomUpdateLoc(int xrange,int yrange){
		xloc = (int)(Math.random()*xrange) ; 
		yloc =(int)( Math.random()*yrange) ;
		//System.out.println("new xloc = " + xloc) ; 
		
	}
	
	public void fixate(player p){
		pfix = p ; 
	}
	
	public void update(){
		double xbound1 = xloc ; 
		double xbound2 = xloc + xspan ; 
		double ybound1 = yloc ; 
		double ybound2 = yloc + yspan ; 
		
		
		if(pfix != null){
			xloc = (int)(pfix.x-xspan/2) ; 
			yloc = (int)(pfix.y-xspan/2) ; 
			
			
			
		}
		else return ; 
	}
	
	
}
