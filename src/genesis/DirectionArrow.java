package genesis;

import java.awt.image.WritableRaster;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DirectionArrow {
	
	int xloc ; 
	int yloc ; 
	
	double angle ; 
	int angleIndex ; 
	
	int timeOffset = (int)(Math.random()*30) ; 
	
	NumberFormat format = new DecimalFormat("#0.00") ; 
	double pi = Math.PI ;
	double[] allowedAngles = {0,pi/4,pi/2,(3*pi)/4,pi,(5*pi)/4,(6*pi)/4,(7*pi)/4,2*pi} ;


	public DirectionArrow(int x, int y, double angle){
		this.xloc = x ; 
		this.yloc = y ;
		this.angle = Double.parseDouble(format.format(angle)) ; 
		System.out.println("angle = " + this.angle) ; 
		for(int i=0;i<allowedAngles.length;i++)
			if(angle == Double.parseDouble(format.format(allowedAngles[i])))  
				angleIndex = i ; 
		//System.out.println("creating new direction arrow x = " + x + " y = " + y + " angle = " + angle) ;
	}
	
	public void drawSelf(short[][][] arr){		
		double xstep = Math.cos(angle) ; 
		double ystep = Math.sin(angle) ; 
		for(int incr=0;incr<20;incr++){
			int addx = (int)(incr*xstep) ;
			int addy = (int)(incr*ystep) ; 
			
			if(incr < 15){
				arr[yloc+addy][xloc+addx][0] = 255 ; 
				arr[yloc+addy][xloc+addx][1] = 0 ; 
				arr[yloc+addy][xloc+addx][2] = 0 ; 
				arr[yloc+addy][xloc+addx][3] = 255 ; 
			}
			else{
				arr[yloc+addy][xloc+addx][0] = 0 ; 
				arr[yloc+addy][xloc+addx][1] = 0 ; 
				arr[yloc+addy][xloc+addx][2] = 255 ; 
				arr[yloc+addy][xloc+addx][3] = 255 ; 
			}
	

		}
	}
	
	public void drawTempSelf(WritableRaster rast,ViewControl vc){		
		double xstep = Math.cos(angle) ; 
		double ystep = Math.sin(angle) ; 
		for(int incr=0;incr<20;incr++){
			int addx = (int)(incr*xstep) ;
			int addy = (int)(incr*ystep) ; 
			if((xloc)+addx > 0 && (yloc) +addy > 0 && (xloc)+addx < vc.width && (yloc) +addy < vc.height){
				
				if(incr < 15){
					rast.setSample( (xloc)+addx,(yloc) +addy,0,125) ; 
					rast.setSample( (xloc)+addx,(yloc) +addy,1,0) ; 
					rast.setSample( (xloc)+addx,(yloc) +addy,2,0) ; 
					rast.setSample( (xloc)+addx,(yloc) +addy,3,255) ;
				}
				else{
					rast.setSample( (xloc)+addx,(yloc) +addy,0,0) ; 
					rast.setSample( (xloc)+addx,(yloc) +addy,1,0) ; 
					rast.setSample( (xloc)+addx,(yloc) +addy,2,125) ; 
					rast.setSample( (xloc)+addx,(yloc) +addy,3,255) ;				
				}
			}
		}
	}
	

	
}
