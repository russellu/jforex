package sprites;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import improc.myframe;

@SuppressWarnings("serial")
public class TestDisplay extends myframe {
	
	private static short[][][]shortData ;
	
	public TestDisplay(){
		super() ; 
	}
	
	public void render(){
		WritableRaster rast = bim.getRaster() ; 
		//System.out.println("bim w = " + bim.getWidth() + " bim h = " + bim.getHeight() + " short w = " + shortData.length + " short h = " + shortData[0].length) ; 
		for(int i=0;i<shortData.length;i++)
			for(int j=0;j<shortData[0].length;j++){
				rast.setSample(i, j, 0, shortData[i][j][0]);				
				rast.setSample(i, j, 1, shortData[i][j][1]);
				rast.setSample(i, j, 2, shortData[i][j][2]);
				rast.setSample(i, j, 3, shortData[i][j][3]);
				
			}
		
		
	}
	
	
	public static void showImage(short[][][]array){
		shortData = array ; 
		TestDisplay disp = new TestDisplay() ; 
		disp.bim = new BufferedImage(array.length,array[0].length,BufferedImage.TYPE_INT_ARGB) ; 

		new Thread(disp).start();  
		
		
		
	}
	
	public static void main(String[]args){
		
		for(int i=1;i<50;i++)
			TestDisplay.showImage(PNG2Short.RGB2SHORT("C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexwalking\\1.png"));
		
			for(int i=1;i<50;i++)
				PNG2Short.RGB2SHORT("C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexwalking\\"+i+".png") ;
	}
	
	
	

}
