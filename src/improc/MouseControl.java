package improc;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseControl implements MouseMotionListener, MouseListener{

	LargeMap lm = null ; 
	int currx = 0 ; 
	int curry = 0 ; 
	int prevx = 0 ; 
	int prevy = 0 ; 
	
	public MouseControl(LargeMap lm){
		this.lm = lm ; 
		lm.addMouseMotionListener(this);
		lm.addMouseListener(this);
		
		
	}
	
	public static void main(String[]args){
		
		
		
		
	}

	public void mouseDragged(MouseEvent e) {
		//System.out.println("mouse dragging x = "+ e.getX() + " y = "+e.getY()) ; 
	}

	public void mouseMoved(MouseEvent e) {
		//System.out.println("mouse moved x = " + e.getX() + " y = " + e.getY()) ; 
	}

	public void mouseClicked(MouseEvent e) {
	
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		prevx = currx ; 
		prevy = curry ; 
		currx = e.getX() ; 
		curry = e.getY() ; 
		lm.computePath(currx,curry,prevx,prevy)  ;
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
