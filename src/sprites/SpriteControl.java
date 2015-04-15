package sprites;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class SpriteControl implements KeyListener, MouseListener,MouseMotionListener,MouseWheelListener{

	
	ScrollableMap map ;
	
	public SpriteControl(ScrollableMap map){
		this.map = map ;
		
	}
	
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		map.zoom(e.getScrollAmount()) ;
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
		map.setMouseLocation(e.getX(), e.getY());
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		map.clickLocation(e.getX(),e.getY()) ;
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

	public void keyTyped(KeyEvent e) {
	
		
	}

	public void keyPressed(KeyEvent e) {
		System.out.println("key pressed") ; 
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			map.bump(-1, 0);
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			map.bump(1, 0);
		if(e.getKeyCode()==KeyEvent.VK_UP) ; 
			map.bump(0, -1);
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
			map.bump(0,1) ;
		if(e.getKeyCode() == KeyEvent.VK_S)
			map.saveEvents() ; 
	}

	public void keyReleased(KeyEvent e) {
		
	}

}
