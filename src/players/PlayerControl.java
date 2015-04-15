package players;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import utility.LowLvl;

public class PlayerControl implements MouseMotionListener,MouseListener,MouseWheelListener,KeyListener {
	
	ControllablePlayer player ;
	boolean up ; 
	boolean down ; 
	boolean left ; 
	boolean right ; 
	int moveStep = 3 ;
		
	//directions clockwise starting in top left corner
	private static int[] upLeft = {-1,-1} ;
	private static int[] upStraight = {0,-1} ;
	private static int[] upRight = {1,-1} ; 
	private static int[] straightRight = {1,0} ; 
	private static int[] downRight = {1,1} ;
	private static int[] downStraight = {0,1} ;
	private static int[] downLeft = {-1,1} ;
	private static int[] straightLeft = {-1,0} ;
	private static int[] zeroDirection = {0,0} ;
	
	// the mapping of the state array to the direction.
	static boolean[] upLeftBool = {true,false,true,false} ; 	
	static boolean[] upStraightBool = {true,false,false,false} ; 	
	static boolean[] upRightBool = {true,false,false,true} ; 	
	static boolean[] straightRightBool = {false,false,false,true} ; 	
	static boolean[] downRightBool = {false,true,false,true} ; 	
	static boolean[] downStraightBool = {false,true,false,false} ; 	
	static boolean[] downLeftBool = {false,true,true,false} ; 
	static boolean[] straightLeftBool = {false,false,true,false} ; 
	
	
	public PlayerControl(ControllablePlayer player){
		System.out.println("player control init") ; 
		this.player = player ;	
		
	}
	
	
	// returns a 2d vector of (x,y) denoting direction at state of up,right,left,down keys
	public int[] get2dDirectionVector(){ 
		boolean[] state = {up,down,left,right} ;
		if(LowLvl.compareBoolean(state, upLeftBool))
			return upLeft ;
		else if(LowLvl.compareBoolean(state, upStraightBool))
			return upStraight ;

		else if(LowLvl.compareBoolean(state, upRightBool))
			return upRight ;

		else if(LowLvl.compareBoolean(state, straightRightBool))
			return straightRight ;

		else if(LowLvl.compareBoolean(state, downRightBool))
			return downRight ;

		else if(LowLvl.compareBoolean(state, downStraightBool))
			return downStraight ;

		else if(LowLvl.compareBoolean(state, downLeftBool))
			return downLeft ;

		else if(LowLvl.compareBoolean(state, straightLeftBool))
			return straightLeft ;

		else return zeroDirection ; 

	}
	
	
	public void passive(){
		
	}
	
	public void keyPressed(KeyEvent e) {
	//	System.out.println(e.getKeyCode()) ; 
		
		if(e.getKeyCode()==KeyEvent.VK_C){
		//	System.out.println("Space pressed") ;
			player.attack(1) ;
		}
		
		else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			left = true ; 
		//	System.out.println("key Event Triggered (left)") ; 
		
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			right = true ; 
		//	System.out.println("key Event Triggered (right)") ; 

		
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP){
			up = true ; 
			//System.out.println("key Event Triggered (up) ") ; 

			
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			down = true ;
		//	System.out.println("key Event Triggered (down)") ; 

			}
	
	
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_C){
			player.haltAttack() ;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
			left = false ;
		}
		if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			right = false ; 
		}
		if(e.getKeyCode()==KeyEvent.VK_UP){
			up = false ; 
		}
		if(e.getKeyCode()==KeyEvent.VK_DOWN){
			down = false ; 
		}

	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("pressed x = " + e.getX() + " y = + " + e.getY()) ; 
		player.moveJump(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
	//	System.out.println("mouse moved") ; 
	}

	
	
	
	
	
	
	
	
	
}
