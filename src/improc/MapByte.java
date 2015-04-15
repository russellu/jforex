package improc;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * map a byte value to an array of double (RGBA)
 * query the value 
 * 
 * @author russ
 *
 */



public class MapByte {
	
	private ArrayList<double[]> groundVals = new ArrayList<double[]>() ; 
	private ArrayList<double[]> waterVals = new ArrayList<double[]>() ; 
	private ArrayList<double[][]> streamVals = new ArrayList<double[][]>() ;  
	private double streamCount = 0 ; 
	
	
	public MapByte(){
		initGroundMapping() ; 
		initWaterMapping() ; 
		initStreamMapping()  ;
		
		
	}
	
	public void updateCounters(){
		streamCount +=.2 ; 
		
	}
	
	public void initGroundMapping(){
		double[] ground1 = {170,30,20,255} ; 	
		groundVals.add(ground1) ; 
	}
	
	public void initWaterMapping(){
		double[] water1 = {20,40,200,255} ;
		waterVals.add(water1) ; 
	}
	
	public void initStreamMapping(){
		System.out.println("initStreamMapping") ; 
		ArrayList<BufferedImage> bims = new MapLoader().getWater("") ; 
		ArrayList<double[][][]> timeSlices = new ArrayList<double[][][]>() ; 
		
		for(int i=0;i<bims.size();i++)
			timeSlices.add(new MapLoader().getFast(bims.get(i))) ; 
		System.out.println("time slices loaded, size = " + timeSlices.size()) ; 
			
		
		
		for(int i=0;i<timeSlices.size();i++){
			int w = timeSlices.get(i).length ; 
			int h = timeSlices.get(i)[0].length ; 		
			System.out.println("W = " + w + " h = " + h) ; 
			streamVals.add(new double[w*h][4]) ; 			
			int totalCounter = 0 ; 
			for(int j=0;j<timeSlices.get(i).length;j++)
				for(int k=0;k<timeSlices.get(i)[0].length;k++){
					streamVals.get(i)[totalCounter][0] = 0;//Math.random() * 30 ; 
					streamVals.get(i)[totalCounter][1] = 0;//Math.random()*50 ; 			
					streamVals.get(i)[totalCounter][2] = timeSlices.get(i)[j][k][0] ; 			
					streamVals.get(i)[totalCounter][3] = 255 ; 			
					totalCounter++ ; 							
				}		
		}	
		System.out.println("stream map initialized, size = " + streamVals.size()) ; 
	}
	
	public double[] getStreamRGBA(short b){
		//System.out.println(b) ; 
		return streamVals.get((int)streamCount%22)[b] ; 
		
		
	}
	
		
	public double[] getDoubleRGBA(short b){
		int val = (int)b ;
		if(val >= 10)
			return waterVals.get(0) ; 
		else if (val<10)
			return groundVals.get(0) ; 
		else return null ; 		
	}
	
	// replicate the tiles in matlab and then assign the global array (small int) a timeseries
	// which is incremented on each frame by the water class (update Terrain + 1) 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
