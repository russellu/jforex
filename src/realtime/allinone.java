package realtime;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
 * stick to the plan precious...
 * 
 * @author gollum
 *
 */

public class allinone extends JPanel implements Runnable{
 
	ArrayList<Double> raw = new ArrayList<Double>() ; 
	ArrayList<ArrayList<Double>> mvgs = new ArrayList<ArrayList<Double>>() ; 
	ArrayList<Integer> periods = new ArrayList<Integer>() ; 
	ArrayList<Integer> slopes = new ArrayList<Integer>() ; 
	int currentDirection = 0 ; 
	double slopeThresh = 0.000006 ; 
	double[] entries = new double[1] ; 
	double[] upls = new double[1] ; 
	double[] stops = {-0.003} ;
	double[] tps = {0.002} ; 
	double[] profits = {0.0} ; 
	double[] ntrades = new double[1] ;
	
	
	///GUI VARS
	JFrame jf ;
	int width = 500 ; 
	int height = 300 ; 
	BufferedImage bim = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB) ; 
	Graphics2D g2 = bim.createGraphics() ; 
	ArrayList<double[]> allvals = null ;
	int period = 500 ; 
	int stepcount = 0 ; 
	int incr = 0 ; 
	double profit ; 
	double ntradesvar =  0 ; 
	double upl = 0 ; 
	double mins = 0  ; 
	ArrayList<Integer>slopeIndicator=null ; 

	public allinone(){
		initframe() ; 
		int[] pers = {12,72,122} ; 
		for(int i=0;i<pers.length;i++){
			periods.add(pers[i]) ; 	
			mvgs.add(new ArrayList<Double>()) ; 
		}		
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
		render() ; 
		repaint() ; 
		stepcount = stepcount + 1 ; 
		if(stepcount > period)
			incr ++ ; 
	}
	
	public void render(){	
		bim = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB) ;
		g2 = bim.createGraphics() ;
		
		Font f = new Font(null,Font.ROMAN_BASELINE,11) ; 
		g2.setFont(f);
		
		NumberFormat formatter = new DecimalFormat("#0.0000");
		NumberFormat formatter2 = new DecimalFormat("#0000.0000");
	
		double maxInRange = 0 ; 
		double minInRange = 0 ; 
		if(allvals.size()>period){
			maxInRange = max(allvals.subList(allvals.size()-period, allvals.size()-1)) ;
			minInRange = min(allvals.subList(allvals.size()-period, allvals.size()-1)) ; 
		}
		else {
			maxInRange = max(allvals) ;
			minInRange = min(allvals) ; 
		}
		double range = maxInRange - minInRange ; 
		
		double leftmarg = 50 ; 
		double rightmarg = 50 ;
		double topmarg = 50 ; 
		double botmarg = 50 ; 
			
		double w = bim.getWidth() ; 
		double h = bim.getHeight() ; 
		double xstep = w/period ; 
		g2.setColor(Color.DARK_GRAY) ; 
		g2.fill(new Rectangle2D.Double(0,0,bim.getWidth(),bim.getHeight()));
		if(profit>0)g2.setColor(Color.GREEN);else g2.setColor(Color.RED); g2.drawString("current profit = " + formatter.format(profit),10,10);
		g2.setColor(Color.WHITE) ; g2.drawString("ntrades = " + formatter.format(ntradesvar),10,20);
		if(upl>0)g2.setColor(Color.GREEN);else g2.setColor(Color.RED) ; g2.drawString("upl = " + formatter.format(upl),10,30);
		g2.setColor(Color.WHITE) ; g2.drawString("days = " + formatter2.format((mins)/(60*24)),10,40);
	
		for(int i=0;i<allvals.get(0).length;i++){
			g2.setColor(new Color((255/allvals.get(0).length)*i,0,0));
			for(int j=incr;j<allvals.size()-1;j++){
				double x1 = (j-incr)*xstep ;
				double x2 = ((j+1)-incr)*xstep ;
				double y1 = ((maxInRange-allvals.get(j)[i])/range)*h ; 
				double y2 = ((maxInRange-allvals.get(j+1)[i])/range)*h ; 
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
	
	public void calculateNext(ArrayList<ArrayList<Double>>mvgs,double price,ArrayList<Integer> periods){
		
		for(int i=0;i<mvgs.size();i++){
			int period = periods.get(i) ; 
			ArrayList<Double> mvg = mvgs.get(i) ; 
			
			if(mvg.size()<period){
				mvg.add(price) ; 
			}
			else if(mvg.size()==period){
				mvg.add(mean(raw.subList(0, raw.size()-1))) ;
			}
			else {
				mvg.add(mean(raw.subList(raw.size()-1-period, raw.size()-1))) ;
			}
		}
	}
	
	public void attemptTrade(int direction){//-1 = sell, 1 = buy
		if(currentDirection != 0) //if already in a trade
			return ; 
		
		if (direction==1){//buy
			currentDirection = 1 ; 
			entries[0] = raw.get(raw.size()-1) ; 
			upls[0] = 0 ; 
			ntrades[0]++ ; 
		}
		else if(direction==-1){
			currentDirection = -1 ;
			entries[0] = raw.get(raw.size()-1) ; 
			upls[0] = 0 ; 
			ntrades[0] ++ ; 
		}
	}
	
	public void checkTrades(){
		if(currentDirection==0)return ; //if no trade in progress, return
		else if(currentDirection==1){
			upls[0] = raw.get(raw.size()-1)-entries[0] ; 
			if(upls[0] > tps[0] || upls[0] < stops[0]){
				profits[0] = profits[0] + upls[0] ; 
				currentDirection = 0 ; 
			}
		}
		else if(currentDirection==-1){
			upls[0] = entries[0]-raw.get(raw.size()-1) ; 		
			if(upls[0] > tps[0] || upls[0] < stops[0]){
				profits[0] = profits[0] + upls[0] ; 
				currentDirection = 0 ; 
			}
		}
	}
	
	public void checkState(){ // check the state of the mvgs, to decide whether or not to buy/sell
		int index = raw.size()-1 ; 
		if(raw.size()>=2){
			double slope = mvgs.get(2).get(index) - mvgs.get(2).get(index-1) ; 
			double dcurr = mvgs.get(1).get(index) - mvgs.get(0).get(index) ; //dcurr = slow-fast
			double dprev = mvgs.get(1).get(index-1) - mvgs.get(0).get(index-1) ; //dprev = slow-fast
			if(slope > slopeThresh){
				if(dcurr>0 && dprev<0){
					slopes.add(1) ; 
					attemptTrade(1) ;
				}
				else slopes.add(0) ; 
			}		
			else if(slope < -slopeThresh){
				if(dcurr<0 && dprev>0){
					slopes.add(-1) ;
					attemptTrade(-1) ;
				}
				else slopes.add(0) ; 
			}	
			else slopes.add(0) ; 
		}
		else{
			slopes.add(0) ;
		}
	}
	
	public void update(double price){
		raw.add(price) ; 
		calculateNext(mvgs,price,periods) ; 
		checkState() ; 
		checkTrades() ; 
	}
	
	public double mean(List<Double>input){
		double total = 0 ;
		for(int i=0;i<input.size();i++)
			total = total + input.get(i) ; 
		return total/(double)input.size() ; 
	}
	public double sum(List<Double>input){
		double total = 0 ;
		for(int i=0;i<input.size();i++)
			total = total + input.get(i) ; 
		return total ; 
	}
	
	public static void main(String[]args){
		
		allinone aio = new allinone() ; 
		for(int i=0;i<100000;i++){
			try{Thread.sleep(10);}catch(Exception e){}
			double newval = Math.random() ; 
			aio.update(newval) ; 
			double[] newvals = new double[aio.mvgs.size()+1] ; 
			newvals[0] = newval ; 
			for(int s=1;s<newvals.length;s++)
				newvals[s] = aio.mvgs.get(s-1).get(aio.mvgs.get(s-1).size()-1) ;
			aio.update(newvals, aio.profit) ;
		}		
		
		
		
	} 
}
