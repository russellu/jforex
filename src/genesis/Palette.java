package genesis;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import improc.myframe;

/**
 * the palette sets the current color of the mouse, or the object you want to draw
 * 
 * 
 * @author russ
 *
 */

public class Palette extends myframe{

	
	CreatorFrame cf ;
	
	public int rotateAngle = 0 ; 
	public int angleCount = 0 ;
	double pi = Math.PI ;
	double[] allowedAngles = {0,pi/4,pi/2,(3*pi)/4,pi,(5*pi)/4,(3*pi)/2,(7*pi)/4,2*pi} ; 
	
	DirectionArrow currentArrow ; 
	
	public Palette(CreatorFrame cf){
		super() ; 
		jf.setSize(new Dimension(500,150));
		jf.setLocation(500,500);
		this.cf = cf ; 
		initButtons() ; 

		
	}
	
	public void updateCurrentArrow(){
		//rotateAngle = angleCount%360 ; 
		currentArrow = new DirectionArrow(cf.vc.currentMouseY,cf.vc.currentMouseX,allowedAngles[rotateAngle%8]) ; 
		//System.out.println("Adding angle at " + allowedAngles[rotateAngle%8] + " radians") ; 
	}
	
	public void updateAngle(int amt){
		if(currentArrow == null)
			currentArrow = new DirectionArrow(cf.vc.currentMouseX,cf.vc.currentMouseY,rotateAngle) ; 
		
		
		rotateAngle ++ ; 
		
		
	}

	public void addObject(String title){
		
		
	}
	
	public void newButton(){
		
		
	}
	
	public void initButtons(){
		JButton saveButton = new JButton("save current") ; 
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("saving...") ; 
				cf.saveImage() ; 
				cf.saveArrows() ; 
				
			}
			
		});
		add(saveButton) ; 
	}
	

	public static void main(String[]args){
		
	//	new Palette() ; 
	}
	
	
	
}
