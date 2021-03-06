package improc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


/**
 * mygame
 * physics engine should deal separately with collions...based on nearestneighbourhood only for speed
 * 
 * start simple...blue vs red.
 * 
 * many ways to start at the bottom...AI, collisions, sprites, story, etc.
 * 
 * story: sonce upon a time, there was an evil king who was the hero's father. (Star wars)
 * you have to get the magic sword and kill the king. only then do you realize that he was
 * a puppet, and the real enemy is your mentor (baldur's gate) who knows all your strengths 
 * and weaknesses. you have to change your style to beat him. 
 * so basically, you start in a small village and venture forth at will, but with direction
 * from a mentor. 
 * village is surrounded by forest full of beasts (wolves, bears). they will kill you if 
 * you go alone or too weak. the territory is huge. you have to follow the road or you will
 * get lost. 
 * fog of war surround you at all times, but later you can get spells that will illuminate certain regions
 * and go out searching on their own. if you are a druid you can use trees to see. 
 * 
 * graphics: 3-4 layers of double, combine them into one and then form BufferedImage for paint.
 * 
 * combat animation: each item has its own 3d volume of its 2-d shape in various poses (ie at various angles)
 * each player is the same, when the player moves, you first update the square that it was on, and then
 * update the new square with the player's actual shape/colors. after you leave a zone, the effects of your
 * actions fade. 
 * 
 * you need a way of directly going from an artist's rendition to the actual game state.
 * 
 * you should be able to set the player running with a single keystroke, and then just wait and adjust
 * 
 * physics: check in vicinity of all currently moving players during updateState, if at boundary or 
 * near other object/player, stop
 * 
 * image derivative is the see through version. 
 * 
 * use heat maps from matlab to set tree density and whatnot. 
 * 
 * DONE: 3 layers, occupied array (For building collisions), simple player movement, loading textures
 * tomorrow: player textures (run, walk, attack), timing of refresh/draw rate, moving map, zoom?
 * autogenerated terrains with random heat maps, use heat maps to create vector for altitude (Gradient) 
 * so you can feign movement in the z direction
 * sprites: character movement: run sequence has 5-10 frames for a nice fluid motion...
 * 
 * workers chopping trees? tree sprites for shaking tree
 * 
 * rotate sprites in matlab, make part of player class
 * 
 * use sprite derivative to highlight when you pass mouse over it.
 * 
 * first character: peasant. abilities: chop wood, gather food, etc
 * walk animation 2-5 frames chop animation, run animation, wounded (limping)
 * bleeding, dead
 * peasant clothes
 * 
 * first map: training ground. one enemy and some resources. river and trees
 * 
 * the rotund ball composed of many ellipses. the composite sprite...weapons, armor
 * and underneath it all the stick figure. 
 * 
 * 
 * the map is a ginormous array of doubles. set the raster value of bim to a subsection of 
 * the array of doubles
 * 
 * the come from behind knockdown will be key when chasing down and slaughtering opponents, 
 * be sure to add a sprite for that as well 
 * 
 * color coding for modular sprites : left arm red, right arm green=> where to place the other
 * objects
 * 
 * how to create big levels? USING Huge arrays does not work because memory is out of control.
 * need a compact way to represent the level and then draw only the region in the current FOV. 
 * idea 1: store all items/players/terrain objects in an array, and draw them only when you come near
 * 		what about the ground itself? have a smaller hierarchical array of ground pixels ? what if you want
 * 		to draw rivers and roads and shit...
 * divide the region into patches...have a smaller, more abstract array that you reference each time
 * you update the viewport
 * 
 * @author russ
 *
 */

public class globalarr extends myframe implements StateUpdater{

	double[][][] terrain = null ; //grass, mountains, etc
	double[][][] constructs = null ; // buildings, gates, dead creatures etc
	double[][][] players = null ; // live players and creatures

	double[][][]all = null ; 

	double[][][]lvl1 = null ; 
	
	
	boolean[][] occupied = null ; 
	
	ArrayList<player> peeps = new ArrayList<player>() ; 
	
	double[] testboxloc = new double[2] ; 
	double[] testboxsize = new double[2] ; 
	
	Viewport vp = new Viewport() ; 
	MapLoader ml = new MapLoader() ; 

	BufferedImage img = null;
	BufferedImage viewImage = new BufferedImage(vp.xspan,vp.yspan,BufferedImage.TYPE_INT_ARGB)  ;
	
	boolean finishedInit = false ; 
	

	public globalarr(){
		super() ;
		jf.setSize(new Dimension(516,539));
		jf.pack(); 
		bim = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB) ;
		g2 = bim.createGraphics() ; 
		loadGame() ; 
		initGame() ; 
		ml.initLevel(constructs,occupied) ; 
		combineLayers(0,0,terrain.length,terrain[0].length) ; // combine the layers to make the initial game starting screen
		finishedInit = true ; 
	}
	
	public void setOccupied(boolean[][] occupied){
		this.occupied = occupied ; 
		
	}
	
	public void loadGame(){	
		ml.loadTerrains() ; 
		lvl1 = ml.getMapFast("") ; 
		System.out.println("got map") ; 
		ArrayList<double[][][]> allmaps = ml.setupTrees(lvl1,this) ; 
		System.out.println("trees set up") ; 
		terrain = allmaps.get(0) ; 
		constructs = allmaps.get(1) ; 
		players = allmaps.get(2) ; 
		all = new double[terrain.length][terrain[0].length][4] ; 	
	}
	
	public void initGame(){

		initplayers() ;
		
		for(int p=0;p<peeps.size();p++){
			double[][][] currskin = peeps.get(p).currentskin ;
			double playerw = peeps.get(p).currentskin.length ; 
			double playerh = peeps.get(p).currentskin[0].length ; 
			double istart = peeps.get(p).x + playerw/2 ; 
			double jstart = peeps.get(p).y + playerh/2 ; 
			for(int i=(int)istart-(int)(playerw/2);i<istart+(int)(playerw/2);i++)
				for(int j=(int)jstart-(int)(playerh/2);j<jstart+(int)(playerh/2);j++){
					int ist = (int)(i-(istart-(int)(playerw/2))) ; 
					int jst = (int)(j-(jstart-(int)(playerh/2))) ;
					double[] currentpixel = {currskin[ist][jst][0],currskin[ist][jst][1],currskin[ist][jst][2],currskin[ist][jst][3]} ;
					players[i][j] = currentpixel ; 	
				}	
		}
	}
	
	public void initplayers(){
		for(int i=0;i<1;i++){
			peeps.add(new player(100,100,occupied)) ; 	
			initPlayerTex(peeps.get(i)) ; 
			vp.fixate(peeps.get(0));
		}	
	}
	
	public void initPlayerTex(player p){
		ArrayList<ArrayList<String>> playerImages = GetTextures.getNames("res_man.png") ;
		
		for(int seq=0;seq<7;seq++){
			int theta = 0 ; 
			for(int i=0;i<playerImages.get(seq).size();i++){
				try{
					System.out.println(playerImages.get(seq).get(i)) ; 
					img = ImageIO.read(new File(playerImages.get(seq).get(i))) ; 				
				}catch(IOException e){e.printStackTrace();}
				img = buffconvert.getScaledImage(img,100,100) ; 
				peeps.get(0).addMovementSkin(buffconvert.getDoubleArr(img),theta,seq);
				theta += 45 ; 
			}
			peeps.get(0).currentskin = peeps.get(0).dskins[0].getDirectionSkin(0) ; 	
		}
	}
	
	public void combineLayers(int x,int y,int w,int h){ // combine layers in a certain sub-rectangle only
		// combine the layers...alpha of superior layer times value beneath
			for(int j=x;j<x+w;j++)
				for(int k=y;k<y+h;k++){
					double[] allpixel1 = new double[4] ; 
					double constrans = ((255-constructs[j][k][3])/255) ;
					allpixel1[0] = constrans*terrain[j][k][0] + constructs[j][k][0] ;
					allpixel1[1] = constrans*terrain[j][k][1] + constructs[j][k][1] ;
					allpixel1[2] = constrans*terrain[j][k][2] + constructs[j][k][2] ;
					allpixel1[3] = 255  ;				
					double[]allpixel2 = new double[4] ; 
					double playtrans = ((255-players[j][k][3])/255) ;
					allpixel2[0] = playtrans*allpixel1[0] + players[j][k][0] ;					
					allpixel2[1] = playtrans*allpixel1[1] + players[j][k][1] ;
					allpixel2[2] = playtrans*allpixel1[2] + players[j][k][2] ;
					allpixel2[3] = 255 ;				
					all[j][k] = allpixel2 ; 					
				}
	}

	public void updateState(player p){		

		// update the previous state region 
		double xst_prev = (p.xprev) ; 
		double yst_prev = (p.yprev) ;
		double w_prev = p.currentskin.length ; 
		double h_prev = p.currentskin[0].length ; 	
		//System.out.println("UPDATING STATE : xst_prev = " + (xst_prev +w_prev) + " yst_prev = " + (yst_prev+h_prev) ) ;		
		//draw over the old PLAYER in the double array
		for(int i=(int)xst_prev;i<xst_prev+w_prev;i++)
			for(int j=(int)yst_prev;j<yst_prev+h_prev;j++){
				terrain[i][j][0] += .0;// 30+Math.random()*40 ;
				terrain[i][j][1] += .0;//125+Math.random()*80 ;
				terrain[i][j][2] += .0;//Math.random()*20;
				terrain[i][j][3] -= 0;//255 ;
		
				players[i][j][0] = 0 ;
				players[i][j][1] = 0 ;
				players[i][j][2] = 0 ;
				players[i][j][3] = 0 ;				
			}

		// update the current state
		double xst = p.x ; 
		double yst = p.y ;
		double w = p.currentskin.length ; 
		double h = p.currentskin[0].length ; 
				
		for(int i=(int)xst+2;i<xst+w-2;i++)
			for(int j=(int)yst+2;j<yst+h-2;j++){
				int xind = (int)(i-xst) ;
				int yind = (int)(j-yst) ; 
				try{
				players[i][j][0] = p.currentskin[xind][yind][0] ; 
				players[i][j][1] = p.currentskin[xind][yind][1] ; 
				players[i][j][2] = p.currentskin[xind][yind][2] ; 
				players[i][j][3] = p.currentskin[xind][yind][3] ; 		
				}catch(Exception e){}
			}
		
		combineLayers((int)xst_prev,(int)yst_prev,(int)w,(int)h) ; 
		combineLayers((int)xst,(int)yst,(int)w,(int)h) ; 

	}
	
	public void render(){
		WritableRaster rast = viewImage.getRaster() ;
		for(int i=vp.xloc;i<vp.xloc+vp.xspan;i++)
			for(int j=vp.yloc;j<vp.yloc+vp.yspan;j++){
				if(i-vp.xloc>0 && j-vp.yloc >0 && i> 0 && j>0 && i<terrain.length &&j<terrain[0].length){
					rast.setSample(j-vp.yloc, i-vp.xloc, 0, all[i][j][0]);
					rast.setSample(j-vp.yloc, i-vp.xloc, 1, all[i][j][1]);
					rast.setSample(j-vp.yloc, i-vp.xloc, 2, all[i][j][2]);
					rast.setSample(j-vp.yloc, i-vp.xloc, 3, all[i][j][3]);
				}
			}	
		bim = buffconvert.getScaledImage(viewImage, 500, 500) ; 
		viewImage = new BufferedImage(vp.xspan,vp.yspan,BufferedImage.TYPE_INT_ARGB) ; 
	}
	
	public void run(){
		int rcount = 0 ;
		ControlMovement cm = new ControlMovement(this) ; 
		
		if(finishedInit)
			while(true ){ // turn based game every player gets their turn
				cm.update(peeps.get(0));
				//try{Thread.sleep(1);}catch(Exception e){}
				System.out.println("w = " + jf.getWidth() + " h = " + jf.getHeight()) ; 
				vp.update();
				render() ; 
				repaint() ; 
				//System.out.println("rcount = "  + rcount++) ; 
			}
	}
	
	public static void main(String[]args){
		new Thread(new globalarr()).start() ; 	
	}
}
