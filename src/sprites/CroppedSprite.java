package sprites;

import java.awt.image.BufferedImage;

public class CroppedSprite {

	BufferedImage bim ; 
	int x ;
	int y ; 
	int frameIndex ; 
	
	public CroppedSprite(BufferedImage bim, int x, int y,int frameIndex){
		this.bim = bim ; 
		this.x = x ; 
		this.y = y ; 
		this.frameIndex = frameIndex ; 
	}
}
