package jforex;

/** 
 * dukascopy market structure for a market time point, contains : date,time,open,high,low,close,volume
 * @author russ
 *
 */

public class dukas {
   
	public int year ;
	public int month ;
	public int day ;
	public int hour ;
	public int minute ;
	public int second ;
	public float open ;
	public float high ;
	public float low ;
	public float close ;
	public float volume ;
		
	public dukas(int year,int month,int day,int hour,int minute,int second,float open,float high,float low,float close,float volume){
		this.year = year ;
		this.month = month ;
		this.day = day ;
		this.hour = hour ;
		this.minute = minute ;
		this.second = second ;
		this.open = open ; 
		this.high = high ;
		this.low = low ; 
		this.close = close ;
		this.volume = volume ;	
	}

	public String toString(){
		return year+"."+month+"."+day+" "+hour+":"+minute+":"+second+" "+open+" "+high+" "+low+" "+close+" "+volume ;		
	}
}
