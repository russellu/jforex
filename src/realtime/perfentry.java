package realtime;

import java.util.ArrayList;
import java.util.Arrays;
import jforex.*; 

/**
 * run matlab - code to run the strategy you code in matlab
 * the class has to be encapsulated in that it has an ontick method that everything else depends on
 * call the ontick method with the historical data, and from there evaluate the trade
 * 
 * put all currencies on the same plot, show all upl at same time
 * use that to sync your trades?
 * 
 * heding with multiple currencies and improving the entrance criteria:
 * multi currency hedging never have a buy on in USD
 * 
 * strategy convolution use only the strategy that has been most successful in the past n timepoints, n > several days/hrs
 * fix the entry so it doesn't just enter whenever but rather at a high/low
 * 
 * 
 * @author russ
 *
 */

public class perfentry {
	
	ArrayList<Double> profits = new ArrayList<Double>() ;
	ArrayList<Double> uplarr = new ArrayList<Double>() ; 
	ArrayList<Double> entryarr = new ArrayList<Double>() ; 
	ArrayList<Double> alltics = new ArrayList<Double>() ; 
	
	plotforex pf = null ; 
	
	int windsize = 200 ;
	int entrysize = 200 ;
	double upl = 0 ; 
	double prevprice = 0 ; 
	double entryprice = 0 ; 
	double hardstop = .90 ; ;
	
	int ticcount = 0 ;
	int incount = 0 ; 
	int ntrades = 0 ;
	int outcount = 0 ; 
	
	int dircount = 0 ;
	boolean up = false ;
	boolean down = false ; 
	
	boolean buy = false ; 
	boolean sell = false ;
	
	boolean disp = false ; 
	
	
	public perfentry(){
		if(this.disp==true)
			pf = new plotforex() ; 
	}
		
	public perfentry(boolean disp){
		this.disp = disp ;
		if(this.disp==true)
			pf = new plotforex() ; 
	}
	
	public perfentry(boolean disp,int windsize,int entrysize){
		this.disp = disp ;
		this.windsize = windsize ; 
		this.entrysize = entrysize ; 
		
		if(this.disp==true)
			pf = new plotforex() ; 
	}
	public void setTitle(String title){
		if(pf != null)
			pf.setTitle(title);
	}
	
	public void feedtick(double newprice){	
		alltics.add(newprice) ; 
		if(ticcount > entrysize){
			entryarr = Primitives.chunk(alltics,ticcount-entrysize,ticcount) ; 
			entryarr = Primitives.subtractval(entryarr, alltics.get(ticcount-(entrysize+1))) ; 
			
			if(disp==true){			
				//update display price
				if(buy==true)
					pf.update(newprice,1);
				else if(sell==true)
					pf.update(newprice,2);
				else 
					pf.update(newprice,0);			
				//update display variables
				pf.setupl(uplarr);
				pf.setprofit(profits);
				try{Thread.sleep(25);}catch(Exception e){}					
			}
					
			// update upl and exit the buy/sell if at a hard stop
			if(buy==true){
				upl = newprice - entryprice ; 
				uplarr.add(upl) ; 
				if(upl > hardstop || upl < -hardstop){
					//System.out.println("exiting buy, upl = " + upl) ;
					profits.add(upl) ; 
					upl = 0 ;
					buy = false ;
					uplarr = new ArrayList<Double>() ; 		
				}					
			}
			else if(sell==true){
				upl = entryprice - newprice ;
				uplarr.add(upl) ; 
				if(upl > hardstop || upl < -hardstop){
					//System.out.println("exiting sell, upl = " + upl) ;
					profits.add(upl) ; 
					upl = 0 ; 
					sell = false ;
					uplarr = new ArrayList<Double>() ; 		
				}
			}					
			
			// check for exit based on upl, if upl == max of current window
			if(buy || sell){
				incount = incount + 1 ;
				if(buy==true && incount > windsize){
					//System.out.println("upl = " + upl + " max = " + Primitives.max(Arrays.copyOfRange(Primitives.list2arr(uplarr),incount-windsize,incount)));
					if(upl == Primitives.min(Arrays.copyOfRange(Primitives.list2arr(uplarr),incount-windsize,incount))){
					//	System.out.println("exiting buy, upl = " + upl) ;
						buy = false ; 
						profits.add(upl) ; 
						uplarr = new ArrayList<Double>() ;		
						incount = 0 ;
						outcount = 0 ;
					} 				
				}
				else if (sell==true && incount > windsize){
					//System.out.println("upl = " + upl + " max = " + Primitives.max(Arrays.copyOfRange(Primitives.list2arr(uplarr),incount-windsize,incount)));
					if(upl == Primitives.min(Arrays.copyOfRange(Primitives.list2arr(uplarr),incount-windsize,incount))){
					//	System.out.println("exiting sell, upl = " + upl) ;
						sell = false ; 
						profits.add(upl) ; 
						uplarr = new ArrayList<Double>() ; 		
						incount = 0 ; 
						outcount = 0 ;
					} 				
				}
			}		
			else if(!buy && !sell){
				outcount = outcount + 1 ; 
				if(entryarr.get(entryarr.size()-1) == Primitives.min(entryarr) && outcount > entrysize){
					sell = true ; 
					entryprice = newprice ;
					ntrades ++ ;
				}
				else if(entryarr.get(entryarr.size()-1) == Primitives.max(entryarr) && outcount > entrysize){
					buy = true ;
					entryprice = newprice ; 
					ntrades ++ ; 
				}			
			}	
		}		
		prevprice = newprice ;  
		ticcount = ticcount + 1 ; 		
	}
	

	public static void main(String[]args){	
		//String title = "AUDUSD_UTC_1 Min_Bid_2007.01.01_2014.08.22.csv" ;	
		String title = "AUDUSD_UTC_5 Mins_Bid_2007.01.01_2014.08.22.csv" ; 
		//String title = "GBPJPY_UTC_5 Mins_Bid_2007.01.01_2014.08.22.csv" ; 
		//String title = "USDJPY_UTC_1 Min_Bid_2013.01.01_2014.08.22.csv" ;
		ArrayList<dukas> arr = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\fxfiles",title) ;
		plotforex pf = null ; 
		plotforex rf = null ; 
		boolean plotrf = false ; 
		for(int run=2;run<200;run++){
			pf = new plotforex() ; 
			pf.setTitle(new String("entrythresh = " + run + " file = " + title));
			for(int wsize=2;wsize<300;wsize+=2){ //20+3*65	
				ArrayList<Double>finalresults = new ArrayList<Double>() ;	
				if(plotrf){ 
					rf = new plotforex() ; 
					rf.setTitle(new String("entrythresh = " + run + " entrance window = " + wsize + " file = " + title));
				}
				for(int rndm = 1;rndm<25;rndm++){
					perfentry me = new perfentry(false,wsize,run) ; 	
					me.setTitle("entrythresh = " + run + " entrance window = " + wsize + " file = " + title);
					int rnd = (int)(Math.random()*(double)arr.size()/2)+wsize*4 ; 
					for(int i=rnd;i<rnd+(1440/5)*30;i++){
						me.feedtick(arr.get(i).open);
					}
					double rmean = Primitives.mean(Primitives.list2arr(me.profits)) ;
					finalresults.add(rmean);
					System.out.println("wsize = " + wsize + " ppt = " + finalresults.get(finalresults.size()-1)) ;
					if(plotrf){if(rmean > 0)rf.update(rmean,1);else rf.update(rmean,2);}
				}			
				double totalmean = Primitives.mean(Primitives.list2arr(finalresults)) ; 
				if(totalmean > 0)
					pf.update(totalmean,1);
				else pf.update(totalmean,2);
			}
			//System.out.println(Primitives.mean(Primitives.list2arr(finalresults))) ; 
		}
		//new plot(finalresults) ; 
		//for(int i=0;i<)
			//System.out.println(arr.get(i).open) ;
	}
}
