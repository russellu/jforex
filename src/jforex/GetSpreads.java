package jforex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class GetSpreads {
	
	
	
	
	
	public static HashMap<String,Double> getCommonSpreads(){
		
		File f = new File("C:\\Users\\Acer\\Documents\\COMMON_SPREADS.txt") ;
		HashMap<String,Double> spreads = new HashMap<String,Double>() ; 
		try{
			BufferedReader buff = new BufferedReader(new FileReader(f)) ;
			String s = "" ;
			int count = 0 ; 
			while((s=buff.readLine()) != null){
				if(count>0){
					String[] pieces = s.split(",") ; 
					String name = pieces[0] ; 
					double total = 0 ; 
					for(int i=1;i<pieces.length;i++)
						total += Double.parseDouble(pieces[i]) ; 
					total = (total/3)/10000 ; 
					spreads.put(name, total) ; 
				}
				count++ ;
			}
			buff.close() ; 
		}catch(Exception e){e.printStackTrace();}

		return spreads ; 
	}
	
	public static void main(String[]args){
		
		HashMap<String,Double> map = getCommonSpreads() ; 
		System.out.println(map.get("GBPUSD")) ; 
		
	}

}
