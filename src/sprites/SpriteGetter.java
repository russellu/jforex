package sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * class SpriteGetter: send it a path, it returns a 2d ArrayList of short[][][]
 * 
 * 
 * @author russ
 *
 */

public class SpriteGetter {

	static double pi = Math.PI ; 
	static double[] allowedAngles = {0,pi/4,pi/2,(3*pi)/4,pi,(5*pi)/4,(6*pi)/4,(7*pi)/4,2*pi} ;

	
	// get all the PNG files in a directory specified by path (they should all be oriented with the front facing up in the PNG)
	public static ArrayList<ArrayList<short[][][]>>get(String path){
		File f = new File(path) ; 
		String[] allPNGS = f.list() ;
		ArrayList<ArrayList<short[][][]>> allSprites = new ArrayList<ArrayList<short[][][]>>() ; 
		for(int i=0;i<allPNGS.length;i++){
			//System.out.println(path+allPNGS[i]) ; 
			BufferedImage currentFrame = PNG2Short.RGB2BufferedImage(path + allPNGS[i]); 
			allSprites.add(new ArrayList<short[][][]>()) ; 
			for(int j=0;j<allowedAngles.length;j++){
				allSprites.get(i).add(PNG2Short.BufferedImage2Short(PNG2Short.rotateTexture(currentFrame,allowedAngles[j]))) ; 				
			}
		}		
		return allSprites ; 
	}
	
	
	public static void main(String[]args){
		ArrayList<ArrayList<short[][][]>> allSprites = SpriteGetter.get("C:\\Users\\Acer\\Pictures\\improc\\topdown\\spiderProc\\") ; 
		System.out.println("number of sprites = " + allSprites.size() + 
				" number of angles = " + allSprites.get(0).size() +
				" w = " + allSprites.get(0).get(0).length + 
				" h = " + allSprites.get(0).get(0)[0].length) ; 
		
		
		
	}
	
}
