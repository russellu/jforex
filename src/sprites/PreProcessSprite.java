package sprites;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import improc.myframe;


/**
 * pre process sprites by cropping them by hand
 * save as .png, load into matlab. apply image processing, save 
 * normalized image. 
 * 
 * @author russ
 *
 */

public class PreProcessSprite extends myframe implements ScrollableMap {

	 
	SpriteControl control ;
	ArrayList<short[][][]> spriteFrames = new ArrayList<short[][][]>() ; 
	int currentFrame = 0 ;
	ArrayList<CroppedSprite> saveRects = new ArrayList<CroppedSprite>() ;
	//String savePath = "C:\\Users\\Acer\\Pictures\\improc\\topdown\\spiderCrop\\" ;
	String savePath = "C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexAttackingCrop\\" ;
	int mouseX ; 
	int mouseY ; 
	int boxSize = 50 ; 
	
	public PreProcessSprite(){
		bim = new BufferedImage(1400,1400,BufferedImage.TYPE_INT_ARGB) ; 
		jf.setSize(new Dimension(1400,1400));
		g2 = bim.createGraphics() ; 
		control = new SpriteControl(this) ;
		jf.addKeyListener(control);
		addMouseMotionListener(control) ; 
		addMouseListener(control) ;
		addMouseWheelListener(control) ; 
		getSprites("C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexAttacking\\") ; 
		
		
	}
	public void getSprites(String path){
		File f = new File(path) ; 
		File[] allFiles = f.listFiles() ; 
		for(int i=0;i<allFiles.length;i++){
			System.out.println(allFiles[i].getName()) ; 
			spriteFrames.add(PNG2Short.RGB2SHORT(path + allFiles[i].getName())) ; 
			
		}
	}
	
	
	
	public void render(){
		//System.out.println("Rendering") ; 
		WritableRaster rast = bim.getRaster() ;
		short[][][] currentIm = spriteFrames.get(currentFrame) ; 
		
		for(int i=0;i<currentIm.length;i++)
			for(int j=0;j<currentIm[0].length;j++){
				rast.setSample(i,j,0,currentIm[i][j][0]) ; 				
				rast.setSample(i,j,1,currentIm[i][j][1]) ; 
				rast.setSample(i,j,2,currentIm[i][j][2]) ; 
				rast.setSample(i,j,3,255) ; 
			}
		
		g2.setColor(new Color(125,0,0));
		g2.draw(new Rectangle2D.Double(mouseX,mouseY,boxSize,boxSize));
		
		g2.setColor(Color.RED);
		for(int i=0;i<saveRects.size();i++){
			if(currentFrame == saveRects.get(i).frameIndex){
				double x = saveRects.get(i).x ;
				double y = saveRects.get(i).y ; 
				g2.draw(new Rectangle2D.Double(x,y,boxSize,boxSize)) ;	
			}		
		}	
	}
	
	public void saveEvents(){
		System.out.println("saving Images...") ;
		
		Format form = new DecimalFormat("#000") ; 
		
		for(int i=0;i<saveRects.size();i++){
		try {
		    // retrieve image
		    BufferedImage bi = saveRects.get(i).bim ; 
		    File outputfile = new File(savePath + form.format(i) + ".png");
		    ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace(); 
		}
		}
		System.out.println("images saved.") ; 
		
	}
	
	public void bump(int xIncr, int yIncr) {
		if(xIncr > 0){
			if(currentFrame < spriteFrames.size()-1)
				currentFrame ++ ; 
			else currentFrame = 0 ; 
		}
		else if(xIncr < 0){
			if(currentFrame > 0)
				currentFrame-- ; 
			else if(currentFrame==0)
				currentFrame = spriteFrames.size()-1 ; 		
		}	
	}

	public void jump(int xPos, int yPos) {
		// TODO Auto-generated method stub
		
	}

	public void zoom(int amt) {
		boxSize += amt ; 
	}
	
	public void clickLocation(int x, int y){
		//System.out.println("clicked at x = " + x + " y = " + y)  ;
		saveRects.add(new CroppedSprite(PNG2Short.shortRGBA2BuffRGB(spriteFrames.get(currentFrame), x, y, x+boxSize, y+boxSize),x,y,currentFrame)) ;
		
		
	}
	
	public void run(){
		while(true){
			try{Thread.sleep(10);}catch(Exception e){}
			render() ; 
			repaint() ; 		
		}	
	}
	
	public void setMouseLocation(int x, int y) {
		this.mouseX = x ;
		this.mouseY = y ;
	}

	
	public static void main(String[]args){
		

		PreProcessSprite pps = new PreProcessSprite() ; 
		new Thread(pps).start();
		
	}


}

