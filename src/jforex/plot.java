package jforex;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * plot a 1d vector
 * @author russ
 *
 */
public class plot extends JPanel implements Runnable{
	ArrayList<Double> vec ;
	boolean bar ;
	boolean line ;
	double largest ;
	double smallest ;
	
	
	public plot(ArrayList<Double> vec){
		JFrame jf = new JFrame() ;
		jf.setSize(800,500) ;
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		jf.add(this) ;
		jf.setVisible(true) ;
		this.vec = vec ;
		new Thread(this).start() ;
	}
	
	
	public plot(ArrayList<Double> vec,String title){
		JFrame jf = new JFrame(title) ;
		jf.setSize(800,500) ;
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		jf.add(this) ;
		jf.setVisible(true) ;
		this.vec = vec ;
		new Thread(this).start() ;
	}
		
	public void run(){
		largest = Primitives.max(Primitives.list2arr(vec)); 
		smallest = Primitives.min(Primitives.list2arr(vec)); 
		repaint() ;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g) ;
		Graphics2D g2 = (Graphics2D)g ; 
		double w = getWidth() ;
		double h = getHeight() ;
		double xstep = (w-100)/vec.size() ;
		g2.setColor(Color.DARK_GRAY) ;
		g2.fill(new Rectangle2D.Double(100,0,w,h)) ;
		g2.setColor(Color.BLACK) ;
		
 	//	System.out.println(largest) ;
	//	System.out.println(smallest) ;
		for(int i=0;i<vec.size()-1;i++){
			double y1 = ((largest-vec.get(i))/(largest-smallest))*h ;
			double y2 = ((largest-vec.get(i+1))/(largest-smallest))*h ;
			
			
			if(vec.get(i) > 0)
				g2.setColor(Color.GREEN); 
			else g2.setColor(Color.RED) ; 
			g2.fill(new Ellipse2D.Double(i*xstep+100-2,y1-2,4,4)) ;	
	//		g2.draw(new Rectangle2D.Double(i*xstep,y1,xstep,h)) ;
		}
		//g2.draw(new Line2D.Double(0,h/2,w,h/2)) ;
		//g2.drawString(Double.toString(largest),0,10) ;
		//g2.drawString(Double.toString((largest+smallest)/2),0,(int)(h/2)) ;
		//g2.drawString(Double.toString(smallest),0,(int)h-10) ;

		
		double range = largest-smallest ; 
		double rangeincr = range/20 ; 
		for(int i=1;i<20;i++){
			double y1 = ((rangeincr*i)/(largest-smallest))*h ;
			
			g2.setColor(new Color(125,125,125)) ; 
			g2.draw(new Line2D.Double(0,y1,w,y1)) ;
			
			g2.setColor(Color.BLACK) ;
		//	g2.drawString(Double.toString((largest-rangeincr*i)*100).substring(0,5),0,(int)y1) ;

			
		}
		
		
		
	}
	
	public static void main(String[] args) {
		ArrayList<Double> rand = new ArrayList<Double>() ;
		for(int i=0;i<100;i++)
			rand.add(Math.random()) ;
		new plot(rand) ;
	}

}
