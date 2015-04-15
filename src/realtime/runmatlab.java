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
 * 
 * 
 * @author russ
 *
 */

public class runmatlab {
	
	ArrayList<Double> profits = new ArrayList<Double>() ;
	ArrayList<Double> uplarr = new ArrayList<Double>() ; 
	
	plotforex pf = null ; 
	
	int windsize = 375 ;
	double nconsec = 3 ; 
	double upl = 0 ; 
	double prevprice = 0 ; 
	double entryprice = 0 ; 
	double hardstop = .09 ;
	
	int ticcount = 0 ;
	int incount = 0 ; 
	int ntrades = 0 ;
	
	int dircount = 0 ;
	boolean up = false ;
	boolean down = false ; 
	
	boolean buy = false ; 
	boolean sell = false ;
	
	boolean disp = false ; 
	
	
	public runmatlab(){
		if(this.disp==true)
			pf = new plotforex() ; 
	}
	
	
	public runmatlab(boolean disp){
		this.disp = disp ;
		if(this.disp==true)
			pf = new plotforex() ; 
	}
	
	public runmatlab(boolean disp,int windsize,int nconsec){
		this.disp = disp ;
		this.windsize = windsize ; 
		this.nconsec = nconsec ; 
		
		if(this.disp==true)
			pf = new plotforex() ; 
	}
	
	
	public void feedtick(double newprice){	
				
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
	
	// get the number of consecutive changes
		
		
		//have a small bar chart in the display to show all the wins/losses
		
		// check for exit based on upl, if upl == max of current window
		if(buy || sell){
			incount = incount + 1 ;
			if(buy==true && incount > windsize){
				//System.out.println("upl = " + upl + " max = " + Primitives.max(Arrays.copyOfRange(Primitives.list2arr(uplarr),incount-windsize,incount)));
				if(upl == Primitives.max(Arrays.copyOfRange(Primitives.list2arr(uplarr),incount-windsize,incount))){
				//	System.out.println("exiting buy, upl = " + upl) ;
					buy = false ; 
					profits.add(upl) ; 
					uplarr = new ArrayList<Double>() ;		
					incount = 0 ;
				} 				
			}
			else if (sell==true && incount > windsize){
				//System.out.println("upl = " + upl + " max = " + Primitives.max(Arrays.copyOfRange(Primitives.list2arr(uplarr),incount-windsize,incount)));
				if(upl == Primitives.max(Arrays.copyOfRange(Primitives.list2arr(uplarr),incount-windsize,incount))){
				//	System.out.println("exiting sell, upl = " + upl) ;
					sell = false ; 
					profits.add(upl) ; 
					uplarr = new ArrayList<Double>() ; 		
					incount = 0 ; 
				} 				
			}
		}		
		else if(!buy && !sell){
			if(up==true && dircount >= nconsec){
				//System.out.println("entering sell") ;
				sell = true ; 
				entryprice = newprice ;
				ntrades ++ ; 
			}
			else if (down==true && dircount >= nconsec){
				//System.out.println("entering sell") ;
				buy = true ;
				entryprice = newprice ;
				ntrades ++ ; 
			}
			
			
		}
					
		prevprice = newprice ;  
		ticcount = ticcount + 1 ; 
		
	}
	

	public static void main(String[]args){
		
		
		ArrayList<dukas> arr = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\fxfiles1m","EURUSD_UTC_5 Mins_Bid_2012.01.01_2014.08.18.csv") ;
		plotforex pf = null ; 
		for(int run=3;run<6;run++){	
			pf = new plotforex() ; 
			ArrayList<Double>finalresults = new ArrayList<Double>() ;
			for(int wsize=150;wsize<400;wsize++){
				runmatlab rm = new runmatlab(true,wsize,run) ; 		
				int rnd = (int)(Math.random()*100000) ; 
				for(int i=rnd;i<rnd+1440*120;i++){
					rm.feedtick(arr.get(i).open);
				}
				//System.out.println(Primitives.mean(Primitives.list2arr(rm.profits))) ; 
				//new plot(rm.profits,Double.toString(Primitives.mean(Primitives.list2arr(rm.profits)))) ; 
				//try{Thread.sleep(30);}catch(Exception e){}
				//System.out.println(rm.ntrades) ;
				finalresults.add(Primitives.mean(Primitives.list2arr(rm.profits)));
				System.out.println("wsize = " + wsize + " ppt = " + finalresults.get(finalresults.size()-1)) ; 
				if(finalresults.get(finalresults.size()-1) > 0)
					pf.update(finalresults.get(finalresults.size()-1),1);
				else pf.update(finalresults.get(finalresults.size()-1),2);

			}
			System.out.println(Primitives.mean(Primitives.list2arr(finalresults))) ; 
		}
		//new plot(finalresults) ; 
		//for(int i=0;i<)
			//System.out.println(arr.get(i).open) ;
		
	}
}
