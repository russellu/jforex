package genesis;

import java.awt.MouseInfo;
import java.awt.Point;

public class ViewControl{
	
	int xpos ; // x/y positions in pixels of the top left corner of the viewport
	int ypos ; 
	int width ; // sizes of viewport
	int height ; 
	
	int currentMouseX ; 
	int currentMouseY ; 
	
	int MAXWIDTH ;
	int MAXHEIGHT ;
	
	int movincr = 2 ; 
	
	Keys keys ; 
	Souris mouse ; 
	CreatorFrame cf ; 
	
	boolean focused = false ;
	
	public ViewControl(int w,int h, CreatorFrame cf){
		this.cf = cf ; 
		this.width = w ; 
		this.height = h ; 

		this.xpos = 0 ; 
		this.ypos = 0 ; 
		keys = new Keys() ; 
		cf.addKeyBoard(keys) ;
		
		mouse = new Souris(cf) ; 
		cf.addMouseListener(mouse) ;
		cf.addMouseMotionListener(mouse) ;
		cf.addMouseWheelListener(mouse) ;
		System.out.println("viewControl init") ; 
	}
	public void setMax(int w,int h){
		this.MAXWIDTH = cf.pixMap.length ;
		this.MAXHEIGHT = cf.pixMap[0].length ; 
	}
	
	public void update(){
		checkArrowKeys() ; 
	
	}
		
	// bump over a couple pixels
	public void bump(int xincr, int yincr){		
		// first do some bounds checking
		if(xpos + xincr >= 0 && ypos + yincr >= 0 && (xpos + width + xincr) < MAXWIDTH && (ypos + height + yincr) < MAXHEIGHT){
		//	System.out.println("updating view position") ; 
			xpos = xpos + xincr ; 
			ypos = ypos + yincr ; 
		}	
	}
	
	// jump to a new location (using mini map)
	public void jump(int newx, int newy){
		
		
	}

	public void checkArrowKeys(){
		//System.out.println("checking arrow keys") ; 
		if(keys.up&&keys.right){
			bump(-movincr,movincr) ; 
		}
		else if(keys.up&&keys.left){
			bump(-movincr,-movincr) ; 
		}
		else if(keys.down&&keys.right){
			bump(movincr,movincr) ; 
		}
		else if(keys.down&&keys.left){
			bump(movincr,-movincr) ; 
		}
		else if(keys.up){
			System.out.println("moving up") ; 
			bump(-movincr,0) ; 
		}
		else if(keys.down){
			bump(movincr,0) ; 
		}
		else if(keys.left){
			bump(0,-movincr) ; 
		}
		else if(keys.right){
			bump(0,movincr) ; 
		}	
	}
}
