package live_fx;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import nifti.Array2Nifti;
import utility.SaveArray;
import jforex.Primitives;
import jforex.dukas;
import jforex.parseDukas;

/**
 * allparams is a class to test all 6 forex parameters: m1,m2,m3,slope,tp,stop
 * for all currency pairs. (params necesary for a trending pullback entry strategy)
 * 
 * do a sparse randomized brute force search of the parameter space, yielding a  * [m1,m2,m3,slope,tp,stop,run] vector where each run is a time point from the 
 * sample space of all possible time points. 
 * 
 * another idea: close on the decorrelated fluctuations between pairs (later)
 * 
 * 
 * @author russ
 *
 */
public class AllParams {

	static String basePath = "C:\\Users\\Acer\\Documents\\fxParams" ;

	
	public static ArrayList<ArrayList<String>> createDirStructure(ArrayList<Double>stopRange,ArrayList<Double>tpRange,ArrayList<Double>slopeRange,String ccy){
		ArrayList<String> stopNames = new ArrayList<String>() ; 
		ArrayList<String> tpNames = new ArrayList<String>() ; 
		ArrayList<String> slopeNames = new ArrayList<String>() ; 
		NumberFormat form = new DecimalFormat("#0000.0000000") ; 
		
		ArrayList<String> currencies = new ArrayList<String>() ; currencies.add(ccy) ; 
		//order = currency,tp,stop,slope
		ArrayList<ArrayList<String>>dirNames = new ArrayList<ArrayList<String>>() ; 
		dirNames.add(currencies) ; dirNames.add(tpNames) ; dirNames.add(stopNames) ; dirNames.add(slopeNames) ; 
		
		for(Double d:stopRange)stopNames.add(form.format(d)) ; 
		for(Double d:tpRange)tpNames.add(form.format(d)) ; 	
		for(Double d:slopeRange)slopeNames.add(form.format(d)) ; 		

		File dir1 = new File(basePath) ; dir1.mkdir() ;	
		File dir2 = new File(basePath+"\\"+currencies.get(0)) ; dir2.mkdir() ; 
		for(int i=0;i<tpNames.size();i++){
			File idir = new File(basePath+"\\"+currencies.get(0)+"\\"+tpNames.get(i)) ;
			idir.mkdir() ; 
			for(int j=0;j<stopNames.size();j++){
				File jdir = new File(basePath+"\\"+currencies.get(0)+"\\"+tpNames.get(i)+"\\"+stopNames.get(j)) ; 
				jdir.mkdir() ; 
				for(int k=0;k<slopeNames.size();k++){
					File kdir = new File(basePath+"\\"+currencies.get(0)+"\\"+tpNames.get(i)+"\\"+stopNames.get(j)+"\\"+slopeNames.get(k)) ; 
					kdir.mkdir() ; 
				}
			}		
		}		
		return dirNames ; 
	}
	
	public static void main(String[] args)throws Exception{
		ArrayList<dukas> arr = parseDukas.parseOne("C:\\Users\\Acer\\Documents\\FX1MYR\\","EURUSD"+"_UTC_1 Min_Bid_2014.01.01_2015.03.20.csv") ;	
		//parameters (x6, mvg1,mvg2,mvg3,slope,tp,stop)
		int[] m1Params = {5,25,8} ; ArrayList<Integer>m1Range = FileTest.getIntegerInRange(m1Params[0],m1Params[1],m1Params[2]) ; 
		int[] m2Params = {35,95,15} ; ArrayList<Integer>m2Range = FileTest.getIntegerInRange(m2Params[0],m2Params[1],m2Params[2]) ;  
		int[] m3Params = {100,250,60} ; ArrayList<Integer>m3Range = FileTest.getIntegerInRange(m3Params[0],m3Params[1],m3Params[2]) ; 
		double[] slopeParams = {0.000005,0.00002,0.000005} ; ArrayList<Double>slopeRange = FileTest.getDoubleInRange(slopeParams[0], slopeParams[1], slopeParams[2]) ; 
 		double[] tpParams = {0.0001,0.015,0.0015} ; ArrayList<Double>tpRange = FileTest.getDoubleInRange(tpParams[0], tpParams[1], tpParams[2]) ; 
		double[] stopParams = {0.0001,0.025,0.005} ; ArrayList<Double>stopRange = FileTest.getDoubleInRange(stopParams[0], stopParams[1], stopParams[2]) ; 
		
		ArrayList<ArrayList<String>> dirNames = createDirStructure(stopRange,tpRange,slopeRange,"EURUSD") ; 
				
		for(int p1=0;p1<tpRange.size();p1++){
			for(int p2=0;p2<stopRange.size();p2++){
				for(int p3=0;p3<slopeRange.size();p3++){
					ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> meanProfits = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>() ; 
					ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> nTrades = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>() ; 
					ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> params = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>() ; 

					for(int p4=0;p4<m1Range.size();p4++){
						meanProfits.add(new ArrayList<ArrayList<ArrayList<Double>>>()) ;
						nTrades.add(new ArrayList<ArrayList<ArrayList<Double>>>()) ;
						params.add(new ArrayList<ArrayList<ArrayList<Double>>>()) ;

						for(int p5=0;p5<m2Range.size();p5++){
							meanProfits.get(p4).add(new ArrayList<ArrayList<Double>>()) ;
							nTrades.get(p4).add(new ArrayList<ArrayList<Double>>()) ;
							params.get(p4).add(new ArrayList<ArrayList<Double>>()) ;
						
							for(int p6=0;p6<m3Range.size();p6++){
								meanProfits.get(p4).get(p5).add(new ArrayList<Double>()) ; 
								nTrades.get(p4).get(p5).add(new ArrayList<Double>()) ; 
								params.get(p4).get(p5).add(new ArrayList<Double>()) ; 
		
								RobinHood rh = null ; 
								ArrayList<Double> runIndices = new ArrayList<Double>() ;
								for(int run=0;run<50;run++){
									System.out.println("p1="+tpRange.get(p1)+" p2="+stopRange.get(p2)+" p3="+slopeRange.get(p3)+" p4="+m1Range.get(p4)+" p5="+m2Range.get(p5)+" p6="+m3Range.get(p6)+" run="+run) ;					
									rh = new RobinHood("EURUSD",tpRange.get(p1),-tpRange.get(p2),m1Range.get(p4),m2Range.get(p5),m3Range.get(p6),slopeRange.get(p3)) ;
									//LinePlot lp = new LinePlot() ; int count = 0 ;  		
									int iStart =(int)(Math.random()*100000) ; 
									runIndices.add((double)iStart) ; 
									for(int i=iStart;i<arr.size();i++){
										rh.update(arr.get(i).open) ;
										//double[] newvals = {rh.raw.get(count),rh.mvgs.get(0).get(count),rh.mvgs.get(1).get(count),rh.mvgs.get(2).get(count)} ;count ++ ; lp.update(newvals, rh.profits) ;try{Thread.sleep(20);}catch(Exception e){}			
									}		
									meanProfits.get(p4).get(p5).get(p6).add(rh.profits/rh.ntrades) ; 
									//meanProfits.get(p4).get(p5).get(p6).add((double)(p1+p2+p3+p4+p5+p6)) ; 
									System.out.println("Adding value = " + (double)(p1+p2+p3+p4+p5+p6)) ; 
									nTrades.get(p4).get(p5).get(p6).add(rh.ntrades) ; 
								}										
								ArrayList<Double> paramList = params.get(p4).get(p5).get(p6) ; 
								paramList.add(tpRange.get(p1)) ; paramList.add(stopRange.get(p2)) ; paramList.add(slopeRange.get(p3)) ; paramList.add((double)m1Range.get(p4)) ; paramList.add((double)m2Range.get(p5)) ; paramList.add((double)m3Range.get(p6)) ; 
								for(Double d:runIndices)paramList.add(d) ; 
							}
						}
					}					
					String cPath = dirNames.get(0).get(0) +"//"+ dirNames.get(1).get(p1) +"//"+ dirNames.get(2).get(p2) +"//"+ dirNames.get(3).get(p3)+"//" ;
					Array2Nifti.writeArrayList4d(meanProfits, basePath+"//"+cPath+"meanProfits") ;
					Array2Nifti.writeArrayList4d(nTrades, basePath+"//"+cPath+"//"+"nTrades") ;
					Array2Nifti.writeArrayList4d(params, basePath+"//"+cPath+"//"+"params") ;
					//System.out.println("path = " + dirNames.get(0).get(0) +"//"+ dirNames.get(1).get(p1) +"//"+ dirNames.get(2).get(p2) +"//"+ dirNames.get(3).get(p3)+"//") ;
				}		
			}
		}
	}
}
