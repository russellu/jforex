package utility;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class SaveArray {
	
	
	public static void main(String[]args){
		
		ArrayList<ArrayList<ArrayList<Double>>> views = new ArrayList<ArrayList<ArrayList<Double>>>() ;
		for(int i=0;i<100;i++){
			views.add(new ArrayList<ArrayList<Double>>()) ;
			for(int j=0;j<100;j++){
				views.get(i).add(new ArrayList<Double>()) ; 
				for(int k=0;k<100;k++){
					views.get(i).get(j).add(Math.random()) ; 
				}
			}
		}
		saveArrayList3d(views,"test.txt") ; 
	}
	public static void saveArrayList(ArrayList<ArrayList<Double>> views,String name){
		
		PrintWriter pr = null;
		try {
			pr = new PrintWriter("C:\\Users\\Acer\\Documents\\"+name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i=0; i<views.size() ; i++){
			for(int j=0;j<views.get(i).size();j++){
				pr.print(views.get(i).get(j)+" ");
			}
				
		  pr.println();
		}
		pr.close();			
	}

public static void saveArrayList3d(ArrayList<ArrayList<ArrayList<Double>>> views,String name){
		System.out.println("Saving") ; 
		PrintWriter pr = null;
		try {
			pr = new PrintWriter("C:\\Users\\Acer\\Documents\\"+name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i=0; i<views.size() ; i++){
			for(int j=0;j<views.get(i).size();j++){

				for(int k=0;k<views.get(i).get(j).size();k++){
					pr.print(views.get(i).get(j).get(k)+" ");
			}				
			}
			pr.println(); 
		}
		pr.close();			
	}
	
	
}
