package live_fx;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.text.NumberFormatter;

public class FileTest {

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
		//directory structure:
		//currency//stop//tp//nifti
		ArrayList<Double> stopRange = getDoubleInRange(1,5,.1) ; 
		ArrayList<Double> tpRange = getDoubleInRange(1,10,.2) ; 
		ArrayList<String> stopNames = new ArrayList<String>() ; 
		ArrayList<String> tpNames = new ArrayList<String>() ; 
		NumberFormat form = new DecimalFormat("#00.00") ; 
		
		ArrayList<String> currencies = new ArrayList<String>() ; currencies.add("EURUSD") ; 
		for(Double d:stopRange)stopNames.add(form.format(d)) ; 
		for(Double d:tpRange)tpNames.add(form.format(d)) ; 		
		String basePath = "C:\\Users\\Acer\\Documents\\fxParams" ;
		File dir1 = new File(basePath) ; dir1.mkdir() ;	
		File dir2 = new File(basePath+"\\"+currencies.get(0)) ; dir2.mkdir() ; 
		for(int i=0;i<tpNames.size();i++){
			File idir = new File(basePath+"\\"+currencies.get(0)+"\\"+tpNames.get(i)) ;
			idir.mkdir() ; 
			for(int j=0;j<stopNames.size();j++){
				File jdir = new File(basePath+"\\"+currencies.get(0)+"\\"+tpNames.get(i)+"\\"+stopNames.get(j)) ; 
				jdir.mkdir() ; 
			}		
		}	
	}
}
