package genesis;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Souris implements MouseMotionListener, MouseListener, MouseWheelListener{

	CreatorFrame cf ; 
	
	public Souris(CreatorFrame cf){
		this.cf = cf ; 
		
		
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println(e.getWheelRotation()) ;
		if(e.getWheelRotation() < 1)
			cf.pal.updateAngle(1) ;
		else cf.pal.updateAngle(-1) ;
		cf.pal.updateCurrentArrow(); 

	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		
		System.out.println("mouse pressed x = " + e.getX() + " y = " + e.getY()) ;
		cf.addArrow(e.getX(),e.getY(),cf.pal.allowedAngles[cf.pal.rotateAngle%8]);
		
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
		cf.vc.currentMouseX = e.getY() ; 
		cf.vc.currentMouseY = e.getX() ;
		cf.pal.updateCurrentArrow(); 
		
	}

}
