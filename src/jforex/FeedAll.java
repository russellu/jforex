package jforex;

import java.util.ArrayList;

/**
 * Feeding the inputs...need to synchronize all the bars together.
 * or maybe put the longer term bars into a hash table and just access them when you want to check the trend
 * steepness of inter day trend, for example. day(i) - day(i-1) actually you could do that on the 1m BARS as well
 * 
 * use a hash table and hash the hr/day of the minute bar. 
 * 
 * 
 * @author russ
 *
 */

public class FeedAll {

	
	
	
	public static void main(String[] args){
		
		ArrayList<dukas> arr = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\","EURUSD_UTC_1 Min_Bid_2014.01.01_2015.01.31.csv") ;				
		for(int i=0;i<arr.size();i++)
			System.out.println(arr.get(i).hour) ;
		
		
		
	}
}
