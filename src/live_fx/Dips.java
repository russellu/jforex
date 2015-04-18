package live_fx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


///private class for running the strategy on. 
public class Dips{
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
	double commission = 0.00007 ; // 7$ per 100k round trip
	double[] allParams = new double[5] ; 
	int[] mperiods = null ; 
	String pair = "" ; 
	String c1 = "" ; 
	String c2 = "" ; 
	HashMap<String,Double> allSpreads = new HashMap<String,Double>() ;
	//add the spread lookup table (dynamically updated in JForex)
	
		
	public Dips(String pair, double tp, double stop, int mvg1, int mvg2, int mvg3, double slope){ // where pers is a 1x3 array of mvg periods
		initSpreads(pair) ; 
		this.pair = pair ; c1 = pair.substring(0,3) ; c2 = pair.substring(3,6) ; 
		int[] pers = {mvg1,mvg2,mvg3} ; 
		this.mperiods = pers ; 
		this.slopeThresh = slope ; 
		this.tp = tp ; 
		this.stop = stop ; 
		for(int i=0;i<pers.length;i++){
			periods.add(pers[i]) ; 
			mvgs.add(new ArrayList<Double>()) ; 
		}			
	}
	
	
	public void initSpreads(String pair){	
		allSpreads.put("AUDCAD",(3.03+3.41+3.07)/3);
		allSpreads.put("AUDCHF",(2.87+3.18+3.24)/3);
		allSpreads.put("AUDJPY",(1.35+1.54+1.32)/3);
		allSpreads.put("AUDNZD",(2.59+3.03+2.66)/3);
		allSpreads.put("AUDUSD",(1.05+1.18+1.05)/3);
		allSpreads.put("CADCHF",(3.90+4.32+4.40)/3);
		allSpreads.put("CADJPY",(1.35+1.43+1.45)/3);
		allSpreads.put("CHFJPY",(2.82+2.98+3.60)/3);
		allSpreads.put("EURAUD",(1.79+2.06+1.92)/3);
		allSpreads.put("EURCAD",(2.32+2.44+2.52)/3);
		allSpreads.put("EURCHF",(2.35+2.43+2.99)/3);
		allSpreads.put("EURGBP",(0.94+1.06+0.97)/3);
		allSpreads.put("EURJPY",(0.84+1.00+0.82)/3);
		allSpreads.put("EURNZD",(4.11+4.61+4.21)/3);
		allSpreads.put("EURSGD",(5.01+6.01+4.46)/3);
		allSpreads.put("EURUSD",(0.29+0.33+0.30)/3);
		allSpreads.put("GBPAUD",(2.89+3.59+3.03)/3);
		allSpreads.put("GBPCAD",(3.77+4.27+4.13)/3);
		allSpreads.put("GBPCHF",(3.73+4.07+4.56)/3);
		allSpreads.put("GBPJPY",(1.83+2.09+1.94)/3);
		allSpreads.put("GBPNZD",(6.44+7.20+6.70)/3);
		allSpreads.put("GBPUSD",(0.85+1.00+1.00)/3);
		allSpreads.put("NZDCAD",(4.40+4.69+4.53)/3);
		allSpreads.put("NZDCHF",(3.33+3.72+3.75)/3);
		allSpreads.put("NZDJPY",(2.56+2.87+2.60)/3);
		allSpreads.put("NZDUSD",(1.25+1.50+1.31)/3);
		
		this.spread = allSpreads.get(pair)/10000 ; 
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
				mvg.add(mvg.get(mvg.size()-1) - raw.get(raw.size()-period-1)/period + raw.get(raw.size()-1)/period) ; 
			}
		}
	}
	
	public void attemptTrade(int direction){//-1 = sell, 1 = buy
		if(currentDirection != 0 || raw.size() < periods.get(2) ) //if already in a trade
			return ; 
		
		int cthresh = 8 ;
		
		if (direction==1){//buy  && currs.get(c1) <= cthresh && currs.get(c2) >= -cthresh
			//currs.put(c1, currs.get(c1)+1) ; currs.put(c2, currs.get(c2)-1) ; 
			currentDirection = 1 ; 
			entries = raw.get(raw.size()-1) ; 
			upls = 0 ; 
			ntrades++ ; 
	
		//	System.out.println("buying") ;
		}
		else if(direction==-1 ){// sells && currs.get(c1) >= -cthresh && currs.get(c2) <= cthresh
			//currs.put(c1, currs.get(c1)-1) ; currs.put(c2, currs.get(c2)+1) ; 
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
				profits = profits + upls ;//- (spread+commission) ; 
		//		System.out.println("Closing buy, profit = " + profits[0]) ; 
				currentDirection = 0 ; 
				//currs.put(c1, currs.get(c1)-1) ; currs.put(c2, currs.get(c2)+1) ; 
			}
		}
		else if(currentDirection==-1){
			upls = entries-raw.get(raw.size()-1) ; 		
			if(upls > tp || upls < stop){
				profits = profits + upls ;//- (spread+commission) ; 
			//	System.out.println("closing sell, profit = " + profits[0]) ; 
				currentDirection = 0 ; 
				//currs.put(c1, currs.get(c1)+1) ; currs.put(c2, currs.get(c2)-1) ; 
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
	
	public static void main(String[]args){
		
		
		
		
	}
	
	
	
	
	
}	