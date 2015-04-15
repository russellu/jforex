package jforex ;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Primitives {
	public static char max(char[] values) {
		char max = Character.MIN_VALUE;
		for(char value : values) {
			if(value > max)
				max = value;
		}
		return max;
	}

	public static byte max(byte[] values) {
		byte max = Byte.MIN_VALUE;
		for(byte value : values) {
			if(value > max)
				max = value;
		}
		return max;
	}

	public static short max(short[] values) {
		short max = Short.MIN_VALUE;
		for(short value : values) {
			if(value > max)
				max = value;
		}
		return max;
	}

	public static int max(int[] values) {
		int max = Integer.MIN_VALUE;
		for(int value : values) {
			if(value > max)
				max = value;
		}
		return max;
	}

	public static long max(long[] values) {
		long max = Long.MIN_VALUE;
		for(long value : values) {
			if(value > max)
				max = value;
		}
		return max;
	}
	public static double max(double[] values) {
		double max = -Double.MAX_VALUE;
		for(double value : values) {
			if(value > max)
				max = value;
		}
		return max;
	}
	
	public static int maxind(double[] values) {
		double max = Double.MIN_VALUE;
		int maxindex = 0 ;
		for(int i=0;i<values.length;i++)
			if(values[i] > max)
				maxindex = i ;
		return maxindex;
	}
	public static double max(double[] values,int start,int end) {
		double max = Double.MIN_VALUE;
		for(int i=start;i<end;i++){
			if(values[i] > max)
				max = values[i] ;
		}
		return max;
	}
	public static double max(double[][] values) {
		double max = Double.MIN_VALUE;
		for(int i=0;i<values.length;i++)
		for(double value : values[i]) {
			if(value > max)
				max = value;
		}
		return max;
	}
	
	public static double max(ArrayList<Double>input){
		double max = -Double.MAX_VALUE ; 
		for(int i=0;i<input.size();i++){
			if(input.get(i) > max)
				max = input.get(i) ; 
		}
		return max ; 
	}
	public static double min(ArrayList<Double>input){
		double min = Double.MAX_VALUE ; 
		for(int i=0;i<input.size();i++){
			if(input.get(i) < min)
				min = input.get(i) ; 
		}
		return min ; 
	}
	public static char maxChar(char... values) {
		return max(values);
	}

	public static byte maxByte(byte... values) {
		return max(values);
	}

	public static short maxShort(short... values) {
		return max(values);
	}

	public static int maxInt(int... values) {
		return max(values);
	}

	public static long maxLong(long... values) {
		return max(values);
	}

	public static char min(char[] values) {
		char min = Character.MAX_VALUE;
		for(char value : values) {
			if(value < min)
				min = value;
		}
		return min;
	}

	public static byte min(byte[] values) {
		byte min = Byte.MAX_VALUE;
		for(byte value : values) {
			if(value < min)
				min = value;
		}
		return min;
	}

	public static short min(short[] values) {
		short min = Short.MAX_VALUE;
		for(short value : values) {
			if(value < min)
				min = value;
		}
		return min;
	}

	public static int min(int[] values) {
		int min = Integer.MAX_VALUE;
		for(int value : values) {
			if(value < min)
				min = value;
		}
		return min;
	}

	public static long min(long[] values) {
		long min = Long.MAX_VALUE;
		for(long value : values) {
			if(value < min)
				min = value;
		}
		return min;
	}
	
	public static double min(double[] values) {
		double min = Double.MAX_VALUE;
		for(double value : values) {
			if(value < min)
				min = value;
		}
		return min;
	}
	public static double min(double[] values,int start,int end) {
		double min = Double.MAX_VALUE;
		for(int i=start;i<end;i++){
			if(values[i] < min)
				min = values[i] ;
		}
		return min;
	}
	
	public static double min(double[][] values) {
		double min = Double.MAX_VALUE;
		for(int i=0;i<values.length;i++)
		for(double value : values[i]) {
			if(value < min)
				min = value;
		}
		return min;
	}

	public static char minChar(char... values) {
		return min(values);
	}

	public static byte minByte(byte... values) {
		return min(values);
	}

	public static short minShort(short... values) {
		return min(values);
	}

	public static int minInt(int... values) {
		return min(values);
	}

	public static long minLong(long... values) {
		return min(values);
	}
	
	public static double[][]normneg1(double[][]input){
		double max = max(input) ; 
		double min = min(input) ; 
		double[][]output = new double[input.length][input[0].length] ; 
		for(int i=0;i<input.length;i++)
			for(int j=0;j<input[i].length;j++){
				output[i][j] = ((input[i][j] - min)/(max-min))*2-1 ; 		
			}
		return output ; 
	}
	
	public static double[][] transpose(double[][]input){
		double[][] trans = new double[input[0].length][input.length] ;
		for(int i=0;i<input.length;i++)
			for(int j=0;j<input[i].length;j++){
				trans[j][i] = input[i][j] ;
			}
		return trans ;
		
	}
	public static double[] list2arr(ArrayList<Double>input){
		double[]retr = new double[input.size()] ;
		for(int i=0;i<input.size();i++)
			retr[i] = input.get(i) ;			
		return retr ;
	}
	public static int[] intlist2arr(ArrayList<Integer>input){
		int[]retr = new int[input.size()] ;
		for(int i=0;i<input.size();i++)
			retr[i] = input.get(i) ;			
		return retr ;
	}
	
	public static ArrayList<Double>chunk(ArrayList<Double>input,int st,int en){
		ArrayList<Double>output = new ArrayList<Double>() ;
		for(int i=st;i<en;i++){
			output.add(input.get(i)) ; 			
		}
		return output ;
	}
	
	public static ArrayList<Double> subtractval(ArrayList<Double>arr, double val){
		for(int i=0;i<arr.size();i++){
			arr.set(i, arr.get(i)-val) ; 
		}
		return arr ; 
	}
	
/*	
	public static double[][] list2arr(ArrayList<ArrayList<Double>>input){
		double[][]retr = new double[input.size()][input.get(0).size()] ;
		for(int i=0;i<input.size();i++)
			for(int j=0;j<input.get(i).size();j++)
				retr[i][j] = input.get(i).get(j) ;			
		return retr ;
	}
*/	
	public static double[][]arrdiff(double[][]subtractee,double[][]subtractor){
		double[][] output = new double[subtractee.length][subtractee[0].length] ;
		for(int i=0;i<subtractee.length;i++)
			for(int j=0;j<subtractor[i].length;j++)
				output[i][j] = subtractee[i][j] - subtractor[i][j] ;
				
		return output ;
		
	}
	
	public static ArrayList<Double>arr2list(double[]input){
		ArrayList<Double> list = new ArrayList<Double>() ; 
		for(int i=0;i<input.length;i++)
			list.add(input[i]) ; 
		return list ; 
	}
	
	public static ArrayList<Integer>arr2list(int[]input){
		ArrayList<Integer> list = new ArrayList<Integer>() ; 
		for(int i=0;i<input.length;i++)
			list.add(input[i]) ; 
		return list ; 
	}
	
	public static void accumulate(double[][]input,double[][]accum){
		
		for(int i=0;i<input.length;i++)
			for(int j=0;j<input[i].length;j++){
				accum[i][j] = accum[i][j] + input[i][j] ;
			}
		//return accum ;
	
	}

	// get the sum of an array list of doubles
	public static double sum(ArrayList<Double> input){
		double total = 0 ; 
		for(int i=0;i<input.size();i++)
			total += input.get(i) ; 
		return total ;
		
	}
	
	
	public static void timeseries(double[][]prevdata,double[]newdata,int count){
		if(prevdata[0].length > count){
			for(int i=0;i<prevdata.length;i++)
				prevdata[i][count] = newdata[i] ;
		}
		else {
			for(int i=0;i<prevdata.length;i++)
				for(int j=0;j<prevdata[i].length-1;j++){
					prevdata[i][j] = prevdata[i][j+1] ;
				}
			for(int i=0;i<prevdata.length;i++)
				prevdata[i][prevdata[0].length-1] = newdata[i] ;
		}
	}
	public static double mean(double[]input){
		double sum = 0 ;
		for(int i=0;i<input.length;i++)
			sum += input[i] ;
		return sum/(double)input.length ;
			
	}
	public static double var(double[]input){
		double mean = mean(input) ;
		double var = 0 ;
		for(int i=0;i<input.length;i++)
			var += (input[i]-mean)*(input[i]-mean) ;
		return var/(double)(input.length-1) ;
		
	}
	public static double[]cumsum(double[] input){
		double[]output = new double[input.length-1] ;
		output[0] = input[0] ;
		for(int i=1;i<output.length;i++)
			output[i] = output[i-1] + input[i] ;
		return output ;
	}
	public static double[]sma(double[] input,int period){
		//try collapsing sma where you calculate from oldest to newst, and take the sma of
		//the previous, not the fresh array.
		double[] output = input.clone() ;
		if(input.length>period*2){
			for(int i=input.length-period-1;i<input.length;i++){
				double sum = 0 ;
				for(int j=i-period;j<i;j++){
					sum += input[j] ;
				}
				sum = sum/(double)period ;
				output[i] = sum ;
			}
		}
		return output ;
	}
	public static void saveRecordingtxt(String filename,ArrayList<Double>pmat){

    	try { 		
    	    PrintWriter pr = new PrintWriter(filename);
    	        for (int i=0; i<pmat.size() ; i++){   	           
    	        	   pr.print(pmat.get(i) + " ");
    	        }
    	        pr.close();   	     
    	    }
    	    catch (Exception e)
    	    {
    	        e.printStackTrace();
    	        System.out.println("No such file exists.");
    	    }
    	} 
	public static double[]abs(double[]input){
		for(int i=0;i<input.length;i++)
			input[i] = Math.abs(input[i]) ;
		return input ;
	}
   public static double[] smoothfinal(double[][] input,int amt){
	   //assumes a mxn array
	   //smooths the  columns, returns the smoothed endpoints
	   double[] sums = new double[input.length] ;

	   if(amt >= input[0].length){
		   for(int i=0;i<input.length;i++){
			   for(int j=0;j<amt;j++){
				   sums[i] += input[i][j] ;
			   }
			sums[i] = sums[i]/(double)amt ;
			System.out.println("sum = " + sums[i]) ;
		   	}
		   }
	   else if (amt< input[0].length){
		   for(int i=0;i<input.length;i++){
			   for(int j=input[i].length-amt-1;j<input.length;j++){
				   sums[i] += input[i][j] ;
			   }
			   sums[i] = sums[i]/(double)amt ;
				System.out.println("sum = " + sums[i]) ;

		   }
	   }
	   return sums;
   }
   public static boolean contains(ArrayList<Double>input,double value){
	   for(int i=0;i<input.size();i++)
		   if(input.get(i)==value)
			   return true ;
	   return false ;
   }
   
   public static double[]derive(double[]input){
	   double[] output = new double[input.length-1] ;
	   for(int i=1;i<input.length;i++)
		   output[i-1] = input[i]-input[i-1] ;
	   return output ;
   }
   
   public static void main(String[] args){
	   
   }
	
}