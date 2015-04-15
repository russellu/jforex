package behaviors;

import physics.PhysicalReality;
import players.ControllablePlayer;
import players.PlayerControl;


/**
 * give each player a magnetic field so that they repel each other and are attracted towards enemies.
 * this field can warp the path. what if the player is following a path, but still needs to take into
 * account the field heat map which is dynamic with time? 
 * 
 * also, how to handle collisions and path finding? will have to "Get back" to the path
 * 
 * have to go from abstract path space to concrete game physics space. the path is just a suggestion??
 * 
 * flood fill search for shortest path? search on discrete pixel space?
 * 
 * @author russ
 *
 */


public class BehavingPlayer implements Pathable, ControllablePlayer{

	double xloc ; 
	double yloc ; 
	boolean following = true ; 
	int pathIndex = 0 ; 
	PhysicalReality world ; 
	NavPath pathFinder ; 
	PlayerControl control ; 
	
	public BehavingPlayer(PhysicalReality world, double xloc, double yloc){
		
		
		this.world = world ; 
		this.xloc = xloc ; 
		this.yloc = yloc ; 

		initPath() ; 
		initControls() ;		

	}
	
	public void passTime(){
		followPath() ; 
		drawSelf(world.getWorld()) ; 
		drawPath() ; 
		
		
	}
	
	public void followPath(){
		
		if(pathIndex < pathFinder.path.size()){
			double[] newLoc = pathFinder.path.get(pathIndex) ;			
			pathIndex++ ; 
			this.xloc = newLoc[0] ;
			this.yloc = newLoc[1] ; 
		} 	
	}
	
	
	public void initPath(){
		double[] start = {10,10} ; 
		double[] end = {350,100} ; 
		
		pathFinder = new NavPath(world.getIdentifiers(),start,end) ;
		pathFinder.calculatePath() ; 	
	}
	
	public void updatePath(double[] newDestination){
		pathIndex = 0 ; 
		double[]start = {xloc,yloc} ;
		pathFinder = new NavPath(world.getIdentifiers(),start,newDestination) ; 
		pathFinder.calculatePath() ; 
		
	}
	
	public void initControls(){
		control = new PlayerControl(this) ;
		world.getFrame().addKeyListener(control) ;
		world.getPanel().addMouseListener(control) ;
		world.getPanel().addMouseMotionListener(control) ;
		world.getPanel().addMouseWheelListener(control) ;
	}
	
	public void drawSelf(double[][][]world){
		
		for(int i=0;i<10;i++)
			for(int j=0;j<10;j++){
				
				world[i+(int)xloc][j+(int)yloc][0] = 122 ; 
				world[i+(int)xloc][j+(int)yloc][1] = 122 ; 
				world[i+(int)xloc][j+(int)yloc][2] = 122 ; 
				world[i+(int)xloc][j+(int)yloc][3] = 122 ; 	
			}	
	}

	public void drawPath() {
		pathFinder.drawSelf(world.getWorld()) ; 
	}

	public void moveStep(double xIncr, double yIncr) {
		// TODO Auto-generated method stub
		
	}

	
	// method where you update the new path
	public void moveJump(int newX, int newY) {
		System.out.println("new path location x = " + newX + " y = " + newY) ; 
		double[] newPath = {newX,newY} ;
		updatePath(newPath) ; 
	}

	public void attack(int attackCode) {
		// TODO Auto-generated method stub
		
	}

	public void haltAttack() {
		// TODO Auto-generated method stub
		
	}
}
