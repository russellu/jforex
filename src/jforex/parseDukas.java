package jforex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * class to load all CSV files in a directory, give by path
 * and then parse all the files individually into array lists of dukascopy objects
 * @author russ
 *
 */
public class parseDukas {
	
	//parse all files in the directory specified by 'path'
		public static ArrayList<dukas> parseOne(String path,String filename){
			//System.out.println("getting file : ") ;
			String files;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles(); 
			ArrayList<File> csvs = new ArrayList<File>() ;
			for (int i = 0; i < listOfFiles.length; i++){		 
			   if (listOfFiles[i].isFile()){
				   files = listOfFiles[i].getName();
			       //   System.out.println(files);

			       if (files.endsWith(filename)){			    	  
			       //   System.out.println(files);
			          csvs.add(listOfFiles[i]) ;
			       }
			   }
			}
			ArrayList<String> strs = parseDukas.getStrings(csvs.get(0)) ;
		//	ArrayList<dukas> marketPoints = parseDukas.getDukas(strs) ;
			ArrayList<dukas> marketPoints = parseDukas.getDukasCommas(strs) ;

			return marketPoints ;
		}
	
	
	//parse all files in the directory specified by 'path'
	public static ArrayList<ArrayList<dukas>> parseAll(String path){
	//	System.out.println("getting the following files : ") ;
		ArrayList<ArrayList<dukas>> allMarketPoints = new ArrayList<ArrayList<dukas>>() ;
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
		ArrayList<File> csvs = new ArrayList<File>() ;
		for (int i = 0; i < listOfFiles.length; i++){		 
		   if (listOfFiles[i].isFile()){
			   files = listOfFiles[i].getName();
		       if (files.endsWith(".csv") || files.endsWith(".CSV")){
		    	  
		          System.out.println(files);
		          csvs.add(listOfFiles[i]) ;
		       }
		   }
		}
		for(int i=0;i<csvs.size();i++){
			ArrayList<String> strs = parseDukas.getStrings(csvs.get(i)) ;
		//	ArrayList<dukas> marketPoints = parseDukas.getDukasCommas(strs) ;
			ArrayList<dukas> marketPoints = parseDukas.getDukas(strs) ;
			allMarketPoints.add(marketPoints) ;
		} 
		return allMarketPoints ;
	}
	
	
	// get all the strings out of the file currently being parsed 
	public static ArrayList<String> getStrings(File csvFile){
		ArrayList<String> datas = new ArrayList<String>() ;
		try {	
			    BufferedReader in = new BufferedReader(new FileReader(csvFile));
			    in.readLine() ; //skip over header
			    //System.out.println("reading "+csvFile) ;
			    for (String x = in.readLine(); x != null ; x = in.readLine()){
			    	datas.add(x) ;
			    }			    
			    in.close() ;
			   } catch (IOException e) {
			    System.out.println("File I/O error!");
			   }			
		return datas ;
	}
	
	// parse the strings individually into a dukascopy object
	public static ArrayList<dukas> getDukas(ArrayList<String> inStrings){	
		ArrayList<dukas> marketPoints = new ArrayList<dukas>() ;
		for(int i=0;i<inStrings.size();i++){
			// first split
			String[] tokens = inStrings.get(i).split("[ ]") ;	//System.out.println(tokens) ; 
			// second split (split date)
			String[] dateTokens = tokens[0].split("[.]") ;// System.out.println(dateTokens) ; 
 			int year = Integer.parseInt(dateTokens[0]) ;
			int month = Integer.parseInt(dateTokens[1]) ;
			int day = Integer.parseInt(dateTokens[2]) ;
			// third split (split time)
			String[] timeTokens = tokens[1].split("[:]") ;
			int hour = Integer.parseInt(timeTokens[0]) ;
			int minute = Integer.parseInt(timeTokens[1]) ;
			int second = Integer.parseInt(timeTokens[2]) ;
			// get the price points and volume
			float open = Float.parseFloat(tokens[2]) ;
			float high = Float.parseFloat(tokens[3]) ;
			float low = Float.parseFloat(tokens[4]) ;
			float close = Float.parseFloat(tokens[5]) ;
			float volume = Float.parseFloat(tokens[6]) ;
		//	if(volume != 0)
				marketPoints.add(new dukas(year,month,day,hour,minute,second,open,high,low,close,volume)) ;
		}
		return marketPoints ;
	}
	
	// parse the strings individually into a dukascopy object
		public static ArrayList<dukas> getDukasCommas(ArrayList<String> inStrings){	
			ArrayList<dukas> marketPoints = new ArrayList<dukas>() ;
			for(int i=0;i<inStrings.size();i++){	
				String[] tokens = inStrings.get(i).split("[ ]") ; 
				String[] datetokens = tokens[0].split("[.]") ; // System.out.println(datetokens[0]) ; 
				//System.out.println(tokens[0]) ; 
				String[] tokens2 = (tokens[1].split("[,]")) ; //System.out.println(tokens2[0]) ; 
				//System.out.println(tokens2[0]) ; 			
				int year = Integer.parseInt(datetokens[0]) ;
				int month = Integer.parseInt(datetokens[1]) ;
				int day = Integer.parseInt(datetokens[2]) ;
				// third split (split time)
				String[] timeTokens = tokens2[0].split("[:]") ;
				int hour = Integer.parseInt(timeTokens[0]) ;
				int minute = Integer.parseInt(timeTokens[1]) ;
				int second = Integer.parseInt(timeTokens[2]) ;
				// get the price points and volume
				float open = Float.parseFloat(tokens2[1]) ;
				float high = Float.parseFloat(tokens2[2]) ;
				float low = Float.parseFloat(tokens2[3]) ;
				float close = Float.parseFloat(tokens2[4]) ;
				float volume = Float.parseFloat(tokens2[5]) ;
			    marketPoints.add(new dukas(year,month,day,hour,minute,second,open,high,low,close,volume)) ;
			}
			return marketPoints ;
		}
	
	public static void main(String[] args) {
		ArrayList<ArrayList<dukas>> dukies = parseDukas.parseAll("C:\\Users\\russ\\Documents\\dukascopy\\corrpairs\\corr_1") ;
	}

}
