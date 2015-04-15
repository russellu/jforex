package realtime;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import realtime.loadParams.TPclass;
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

public class testall_nifti {
 
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

	public testall_nifti(int[] input){
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
		
		System.out.println("lol") ; 
		ArrayList<TPclass>tps = loadParams.get("C:\\Users\\russ\\Downloads", "celldata.txt") ; 
		System.out.println(tps.size()) ; 
		
		for(int tpcount = 0;tpcount<tps.size();tpcount++){
			String pair = tps.get(tpcount).pair ; 
			
			
				ArrayList<dukas> arr = parseDukas.parseOne("C:\\Users\\russ\\Documents\\",pair+"_UTC_1 Min_Bid_2007.12.31_2015.02.17.csv") ;				
				double stop = 0.0005+.0005*tps.get(tpcount).stop ;
			    double tp = 0.0001+.0002*tps.get(tpcount).tp ;
				ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> alist1 = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>() ; 
				ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> alist2 = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>() ; 
				int m1start = 1 ,m2start = 35, m3start = 100, m1incr = 5, m2incr = 5, m3incr = 25; double sthreshincr = .000001 ; 
				int m1count = 0 ,m2count = 0,m3count = 0 ; 
				m1count = 0 ; 
				for(int i=m1start;i<30;i+=5){ ; m2count = 0 ; 
					System.out.println("i = " + i) ; 
					alist1.add(new ArrayList<ArrayList<ArrayList<Double>>>()) ; 
					alist2.add(new ArrayList<ArrayList<ArrayList<Double>>>()) ; 
					for(int j=m2start;j<90;j+=5){ ; m3count = 0 ; 
						alist1.get(m1count).add(new ArrayList<ArrayList<Double>>()) ;
						alist2.get(m1count).add(new ArrayList<ArrayList<Double>>()) ; 
						for(int k=m3start;k<300;k+=25){ ;  
							alist1.get(m1count).get(m2count).add(new ArrayList<Double>()) ;
							alist2.get(m1count).get(m2count).add(new ArrayList<Double>()) ; 
				//LinePlot lp = new LinePlot() ; 
							for (double sthresh = 0.000005;sthresh<0.00002;sthresh+=0.000001){
								ArrayList<Double>singles = new ArrayList<Double>() ; 
								ArrayList<Double>nsingles = new ArrayList<Double>() ; 
				for(int p=0;p<100;p++){					
					int index = (int)(arr.size()*Math.random()*.5) ;	
					int[] params = {i,j,k} ; 
					testall_nifti p2 = new testall_nifti(params) ; 
					p2.slopeThresh = sthresh ; p2.mthresh = i+99999999 ; p2.tps[0] = tp ; p2.stops[0] = -stop ; 			
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
					singles.add(newvals[0]) ; 
					nsingles.add(p2.ntrades[0]) ;
					//lp.update(newvals,0);		
				}
				alist1.get(m1count).get(m2count).get(m3count).add(Primitives.mean(Primitives.list2arr(singles))) ; 
				alist2.get(m1count).get(m2count).get(m3count).add(Primitives.mean(Primitives.list2arr(nsingles))) ; 
									} m3count ++ ; 
						} m2count ++ ; 
				} m1count ++ ; 
			}
				Array2Nifti.writeArrayList4d(alist1,"mp_"+pair+"_"+tp+"_"+stop) ;
				Array2Nifti.writeArrayList4d(alist2,"nt_"+pair+"_"+tp+"_"+stop) ;
		}
	}
}
