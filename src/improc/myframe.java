package improc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class myframe extends JPanel implements Runnable{
	int WIDTH = 500 ; 
	int HEIGHT = 500 ; 
	protected BufferedImage bim = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB) ; 
	protected Graphics2D g2 = bim.createGraphics() ; 
	protected JFrame jf = null ; 
	
	public myframe(){
		initbuff() ; 
		initframe() ; 
	}
	
	public myframe(int w,int h){
		WIDTH = w ; 
		HEIGHT = h ; 
		initbuff() ; 
		initframe() ; 
	}
	
	public void render(){
		g2.setColor(Color.BLACK);
		g2.fill(new Rectangle2D.Double(0,0,getWidth(),getHeight())) ; 
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g ; 
		g2.drawImage(bim,0,0,null) ; 
	}
	
	public void run(){
		while(true){
			render() ; 
			repaint() ; 			
			//try{Thread.sleep(0);}catch(Exception e){}		
		}	
	}
	
	public void initbuff(){
		bim = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB) ; 
		g2 = bim.createGraphics() ; 	
	}
	
	public void initframe(){
		jf = new JFrame() ; 
		jf.add(this) ; 
		jf.setPreferredSize(new Dimension(bim.getWidth(),bim.getHeight()));
		jf.setVisible(true);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[]args){	
		new Thread(new myframe(500,500)).start(); 	
	}
}
