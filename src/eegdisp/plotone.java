package eegdisp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jforex.Primitives;

public class plotone extends JPanel implements Runnable{

	//frame stuff
	int xloc = 0 ; 
	int yloc = 0 ; 
	JFrame jf = null ; 
	
	//graphics stuff
	int bwidth = 800 ; 
	int bheight = 600 ; 
	BufferedImage bim = new BufferedImage(bwidth,bheight,BufferedImage.TYPE_INT_ARGB) ;
	Graphics2D g2 = bim.createGraphics() ; 
	
	//data stuff
	ArrayList<Double> datas = new ArrayList<Double>() ; 
	
	
	
	public plotone(){ // default constructor
		initFrame() ; 
		
	}
	public plotone(int x,int y){
		xloc = x ; 
		yloc = y ; 
		initFrame() ; 
		
	}
	
	public void update(){
		render() ; 
		repaint() ; 
	}
	
	public void update(ArrayList<Double>input){
		datas = input ; 
		update() ; 
	}
	
	public void render(){
		//set up the font for graphics2D
		bim = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB) ;
		g2 = bim.createGraphics() ;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Font f = new Font(null,Font.ROMAN_BASELINE,10) ; 
		g2.setFont(f);
		
		// set up size variables
		double w = getWidth() ; 
		double h = getHeight() ;
		// set up variables for the drawing box where the line is plotted
		double lmarg = 50 ; 
		double rmarg = 50 ; 
		double tmarg = 50 ; 
		double bmarg = 50 ; 
		double boxw = w-(lmarg + rmarg) ; 
		double boxh = h-(tmarg + bmarg) ; 
		
		g2.setColor(Color.DARK_GRAY);
		g2.fill(new Rectangle2D.Double(0,0,w,h));
		g2.setColor(Color.BLACK);
		g2.fill(new Rectangle2D.Double(lmarg,tmarg,boxw,boxh));
		
		//set up variables for the line drawing
		double xstep = boxw/(double)datas.size() ; 
		double datasmin = Primitives.min(datas) ; 
		double datasmax = Primitives.max(datas) ; 
		double datasrange = datasmax-datasmin ; 
		double ystep = boxh/datasrange ; 
		
		// draw the y axis labels 
		double ymarg = 5 ; 
		NumberFormat yformatter = new DecimalFormat("#0.00000");
		double nylabels = 20 ; 
		double labelincry = datasrange/nylabels ; 
		double ylabincr = boxh/nylabels ; 
		for(int i=0;i<nylabels+1;i++){
			g2.setColor(Color.WHITE) ; 
			double currlabel = datasmax - i*labelincry ; 
			double ypos = ylabincr*i + tmarg ; 
			g2.drawString(yformatter.format(currlabel),(int)ymarg,(int)ypos) ; 
			g2.draw(new Line2D.Double(lmarg-5,ypos,lmarg,ypos));
			g2.setColor(new Color(50,50,50)) ; 
			g2.draw(new Line2D.Double(lmarg,ypos,w-rmarg,ypos)) ; 
		}
		
		// draw the x axis labels
		double xmarg = 5 ;
		NumberFormat xformatter = new DecimalFormat("#0");
		double nxlabels = 30 ;
		double labelincrx = boxw/nxlabels ;
		double xlabelincr = datas.size()/nxlabels ; 
		Font font = new Font(null, Font.ROMAN_BASELINE, 10);    
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(Math.toRadians(-55), 0, 0);
		Font rotatedFont = font.deriveFont(affineTransform);
		g2.setFont(rotatedFont);
		for(int i=0;i<nxlabels+1;i++){
			g2.setColor(Color.WHITE);
			g2.drawString(xformatter.format(i*xlabelincr), (int)(i*labelincrx+lmarg), (int)(tmarg-xmarg));
			g2.draw(new Line2D.Double((i*labelincrx+lmarg),tmarg-5,(i*labelincrx+lmarg),tmarg));
			g2.setColor(new Color(50,50,50)) ; 
			g2.draw(new Line2D.Double((i*labelincrx+lmarg),tmarg,(i*labelincrx+lmarg),tmarg+boxh));
		}
		
		// draw the plot lines
		g2.setColor(Color.WHITE) ; 
		for(int i=0;i<datas.size()-1;i++){
			double x1 = i*xstep + lmarg ; 
			double x2 = (i+1)*xstep + lmarg ; 
			double y1 = (datasmax-datas.get(i))*ystep + tmarg ; 
			double y2 = (datasmax-datas.get(i+1))*ystep + tmarg ;
			g2.draw(new Line2D.Double(x1,y1,x2,y2));
		}	
	}
	
	
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g ;
		g2.drawImage(bim, 0, 0, null) ; 
		
		
		
	}
	
	
	public void run(){
		
	}
	
	
	public void initFrame(){// default frame initialization		
		jf = new JFrame() ; 
		jf.add(this) ; 
		jf.setPreferredSize(new Dimension(bim.getWidth(),bim.getHeight())) ; 
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack(); 
		jf.setVisible(true);
		update() ; 
	}
	public void initFrame(int x, int y){// initialize frame with location only
		initFrame() ; 
		jf.setLocation(x, y);	
	}
	public void initFrame(int[] x, int[] y){ // initialize frame with size and location
		jf = new JFrame() ; 
		jf.add(this) ; 
		jf.setPreferredSize(new Dimension(x[1],y[1])) ; 
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack(); 
		jf.setVisible(true);
		jf.setLocation(x[0], y[0]);	
	}
	
	public static void main(String[]args){
		
		plotone po = new plotone() ; 
		new Thread(po).start(); 
		ArrayList<Double>inputarr = new ArrayList<Double>() ; 
		for(int i=0;i<1000;i++){
			inputarr.add(Math.random()-.5) ; 
			po.update(inputarr) ; 	
			try{Thread.sleep(10);}catch(Exception e){}
		}	
	}
}
