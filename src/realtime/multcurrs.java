package realtime;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import jforex.dukas;
import jforex.parseDukas;

/**
 * multcurrs: code to load 4 different pairs, and trade them simultaneously.
 * basic strategy: wait for the hedge combination to reach a favorable level: what does this mean?
 * basically it means this:
 * you have eurusd, gbpusd, eurjpy, gbpjpy
 * wait until gbpjpy has gone down a bit and gbpusd has gone up a bit, then buy one and sell the other. then wait for them 
 * to go positive and get out.
 * you can also wait for the perfect storm where all 4 diverge from each other...
 * define a divergence as what? eurusd goes up, eurjpy goes down?
 * what is a favorable entry? 
 * hedging to remove the risk and then exiting on positive fluctuations in the upl...
 * 
 * the concept of a cumulative upl ie the UPL of all currently open trades...
 * 
 * need to find a way to capitalize on these small changes in the total pips 
 * need a smart way of monitoring UPL ie hug the profits cut the losses...try on random number sequence
 * 
 * 
 * @author russ
 *
 */



public class multcurrs {

	boolean tradesopen = false ;
	int conds[] = new int[4] ; 
	ArrayList<plotforex> pfs = null;
	double[]entries = new double[4] ; 
	ArrayList<ArrayList<Double>>upls = new ArrayList<ArrayList<Double>>() ; 
	ArrayList<Double>allupls = new ArrayList<Double>() ; 
	
	plotforex mainfx = new plotforex() ; 
	JFrame jf ;
	
	
	public multcurrs(plotforex eup,plotforex ejp,plotforex gup,plotforex gjp){
		initarrs() ; 
		pfs = new ArrayList<plotforex>() ; 
		pfs.add(eup) ; 
		pfs.add(ejp) ; 
		pfs.add(gup) ; 
		pfs.add(gjp) ; 
		mainfx.jf.setLocation(325,150);
		//initframe() ; 
	}
	
	
	public void sumupls(){
		double uplsum = 0 ; 
		for(int i=0;i<upls.size();i++)
			if(upls.get(i).size()>0)
				if(i==0||i==2){
					uplsum = uplsum + upls.get(i).get(upls.get(i).size()-1)*100 ; // in pips
				}
				else uplsum = uplsum + upls.get(i).get(upls.get(i).size()-1)*10000 ; //in pips
		allupls.add(uplsum) ;
		mainfx.setupl(allupls);
		mainfx.update(uplsum,0) ; 
		System.out.println("uplsum (pips) = "+  uplsum) ; 
		
	}
	
	public void initarrs(){
		for(int i=0;i<4;i++)
			upls.add(new ArrayList<Double>()) ; 	
	}
	
	public void feedtick(double[]ticks){
		// if no trades are open, open them randomly
		sumupls() ; 
		if(!tradesopen){
			double d = Math.random() ;
			if(d > .5){
				conds[0] = 1 ; 
				conds[1] = 2 ; 
				conds[2] = 2 ; 
				conds[3] = 1 ; 			
			}
			else {
				conds[0] = 2 ; 
				conds[1] = 1 ; 
				conds[2] = 1 ; 
				conds[3] = 2 ; 					
			}	
			for(int i=0;i<ticks.length;i++)
				entries[i] = ticks[i] ; 
			
			tradesopen = true ; 
		}
		
		//update the upl array
		if (tradesopen==true){
			for(int i=0;i<entries.length;i++)
				if(conds[i]==1) //if sell
					upls.get(i).add(entries[i]-ticks[i]) ;
				else if(conds[i]==2) // if buy
					upls.get(i).add(ticks[i]-entries[i]) ; 
		}
		
		
		
		
		// update the plots
		for(int i=0;i<conds.length;i++){
			pfs.get(i).update(ticks[i],conds[i]);
			pfs.get(i).setupl(upls.get(i)) ; 
			
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	public void initframe(){
		jf = new JFrame() ;
		jf.setPreferredSize(new Dimension(400,150));
		jf.setLocation(0,600);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack() ; 
		mainfx.jf.setLocation(0,600);
		//jf.add(mainfx) ; 
		
		
		
	}
	
	
	
	
	
	
	public static void main(String[]args){
		ArrayList<dukas> ej = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\multcurrs","EURJPY_UTC_5 Mins_Bid_2010.08.18_2014.08.22.csv") ;
		ArrayList<dukas> eu = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\multcurrs","EURUSD_UTC_5 Mins_Bid_2010.08.18_2014.08.22.csv") ;
		ArrayList<dukas> gj = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\multcurrs","GBPJPY_UTC_5 Mins_Bid_2010.08.18_2014.08.22.csv") ;
		ArrayList<dukas> gu = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\multcurrs","GBPUSD_UTC_5 Mins_Bid_2010.08.18_2014.08.22.csv") ;
		System.out.println("ej = " + ej.size() + " eu = " + eu.size() + " gj = " + gj.size() + " gu = " + gu.size()) ; 
		plotforex ejp = new plotforex(0,0) ; ejp.setTitle("EURJPY") ; 
		plotforex eup = new plotforex(0,300) ; eup.setTitle("EURUSD") ;
		plotforex gjp = new plotforex(650,0) ; gjp.setTitle("GBPJPY") ;
		plotforex gup = new plotforex(650,300) ; gup.setTitle("GBPUSD") ;
		
		double[] ticks = new double[4] ; 
		multcurrs mc = new multcurrs(eup,ejp,gup,gjp) ; mc.mainfx.ntimes = 400000 ; 
		for(int i=0;i<ej.size();i+=10){
			ticks[0] = ej.get(i).open ;
			ticks[1] = eu.get(i).open ;
			ticks[2] = gj.get(i).open ;
			ticks[3] = gu.get(i).open ;		
			mc.feedtick(ticks);
		}
		
		
		
		
	}
	
	
	
	
}

