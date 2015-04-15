package genesis;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import sprites.PNG2Short;
import improc.myframe;

@SuppressWarnings("serial")
public class PlayWater extends myframe{

	ArrayList<ArrayList<short[][][]>>waters = new ArrayList<ArrayList<short[][][]>>() ; 
	ArrayList<DirectionArrow> arrows = MapObjectLoader.loadArrows("C:\\Users\\Acer\\Pictures\\improc\\maps\\thickangles.txt") ; 
	short[][][]currentwater  ;
	short[][]pixMap ; 
	short[][]watermask ; 
	double[][][]finalArr ; // the final viewing array where all the scenery and player layers are combined.
	int texcount = 0 ; 
	BufferedImage tempBim = null ; 
	
	public PlayWater(){
		super() ; 
		double pi = Math.PI ;
		double[] allowedAngles = {0,(7*pi)/4,(3*pi)/2,(5*pi)/4,pi,pi,(3*pi)/4,pi/2,pi/4} ;
		for(double d:allowedAngles){
			waters.add(Get3dTexture.getRotated3dRGB("",d)) ; 
		}
		String input = "C:\\Users\\Acer\\Pictures\\improc\\maps\\defaultmask.png" ; 
		String maskInput= "C:\\Users\\Acer\\Pictures\\improc\\maps\\defaultmask.png" ;
		watermask = PNG2Short.gray2Short(maskInput) ; 
		currentwater = waters.get(0).get(0) ; 
		pixMap = PNG2Short.gray2Short(input) ;
		finalArr = new double[pixMap.length][pixMap[0].length][4] ; 
		tempBim = new BufferedImage(pixMap.length,pixMap[0].length,BufferedImage.TYPE_INT_ARGB) ; 
	}
	
	public void blendAll(){ // blend the textures with the scenery and the transparency arrays
		
		
		
		
		
	}
	
	public void render(){

		WritableRaster rast = tempBim.getRaster() ; 
		int viewW = tempBim.getWidth() ; 
		int viewH = tempBim.getHeight() ; 
		for(int a=0;a<arrows.size();a++){
			DirectionArrow current = arrows.get(a) ; 
			currentwater = waters.get( current.angleIndex).get(texcount%49) ; 
			int cx = current.xloc ; 
			int cy = current.yloc ;		
			if(cx < viewW-145 && cy < viewH-145)			
			for(int i=cx;i<cx+currentwater.length;i++)
				for(int j=cy;j<cy+currentwater[0].length;j++){
					if(currentwater[i-cx][j-cy][2]!=0){
								rast.setSample(i, j, 0, currentwater[i-cx][j-cy][0]);				
								rast.setSample(i, j, 1, currentwater[i-cx][j-cy][1]);
								rast.setSample(i, j, 2, currentwater[i-cx][j-cy][2]);
								rast.setSample(i, j, 3,watermask[i][j]*currentwater[i-cx][j-cy][2]);
					}
				}			
		}		
		texcount ++ ; ;
		swapBuffers() ; 
	}
	
	public void swapBuffers(){
		g2 = bim.createGraphics() ; 
		g2.drawImage(tempBim, 0, 0, null) ; 
//		try{Thread.sleep(2);}catch(Exception e){}

		
	}
	
	public void run(){
		double startT = System.nanoTime() ; 
		int fcount = 0 ;
		while(true){
			double elapsed = (System.nanoTime()-startT)/1000000000 ; 
			render() ; 
			repaint() ; 
		System.out.println("fps = " + ++fcount/elapsed) ; 
			
			
		}
		
		
		
	}
	
	
	public static void main(String[]args){
		
		PlayWater pw = new PlayWater() ; 
		new Thread(pw).start(); 
		
		
		
	}
}
