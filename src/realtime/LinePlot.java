package realtime;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * plot lines. takes as input either a single vector of rows = variable, column = tpoints
 * or a single vector of double[] which it adds to its current vector and plots
 * 
 * putting multiple currencies on a single plot...break it up into multi-plots, or make it so you 
 * can swap between different plots?
 * 
 * things to add: make it scrollable, put the upl beside each trade, draw lines from trade to trade,
 * x and y axes (With labels), win/loss indicators (bar charts showing total win/loss in pips). 
 * need to log every single aspect of every single trade to be sure the broker isn't fucking with you. 
 * 
 * @author russ
 *
 */

public class LinePlot extends JPanel implements Runnable{

	JFrame jf ;
	int width = 800 ; 
	int height = 500 ; 
	BufferedImage bim = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB) ; 
	Graphics2D g2 = bim.createGraphics() ; 
	ArrayList<double[]> allvals = null ;
	int period = 200 ; 
	int tcount = 0 ; 
	double profit ; 
	double ntrades =  0 ; 
	double upl = 0 ; 
	double mins = 0  ; 	
	ArrayList<Integer>slopeIndicator=null ; 
	int jcounter = 0 ; 

	public LinePlot() {
		initframe() ; 
		
	}
	
	public void run(){
		
		
	}
	
	public void update(double[] newvals,double profit){
		this.profit = profit ; 
		if(allvals==null){
			allvals = new ArrayList<double[]>() ; 
			allvals.add(newvals) ;
		}
		else allvals.add(newvals) ; 		
		tcount ++ ; 
		if(tcount > period)
			jcounter++ ; 
		render() ; 
		repaint() ; 
	}
	
	public void render(){	
		bim = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB) ;
		g2 = bim.createGraphics() ;
		Font f = new Font(null,Font.ROMAN_BASELINE,11) ; 
		g2.setFont(f) ; g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		NumberFormat formatter = new DecimalFormat("#0.0000") ;
		NumberFormat formatter2 = new DecimalFormat("#0000.0000") ;	
		NumberFormat formatter3 = new DecimalFormat("#000.0") ;	
		double maxInRange = 0 ; 
		double minInRange = 0 ; 
		maxInRange = max(allvals.subList(jcounter, allvals.size()-1)) ;
		minInRange = min(allvals.subList(jcounter, allvals.size()-1)) ; 
		double range = maxInRange - minInRange ; 
		double leftmarg = 80 ; 
		double rightmarg = 50 ;
		double topmarg = 50 ; 
		double botmarg = 50 ; 
		double w = bim.getWidth()-(leftmarg+rightmarg) ; 
		double h = bim.getHeight()-(topmarg+botmarg) ; 
		double xstep = w/(tcount-jcounter) ; 
		g2.setColor(Color.LIGHT_GRAY) ; g2.fill(new Rectangle2D.Double(0,0,bim.getWidth(),bim.getHeight()));
		// main loop to draw all the lines
		for(int i=0;i<allvals.get(0).length;i++){
			g2.setColor(new Color((255/allvals.get(0).length)*i,0,0));
			for(int j=jcounter;j<allvals.size()-1;j++){
				double x1 = (j-jcounter)*xstep + leftmarg ;
				double x2 = (j-jcounter+1)*xstep + leftmarg ;
				double y1 = ((maxInRange-allvals.get(j)[i])/range)*h + topmarg ; 
				double y2 = ((maxInRange-allvals.get(j+1)[i])/range)*h + topmarg ; 
				g2.draw(new Line2D.Double(x1,y1,x2,y2));	
				
				if(i==0 && slopeIndicator != null){
					if(slopeIndicator.get(j)==1){
						g2.setColor(Color.GREEN) ;
						g2.fill(new Ellipse2D.Double(x2-3,y2-3,6,6));			
						g2.setColor(new Color((255/allvals.get(0).length)*i,0,0));
					}
					else if(slopeIndicator.get(j)==-1){
						g2.setColor(Color.RED) ; 
						g2.fill(new Ellipse2D.Double(x2-3,y2-3,6,6));
						g2.setColor(new Color((255/allvals.get(0).length)*i,0,0));
					}					
				}
			}
		}
		//draw all the state variables 
		if(profit>0)g2.setColor(Color.GREEN);else g2.setColor(Color.RED); g2.drawString("current profit = " + formatter.format(profit),10,10);
		g2.setColor(Color.WHITE) ; g2.drawString("ntrades = " + formatter.format(ntrades),10,20);
		if(upl>0)g2.setColor(Color.GREEN);else g2.setColor(Color.RED) ; g2.drawString("upl = " + formatter.format(upl),10,30);
		g2.setColor(Color.WHITE) ; g2.drawString("days = " + formatter2.format((mins)/(60*24)),10,40);
		g2.setColor(Color.WHITE) ; g2.drawString(",  pips/trade = " + formatter2.format(profit/ntrades),100,40);	
		// draw the bounding box around the plot
		g2.setStroke(new BasicStroke(3)) ;
		g2.setColor(Color.BLACK) ; g2.draw(new Rectangle2D.Double(leftmarg-1,topmarg-1,w+1,h+1)); g2.setStroke(new BasicStroke(1)) ;
		// draw the y-axis tics
		int nticsy = 10 ; double ticstepy = h/nticsy ; 
		double yValStep = (maxInRange-minInRange)/10 ; double yTicOffset = 60 ; 
		for(int i=0;i<nticsy+1;i++){
			double ypos = topmarg + i*ticstepy ;
			g2.draw(new Line2D.Double(leftmarg,ypos,leftmarg-5,ypos)) ;
			g2.drawString(formatter2.format(maxInRange-yValStep*i),(int)(leftmarg-yTicOffset),(int)ypos);
		}
		// draw the x-axis tics
		Font font = new Font(null, Font.ROMAN_BASELINE, 11) ;    
		AffineTransform affineTransform = new AffineTransform() ;
		affineTransform.rotate(Math.toRadians(-55), 0, 0) ;
		Font rotatedFont = font.deriveFont(affineTransform) ;
		g2.setFont(rotatedFont) ; int rotCorrection = 8 ; 
		g2.setColor(Color.BLACK) ;
		int ntics = 10 ; double ticstep = w/ntics ; 
		for(int i=0;i<ntics+1;i++){
			double xpos = leftmarg + i*ticstep ; 
			g2.draw(new Line2D.Double(xpos,h+topmarg,xpos,h+topmarg+5)) ;
			g2.drawString(formatter3.format(tcount/60),(int)xpos - rotCorrection,(int)(h+topmarg+32));
		}
	}
	
	public double max(List<double[]>inputs){
		double high = -Double.MAX_VALUE ; 
		for(int i=0;i<inputs.size();i++)
			for(int j=0;j<inputs.get(i).length;j++)
				if(inputs.get(i)[j] > high)
					high = inputs.get(i)[j] ; 
		return high ;					
	}
	public double min(List<double[]>inputs){
		double low = Double.MAX_VALUE ; 
		for(int i=0;i<inputs.size();i++)
			for(int j=0;j<inputs.get(i).length;j++)
				if(inputs.get(i)[j] < low)
					low = inputs.get(i)[j] ; 
		return low ;			
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g; 
		g2.drawImage(bim,0,0,null) ;		
	}
	public void initframe(){
		jf = new JFrame() ; 
		jf.setPreferredSize(new Dimension(width,height));
		jf.add(this) ; 
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);	
		jf.pack();
	}
	
	public static void main(String[] args){
		
		LinePlot lp = new LinePlot() ; 
		for(int i=0;i<10000;i++){
			try{Thread.sleep(5);}catch(Exception e){}
			double[] inputs = new double[2] ; 
			for(int j=0;j<2;j++){
				inputs[j] = Math.random()-.5 ;				
			}
			lp.update(inputs,0) ;
		}		
	}
}
