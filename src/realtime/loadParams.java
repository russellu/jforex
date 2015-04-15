package realtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class loadParams {

	class TPclass{
		String pair ; 
		double tp ; 
		double stop ; 
		public TPclass(String pair, double tp, double stop){
			this.pair = pair ; 
			this.tp = tp ; 
			this.stop = stop ; 
		}
		public String toString(){
			return "pair = " + pair + " tp = " + tp + " stop = " + stop ;
		}
	}
	
	public static ArrayList<TPclass> get(String path,String id){
		loadParams lp = new loadParams() ; //to instantiate the inner class...
		ArrayList<TPclass> tpclasses = new ArrayList<TPclass>() ; 
		File folder = new File(path);
		File[] files = folder.listFiles() ; 
		for(int i=0;i<files.length;i++){
			if(files[i].getName().equals(id)){
				System.out.println(files[i].getName()) ;
				try{
					BufferedReader in = new BufferedReader(new FileReader(files[i])) ; 
					for(String x = in.readLine(); x != null ; x = in.readLine()){
						//System.out.println(x) ; 
						String[] output = x.split(" ") ; 
						tpclasses.add(lp.new TPclass(output[0],Double.parseDouble(output[1]),Double.parseDouble(output[2]))) ; 
						//for (String s:output)System.out.println(s) ; 
					}					
				}catch(Exception e){}
			}
		}
		return tpclasses ; 
	}
	public static void main(String[]args){
		ArrayList<TPclass> tps = get("C:\\mscripts\\forex","celldata.txt") ; 
		for(TPclass t: tps)
			System.out.println(t) ; 
		
		
	}
}
