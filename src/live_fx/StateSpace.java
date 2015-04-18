package live_fx;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


/**
 * state space search : find local extrema in n dimensional space
 * approach #1) brute force search: search all possibilities.
 * 	computationally intense but works, but cannot search on a very fine grid
 * 	macro scale search is feasible, would it be possible to refine to lower scales to avoid PVE?
 * recursive state space search: recurse on the point of highest probability, in a finer mesh
 * or rather, re-center the search over the hotspot and run on a finer scale.
 * 
 * 
 * some kind of boundary value problem: you set an extent to the search space and an interval to step over
 * find the top n indices, search them all recursively. depth first or breadth first? if you want to achieve
 * the maximal values faster use depth first, else use breadth...but an exhaustive search won't matter. 
 * of course this has its own difficulties...effects may be nonlinear in that a broader search would not pick them out
 * so it would never converge...
 * 
 * state space takes a set of points and intervals which specify the dimension and size
 * 
 * @author russ
 *
 */
public class StateSpace {

	public ArrayList<Double> centerPoints ; 
	public ArrayList<double[]> ranges ; 
	public ArrayList<ArrayList<Double>> samplePoints ; 
	
	//the search space has the same dimension as centerPoints.size
	//intervals are the points to search. the first size dimension of intervals is the dimension of
	// the search space as well, the second dimension is the boundary points of that dimension
	public StateSpace(ArrayList<Double> centerPoints, ArrayList<double[]> ranges){
		this.centerPoints = centerPoints ; 
		this.ranges = ranges ; 
		initSamplePoints() ; 
	}
	
	public void initSamplePoints(){
		samplePoints = new ArrayList<ArrayList<Double>>() ; 
		for(int i=0;i<centerPoints.size();i++){
			samplePoints.add(getDoubleInRange(ranges.get(i)[0],ranges.get(i)[1],(ranges.get(i)[1]-ranges.get(i)[0])/5)) ; 				
		}		
	}

	public void printSamplePoints(){
		NumberFormat nf = new DecimalFormat("#000.0000000") ;
		for(int i=0;i<samplePoints.size();i++){
			String s = "" + i + " : "; 
			for(int j=0;j<samplePoints.get(i).size();j++){
				s += nf.format(samplePoints.get(i).get(j)) + " " ; 
			}
			System.out.println(s) ;
		}
	}
		
	////SOME UTILITY FUNCTIONS BELOW
	
	public static ArrayList<Double> getDoubleInRange(double low,double high,double increment){
		ArrayList<Double> doubleRange = new ArrayList<Double>() ; 
		for(double d=low;d<=high;d+=increment)
			doubleRange.add(d) ; 
		return doubleRange ; 
	}
	
	public static ArrayList<Integer> getIntegerInRange(int low, int high, int increment){
		ArrayList<Integer> integerRange = new ArrayList<Integer>() ; 
		for(int i=low;i<=high;i+=increment)
			integerRange.add(i) ; 
		return integerRange ; 		
	}	

	
	
	public static void main(String[] args){
		// initialize the parameters: min and max
		double[] m1Params = {5,25} ; 
		double[] m2Params = {35,95} ; 
		double[] m3Params = {100,150} ; 
		double[] slopeParams = {0.000005,0.00002} ;  
 		double[] tpParams = {0.0001,0.008} ; 
		double[] stopParams = {0.005,0.02} ; 
		ArrayList<double[]> params = new ArrayList<double[]>() ; 
		params.add(m1Params) ; params.add(m2Params) ; params.add(m3Params) ; 
		params.add(slopeParams) ; params.add(tpParams) ; params.add(stopParams) ;
 		ArrayList<Double> centerPoints = new ArrayList<Double>() ;
 		for(double[] d:params)
 			centerPoints.add((d[0]+d[1])/2) ; 
		StateSpace sp = new StateSpace(centerPoints,params) ; 
		sp.printSamplePoints();
	}
}
