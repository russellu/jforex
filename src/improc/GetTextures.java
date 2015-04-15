package improc;

import java.util.ArrayList;

public class GetTextures {

	public static ArrayList<ArrayList<String>> getNames(String name){
		ArrayList<ArrayList<String>> playerImages = new ArrayList<ArrayList<String>>() ; 
		for(int i=0;i<7;i++){
			playerImages.add(new ArrayList<String>()) ; 	
			String extension = "res_man" + Integer.toString(i+1)+".png" ; 
			playerImages.get(i).add("C:\\Users\\Acer\\Pictures\\improc\\trees\\rot_0_"+extension) ; 
			playerImages.get(i).add("C:\\Users\\Acer\\Pictures\\improc\\trees\\rot_45_"+extension) ; 
			playerImages.get(i).add("C:\\Users\\Acer\\Pictures\\improc\\trees\\rot_90_"+extension) ; 
			playerImages.get(i).add("C:\\Users\\Acer\\Pictures\\improc\\trees\\rot_135_"+extension) ; 
			playerImages.get(i).add("C:\\Users\\Acer\\Pictures\\improc\\trees\\rot_180_"+extension) ; 
			playerImages.get(i).add("C:\\Users\\Acer\\Pictures\\improc\\trees\\rot_225_"+extension) ; 
			playerImages.get(i).add("C:\\Users\\Acer\\Pictures\\improc\\trees\\rot_270_"+extension) ; 
			playerImages.get(i).add("C:\\Users\\Acer\\Pictures\\improc\\trees\\rot_315_"+extension) ; 
		}
		return playerImages ; 	
	}
	
}
