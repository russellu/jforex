package sprites;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.* ;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * gray2short => intensity image to 2-d array of shorts (alpha channel) 
 * rgb2short => rgb (3d) image to 3-d array of shorts (red,green,blue)
 * 
 * @author russ
 *
 */

public class PNG2Short {
	
	
	
	
	public static short[][][]RGB2SHORT(String path){
		BufferedImage bim = null ; 
		try{
			bim = ImageIO.read(new File(path)) ;
		}catch(Exception e){
			e.printStackTrace() ; 
		}
		short[][][] shorts = BufferedImage2Short(bim) ;
		return shorts ;
	}
	
	public static BufferedImage RGB2BufferedImage(String path){
		BufferedImage bim = null ; 
		try{
			bim = ImageIO.read(new File(path)) ;
		}catch(Exception e){
			e.printStackTrace() ; 
		}
		
		//bim = rotateTexture(bim,Math.PI/2) ; 
		return bim ; 
	}
	
	public static BufferedImage RGB2RotatedBufferedImage(String path,double radians){
		BufferedImage bim = null ; 
		try{
			bim = ImageIO.read(new File(path)) ;
		}catch(Exception e){
			e.printStackTrace() ; 
		}
		//getSubSection(bim,10,10,20,20)
		bim = rotateTexture(bim,radians) ; 
		return bim ; 
	}
	
	 public static short[][][] BufferedImage2Short(BufferedImage image) {

	      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      System.out.println("image width = " + width + " image height = " + height)  ;

	      int[][] result = new int[height][width];
	      short[][][] output = new short[height][width][4] ;
	      
	         final int pixelLength = 3 ;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += -16777216; // 255 alpha
	            argb += ((int) pixels[pixel] & 0xff); // blue
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
	 
	            output[row][col][0] = (short) ((argb >> 16) & 0x000000FF) ;       
	            output[row][col][1] = (short) ((argb >>8 ) & 0x000000FF) ; 
	            output[row][col][2] = (short) ((argb) & 0x000000FF) ; 
	            output[row][col][3] = 255 ;
	            
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      

	      return output ; 
	   }

	 
	 public static BufferedImage getSubSection(BufferedImage input,int x,int y,int w,int h){
		 short[][][] arr = BufferedImage2Short(input) ; 
		 BufferedImage output = new BufferedImage(w,h,input.getType()) ; 
		 WritableRaster rast = output.getRaster() ; 
		 
		 for(int i=0;i<w;i++)
			 for(int j=0;j<h;j++){
				 rast.setSample(i,j,0,arr[i+w][j+h][0]) ; 
				 rast.setSample(i,j,1,arr[i+w][j+h][1]) ; 
				 rast.setSample(i,j,2,arr[i+w][j+h][2]) ; 
			//	 rast.setSample(j,j,3,arr[i+w][j+h][3]) ; 				 
			 }
		 
		 return output ;
		 
	 }

		public static BufferedImage rotateTexture(BufferedImage bim,double radians){
			
			AffineTransform transform = new AffineTransform();
			transform.rotate(radians, bim.getWidth()/2, bim.getHeight()/2);
			AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			bim = op.filter(bim, null) ;
			//System.out.println(bim.getSubimage(5, 5, 25, 25).getWidth()) ; 
			return bim ;		
		}
	
		
		public static short[][] gray2Short(String path){ // get a grayscale PNG and turn it into a short[][]
			BufferedImage image = null ; 
			try{
				image = ImageIO.read(new File(path)) ;
			}catch(Exception e){
				e.printStackTrace() ; 
			}
			 final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		     final int width = image.getWidth();
		     final int height = image.getHeight();

		     int[][] result = new int[height][width];
		     short[][] output = new short[height][width] ;
		      
		        final int pixelLength = 1 ;
		        for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
		            int argb = 0;
		            argb += -16777216; // 255 alpha
		            argb += ((int) pixels[pixel] & 0xff); // blue
		            output[row][col] = (short) ((argb) & 0x000000FF) ;  		            
		            result[row][col] = argb;
		            col++;
		            if (col == width) {
		               col = 0;
		               row++;
		            }
		        }		      

		     return output ; 
		}
		
		
		// get a subsection or the whole short[][][] array and turn it back into a bufferedImage of that subsection size
		public static BufferedImage shortRGBA2BuffRGB(short[][][]input,int startX, int startY,int endX,int endY){
			
			BufferedImage output = new BufferedImage(endX-startX,endY-startY,BufferedImage.TYPE_INT_RGB) ; 
			WritableRaster rast = output.getRaster() ; 
			
			for(int i=startX;i<endX;i++)
				for(int j=startY;j<endY;j++){
					rast.setSample(i-startX, j-startY, 0, input[i][j][0]);					
					rast.setSample(i-startX, j-startY, 1, input[i][j][1]);
					rast.setSample(i-startX, j-startY, 2, input[i][j][2]);
				}
				
			return output ; 			
		}
		
		
		
		
		
		
		
		
}
