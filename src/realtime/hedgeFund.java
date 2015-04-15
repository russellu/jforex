package realtime;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utility.SaveArray;
import jforex.GetSpreads;
import jforex.dukas;
import jforex.parseDukas;

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
 * have a grid at the top that you can select with mouse clicks and that shows you the current profit/loss of each currency.
 * other cool stuff: all "grand average" currency stats, display the currency with a color relative to its stop/tp
 * make sure you write the trades to a text file or java object (or both) display all trades as bar chart for that ccy
 * 
 * hedging: have a bar chart for the x separate currencies (they should all hover around zero) 
 * 
 * @author russ
 *
 */

public class hedgeFund extends JPanel implements Runnable, MouseMotionListener,MouseListener {

	JFrame jf ;
	int width = 500 ; 
	int height = 500 ; 
	BufferedImage bim = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB) ; 
	Graphics2D g2 = bim.createGraphics() ; 
	ArrayList<double[]> allvals = null ;
	ArrayList<ArrayList<double[]>> allPairs = new ArrayList<ArrayList<double[]>>() ; 
	int period = 400 ; 
	int tcount = 0 ; 
	double profit ; 
	double ntrades =  0 ; 
	double upl = 0 ; 
	double mins = 0  ; 	
	ArrayList<Integer>slopeIndicator=null ; 
	int jcounter = 0 ; 
	//int nCurrencies = 10 ; 
	String[] stateLabels = {"profit:","#trades:","pips/trade:","UPL:","time(days):"} ; 
	String[] globalLabels = {"pair:","profit:","#trades:","pips/trade:","total UPL:","trades open"} ; 
	double[] stateVars = new double[stateLabels.length] ; 
	String[] globalstateVars = new String[globalLabels.length] ; 
	String[] pairs = {"AUDCAD","AUDCHF","AUDJPY","AUDNZD","AUDUSD","CADCHF","CADJPY","CHFJPY","EURAUD","EURCAD","EURCHF","EURGBP","EURJPY","EURNZD","EURUSD","GBPAUD","GBPCAD","GBPCHF","GBPJPY","GBPUSD","NZDCHF","NZDJPY","NZDUSD","USDCAD","USDCHF","USDJPY","USDSGD"} ; 	
	String currencyHovered = "" ; 
	ArrayList<String> activeCurrencies = new ArrayList<String>() ; 
	int pairIndex = 0 ; 
	boolean[] isColored = {false,true,false,true,true,false} ;
	
	HashMap<String,Integer> currs = new HashMap<String,Integer>() ;
	
	ArrayList<FXBOT> bots ; 
	FXBOT currentBot = null ; 
	
	//mouse variables
	int mouseX ; 
	int mouseY ; 
	int clickedX ; 
	int clickedY ; 
	
	public void reset(){
		pairIndex = 0 ;
		currs = new HashMap<String,Integer>() ;
		activeCurrencies = new ArrayList<String>() ; 
		allPairs = new ArrayList<ArrayList<double[]>>() ; 
		initHedgeMap() ; 
	}
	
	public hedgeFund() {
		initframe() ; 	
		addMouseMotionListener(this) ; 		
		addMouseListener(this) ; 
		initHedgeMap() ; 
	}
	public void initHedgeMap(){
		currs.put("AUD",0) ; currs.put("CAD", 0) ; currs.put("CHF", 0) ; currs.put("EUR",0) ; 
		currs.put("GBP",0) ; currs.put("NZD",0) ; currs.put("USD",0) ; 
	}
	
	public void run(){}
	
	public int findIndex(String id){
		for(int i=0;i<activeCurrencies.size();i++)
			if(activeCurrencies.get(i).equals(id))
				return i ; 
		return 0 ; 
	}
	public void update(double[] newvals,String pair){		
		
		if(!activeCurrencies.contains(pair)){		
			activeCurrencies.add(pair) ;
			allPairs.add(new ArrayList<double[]>()) ; 
			allPairs.get(allPairs.size()-1).add(newvals) ; 
			if(currentBot==null)
				currentBot = findBot(pair) ; 
		}
		else {
			int ind = findIndex(pair) ;
			allPairs.get(ind).add(newvals) ; 
		}
		allvals = allPairs.get(pairIndex) ; 	
	}
	public void advance(){
		sumBots() ; 
		tcount ++ ; 
		if(tcount > period)
			jcounter++ ; 		
		render() ; 
		repaint() ; 
	}
	public FXBOT findBot(String pair){
		for(int i=0;i<bots.size();i++)
			if(bots.get(i).pair.equals(pair))
				return bots.get(i) ; 
		return null ; 
	}
	public void sumBots(){
		NumberFormat form = new DecimalFormat("#0000.0000") ;	
		if(currentBot!=null)
			globalstateVars[0] = currentBot.pair ; 
		else globalstateVars[0] = "n/a" ; 
		double gProfit = 0 ; double gNTrades = 0 ; double gppt = 0 ; double gUPL = 0 ; double gOpen = 0 ; 
		for(int i=0;i<bots.size();i++){
			gProfit += bots.get(i).profits ; 
			gNTrades += bots.get(i).ntrades ; 
			gUPL += bots.get(i).upls ; 
			if(bots.get(i).currentDirection!=0)
				gOpen++ ; 		
		}
		if(gNTrades>0)gppt = gProfit/gNTrades ; else gppt = 0 ; 
		globalstateVars[1] = form.format(gProfit) ; globalstateVars[2] = form.format(gNTrades) ; globalstateVars[3] = form.format(gppt) ; 
		globalstateVars[4] = form.format(gUPL) ; globalstateVars[5] = form.format(gOpen) ; 
	}
	
	public void render(){	
		bim = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB) ;
		g2 = bim.createGraphics() ;
		Font f = new Font(null,Font.ROMAN_BASELINE,11) ; 
		g2.setFont(f) ; g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		NumberFormat formatter2 = new DecimalFormat("#0000.0000") ;	
		NumberFormat formatter3 = new DecimalFormat("#000.0") ;	
		double maxInRange = 0 ; 
		double minInRange = 0 ; 
		maxInRange = max(allvals.subList(jcounter, allvals.size()-1)) ;
		minInRange = min(allvals.subList(jcounter, allvals.size()-1)) ; 
		double range = maxInRange - minInRange ; 
		double leftmarg = 80 ; 
		double rightmarg = 50 ;
		double topmarg = 150 ; 
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
		g2.setColor(Color.BLACK); g2.fill(new Rectangle2D.Double(0,0,getWidth(),topmarg/4));
		double nStateVars = stateVars.length ; double stateStep = getWidth()/nStateVars ;		
		g2.setColor(Color.CYAN);
		for(int lab=0;lab<stateVars.length;lab++){
			//g2.drawString(stateLabels[lab], (int)(lab*stateStep), (int)(topmarg/6));
			if(currentBot!=null){
				g2.drawString(formatter2.format(currentBot.allParams[lab]), (int)(lab*stateStep)+5, (int)(topmarg/10));
			}
		}
		//draw the global variables
		double nGlobalStateVars = globalstateVars.length ; stateStep = getWidth()/nGlobalStateVars ;		
		for(int lab=0;lab<globalstateVars.length;lab++){
			//g2.drawString(stateLabels[lab], (int)(lab*stateStep), (int)(topmarg/6));
			if(currentBot!=null){
				g2.setColor(Color.MAGENTA);
				if(isColored[lab] && Double.parseDouble(globalstateVars[lab])<0)g2.setColor(Color.RED);
				else if(isColored[lab] && Double.parseDouble(globalstateVars[lab])>0)g2.setColor(Color.GREEN);
				g2.drawString(globalstateVars[lab], (int)(lab*stateStep)+5, (int)(topmarg/6));
			}
		}	
		// draw the bounding box around the plot
		g2.setStroke(new BasicStroke(3)) ; g2.setColor(Color.BLACK) ; 
		g2.draw(new Rectangle2D.Double(leftmarg-1,topmarg-1,w+1,h+1)); g2.setStroke(new BasicStroke(1)) ;
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
			g2.drawString(formatter3.format(tcount/60),(int)xpos - rotCorrection,(int)(h+topmarg+32)) ;
		}

		// draw the grid:
		font = new Font(null, Font.ROMAN_BASELINE, 11) ; g2.setFont(font);
		double xgridstep = w/7 ; double ygridstep = (topmarg-topmarg/3)/4 ; //ncurrencies = 6*5 = 30 ; 
		int cCount = 0 ; 
		for(int xg=0;xg<7;xg++)
			for(int yg=0;yg<4;yg++){
				if(cCount<pairs.length){
					g2.setColor(Color.LIGHT_GRAY) ;
					int x = (int)(leftmarg+xg*xgridstep) ; int y = (int)(topmarg/4+yg*ygridstep) ; 
					if(mouseX <x+xgridstep && mouseX>x && mouseY<y+ygridstep && mouseY>y){
						currencyHovered = pairs[cCount] ; g2.setColor(Color.WHITE) ;
						if (activeCurrencies.contains(currencyHovered))	g2.setColor(new Color(122,122,0)) ;
					}
					else if(activeCurrencies.contains(pairs[cCount])){
						if(findBot(pairs[cCount]).currentDirection > 0)
							g2.setColor(new Color(0,125,0)) ;
						else if(findBot(pairs[cCount]).currentDirection < 0)
							g2.setColor(new Color(125,0,0)) ;		
						else g2.setColor(new Color(150,150,150));
					}
					g2.fill(new Rectangle2D.Double(x,y,xgridstep,ygridstep)) ;	
					if(clickedX <x+xgridstep && clickedX>x && clickedY<y+ygridstep && clickedY>y){
						pairIndex = findIndex(pairs[cCount]) ; 	
						this.currentBot = findBot(pairs[cCount]) ; 						
					}
					g2.setColor(new Color(50,0,100)) ;
					g2.drawString(pairs[cCount], x, y+15);
					cCount++ ; 
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
		g2.drawImage(bim,0, 0, null) ; 	
	}
	
	public void initframe(){
		jf = new JFrame() ; 
		jf.setPreferredSize(new Dimension(width,height));
		jf.add(this) ; 
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);	
		jf.pack();
	}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		this.mouseX = e.getX() ; 
		this.mouseY = e.getY() ; 
	}

	public void mouseClicked(MouseEvent e) {
		this.clickedX = e.getX() ; 
		this.clickedY = e.getY() ; 
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	///private class for running the strategy on. 
	private class FXBOT{
		ArrayList<Double> raw = new ArrayList<Double>() ; 
		ArrayList<ArrayList<Double>> mvgs = new ArrayList<ArrayList<Double>>() ; 
		ArrayList<Integer> periods = new ArrayList<Integer>() ; 
		ArrayList<Integer> slopes = new ArrayList<Integer>() ; 
		int currentDirection = 0 ; 
		double slopeThresh = 0 ; 
		double entries = 0 ; 
		double upls = 0 ; 
		double stop = 0 ; 
		double tp = 0 ; 
		double profits = 0 ; 
		double ntrades = 0 ;
		double spread = 0 ; 
		double commission = 0.00007 ; 
		double[] allParams = new double[5] ; 
		String pair = "" ; 
		String c1 = "" ; 
		String c2 = "" ; 
			
		public FXBOT(String pair, double tp, double stop, int mvg1, int mvg2, int mvg3, double slope){ // where pers is a 1x3 array of mvg periods
			this.pair = pair ; c1 = pair.substring(0,3) ; c2 = pair.substring(3,6) ; 
			int[] pers = {mvg1,mvg2,mvg3} ; 
			this.slopeThresh = slope ; 
			this.tp = tp ; 
			this.stop = stop ; 
			for(int i=0;i<pers.length;i++){
				periods.add(pers[i]) ; 
				mvgs.add(new ArrayList<Double>()) ; 
			}			
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
			if(currentDirection != 0 || raw.size() < periods.get(2) ) //if already in a trade
				return ; 
			
			int cthresh = 8 ;
			
			if (direction==1 && currs.get(c1) <= cthresh && currs.get(c2) >= -cthresh){//buy
				currs.put(c1, currs.get(c1)+1) ; currs.put(c2, currs.get(c2)-1) ; 
				currentDirection = 1 ; 
				entries = raw.get(raw.size()-1) ; 
				upls = 0 ; 
				ntrades++ ; 
		
			//	System.out.println("buying") ;
			}
			else if(direction==-1 && currs.get(c1) >= -cthresh && currs.get(c2) <= cthresh){
				currs.put(c1, currs.get(c1)-1) ; currs.put(c2, currs.get(c2)+1) ; 
				currentDirection = -1 ;
				entries = raw.get(raw.size()-1) ; 
				upls = 0 ; 
				ntrades ++ ; 
			//	System.out.println("selling") ;
			}
		}
		
		public void checkTrades(){
			//System.out.println("checking trades,upl= "+ upls[0]) ;
			if(currentDirection==0 )return ; //if no trade in progress, return
			else if(currentDirection==1){
				upls = raw.get(raw.size()-1)-entries ; 
				if(upls > tp || upls < stop){
					profits = profits + upls - (spread+commission) ; 
			//		System.out.println("Closing buy, profit = " + profits[0]) ; 
					currentDirection = 0 ; 
					currs.put(c1, currs.get(c1)-1) ; currs.put(c2, currs.get(c2)+1) ; 

				}
			}
			else if(currentDirection==-1){
				upls = entries-raw.get(raw.size()-1) ; 		
				if(upls > tp || upls < stop){
					profits = profits + upls - (spread+commission) ; 
				//	System.out.println("closing sell, profit = " + profits[0]) ; 
					currentDirection = 0 ; 
					currs.put(c1, currs.get(c1)+1) ; currs.put(c2, currs.get(c2)-1) ; 

				}
			}
		}
		
		public void checkState(){ // check the state of the mvgs, to decide whether or not to buy/sell
			int index = raw.size()-1 ; 
			if(raw.size()>=2){
				double slope = mvgs.get(2).get(index) - mvgs.get(2).get(index-1) ; 
				//System.out.println("slope = " + slope) ; 
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
			allParams[0] = profits ; allParams[1] = ntrades ; allParams[2] = profits/ntrades ; allParams[3] = upls ;   
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
	}	
	
	public ArrayList<FXBOT> setBots(hedgeFund lpm,String[] goodBots){
		
		int m1start = 1 ,m2start = 35, m3start = 100, m1incr = 5, m2incr = 5, m3incr = 25; 
		double threshstart = 0.000005 ; double sthreshincr = .000001 ; 
			
		ArrayList<FXBOT> cbots = new ArrayList<FXBOT>() ;	
				
		cbots.add(lpm.new FXBOT("AUDCAD",.0013,-.0125,m1start+2*m1incr,m2start+11*m2incr,m3start+3*m3incr,threshstart+3*sthreshincr)) ; // 2,11,3,3
		cbots.add(lpm.new FXBOT("AUDCHF",.0023,-.0150,m1start+5*m1incr,m2start+10*m2incr,m3start+5*m3incr,threshstart+11*sthreshincr)) ; // 5,10,5,11
		cbots.add(lpm.new FXBOT("AUDNZD",.0029,-.0195,m1start+1*m1incr,m2start+11*m2incr,m3start+4*m3incr,threshstart+6*sthreshincr)) ; // 1,11,4,6
		cbots.add(lpm.new FXBOT("AUDUSD",.0027,-.0155,m1start+3*m1incr,m2start+11*m2incr,m3start+1*m3incr,threshstart+9*sthreshincr)) ; // 3,11,1,9
		cbots.add(lpm.new FXBOT("CADCHF",.0051,-.0075,m1start+6*m1incr,m2start+8*m2incr,m3start+4*m3incr,threshstart+11*sthreshincr)) ; // 6,8,4,11
		cbots.add(lpm.new FXBOT("EURAUD",.0023,-.0115,m1start+1*m1incr,m2start+9*m2incr,m3start+4*m3incr,threshstart+4*sthreshincr)) ; // 1,9,4,4
		cbots.add(lpm.new FXBOT("EURCAD",.0057,-.0110,m1start+2*m1incr,m2start+8*m2incr,m3start+6*m3incr,threshstart+1*sthreshincr)) ; // 2,8,6,1
		cbots.add(lpm.new FXBOT("EURCHF",.0015,-.0165,m1start+1*m1incr,m2start+11*m2incr,m3start+1*m3incr,threshstart+1*sthreshincr)) ; // 1,11,1,1
		cbots.add(lpm.new FXBOT("EURGBP",.0021,-.0110,m1start+1*m1incr,m2start+8*m2incr,m3start+5*m3incr,threshstart+13*sthreshincr)) ; // 1,8,5,13
		cbots.add(lpm.new FXBOT("EURNZD",.0019,-.0110,m1start+1*m1incr,m2start+3*m2incr,m3start+1*m3incr,threshstart+4*sthreshincr)) ; // 1,3,1,4
		cbots.add(lpm.new FXBOT("EURUSD",.0049,-.0105,m1start+2*m1incr,m2start+6*m2incr,m3start+7*m3incr,threshstart+4*sthreshincr)) ; // 2,6,7,4
		cbots.add(lpm.new FXBOT("GBPAUD",.0029,-.0185,m1start+1*m1incr,m2start+1*m2incr,m3start+7*m3incr,threshstart+15*sthreshincr)) ; // 1,1,7,15
		cbots.add(lpm.new FXBOT("GBPCAD",.0013,-.0110,m1start+1*m1incr,m2start+11*m2incr,m3start+5*m3incr,threshstart+6*sthreshincr)) ; // 1,11,5,6
		cbots.add(lpm.new FXBOT("GBPCHF",.0015,-.0150,m1start+1*m1incr,m2start+8*m2incr,m3start+2*m3incr,threshstart+9*sthreshincr)) ; // 1,8,2,9
		cbots.add(lpm.new FXBOT("GBPNZD",.0021,-.0125,m1start+1*m1incr,m2start+6*m2incr,m3start+4*m3incr,threshstart+8*sthreshincr)) ; // 1,6,4,8
		cbots.add(lpm.new FXBOT("GBPUSD",.0021,-.0065,m1start+2*m1incr,m2start+9*m2incr,m3start+8*m3incr,threshstart+15*sthreshincr)) ; // 2,9,8,15
		cbots.add(lpm.new FXBOT("NZDCHF",.0013,-.0135,m1start+1*m1incr,m2start+7*m2incr,m3start+3*m3incr,threshstart+14*sthreshincr)) ; // 1,7,3,14
		cbots.add(lpm.new FXBOT("NZDUSD",.0031,-.0190,m1start+3*m1incr,m2start+10*m2incr,m3start+10*m3incr,threshstart+4*sthreshincr)) ; // 3,10,1,4
		cbots.add(lpm.new FXBOT("USDCAD",.0019,-.0175,m1start+2*m1incr,m2start+9*m2incr,m3start+4*m3incr,threshstart+7*sthreshincr)) ; // 2,9,4,7
		cbots.add(lpm.new FXBOT("USDCHF",.0039,-.0185,m1start+5*m1incr,m2start+6*m2incr,m3start+6*m3incr,threshstart+10*sthreshincr)) ; // 5,6,6,10
		
		ArrayList<FXBOT> retBots = new ArrayList<FXBOT>() ;	
		
		HashMap<String,Double>spreads = GetSpreads.getCommonSpreads() ; 
		
		for(int i=0;i<goodBots.length;i++){
			for(int j=0;j<cbots.size();j++)
				if(cbots.get(j).pair.equals(goodBots[i])){
				FXBOT b = cbots.get(j) ;
				//System.out.println(b.pair) ; 
				b.spread = spreads.get(b.pair) ; 
				retBots.add(b) ;		
			}
		}	
		return retBots ; 	
	}
	
	public static void main(String[] args){		
		hedgeFund lpm = new hedgeFund() ; 
		ArrayList<dukas> arr = null ; 
		int[] botNumbers = {3,4,6,7,11,13,16,17,18} ; 
		String[] goodBots = {"AUDNZD","AUDUSD","EURAUD","EURCAD","EURUSD","GBPCAD","GBPUSD","NZDCHF","NZDUSD"} ; 
		for(int ccy=0;ccy<20;ccy++){
		//	int[] botNumbers = {ccy} ; 
			ArrayList<ArrayList<Double>> stores = new ArrayList<ArrayList<Double>>() ; 

		ArrayList<ArrayList<dukas>> allcurrs = new ArrayList<ArrayList<dukas>>() ; 
		ArrayList<FXBOT> cbots = lpm.setBots(lpm,goodBots) ; 
		//System.out.println("Cbots size  = " + cbots.size()) ; 
		for(int i=0;i<cbots.size();i++){//lpm.pairs.length
				arr = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\FX1MYR\\",cbots.get(i).pair+"_UTC_1 Min_Bid_2014.01.01_2015.03.20.csv") ;
				allcurrs.add(arr) ; 	
		}
		//System.out.println("allcurrs size = " + allcurrs.size()) ; 
		//ArrayList<ArrayList<Double>> stores = new ArrayList<ArrayList<Double>>() ; 
		for(int run=0;run<1000;run++){
			//for(int c=0;c<cbots.size();c++)
			//	System.out.println("running " + cbots.get(c).pair) ; 
			stores.add(new ArrayList<Double>()) ; 
			//for(FXBOT fxb:cbots){
			//	fxb.reset() ; 
			//}
			lpm.reset() ; 
			cbots = lpm.setBots(lpm,goodBots) ;
			lpm.bots = cbots ; 
			int startIndex = (int)(Math.random()*100000) ; 
			int endIndex = startIndex + 60*24*120 ; 

			for(int i=startIndex;i<endIndex;i++){
				for(int c=0;c<allcurrs.size();c++){
					arr = allcurrs.get(c) ; 
					double[] newvals = {arr.get(i).open,arr.get(i).close,arr.get(i).low,arr.get(i).high} ;
				//	System.out.println("c = " + c + " cbots size = " + cbots.size() + " i = " + i + " arrsize  = " + arr.size()) ; 
					cbots.get(c).update(arr.get(i).open);
				//	lpm.update(newvals,cbots.get(c).pair) ;				
				}
			//	try{Thread.sleep(1);}catch(Exception e){}				
			//	lpm.advance() ; 
			}
			double totalp = 0 ; double ntrades = 0 ; 
			for(int cb=0;cb<cbots.size();cb++){
				totalp += cbots.get(cb).profits/cbots.get(cb).ntrades ;
				ntrades += cbots.get(cb).ntrades ; 
			}
			System.out.println("total profit = " + totalp/cbots.size()+", ntrades = " + ntrades) ;
			stores.get(run).add(totalp/cbots.size()) ; 
		}
//		SaveArray.saveArrayList(stores, "stores_"+cbots.get(0).pair) ;

		
		}
		System.exit(0) ;
	}
}
