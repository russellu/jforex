package improc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class buffconvert {

	/**
	* Resizes an image using a Graphics2D object backed by a BufferedImage.
	* @param srcImg - source image to scale
	* @param w - desired width
	* @param h - desired height
	* @return - the new resized image
	*/
	public static BufferedImage getScaledImage(BufferedImage src, int w, int h){
	    int finalw = w;
	    int finalh = h;
	    double factor = 1.0d;
	    if(src.getWidth() > src.getHeight()){
	        factor = ((double)src.getHeight()/(double)src.getWidth());
	        finalh = (int)(finalw * factor);                
	    }else{
	        factor = ((double)src.getWidth()/(double)src.getHeight());
	        finalw = (int)(finalh * factor);
	    }   

	    BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	    g2.drawImage(src, 0, 0, finalw, finalh, null);
	    g2.dispose();
	    return resizedImg;
	}
	
	
	public static double[][][] getDoubleArr(BufferedImage img){
		double[][][]pinetree = new double[img.getWidth()][img.getHeight()][4] ;	
		for(int i = 0; i < img.getWidth(); i++)
		    for(int j = 0; j < img.getHeight(); j++){
		    	pinetree[i][j][0] = (new Color(img.getRGB(i, j)).getRed());
		    	pinetree[i][j][1] = (new Color(img.getRGB(i, j)).getGreen());
		    	pinetree[i][j][2] = (new Color(img.getRGB(i, j)).getBlue());
		    	if(pinetree[i][j][0]==0.0&&pinetree[i][j][1]==0.0&&pinetree[i][j][2]==0.0)
			    	pinetree[i][j][3] = 0 ; 
		    	else pinetree[i][j][3] = (new Color(img.getRGB(i, j)).getAlpha());	    	
		    }
		return pinetree ;
	}
	
}
