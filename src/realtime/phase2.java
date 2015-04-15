package realtime;

import java.util.ArrayList;
import java.util.List;

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

public class phase2 {
 
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
	
	public phase2(){
		int[] pers = {12,72,122} ; 
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
		if(currentDirection != 0) //if already in a trade
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
		if(currentDirection==0)return ; //if no trade in progress, return
		else if(currentDirection==1){
			upls[0] = raw.get(raw.size()-1)-entries[0] ; 
			if(upls[0] > tps[0] || upls[0] < stops[0]){
				profits[0] = profits[0] + upls[0] ; 
		//		System.out.println("Closing buy, profit = " + profits[0]) ; 

				currentDirection = 0 ; 
			}
		}
		else if(currentDirection==-1){
			upls[0] = entries[0]-raw.get(raw.size()-1) ; 		
			if(upls[0] > tps[0] || upls[0] < stops[0]){
				profits[0] = profits[0] + upls[0] ; 
			//	System.out.println("closing sell, profit = " + profits[0]) ; 

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
	
	public static void main(String[]args){
		//ArrayList<dukas> arr = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\","EURUSD_UTC_1 Min_Bid_2009.02.04_2014.02.04.csv") ;
		ArrayList<dukas> arr = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\","EURUSD_UTC_1 Min_Bid_2014.01.01_2015.01.31.csv") ;

		ArrayList<ArrayList<Double>> results = new ArrayList<ArrayList<Double>>() ; 
		ArrayList<ArrayList<Double>> ntrades = new ArrayList<ArrayList<Double>>() ; 
		System.out.println("line plot") ; 
		LinePlot lp = new LinePlot() ; 
		for(double tp=0.0001+.0002*12;tp<0.01;tp+=.0002){ 
			results.add(new ArrayList<Double>()) ;
			ntrades.add(new ArrayList<Double>()) ;

			for(double stp=0.0005+.0002*80;stp<0.025;stp+=0.0002){
			phase2 p2 = new phase2() ; 
			p2.tps[0] = tp ; 
			p2.stops[0] = -stp ; 
			int icount = 0 ; 
		for(int i=(int)(Math.random()*200000);i<arr.size();i++){
			try{Thread.sleep(10);}catch(Exception e){} 
			p2.update(arr.get(i).close);
			double[] newvals = new double[p2.mvgs.size()+1] ; 
			newvals[0] = arr.get(i).close ; 
			for(int s=1;s<newvals.length;s++)
				newvals[s] = p2.mvgs.get(s-1).get(p2.mvgs.get(s-1).size()-1) ; 
			
			lp.slopeIndicator = p2.slopes ; lp.ntrades = p2.ntrades[0] ; lp.upl = p2.upls[0] ; lp.mins = icount++ ; 
			lp.update(newvals,p2.profits[0]) ;	
		}
		System.out.println(p2.profits[0]/p2.ntrades[0] + " stop =  " + stp + " tp = " + tp) ;
		results.get(results.size()-1).add(p2.profits[0]/p2.ntrades[0]) ; 
		ntrades.get(ntrades.size()-1).add(p2.ntrades[0]) ; 
		}
		}
		SaveArray.saveArrayList(results, "results5.txt");
		SaveArray.saveArrayList(ntrades, "ntrades5.txt");
	} // erichert11@ubishops.ca
}
