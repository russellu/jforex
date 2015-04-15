
package physics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import players.Alex;
import players.Player;
import players.PlayerControl;
import improc.SpriteHolder;
import improc.myframe;
/**
 *
 * 
 * each attack has a velocity depending on where you are hitting from.
 * 
 * the collision checking should happen during the movement.
 * 
 * add path finding to find the square you want to get to, so you don't get stuck on edges...
 * 
 * randomize shot less with increasing skill levels
 * 
 * collision problems: go through walls (player disappears) (fixed) => put player in previous xposition
 * elastic collision seems to be calculated wrong (only works for head-on collisions)
 * box still goes through player at times
 * box gets stuck on walls if you go too far in (fixed) => reverse velocity of box and add it to position
 * 
 * make it so he slides on the wall (Genesis 1:1)
 * 
 * pick up the pieces and put them in your sack.
 * 
 * have people play the game whilst recording EEG, see if they have a larger response when in certain areas of the grid
 * 
 * @author russ
 *
 */



@SuppressWarnings("serial")
public class BodyTester extends myframe implements PhysicalReality {

//	String[] spriteDirNames = {"C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexAttackingProc\\"} ; 
	Alex spider ; 
	double[][][] playerLayer ; 
	int[][] identifierLayer ; 
	PlayerControl control ;
	ArrayList<Body>bodies  = new ArrayList<Body>() ;
	boolean showIdentifiers = false ; 
	HashMap<Integer,Occupying> id2Body = new HashMap<Integer,Occupying>() ; 
	
	public BodyTester(){
		super() ; 
		bim = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB) ; 
		identifierLayer = new int[500][500] ;
 		jf.setSize(new Dimension(530,550)) ;
		g2 = bim.createGraphics() ; 
		playerLayer=  new double[bim.getWidth()][bim.getHeight()][4] ; 
		initPlayers() ; 
		control = new PlayerControl(spider) ; 
		initControls() ; 
		initBodies() ; 
	}
	
	public void initBodies(){
		
		for(int i=9;i<300;i++){
			Body square = (new Body(this,(int)(Math.random()*400),(int)(Math.random()*400),2,3,1,i)) ; 
			bodies.add(square) ; 
			id2Body.put(i, square) ; 
		}
		/*
		
		Body topPanel = (new Body(this,0,0,bim.getWidth(),40,100000,3)) ; 
		bodies.add(topPanel) ; 
		id2Body.put(3, topPanel) ; 

		Body bottomPanel = (new Body(this,0,bim.getHeight()-41,bim.getWidth(),40,100000,4)) ; 
		bodies.add(bottomPanel) ; 
		id2Body.put(4, bottomPanel) ; 
		
		Body leftPanel = (new Body(this,0,40,40,bim.getHeight()-80,100000,5)) ;
		bodies.add(leftPanel) ;
		id2Body.put(5, leftPanel) ; 
		
		Body rightPanel = (new Body(this,bim.getWidth()-40,40,40,bim.getHeight()-41,100000,6)) ; 
		bodies.add(rightPanel) ;
		id2Body.put(6, rightPanel) ; 
*/
	}
	
	public void initPlayers(){
	//	SpriteHolder holder = new SpriteHolder(SpriteGetter.get(spriteDirNames[0])) ; 
		spider = new Alex(this,100, 100,8) ; 
		id2Body.put(8, spider) ; 
	}
	
	public void initControls(){
		addMouseListener(control);
		addMouseMotionListener(control) ;
		addMouseWheelListener(control) ;
		jf.addKeyListener(control) ;	
	}
	
	public double[][][] getWorld() {
		return playerLayer ; 
	}	
	
	public int[][] getIdentifiers(){
		return identifierLayer ;
	}

	public Map<Integer, Occupying> getBodies() {
		return id2Body ; 
	}

	
	public void render(){
		WritableRaster rast = bim.getRaster() ;
		if(!showIdentifiers)
		for(int i=0;i<bim.getWidth();i++)
			for(int j=0;j<bim.getHeight();j++){
				rast.setSample(i, j, 0, playerLayer[i][j][0]);				
				rast.setSample(i, j, 1, playerLayer[i][j][1]);
				rast.setSample(i, j, 2, playerLayer[i][j][2]);
				rast.setSample(i, j, 3, 255);
			}
		
		else if(showIdentifiers)
			for(int i=0;i<bim.getWidth();i++)
				for(int j=0;j<bim.getHeight();j++){
					rast.setSample(i, j, 0, identifierLayer[i][j]*20);				
					rast.setSample(i, j, 1, identifierLayer[i][j]*20);
					rast.setSample(i, j, 2, identifierLayer[i][j]*20);
					rast.setSample(i, j, 3, 255);
				}
	}
	
	
	public void resetScene(){
		
		for(int i=0;i<playerLayer.length;i++)
			for(int j=0;j<playerLayer[0].length;j++){
				playerLayer[i][j][0] = 0 ;			
				playerLayer[i][j][1] = 0 ;
				playerLayer[i][j][2] = 0 ;
				playerLayer[i][j][3] = 0 ;
			
			}
	}
	
	public void updateBodies(){
		for(int i=0;i<bodies.size();i++){
			bodies.get(i).drawSelf(playerLayer,identifierLayer);
			bodies.get(i).actedUpon() ;
			bodies.get(i).finalVelocityUpdate();
		}		
	}
	
	public void updatePlayers(){
		
		spider.updateMovement(control); // get the user input updates
		spider.drawSelf(playerLayer, identifierLayer) ;
		
	}
	
	public void run(){
		while(true){
		// try{Thread.sleep(20);}catch(Exception e){}	
			render() ;
			repaint() ; 
			resetScene() ; 
			updateBodies() ; 

			updatePlayers() ;
		}
	}
	
	public static void main(String[]args){
		BodyTester sbg = new BodyTester() ; 
		new Thread(sbg).start() ;		
	}

	public JFrame getFrame() {
		// TODO Auto-generated method stub
		return null;
	}

	public JPanel getPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
