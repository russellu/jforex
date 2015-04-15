package realtime;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import nifti.Array2Nifti;
import utility.SaveArray;
import jforex.Primitives;
import jforex.dukas;
import jforex.parseDukas;

/**
 * stick to the plan precious...
 * 
 * @author gollum
 *
 */

public class testall_runparams{
 
	ArrayList<Double> raw = new ArrayList<Double>() ; 
	ArrayList<ArrayList<Double>> mvgs = new ArrayList<ArrayList<Double>>() ; 
	ArrayList<Integer> periods = new ArrayList<Integer>() ; 
	ArrayList<Integer> slopes = new ArrayList<Integer>() ; 
	int currentDirection = 0 ; 
	double slopeThresh = 0.0000131 ; 
	double[] entries = new double[1] ; 
	double[] upls = new double[1] ; 
	double[] stops = {-0.003} ;
	double[] tps = {0.002} ; 
	double[] profits = {0.0} ; 
	double[] ntrades = new double[1] ;
	
	int mcount = 0  ;
	int mthresh = 1000 ; 

	public testall_runparams(int[] input){
		int[] pers = input ; 
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
		
		if (direction==1){//buy
			currentDirection = 1 ; 
			entries[0] = raw.get(raw.size()-1) ; 
			upls[0] = 0 ; 
			ntrades[0]++ ; 
		//	System.out.println("buying") ;
		}
		else if(direction==-1){
			currentDirection = -1 ;
			entries[0] = raw.get(raw.size()-1) ; 
			upls[0] = 0 ; 
			ntrades[0] ++ ; 
		//	System.out.println("selling") ;
		}
	}

	public void checkTrades(){
		//System.out.println("checking trades,upl= "+ upls[0]) ;
		if(currentDirection==0 )return ; //if no trade in progress, return
		
		mcount ++ ; 
		if(currentDirection==1){
			upls[0] = raw.get(raw.size()-1)-entries[0] ; 
			if(upls[0] > tps[0] || upls[0] < stops[0] || mcount > mthresh){
				profits[0] = profits[0] + upls[0] ; 
		//		System.out.println("Closing buy, profit = " + profits[0]) ; 
				mcount = 0 ; 
				currentDirection = 0 ; 
			}
		}
		else if(currentDirection==-1){
			upls[0] = entries[0]-raw.get(raw.size()-1) ; 		
			if(upls[0] > tps[0] || upls[0] < stops[0] || mcount > mthresh){
				profits[0] = profits[0] + upls[0] ; 
			//	System.out.println("closing sell, profit = " + profits[0]) ; 
				mcount = 0 ; 
				currentDirection = 0 ; 
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
	
	public static void main(String[]args)throws Exception{
			ArrayList<dukas> arr = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\fx2\\","EURUSD_UTC_1 Min_Bid_2008.01.01_2015.02.15.csv") ;				
			double stop = 0.0005+.0005*18 ;
		    double tp = 0.0001+.0002*25 ;
			// params = 4,15,19 .
		    ArrayList<Double> means = new ArrayList<Double>() ; 
			int m1start = 1 ,m2start = 35, m3start = 100, m1incr = 2, m2incr = 2, m3incr = 5 ; 
			int m1count = 0 ,m2count = 0,m3count = 0 ; 
		
			for (double sthresh = 0.0000001;sthresh<0.00003;sthresh+=0.000001){
			for(int p=0;p<200;p++){					
				int index = (int)(arr.size()*Math.random()*.5) ;	
				int[] params = {m1start+m1incr*4,m2start+m2incr*15,m3start+m3incr*21} ; 
				testall p2 = new testall(params) ; 
				p2.slopeThresh = sthresh ; p2.mthresh = 99999999 ; p2.tps[0] = tp ; p2.stops[0] = -stop ; 			
				for(int t=index;t<index+24*60*30;t++){
					//try{Thread.sleep(2);}catch(Exception e){} 
					p2.update(arr.get(t).close);
					double[] newvals = new double[p2.mvgs.size()+1] ; 
					newvals[0] = arr.get(t).close ; 
					for(int s=1;s<newvals.length;s++)
						newvals[s] = p2.mvgs.get(s-1).get(p2.mvgs.get(s-1).size()-1) ; 					
				//	lp.slopeIndicator = p2.slopes ; lp.ntrades = p2.ntrades[0] ; lp.upl = p2.upls[0] ; lp.mins = icount++ ; 
				//	lp.update(newvals,p2.profits[0]) ;	
				}
				double[] newvals = {p2.profits[0]/p2.ntrades[0]} ; 
				 means.add(newvals[0]) ; 
				//lp.update(newvals,0);			
			}
			System.out.println(Primitives.mean(Primitives.list2arr((means))) + " sthresh = " + sthresh) ; 
			}
	}
}
