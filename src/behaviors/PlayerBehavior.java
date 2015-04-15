package behaviors;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import physics.Body;
import physics.Occupying;
import physics.PhysicalReality;
import improc.myframe;

/** 
 * AI player is perceptively active or not ie on the map of the human player.
 * if the AI is not on or relevant to the human player map
 * 
 * ai behavior: based around survival of self, self preservation
 * => destroy any threat, and if the threat is too powerful, run away.
 * 
 * maximize probability of victory through a series of moves (chess)
 * simple player AI => get from point a to point b.
 * secondary: why did the chicken cross the road? why did the npc go to point b?
 * AI is motivated: kill or be killed. goal oriented behavior. 
 * spread out and attack? flanking? like a pack of wild dogs attacking a hyena.
 * dart in and do maximal damage with minimal exposure. melt away before the more
 * powerful but slower foe. 
 * if you are the stronger, run down your prey and batter them to death. 
 * if you are uncertain, a feeling out process occurs, if there is time.
 * 
 * hierarchical goal orientation: top down 
 * goal1) kill enemy
 * goal2) flank enemy
 * goal3) find path to point b
 * the why and how of NPC behavior
 * 
 * STEP 1: PATHFINDING
 * 1) draw path (Visualization)
 * compute path: how? straight line until obstacle, join corners to minimize walking distance (Based on line of sight/memory)
 * treat players in LOS as obstacles.
 * LOS: radius in which player computes path. use backtracking to explore all possible routes. player has mental pixel map, or
 * some kind of more abstract network? 
 * pathfinding heuristics: go in direction of objective. don't retrace steps unless you have to. look for corners to cut path.
 * the player should start in the direction of the objective until it hits an obstacle. then, it turns in the closest direction and 
 * continues that way. 
 * an obstacle is always a choice. return to the previous choice if you hit a dead end.
 * how would a choice look on your current terrain? you want to go straight towards your target but are blocked. find the next available 
 * point?
 * 
 * have objects emit a field that drives the player away from them? a field that the pathfinding algorithm senses...it then follows
 * the gradient to lowest field as in a directed gradient descent search
 * 
 * STEP2: SMART MOVEMENT (keep archers back, each player has a role)
 * 
 * maximizing probability of success => venturing to areas of the search space with the highest payoff?
 * chess=> infinite search space? or just incredibly huge finite
 * 
 * STEP3 ; the behaving block
 * you want the block to behave. why don't you just record your own behavior and put it on the block?
 * this is just a preperscribed set of actions, not actual behavior. behavior is dynamic 
 * you can pre-program behavior to obstacles, however...and group situations. set up the script beforehand?
 * 
 * have different high level behavioral "modes" ie hovering/cautious, aggressive/intimidating, submissive
 * still relies on pathfinding
 * pathfinding is the start of the NPC's autonomoy. point a to point b. break it down into steps. decision tree? left/right
 * its a binary search tree but what space is it over? pixels? or more abstract obstacles? 
 * use the occupied pixel array...look for corners? sub-goals and backtracking? sub-goals should be limited by the fov.search a 
 * sparse array that grows denser closer to the character...use that for fog of war also (gaussian fog of war mask)
 * gaussian fog of war mask occluded by obstacles? more expensive computations (line of sight)...check circular perimeter?
 * should be a fast computation...ray cast the perimeter. spell eyes in the dark.
 * ray cast perimeter and get obstacles. 
 * 
 * @author russ
 *
 */


/**
 * 
 * use the occupied array to searsch for paths
 * 
 * @author russ
 *
 */

@SuppressWarnings("serial")
public class PlayerBehavior extends myframe implements PhysicalReality{

	
	
	BehavingPlayer bh ;
	double[][][] world ; 
	int[][] occupied ; 	
	HashMap<Integer,Occupying> id2Body = new HashMap<Integer,Occupying>() ; 
	ArrayList<Body>bodies = new ArrayList<Body>() ;

	
		
	public PlayerBehavior(){
		bim = new BufferedImage(400,400,BufferedImage.TYPE_INT_ARGB) ; 
		g2 = bim.createGraphics() ; 
		world = new double[400][400][4] ;
		occupied = new int[400][400] ; 
		bh = new BehavingPlayer(this,50,50) ;
		initBodies() ; 
	}

	
	public void initBodies(){
		
		for(int i=9;i<10;i++){
			Body square = (new Body(this,200,200,50,50,1,i)) ; 
			bodies.add(square) ; 
			id2Body.put(i, square) ; 
		}
	}
	
	public void updatePlayers(){
		bh.passTime()  ;
	}
	
	public void updateBodies(){
		for(int i=0;i<bodies.size();i++){
			bodies.get(i).drawSelf(world,occupied);
			bodies.get(i).actedUpon() ;
			bodies.get(i).finalVelocityUpdate();
		}		
	}
	
	public void clearWorld(){
		
		for(int i=0;i<world.length;i++)
			for(int j=0;j<world[0].length;j++){
				world[i][j][0] = 0 ; 				
				world[i][j][1] = 0 ; 
				world[i][j][2] = 0 ; 
				world[i][j][3] = 255 ; 
				
			}
	}
	

	
	public void render(){
		
		updatePlayers() ; 
		updateBodies() ;
		
		WritableRaster rast = bim.getRaster() ; 
		for(int i=0;i<bim.getWidth();i++)
			for(int j=0;j<bim.getHeight();j++){
				rast.setSample(i, j, 0, world[i][j][0]);
				rast.setSample(i, j, 1, world[i][j][1]);
				rast.setSample(i, j, 2, world[i][j][2]);
				rast.setSample(i, j, 3, world[i][j][3]);
			}

		clearWorld() ; 
	
	}
	
	
	public void run(){
		
		while(true){
			
			try{Thread.sleep(10);}catch(Exception e){}
			render() ;
			repaint() ; 
			
			
		}
	}
	
	
	public static void main(String[]args){
		PlayerBehavior pb = new PlayerBehavior() ; 
		new Thread(pb).start() ; 
		
		
		
	}


	public double[][][] getWorld() {
		return world ;
	}


	public int[][] getIdentifiers() {
		return this.occupied ;
	}


	public Map<Integer, Occupying> getBodies() {
		return null;
	}

	public JFrame getFrame() {
		return jf ;
	}

	public JPanel getPanel() {
		return this ; 
	}
	
	
	
	
	
	
	
}
