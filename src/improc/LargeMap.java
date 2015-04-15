package improc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * large map - draw a large map without wasting memory
 * let say the level is 5000x5000 pixels. you want the map loaded to be small
 * maybe 500x500 pixels, and then you interpolate from there
 * 
 * should have many classes to get values for the interpolation...getDirt(), getRoad(), etc
 * they can just be randomized versions of a base color, forest floor for example (Darker under trees)
 * would just be dark green +/- a couple integers. 
 * 
 * matlab -> create the mapping from color (paint) to integer gray level which is mapped in java to a terrain
 * have a gray level image (masked) that gives the hard values for each block of the map, but then also have a 
 * smoother map which blends the two types of terrain so that there is overlap
 * 
 * how to make a nicer looking terrain/water? because right now it looks like ass...
 * have the color of the water proportional to it's distance from the shore?
 * keep the water mask but blend it with another translucency mask? already making too many large masks...
 * how many terrain masks do you need, at the end of the day? if you keep them small and load them using java 
 * persistence, 5-6+ should be manageable.
 * smooth the shores with a gaussian, load it separately as its own grayScaleImage..make a method
 * that saves maps separately as java objects so they can be loaded quicker for debugging
 * median filter the water map to get rid of bullshit paint edges
 * 
 * how to blend the terrain with the water? you want deeper water ie further away from shore to have less
 * translucency to the terrain underneath...will you have then a ground and a water map? load them separately
 * and then combine later via blending for faster real time rendering?
 * lets say you smooth the water map with a gaussian, how you have mostly zeros,some ones, and all in-between.
 * you want the in between values to represent something on the map
 * maybe just load the grass first, and then try to combine them later?
 * 
 * a bunch of sticks lying on the ground...rotate and superposition to get some nice random patterning for forest floor
 * 
 * dungeon keeper move the dirt to the river so you can cross...dirt is alive in the game. 
 * 
 * save the terrain as an array of indices mapped to color values? 
 * if you just use dlmwrite to save the matrix, what you get is alot of big matrices but speed
 *  
 *  have the ant workers collect gold
 *  
 *  what to work on tonight? terraforming the landscape...or directional water?
 *  directional water => color code river based on direction and then fill with sprite?
 *  this could be nice because you can save 360 small arrays one for each direction and then make the river in 
 *  the custom map generator. you can also change the speed..
 *  selecting and moving creatures, trees and landscapes...dialog? dialog comes last as does story and all that shite
 *  trees can be your first manipulatable terrain. have them become transparent when you move under them
 *  step1: load tree and random seed map - how to make a nice looking contiguous forest from independent trees?
 *  trees man shapes many sizes many rotations...take the color from a photograph, get the general shape off the internet
 *  order the trees according to height, and then draw them in that order. show the stump when the player is under the tree.
 *  
 *  composite sprites: how to have many different armors/weapons? just draw them over top, or pre-blend in the player
 *  pre-blending in the player so that the currentSkin always reflects the state of the payer's weaponry
 *  
 *  have another 5000x5000 short array for terrain modifications, draw it over when rendering the terrain?
 *  collisions need alot more work as well (seeing as they are non-existent at the moment).
 * 
 * tomorrow : new water, trees, and person sprites
 * 
 * @author russ
 *
 */
public class LargeMap extends myframe {
	
	short[][] world = null ;
	short[][] translMask = null ; 
	short[][][] grass = null ; 
	
	MapByte mb = new MapByte() ; 
	Viewport vp = new Viewport() ; 
	MapLoader maploader = new MapLoader() ; 
	ControlMovement cm = new ControlMovement() ;
	
	Wolf w = new Wolf(200,200) ; 
	
	MouseControl mc = new MouseControl(this) ; 
	ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>() ;
	
	BufferedImage viewPortIM = new BufferedImage(vp.xspan,vp.yspan,BufferedImage.TYPE_INT_ARGB) ; 
	double[][][] cview = new double[vp.xspan][vp.yspan][4] ;

	int x = 0 ; 
	double startTime = 0 ; 
	
	public LargeMap(){
		super() ; jf.setLocation(700,00) ; jf.setPreferredSize(new Dimension(500,500));jf.pack();
		initTerrain() ;
		initAnimals() ; 
		startTime = System.nanoTime() ; 
	}
	
	public void initAnimals(){
		w.getPelt("C:\\Users\\Acer\\Pictures\\improc\\animals\\") ; 
		
	}
	
	
	public void computePath(int x1,int y1,int x2, int y2){
		 System.out.println("Adding line x1 = " + x1 + " x2 = " + x2 + " y1 = " + y1 + " y2 = "+y2) ;
		lines.add(new Line2D.Double(x1,y1,x2,y2)) ; 
		
	}

	public void render(){
		mapBytes() ; 
		drawPlayers() ; 
		
		
		WritableRaster rast = viewPortIM.getRaster() ;
		int xcent = vp.xloc ; 
		int ycent = vp.yloc ; 
		for(int i=xcent;i<xcent + vp.xspan;i++)
			for(int j=ycent;j<ycent + vp.yspan;j++){
				rast.setSample(j-ycent, i-xcent, 0, cview[i-xcent][j-ycent][0]);
				rast.setSample(j-ycent, i-xcent, 1, cview[i-xcent][j-ycent][1]);
				rast.setSample(j-ycent, i-xcent, 2, cview[i-xcent][j-ycent][2]);
				rast.setSample(j-ycent, i-xcent, 3, cview[i-xcent][j-ycent][3]);			
			}
		
		
		bim = viewPortIM ; 
		g2 = viewPortIM.createGraphics() ; 
		g2.setColor(Color.RED);
		
		
		for(Line2D.Double l2d:lines){
			g2.draw(l2d);
			//g2.fill(new Rectangle2D.Double(0,0,400,400));
		//System.out.println("Drawing line from w = " + l2d.x1) ; 	
		}
		//vp.randomUpdateLoc(7000, 7000);
		x++ ; 
		//if(x%100==0)printfps() ; 
	}
	
	public void printfps(){
		System.out.println("fps = " + x/((System.nanoTime()-startTime)/1000000000)) ; 

	}
	public void initTerrain(){
		//for(int i=0;i<world.length;i++)
		//	for(int j=0;j<world[0].length;j++)
		//		world[i][j] = (byte)((int)(Math.random()*20)) ; 
		
		//world = maploader.getDLM("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\watermap.txt") ; 
		//translMask = maploader.getDLM("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\watertrans.txt") ;
		//grass = maploader.getDLM("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\grasstex.txt") ;

		world = txt2Mat.get2dImage("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\watermap.png") ; 
		translMask = txt2Mat.get2dImage("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\watertrans.png") ;
		//grass = txt2Mat.get2dImage("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\grasstex.png") ;
		//grass = txt2Mat.RGBPNG("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\mud2.png") ;
		grass = txt2Mat.buffRGB2short_separate("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\mud2") ;
	}
	
	public void drawPlayers(){
		int playerWidth = w.pelts.get(0).get(0).length ; 
		int playerHeight = w.pelts.get(0).get(0)[0].length ; 
		int xloc = (int)vp.xloc ; 
		int yloc = (int)vp.yloc ; 
		int playerxloc = (int)w.xpos ; 
		int playeryloc = (int)w.ypos ; 
		
		//check if player is in view
		// WHAT IS the player position relative to the viewWindow
		if(inView(w))
		for(int i=playerxloc;i<playerxloc+playerWidth;i++)
			for(int j=playeryloc;j<playeryloc+playerHeight;j++){
				if(i >= xloc && j >= yloc && w.currentPelt[i-playerxloc][j-playeryloc][0]!=0 && i-xloc > 0 && j-yloc > 0){
					cview[i-xloc][j-yloc][0] = w.currentPelt[i-playerxloc][j-playeryloc][0] ; 
					cview[i-xloc][j-yloc][1] = w.currentPelt[i-playerxloc][j-playeryloc][1] ; 
					cview[i-xloc][j-yloc][2] = w.currentPelt[i-playerxloc][j-playeryloc][2] ; 
				//	grass[i][j][0] = w.currentPelt[i-xloc][j-yloc][0] ;
					//grass[i][j][1] = w.currentPelt[i-xloc][j-yloc][1] ;
				//	grass[i][j][2]  = w.currentPelt[i-xloc][j-yloc][2] ;
				}
				
				
				
			}
	}
	
	
	public boolean inView(Wolf wolfie){
		if( wolfie.xpos >= vp.xloc-wolfie.currentPelt.length && 
			wolfie.ypos >= vp.yloc-wolfie.currentPelt[0].length && 
			wolfie.xpos < vp.xloc + vp.xspan && 
			wolfie.xpos < vp.yloc + vp.yspan){	
			//System.out.println("Wolfie is in view") ; 

			return true ; 		
		}
		else return false ; 
		
		
		
		
	}
	
	public void mapBytes(){

		int xcent = vp.xloc ; 
		int ycent = vp.yloc ; 
		//double[] temp = new double[4] ; 
		//System.out.println(" tmask length = " + translMask.length + " tmaskwidth = " + translMask[0].length) ; 
		double[] temp = new double[4] ;
		for(int i=xcent;i<xcent+vp.xspan;i++)
			for(int j=ycent;j<ycent+vp.yspan;j++){
				//if(i-xcent > 0 && j-ycent > 0 && i > 0 && j >0){
					//double[]temp = new double[4] ; 
					double alphafac = (double)translMask[i][j]/255.0 ;
					//cview[i-xcent][j-ycent][0] = 90*(1-alphafac)	;
					//cview[i-xcent][j-ycent][1] = 122*(1-alphafac) ;	
					//cview[i-xcent][j-ycent][2] = 0;
					//cview[i-xcent][j-ycent][3] = 255 ;
					
					cview[i-xcent][j-ycent][0] = grass[i][j][0];//*(1-alphafac) ; ///30 ;
					cview[i-xcent][j-ycent][1] =grass[i][j][1];//*(1-alphafac) ; //.6*grass[i][j][0]*(1-alphafac) ;	
					cview[i-xcent][j-ycent][2] = grass[i][j][2];//*(1-alphafac);//50 ; 
					cview[i-xcent][j-ycent][3] = 255 ;
					if(world[i][j]!=0){
						temp = mb.getStreamRGBA(world[i][j]) ;
						//System.out.println("temp[0] = " + temp[2]) ; 

							cview[i-xcent][j-ycent][0] += temp[0]*alphafac ;	
							cview[i-xcent][j-ycent][1] += temp[1]*alphafac ;	
							cview[i-xcent][j-ycent][2] += temp[2]*alphafac ;	
						//	cview[i-xcent][j-ycent][3] = 255 ;	
					}
					// keep all maps inside a single array of shorts?
			}
		
		mb.updateCounters();
		
	}
	
	public void updatePlayers(){
		
		
		
	}
	
	public void run(){
		double fps = 300; 
		double frameTimeMS = 1000/fps ; 
		while(true){
			double frameStartT = System.nanoTime() ;
			render() ; 
			repaint() ; 
			cm.update(vp);
			//cm.update(w);
			updatePlayers() ; 
			//double elapsed = (System.nanoTime()-frameStartT)/1000000 ;
			//if(elapsed < frameTimeMS)
				//try{Thread.sleep((int)(frameTimeMS - elapsed));}catch(Exception e){}
			//else 
				//try{Thread.sleep(5);}catch(Exception e){}	

		}
	}
	
	
	public void testRiver(){
		
	}
	
	
	public static void main(String[]args){
		new Thread(new LargeMap()).start() ; 
		
	}
	
	
	
}
