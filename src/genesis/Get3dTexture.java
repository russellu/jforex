package genesis;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import sprites.PNG2Short;

public class Get3dTexture {
	
	
	public static ArrayList<short[][][]> get3dRGB(String path){
		System.out.println("getting 3d RGB textures") ;
		ArrayList<short[][][]> arr = new ArrayList<short[][][]>() ; 
		for(int i=1;i<51;i++)
			arr.add(PNG2Short.RGB2SHORT(
					"C:\\Users\\Acer\\Pictures\\improc\\maps\\river\\"+i+".png")) ; 
		return arr ; 
		
	}
	
	public static ArrayList<short[][][]> getRotated3dRGB(String path,double radians){
		ArrayList<short[][][]> arr = new ArrayList<short[][][]>() ; 
		for(int i=1;i<51;i++)
			arr.add(PNG2Short.BufferedImage2Short(
					PNG2Short.RGB2RotatedBufferedImage(
							"C:\\Users\\Acer\\Pictures\\improc\\maps\\river\\"+i+".png",radians))) ; 
		return arr ; 	
	}
	


}
