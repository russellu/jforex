package improc;

import java.util.ArrayList;

public class PlayerTextures {

	
	
	
	
	
	
	
	
	public static ArrayList<ArrayList<short[][][]>> getMovementTextures(String basePath,int ntextures){
		
		// "C:\Users\Acer\Pictures\improc\animals"
		ArrayList<String> angleNames = new ArrayList<String>() ; 
		angleNames.add("0\\") ;
		angleNames.add("45\\") ;
		angleNames.add("90\\") ;
		angleNames.add("135\\") ;
		angleNames.add("180\\") ;
		angleNames.add("225\\") ;
		angleNames.add("270\\") ;
		angleNames.add("315\\") ;
		
		//first dimension of array list => angles, 2nd dimension => time
		
		ArrayList<ArrayList<short[][][]>> allTexs = new ArrayList<ArrayList<short[][][]>>() ;  
		for(int i=0;i<angleNames.size();i++){
			allTexs.add(new ArrayList<short[][][]>()) ; 
			for(int j=0;j<ntextures;j++){
				String fullPath = basePath + angleNames.get(i) + j + "" ; 
				allTexs.get(i).add(txt2Mat.buffRGB2short_separate(fullPath)) ;
				System.out.println(fullPath) ; 				
			}
			
			
			
			
		}
		return allTexs ; 
	}
}
