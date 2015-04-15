package realtime;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jforex.Primitives;

public class plotforex extends JPanel implements Runnable{

	int width = 1050 ; 
	int height = 600 ; 
	BufferedImage bim = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB) ; 
	Graphics2D g2 = bim.createGraphics() ; 
	JFrame jf = null ; 	
	ArrayList<Double> datas = new ArrayList<Double>() ; 
	ArrayList<Integer> markers = new ArrayList<Integer>() ; 
	
	ArrayList<Double>uplbox = new ArrayList<Double>() ; 
	ArrayList<Double>profits = new ArrayList<Double>() ; 
	
	int ntimes = 500 ;
	
	//positions on the screen
	int x = 0 ; 
	int y = 0 ;
	
	int ntrades = 0 ;
	double totalprofit = 0 ; 
	
	String title = "" ; 
	
	public plotforex(){
		initframe() ; 
	}
	public plotforex(int x,int y){
		this.x = x ; 
		this.y = y ; 
		initframe() ; 
	}
	
	//update the unrealized profit and loss of the display
	public void setupl(ArrayList<Double>upl){
		this.uplbox = upl ; 	
	}
	
	public void setprofit(ArrayList<Double> profits){
		this.totalprofit = Primitives.sum(profits) ; 
		this.ntrades = profits.size() ; 
		this.profits = profits ; 
	}
	
	public void setTitle(String title){
		this.title = title ; 
	}
	
	public void update(double input,int direction){
		datas.add(input) ; 
		markers.add(direction) ; 
		//System.out.println("direction = " + direction) ; 
		render() ; 
		repaint() ; 		
	}
	
	public void render(){

		bim = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB) ;
		g2 = bim.createGraphics() ;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Font f = new Font(null,Font.ROMAN_BASELINE,11) ; 
		g2.setFont(f);
		
		NumberFormat formatter = new DecimalFormat("#0.0000");
		NumberFormat formatter2 = new DecimalFormat("#0.00000");
		NumberFormat formatter3 = new DecimalFormat("#0");
		
		//create a temporary array showing only a certain range of prices
		ArrayList<Double>tempdatas = new ArrayList<Double>() ; 
		ArrayList<Integer>tempmarkers = new ArrayList<Integer>() ; 
		if(datas.size() > ntimes){
			tempdatas = Primitives.arr2list(Arrays.copyOfRange(Primitives.list2arr(datas),datas.size()-ntimes,datas.size())) ; 		
			tempmarkers = Primitives.arr2list(Arrays.copyOfRange(Primitives.intlist2arr(markers),markers.size()-ntimes,markers.size())) ; 
		}
		else {
			tempdatas = datas ; 
			tempmarkers = markers ; 
		}

		// set up some GUI variables to control scaling, text location etc
		double lmarg = 50 ;//left margin size (pixels), anything that is not margin is active plot
		double rmarg = 50 ; //right margin size
		double tmarg = 50 ;// top margin size
		double bmarg = 50 ; //bottom margin size
		double max = Primitives.max(Primitives.list2arr(tempdatas)) ;
		double min = Primitives.min(Primitives.list2arr(tempdatas)) ; 
		double range = max-min ; 
		double w = getWidth() ; 
		double h = getHeight()-(tmarg+bmarg) ; 
		g2.setColor(Color.BLACK);
		g2.fill(new Rectangle2D.Double(0,0,w,getHeight()));
		g2.setColor(new Color(120,120,120));
		g2.fill(new Rectangle2D.Double(0,0,lmarg,getHeight()));
		g2.fill(new Rectangle2D.Double(0,0,w,tmarg)) ; 
		g2.fill(new Rectangle2D.Double(0,getHeight()-bmarg,w,bmarg));
		double xstep = (w-(lmarg+rmarg))/(double)tempdatas.size() ; 
		
		// draw the upl box at the bottom right
		int nlabels = 20 ; 
		double ystep = range/(double)nlabels ;		
		//g2.setColor(new Color(0,0,0)) ;
		//g2.fill(new Rectangle2D.Double(0,0,getWidth(),tmarg/2));
		if (uplbox.size() > 1){
			double boxincr = 300/(double)uplbox.size() ;  ; 
			double uplmax = Primitives.max(Primitives.list2arr(uplbox)) ; 
			double uplmin = Primitives.min(Primitives.list2arr(uplbox)) ; 
			double uplrange = uplmax-uplmin ; 
			//System.out.println(uplrange) ; try{Thread.sleep(59);}catch(Exception e){}
			
			double cstep = 255/uplrange ; 
			//System.out.println(cstep) ; 

			for(int i=0;i<uplbox.size();i++){
				//System.out.println("int = " + (int)((cstep*(uplbox.get(i)-uplmin)))) ; 
				g2.setColor(new Color((int)(cstep*(uplbox.get(i)-uplmin)),(int)(cstep*(uplbox.get(i)-uplmin)),0)) ; 
				g2.fill(new Rectangle2D.Double(lmarg*10+i*boxincr,getHeight()-bmarg/2,boxincr,bmarg/2));
				
			}
			
		}
		
		
		
		
		g2.setColor(Color.RED);
		if(uplbox.size()>0)
			g2.drawString("current trade upl = " + formatter2.format((uplbox.get(uplbox.size()-1))),(int)lmarg*10,getHeight()-30);
		else g2.drawString("current trade upl = " + formatter2.format((0)),(int)lmarg*10,getHeight()-30);
		
		int srate = 5 ; 
		
		//draw the profit, number of trades, etc
		g2.setColor(Color.YELLOW);
		g2.drawString("total profit = " + formatter.format(totalprofit),0,getHeight()-10) ; 
		g2.setColor(Color.BLACK) ; 
		g2.drawString("number of trades = " + Integer.toString(ntrades) , 0, getHeight()-20);
		g2.drawString("current profit/trade = " + formatter2.format(totalprofit/(double)ntrades),0,getHeight()-30);
		g2.drawString("elapsed time(minutes) = " + datas.size()*srate + ", (hours) = " + (datas.size()*srate)/60 + ", days = " + (datas.size()*srate)/(60*24), (int)lmarg*4, (int)getHeight()-30);
		
		//draw the profits box (of all trades)
		double profmax = Primitives.max(profits) ; 
		double profmin = Primitives.min(profits) ; 
		double profrange = profmax-profmin ; 
		double nprofs = profits.size() ; 
		double cstep = 255/profrange ; 
		double boxincr = 300/nprofs ; 
		for(int i=0;i<nprofs;i++){
			//g2.fill(new Rectangle2D.Double(lmarg*4,getHeight()-bmarg/2,300,bmarg));
			g2.setColor(new Color((int)(cstep*(profits.get(i)-profmin)),(int)(cstep*(profits.get(i)-profmin)),(int)(cstep*(profits.get(i)-profmin)))) ; 
			g2.fill(new Rectangle2D.Double(i*boxincr+lmarg*4,getHeight()-bmarg/2,boxincr,bmarg/2));
		}
		g2.setColor(Color.BLACK) ;
		g2.draw(new Rectangle2D.Double(lmarg*4,getHeight()-bmarg/2,300,bmarg/2));
		
		// draw the y axis labels
		for(int i=0;i<nlabels+1;i++){
			double y1 = ((i*ystep)/range)*h + tmarg ;
			g2.setColor(Color.DARK_GRAY) ;
			g2.draw(new Line2D.Double(lmarg,y1,w,y1));
			g2.setColor(Color.BLACK);
			g2.drawString(formatter.format(max-i*ystep),5,(int)y1);
			g2.draw(new Line2D.Double(lmarg-4,y1,lmarg,y1));
			
		}
		
		
		
		// draw the actual lines
		for(int i=0;i<tempdatas.size()-1;i++){
			//System.out.println("markers i = " + markers.get(i)) ; 
			if(tempmarkers.get(i)==0)g2.setColor(Color.WHITE);
			else if(tempmarkers.get(i)==1)g2.setColor(Color.GREEN);
			else if(tempmarkers.get(i)==2)g2.setColor(Color.RED);
			
			double y1 = ((max-tempdatas.get(i))/range)*h+tmarg ; 
			double y2 = ((max-tempdatas.get(i+1))/range)*h+tmarg ; 
			double x1 = i*xstep+lmarg ;
			double x2 = (i+1)*xstep+lmarg ; 
			g2.draw(new Line2D.Double(x1,y1,x2,y2)) ; 			
		}
		
		// draw the horizontal axis at the top
		
		Font font = new Font(null, Font.ROMAN_BASELINE, 11);    
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(Math.toRadians(-55), 0, 0);
		Font rotatedFont = font.deriveFont(affineTransform);
		g2.setFont(rotatedFont);
		g2.setColor(Color.BLACK);
		double xlength = w-(lmarg+rmarg) ; 
		double nticks = 40 ;
		double ticklength = xlength/nticks ; 
		double xtickincr = datas.size()/nticks ;
		for(int i=0;i<nticks+1;i++){
			double x1 = lmarg + i*ticklength ; 
			g2.draw(new Line2D.Double(x1,tmarg-4,x1,tmarg)) ;
			g2.drawString(formatter3.format(i*xtickincr),(int)x1,(int)tmarg-4) ; 			
		}
		g2 = bim.createGraphics() ; 
		
		// DRAW the title values
		g2.setFont(new Font(null,Font.TRUETYPE_FONT,15));
				g2.setColor(new Color(50,80,120)) ;
				g2.drawString(title,15,15) ;
	}
	
	public void run(){		
		//while(true){
			//try{Thread.sleep(50);}catch(Exception e){}
			//render() ; 
			//repaint() ; 						
		//}	
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
		System.out.println(jf.getWidth()) ; 
		
		//Toolkit tk = Toolkit.getDefaultToolkit();
		//Dimension screenSize = tk.getScreenSize();
		//final int WIDTH = screenSize.width;
		//final int HEIGHT = screenSize.height;
		// Setup the frame accordingly
		// This is assuming you are extending the JFrame //class
		//jf.setSize(WIDTH/2, HEIGHT/2);
		jf.setLocation(x, y);

		
	}
	
	
	public static void main(String[]args){	
		plotforex pf = new plotforex(200,200) ; 		
		new Thread(pf).start() ; 
		for(int i=0;i<1000;i++){
			try{Thread.sleep(50) ;}catch(Exception e){}
			pf.update(Math.random(),0);
		}
	}
}
