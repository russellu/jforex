package genesis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import sprites.PNG2Short;
import improc.myframe;

/**
 * ideas to improve water: more directions, smaller patches : have a more continuous stream...could take longer to draw as well, though
 * it would be much faster to have a general water array where all the patches have been blended during the level loading process
 * and then just a matter of accessing the index...would need to wait until the level was loaded and then use the mask to create the final
 * water array
 * 
 * 
 * @author russ
 *
 */

@SuppressWarnings("serial")
public class TerrainTester extends myframe{

	ArrayList<ArrayList<short[][][]>>waters = new ArrayList<ArrayList<short[][][]>>() ; 
	ArrayList<DirectionArrow> arrows = MapObjectLoader.loadArrows("C:\\Users\\Acer\\Pictures\\improc\\maps\\thickangles.txt") ; 
	short[][][]currentwater  ;
	short[][][]map ; 
	short[][]pixMap ; 
	short[][]riverbank ; 
	short[][] riverbank2 ; 
	short[][]watermask ; 
	double[][][]finalArr ; // the final viewing array where all the scenery and player layers are combined.
	int texcount = 0 ; 
	BufferedImage tempBim = null ; 
	
	public TerrainTester(){
		super() ; 
		String input = "C:\\Users\\Acer\\Pictures\\improc\\maps\\defaultmask.png" ; 
		pixMap = PNG2Short.gray2Short(input) ;
		jf.setPreferredSize(new Dimension(pixMap.length+50,pixMap[0].length+50));
		jf.pack() ;
		
		String rgbMapPath = "C:\\Users\\Acer\\Pictures\\improc\\maps\\dirt.png" ; 
		map = PNG2Short.RGB2SHORT(rgbMapPath) ; 
		
		String riverbankPath = "C:\\Users\\Acer\\Pictures\\improc\\maps\\riverbank.png" ; 
		riverbank = PNG2Short.gray2Short(riverbankPath) ;
		riverbank2 = riverbank ; 
		
		double pi = Math.PI ;
		double[] allowedAngles = {0,(7*pi)/4,(3*pi)/2,(5*pi)/4,pi,pi,(3*pi)/4,pi/2,pi/4} ;
		for(double d:allowedAngles){
			waters.add(Get3dTexture.getRotated3dRGB("",d)) ; 
		}
		String maskInput= "C:\\Users\\Acer\\Pictures\\improc\\maps\\defaultmask.png" ;
		watermask = PNG2Short.gray2Short(maskInput) ; 
		currentwater = waters.get(0).get(0) ; 
		
		int maph = pixMap[0].length ; 
		int mapw = pixMap.length ; 
		
		finalArr = new double[mapw][maph][4] ; 
		tempBim = new BufferedImage(mapw,maph,BufferedImage.TYPE_INT_ARGB) ; 

	}
	
	public void blendAll(){ // blend the textures with the scenery and the transparency arrays
		

		
		/**
		 * set up the terrain
		 * 
		 */
		
		for(int i=0;i<tempBim.getWidth();i++)
			for(int j=0;j<tempBim.getHeight();j++){
				finalArr[i][j][0] = map[i][j][0] * (1-(double)riverbank[i][j]/255) ; 				
				finalArr[i][j][1] = map[i][j][1]* (1-(double)riverbank[i][j]/255) ; 
				finalArr[i][j][2] = map[i][j][2]* (1-(double)riverbank[i][j]/255) ; 
				finalArr[i][j][3] = 255 ; 
			}
		
		
		
		/**
		 * set up the arrows
		 */
		
		boolean[][] taken = new boolean[tempBim.getWidth()][tempBim.getHeight()] ; 
		for(int a=0;a<arrows.size();a++){
			DirectionArrow current = arrows.get(a) ; 
			int cx = current.xloc ;
			int cy = current.yloc ; 
			currentwater = waters.get( current.angleIndex).get((texcount+current.timeOffset)%49) ; 
			if(cx<pixMap.length-50 && cy<pixMap[0].length-50)
			for(int i=cx;i<cx+currentwater.length;i++)
				for(int j=cy;j<cy+currentwater[0].length;j++){
					if(currentwater[i-cx][j-cy][0]!=0 && watermask[i][j] == 255&& !taken[i][j]){ //&& !taken[i][j] 		
						finalArr[i][j][3] = 255 ; 
						finalArr[i][j][0] = finalArr[i][j][0] + currentwater[i-cx][j-cy][0]*(1-riverbank[i][j]/255) ;
						finalArr[i][j][1] = finalArr[i][j][1] + currentwater[i-cx][j-cy][1];//*((double)riverbank[i][j]/255) ;
						finalArr[i][j][2] = finalArr[i][j][2] + currentwater[i-cx][j-cy][2]*((double)riverbank[i][j]/255) ;
						taken[i][j] = true ; 
					}
				}		
		}
	}
	
	public void render(){

		WritableRaster rast = tempBim.getRaster() ; 
		System.out.println(" width = " + tempBim.getWidth() +" height = " + tempBim.getHeight()); 
		for(int i=0;i<tempBim.getWidth();i++)
			for(int j=0;j<tempBim.getHeight();j++){
				rast.setSample(i, j, 0, finalArr[i][j][0]);				
				rast.setSample(i, j, 1, finalArr[i][j][1]);
				rast.setSample(i, j, 2, finalArr[i][j][2]);
				rast.setSample(i, j, 3, finalArr[i][j][3]);
			}
		//g2.setColor(Color.RED);
		texcount ++ ; ;
		swapBuffers() ; 
	}
	
	public void swapBuffers(){
		g2 = bim.createGraphics() ; 
		g2.drawImage(tempBim, 0, 0, null) ; 
//		try{Thread.sleep(2);}catch(Exception e){}
		bim = tempBim ; 
	}
	
	public void run(){
		double startT = System.nanoTime() ; 
		int fcount = 0 ;
		while(true){
			double elapsed = (System.nanoTime()-startT)/1000000000 ; 
			blendAll() ; 
			render() ; 
			repaint() ; 
		System.out.println("fps = " + ++fcount/elapsed) ; 
			
			
		}
	}
	
	
	public static void main(String[]args){
		
		TerrainTester tt = new TerrainTester() ; 
		new Thread(tt).start(); 
		
		
		
	}
}
