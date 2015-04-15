package physics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;

import improc.myframe;

/**
 * it is physically impossible that two bodies occupy the same space. when they collide, one has to slow down and the other speed up
 * or bounce back or something. 
 * 
 * what is the momentum equation for a 2d collision? momentum = p = mv
 * conservation of momentum : m1v1ix + m2v2ix = m1v1fx + m2v2fx 
 * 
 * v1 = (u1(m1-m2)+2m2u2) / (m1+m2) => wikipedia elastic collision
 * 
 * @author russ
 *
 */

@SuppressWarnings("serial")
public class PhysicsTester extends myframe implements PhysicalReality {
	
	Occupying block1 ;
	Occupying block2 ; 
	
	double[][][]world ;
	int[][] ids ; 
	int[][] shared ; 
	Map<Integer,Occupying> int2Body ;
	
	public PhysicsTester(){

		bim = new BufferedImage(400,400,BufferedImage.TYPE_INT_ARGB) ; 
		g2 = bim.createGraphics() ; 
		jf.setSize(new Dimension(500,500));
		world = new double[bim.getWidth()][bim.getHeight()][4] ; 
		ids = new int[bim.getWidth()][bim.getHeight()] ; 
		int2Body = new HashMap<Integer,Occupying>() ; 
		block1 = new Body(this,100,100,50,40,1500,1) ; 
		block1.velocityUpdate(1,0);
		block2 = new Body(this,200,100,40,50,1500,2) ;
		block2.velocityUpdate(-1, -1);
		int2Body.put(1, block1) ; 

		int2Body.put(2, block2) ;

		
	}
	
	public void drawWorld(){
		clearWorld() ; 
		for(int i=2;i>0;i--)
			int2Body.get(i).drawSelf(world, ids) ;
		
		
	}
	
	public void clearWorld(){
		for(int i=0;i<world.length;i++)
			for(int j=0;j<world[0].length;j++){
				world[i][j][0] = 0 ; 
				world[i][j][1] = 0 ; 
				world[i][j][2] = 0 ; 
				world[i][j][3] = 0 ; 

				
			}
		
		
	}
	
	public void render(){
		WritableRaster rast = bim.getRaster() ; 
		for(int i=0;i<bim.getWidth();i++)
			for(int j=0;j<bim.getHeight();j++){
				rast.setSample(i, j, 0, world[i][j][0]);
				rast.setSample(i, j, 1, world[i][j][1]);
				rast.setSample(i, j, 2, world[i][j][2]);
				rast.setSample(i, j, 3, 255);
				
			}
	}

	
	public void run(){
		
		double fps = 60 ; 
		double startT = System.nanoTime() ; 
		double nFrames = 1 ; 
		while(true){
			double tDiff = System.nanoTime() - startT ; 
			try{Thread.sleep(3);}catch(Exception e){}

			int2Body.get(2).actedUpon();
			int2Body.get(1).actedUpon();
			

			int2Body.get(2).finalVelocityUpdate() ;
			int2Body.get(1).finalVelocityUpdate() ; 
			drawWorld() ;
			
			
			System.out.println("fps = " + nFrames/(tDiff/1000000000)) ;
			nFrames ++ ; 
		//	System.out.println("BODY 1 VX = " + int2Body.get(1).getVelocity()[0] + " BODY 2 VX = " + int2Body.get(2).getVelocity()[0]) ; 
			render() ; 
			repaint() ; 
			
		}
	}
	
	
	public static void main(String[]args){
		PhysicsTester pt = new PhysicsTester() ; 
		new Thread(pt).start() ;
		
		
	}







	public double[][][] getWorld() {
		return world;
	}

	public int[][] getIdentifiers() {
		return ids;
	}

	public Map<Integer, Occupying> getBodies() {
		return int2Body;
	}

}
