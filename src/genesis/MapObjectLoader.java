package genesis;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MapObjectLoader {

	
	
	
	
	public static ArrayList<DirectionArrow> loadArrows(String path){
		Scanner sc = null ; 
		ArrayList<DirectionArrow> output = new ArrayList<DirectionArrow>() ; 
		try{
			sc = new Scanner(new FileReader(path)) ; 
			while(sc.hasNextLine()){
				String[] next = sc.nextLine().split(" ") ;
				output.add(new DirectionArrow(Integer.parseInt(next[0]),Integer.parseInt(next[1]),Double.parseDouble(next[2]))) ;					
			}	
			sc.close() ; 
		}catch(Exception e){
			e.printStackTrace();
		}			
		return output ; 	
	}
	
	
	public static void main(String[]args){
		MapObjectLoader.loadArrows("C:\\Users\\Acer\\Pictures\\improc\\maps\\default.txt") ; 
		
		
		
		
	}
	
}
