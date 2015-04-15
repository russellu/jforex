package sprites;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

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
public class SpriteBattleground extends myframe{

	String[] spriteDirNames = {"C:\\Users\\Acer\\Pictures\\improc\\topdown\\alexAttackingProc\\"} ; 
	players.Player spider ; 
	double[][][] playerLayer ; 
	PlayerControl control ;
	
	public SpriteBattleground(){
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
		SpriteHolder holder = new SpriteHolder(SpriteGetter.get(spriteDirNames[0])) ; 
		spider = new Player(holder, 100, 100) ; 
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
		
		g2.setColor(Color.BLACK) ; 
		g2.draw(new Rectangle2D.Double(0,0,100,100));
	}
	
	public void drawPlayers(){
		int currentX = spider.getX() ;
		int currentY = spider.getY() ;

		//System.out.println("currentx = "+ currentX + " current y = " + currentY + " currentW = " + currentW + " currentH = " + currentH) ; 
		short[][][] currentPic = spider.getCurrentPic() ; 
		int currentW = currentPic.length ; 
		int currentH = currentPic[0].length ; 
		for(int i=currentX; i<currentX+currentW;i++)
			for(int j=currentY;j<currentY+currentH;j++){
				//System.out.println(currentPic[i-currentX][j-currentY][0]) ; 
				playerLayer[i][j][0] = currentPic[i-currentX][j-currentY][0] ;  				
				playerLayer[i][j][1] = currentPic[i-currentX][j-currentY][1] ;  
				playerLayer[i][j][2] = currentPic[i-currentX][j-currentY][2] ;  
				playerLayer[i][j][3] = 255 ; //currentPic[i-currentX][j-currentY][3] ;  
			}
	}
	
	public void run(){
		while(true){
			try{Thread.sleep(20);}catch(Exception e){}	
			render() ;
			repaint() ; 
			spider.updateMovement(control);
			drawPlayers() ; 

		}
	}
	
	public static void main(String[]args){
		SpriteBattleground sbg = new SpriteBattleground() ; 
		new Thread(sbg).start() ;		
	}	
}
