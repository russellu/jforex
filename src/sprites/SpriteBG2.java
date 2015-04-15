package sprites;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import players.Alex;
import players.Player;
import players.PlayerControl;
import improc.SpriteHolder;
import improc.myframe;
/**
 * load and display sprites. test their animations against an enemy
 * 
 * need a new player class...player being synonymous with sprite? not really. sprite is just the skin, player is behavior...
 * load the basic sprite first and then do the 8 angle rotations...store textures
 * each player has a SpriteManager class attached to it?
 * player as an interface? player.move(), player.attack(), ?
 * 
 * simplest most modular way to load sprites: send the path to the sprite loader object, it returns an ArrayList<ArrayList<short>>
 * of rotated sprites, each arrayList is a rotation, within each is a time point
 * 
 * add flies to the web...make it so the spider gets faster when he's walking on silk...magnetic attraction to the silk 
 * allows you to move unto it more easily.
 * 
 * @author russ
 *
 */



@SuppressWarnings("serial")
public class SpriteBG2 extends myframe{

//	String[] spriteDirNames = {"C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexAttackingProc\\"} ; 
	Alex spider ; 
	double[][][] playerLayer ; 
	PlayerControl control ;
	
	public SpriteBG2(){
		super() ; 
		bim = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB) ; 
		jf.setSize(new Dimension(700,700));
		g2=  bim.createGraphics() ; 
		playerLayer=  new double[bim.getWidth()][bim.getHeight()][4] ; 
		initPlayers() ; 
		control = new PlayerControl(spider) ; 
		initControls() ; 
	}
	
	public void initPlayers(){
	//	SpriteHolder holder = new SpriteHolder(SpriteGetter.get(spriteDirNames[0])) ; 
		spider = new Alex(100, 100) ; 
	}
	
	public void initControls(){
		addMouseListener(control);
		addMouseMotionListener(control) ;
		addMouseWheelListener(control) ;
		jf.addKeyListener(control) ;	
	}
	
	public void render(){
		WritableRaster rast = bim.getRaster() ;
		for(int i=0;i<bim.getWidth();i++)
			for(int j=0;j<bim.getHeight();j++){
				rast.setSample(i, j, 0, playerLayer[i][j][0]);				
				rast.setSample(i, j, 1, playerLayer[i][j][1]);
				rast.setSample(i, j, 2, playerLayer[i][j][2]);
				rast.setSample(i, j, 3, 255);
			}
		
		resetScene() ; 
	}
	
	
	public void resetScene(){
		
		for(int i=0;i<playerLayer.length;i++)
			for(int j=0;j<playerLayer[0].length;j++){
				playerLayer[i][j][0] = 0 ;			
				playerLayer[i][j][1] = 0 ;
				playerLayer[i][j][2] = 0 ;
				playerLayer[i][j][3] = 0 ;
			
			}
	}
	
	public void drawPlayers(){
		int currentX = spider.getX() ;
		int currentY = spider.getY() ;

		//System.out.println("currentx = "+ currentX + " current y = " + currentY + " currentW = " + currentW + " currentH = " + currentH) ; 
		short[][][] currentPic = spider.getCurrentPic() ; 
		short[][][] currentAura = spider.getCurrentAura() ;
		int auraW = currentAura.length ; 
		int auraH = currentAura[0].length ; 
		int currentW = currentPic.length ; 
		int currentH = currentPic[0].length ; 
		
		for(int i=currentX; i<currentX+currentW;i++)
			for(int j=currentY;j<currentY+currentH;j++)
			if(currentPic[i-currentX][j-currentY][0]!=0){
				//System.out.println(currentPic[i-currentX][j-currentY][0]) ; 
				playerLayer[i][j][0] = currentPic[i-currentX][j-currentY][0] ;  				
				playerLayer[i][j][1] = currentPic[i-currentX][j-currentY][1] ;  
				playerLayer[i][j][2] = currentPic[i-currentX][j-currentY][2] ;  
				playerLayer[i][j][3] = 255 ; //currentPic[i-currentX][j-currentY][3] ;  
			}
			else if(i-currentX < auraW && j-currentY < auraH){
				playerLayer[i][j][0] = currentAura[i-currentX][j-currentY][0]*spider.redScale ; 				
				playerLayer[i][j][1] = currentAura[i-currentX][j-currentY][1]*spider.greenScale ; 
				playerLayer[i][j][2] = currentAura[i-currentX][j-currentY][2]*spider.blueScale ; 
				playerLayer[i][j][3] = currentAura[i-currentX][j-currentY][3] ; 
			}
	}
	
	public void run(){
		while(true){
			try{Thread.sleep(30);}catch(Exception e){}	
			render() ;
			repaint() ; 
			spider.updateMovement(control);
			drawPlayers() ; 
		}
	}
	
	public static void main(String[]args){
		SpriteBG2 sbg = new SpriteBG2() ; 
		new Thread(sbg).start() ;		
	}	
}
