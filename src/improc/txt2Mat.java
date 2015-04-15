package improc;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class txt2Mat {

	private static double startT = 0 ;
	
	private static void printStartTime(String path,String function){
		startT = System.nanoTime() ;
		System.out.println("calling " + function + " on file "  + path) ; 
	}
	
	private static void printEndTime(String path,String function){
		System.out.println("done calling " + function + " on " + path + ", time elapsed (s) = " + (System.nanoTime()-startT)/1000000000) ; 
	}
	
	public static ArrayList<ArrayList<Double>> get2dArrayList(String path){
		printStartTime(path,"get2dDouble()") ;
		Scanner sc = null ; 
		ArrayList<ArrayList<Double>>output = new ArrayList<ArrayList<Double>>() ; 	
		try{
			sc = new Scanner(new FileReader(path)) ; 
			while(sc.hasNextLine()){
				output.add(new ArrayList<Double>()) ; 
				String[] next = sc.nextLine().split(" ") ;
				for(int i=0;i<next.length;i++){
					output.get(output.size()-1).add(Double.parseDouble(next[i])) ; 					
				}			
			}	
		}catch(Exception e){
			e.printStackTrace();
		}	
		printEndTime(path,"get2dDouble()") ; 	
		return output ; 
	}
	
	public static short[][] get2dArray(String path){
		printStartTime(path,"get2dArray()") ;
		Scanner sc = null ; 
		short[][] output = new short[5000][5000]; 
		try{
			sc = new Scanner(new FileReader(path)) ; 
			int r = 0 ; 
			while(sc.hasNextLine()){
				String[] next = sc.nextLine().split(" ") ;
				for(int c=0;c<next.length;c++){
					output[r][c] = (Short.parseShort(next[c])) ; 					
				}			
				r++ ; 
			}	
		}catch(Exception e){
			e.printStackTrace();
		}	
		printEndTime(path,"get2dDouble()") ; 	
		return output ; 
	}
	
	public static short[][] buff2short(BufferedImage image) {

	      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

	      short[][] result = new short[height][width];
	      if (hasAlphaChannel) {
	         final int pixelLength = 4;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            short argb = 0;
	            argb += (((short) pixels[pixel] & 0xff) << 24); // alpha
	            argb += ((short) pixels[pixel + 1] & 0xff); // blue
	            argb += (((short) pixels[pixel + 2] & 0xff) << 8); // green
	            argb += (((short) pixels[pixel + 3] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      } else {
	         final int pixelLength = 1;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            short argb = 0;
	            argb += -16777216; // 255 alpha
	            argb += ((short) pixels[pixel] & 0xff); // blue
	           // argb += (((short) pixels[pixel + 1] & 0xff) << 8); // green
	           // argb += (((short) pixels[pixel + 2] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      }

	      return result;
	   }
	
	
	
	
	public static short[][][] buffRGB2short(BufferedImage image) {

	      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      final int width = image.getWidth();
	      final int height = image.getHeight();

	      short[][][] result = new short[height][width][3];
	     
	         final int pixelLength = 3;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	        	// System.out.println("got pixel") ; 
	            short rgb = 0;
	            rgb += -16777216; // 255 alpha
	            rgb += ((short) pixels[pixel] & 0xff); // blue
	            rgb += (((short) pixels[pixel + 1] & 0xff) << 8); // green
	            rgb += (((short) pixels[pixel + 2] & 0xff) << 16); // red
	            Color c = new Color(rgb) ; 
	            result[row][col][0] = (short)c.getRed() ; 
	            result[row][col][1] = (short)c.getGreen() ; 
	            result[row][col][2] = (short)c.getBlue() ; 
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      

	      return result;
	   }
	
	
	public static short[][][] buffRGB2short_separate(String path) {

		ArrayList<String>colornames = new ArrayList<String>() ; 
		colornames.add("_red.png") ;		
		colornames.add("_green.png") ;
		colornames.add("_blue.png") ;
	      BufferedImage bim = null ; //new BufferedImage(5000,5000,BufferedImage.TYPE_INT_ARGB) ; 
			try{
				bim = ImageIO.read(new File(path + colornames.get(0))) ;
			}catch(Exception e){
				e.printStackTrace() ; 
			}
		short[][][] result = new short[bim.getHeight()][bim.getWidth()][3];

		for(int name = 0;name<colornames.size();name++){
			printStartTime(path,"get2dImage") ;
			bim = null ; //new BufferedImage(5000,5000,BufferedImage.TYPE_INT_ARGB) ; 
			try{
				bim = ImageIO.read(new File(path+colornames.get(name))) ;
			}catch(Exception e){
				e.printStackTrace() ; 
			}
			printEndTime(path,"get2dImage") ; 	
			
		      final byte[] pixels = ((DataBufferByte) bim.getRaster().getDataBuffer()).getData();
		      final int width = bim.getWidth();
		      final int height = bim.getHeight();
	
		         final int pixelLength = 1;
		         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
		            short gray = 0;
		            gray += -16777216; // 255 alpha
		            gray += ((short) pixels[pixel] & 0xff); // blue
		            result[row][col][name] = gray ;
		            col++;
		            if (col == width) {
		               col = 0;
		               row++;
		            }
		         }	      
		}
	      return result;
	   }
	
	
	
	public static short[][][]RGBPNG(String path){
		printStartTime(path,"get2dImage") ;
		BufferedImage bim = null ; //new BufferedImage(5000,5000,BufferedImage.TYPE_INT_ARGB) ; 
		try{
			bim = ImageIO.read(new File(path)) ;
		}catch(Exception e){
			e.printStackTrace() ; 
		}
		short[][][] shorts = buffRGB2short(bim) ;
		printEndTime(path,"get2dImage") ; 	
		return shorts ;
	}
	
	
	public static short[][] get2dImage(String path){
		printStartTime(path,"get2dImage") ;

		BufferedImage bim = null ; //new BufferedImage(5000,5000,BufferedImage.TYPE_INT_ARGB) ; 
		try{
			bim = ImageIO.read(new File(path)) ;
		}catch(Exception e){
			e.printStackTrace() ; 
		}
		
		short[][] shorts = buff2short(bim) ;
		
		printEndTime(path,"get2dImage") ; 	
		return shorts ;
	}
	
	public static short[][] get2dImageTest(String path){
		printStartTime(path,"get2dImage") ;
		
		BufferedImage bim = null ; //new BufferedImage(5000,5000,BufferedImage.TYPE_INT_ARGB) ; 
		try{
			bim = ImageIO.read(new File(path)) ;
		}catch(Exception e){
			e.printStackTrace() ; 
		}
		
		short[][] ints = buff2short(bim) ;
		
		printEndTime(path,"get2dImage") ; 
		return ints ;

	}
	
	
	public static void main(String[]args){
		//get2dArray("c://stuffs//5k.txt") ; 
		get2dImageTest("c://stuffs//5000.png") ; 
	}
	
}
