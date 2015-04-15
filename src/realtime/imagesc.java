package realtime;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;





import javax.swing.JFrame;
import javax.swing.JPanel;

import jforex.Primitives;


/**
 * plot a 1d vector
 * @author russ
 *
 */
public class imagesc extends JPanel implements Runnable{
	boolean bar ;
	boolean line ;
	double largest ;
	double smallest ;
	JFrame jf = null ; 
	BufferedImage bim = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB) ; 
	Graphics2D g2 = bim.createGraphics() ; 
	double[][]img = null ; 
	
	
	public imagesc(){
		initframe() ; 
		initim() ; 
	} 
	public imagesc(int x,int y){
		initframe() ; 
		jf.setLocation(x,y) ; 
			
		
	}
	public void initim(){
		img = new double[200][200] ; 
		for(int i=0;i<img.length;i++)
			for(int j=0;j<img[0].length;j++)
				img[i][j] = Math.sqrt(i*j) ; 
		img = Primitives.normneg1(img) ; 
		
		
	}
	public void render(){		
		
		double xstep = (double)bim.getWidth()/(double)img.length ;
		double ystep = (double)bim.getHeight()/(double)img[0].length ; 
		for(int i=0;i<img.length;i++)
			for(int j=0;j<img[i].length;j++){
				g2.setColor(new Color((int)(jet.red(img[i][j])*255),(int)(jet.green(img[i][j])*255),(int)(jet.blue(img[i][j])*255))) ;
				g2.fill(new Rectangle2D.Double(i*xstep,j*ystep,xstep,ystep));
				
			}		
	}
		
	public void run(){
		int i = 0 ; 
		while(true){
			System.out.println("i = " + i++) ; 
			try{Thread.sleep(0);}catch(Exception e){}
			render() ; 
			repaint() ; 
			
			
			
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g) ;
		Graphics2D g2 = (Graphics2D)g ;
		g2.drawImage(bim,0,0,null) ; 

		
		
		
	}
	
	public void initframe(){
		jf = new JFrame() ; 
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setPreferredSize(new Dimension(bim.getWidth(),bim.getHeight()));
		jf.setVisible(true);
		jf.pack();
		jf.add(this) ;
		
		
		
		
	}
	
	public static void main(String[] args) {
		
		new Thread(new imagesc()).start() ; 
		//new plot(rand) ;
	}

}
