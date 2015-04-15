package genesis;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import sprites.PNG2Short;
import improc.myframe; 

/**
 * game of thrones => multiple threads coming together to form the plot. have it the same way in your
 * game, you can take multiple personalities at the start of the game otherwise they beome AI controlled
 * 
 * save the water directions as an ascii file of points and angles.
 * 
 * 
 * 
 * @author russ
 *
 */



@SuppressWarnings("serial")
public class CreatorFrame extends myframe {

	String defaultFolder = "C:\\Users\\Acer\\Pictures\\improc\\maps\\" ;
	String imgTitle = "default.png" ; 
	public Palette pal = new Palette(this) ; 
	ViewControl vc = new ViewControl(500,500,this) ;

	short[][][] pixMap ; // the pixel map that is drawn on each refresh
	ArrayList<DirectionArrow> arrows = new ArrayList<DirectionArrow>() ; 
	
	final int MAXFRAMEWIDTH = 500 ; 
	final int MAXFRAMEHEIGHT = 500 ; 
	
	boolean alreadyInFocus = false ;
	
	public CreatorFrame(int w, int h){
		super() ; 
		jf.setPreferredSize(new Dimension(w,h));	
	}

	
	public CreatorFrame(String input,int w,int h){
		super(); 
		jf.setLocation(500,0) ; 
		if (input.equals("blank")){
			System.out.println("Creating blank image of width = " + w + " and height = " + h) ; 
			createBlankGraded(w,h) ; 
		}
		else{
			pixMap = PNG2Short.RGB2SHORT(input) ;
			bim = new BufferedImage(vc.width,vc.height,BufferedImage.TYPE_INT_ARGB) ;	
		}
		
		
		
		vc.setMax(pixMap.length, pixMap[0].length);	
	}
	
	
	public void render(){
		
		// first get a new bufferedImage if the zoom has changed
		//System.out.println("bim width = " + bim.getWidth() + " bim height = " + bim.getHeight()) ; 
		
		WritableRaster rast = bim.getRaster() ; 
		
		int worldWidth = pixMap.length ; 
		int worldHeight = pixMap[0].length ;  
		
		// the raster must write from the ViewControl's viewpoint, but check the bounds of the bufferedImage also...
		int vcxpos = vc.xpos ; 
		int vcypos = vc.ypos ; 

		for(int i=vcxpos;i<vcxpos+vc.width;i++)
			for(int j=vcypos;j<vcypos+vc.height;j++){
				if(i < worldWidth && j < worldHeight && i > 0 && j > 0){
				//	System.out.println("Render loop bimw = " + bim.getWidth() + " bim h = " + bim.getHeight() ) ; 
					/*
					rast.setSample(i-vcxpos, j-vcypos, 0, pixMap[i][j][0]) ;
					rast.setSample(i-vcxpos, j-vcypos, 1, pixMap[i][j][1]) ;
					rast.setSample(i-vcxpos, j-vcypos, 2, pixMap[i][j][2]) ;
					rast.setSample(i-vcxpos, j-vcypos, 3, pixMap[i][j][3]) ;
					*/
				//	System.out.println(j) ; 
					rast.setSample(j-vcypos,i-vcxpos, 0, pixMap[i][j][0]) ;
					rast.setSample(j-vcypos, i-vcxpos, 1, pixMap[i][j][1]) ;
					rast.setSample(j-vcypos, i-vcxpos, 2, pixMap[i][j][2]) ;
					rast.setSample(j-vcypos, i-vcxpos, 3, pixMap[i][j][3]) ;
				}	
			}	
		if(pal.currentArrow != null){
			//System.out.println("drawing current arrow") ; 
			pal.currentArrow.drawTempSelf(rast, vc) ;
			}
		//pixMap[100][200][0] = 255 ; 
		//System.out.println("exiting render") ; 
	}
	
	public void createBlank(int w,int h){
		bim = new BufferedImage(vc.width,vc.height,BufferedImage.TYPE_INT_ARGB) ;
		pixMap = new short[w][h][4] ;

		for(int i=0;i<w;i++)
			for(int j=0;j<h;j++){
				pixMap[i][j][0] = 0 ;
				pixMap[i][j][1] = 0 ;
				pixMap[i][j][2] = 0 ;
				pixMap[i][j][3] = 255 ;
			}
		adjustFrame(w,h) ; 
	}
	
	public void createBlankGraded(int w,int h){ // create a graded meshgrid from 0 to max
		double max = w*h ; 
		double scale = 255/max ;
		bim = new BufferedImage(vc.width,vc.height,BufferedImage.TYPE_INT_ARGB) ;
		pixMap = new short[w][h][4] ;
		
		for(int i=0;i<w;i++)
			for(int j=0;j<h;j++){
				pixMap[i][j][0] = (short)((i*j)*scale) ;
				pixMap[i][j][1] = (short)((i*j)*scale) ;
				pixMap[i][j][2] = (short)((i*j)*scale) ;
				pixMap[i][j][3] = 255 ;

			}	
		adjustFrame(w,h) ; 
	}
	
	public void addArrow(int x,int y,double angle){
		
		
		NumberFormat format = new DecimalFormat("#0.00") ; 

		System.out.println("adding new arrow at x = " + (x + vc.xpos) + " y = " + (y + vc.ypos) + " angle = " + Double.parseDouble(format.format(angle)) ) ; 
		arrows.add(new DirectionArrow(x + vc.ypos ,y + vc.xpos , Double.parseDouble(format.format(angle)))) ;	
	}
	
	public void adjustFrame(int w, int h){
		
		if(w < MAXFRAMEWIDTH && h < MAXFRAMEHEIGHT){	
			jf.setPreferredSize(new Dimension(w+20,h+43)) ;
			jf.pack() ;
		}
		else {
			jf.setPreferredSize(new Dimension(MAXFRAMEWIDTH,MAXFRAMEHEIGHT)) ;
			jf.pack() ;			
		}		
	}

	public void addKeyBoard(Keys k){
		jf.addKeyListener(k) ;
	}
	
	public void saveImage(){ // save the current map
		BufferedImage toBeSaved = setFullImage() ; 
		System.out.println("creator frame writing image...") ; 
		
		try {
		    File outputfile = new File(defaultFolder+imgTitle);
		    ImageIO.write(toBeSaved, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}		
		System.out.println("image: " + defaultFolder + imgTitle + " saved.") ; 
	}
	public void saveArrows(){ //save the current vectors as a text file next to the map
		TxtSaver.saveArrows(defaultFolder+"default.txt",arrows);
		

	}
	
	//set the full image for saving
	public BufferedImage setFullImage(){
		BufferedImage saveIMG = new BufferedImage(pixMap.length,pixMap[0].length,BufferedImage.TYPE_INT_ARGB) ;
		WritableRaster rast = saveIMG.getRaster() ; 
		for(int i=0;i<pixMap.length;i++)
			for(int j=0;j<pixMap[0].length;j++){
				rast.setSample(i,j,0,pixMap[i][j][0]) ; 
				rast.setSample(i,j,1,pixMap[i][j][1]) ; 
				rast.setSample(i,j,2,pixMap[i][j][2]) ; 
				rast.setSample(i,j,3,pixMap[i][j][3]) ; 			
			}
		return saveIMG ; 
	}
	
	public void drawObjects(){
		for(int i=0;i<arrows.size();i++)
			arrows.get(i).drawSelf(pixMap);
		
		
	}
	public void run(){
		
		while(true){
			//try{Thread.sleep(5);}
			//catch(Exception e){}
			vc.update() ;
			drawObjects() ; 
			checkFocus() ; 
			render() ; 
			repaint() ; 
			
		}	
	}
	
	public void checkFocus(){
		Point jp = jf.getLocation() ;
		Point p = MouseInfo.getPointerInfo().getLocation() ; 
		//System.out.println("px = " + p.x + " py = " + p.y) ;
		if(p.x > jp.x && p.y > jp.y && p.x < jp.x + jf.getWidth() && p.y < jf.getHeight()){
			
			if(!alreadyInFocus){
				jf.requestFocus() ;
				alreadyInFocus = true ; 
				System.out.println("requesting focus") ; 
			}
		}
		else alreadyInFocus = false ;
	}
	
	
	public static void main(String[]args){
		
		new Thread(new CreatorFrame("C:\\Users\\Acer\\Pictures\\campics2\\river.png",800,800)).start() ; 
	}
	
}
