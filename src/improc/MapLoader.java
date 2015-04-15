package improc;

import java.awt.image.* ;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class MapLoader {

	
	BufferedImage img = null ; 
	ArrayList<double[][][]> trees = new ArrayList<double[][][]>() ; 
	
	public double[][][] GetMap(String target){
		target = "C:\\Users\\Acer\\Pictures\\improc\\forests\\forestseed.png" ; 
		BufferedImage bim = null ; 
		try{
			bim = ImageIO.read(new File(target)) ; 
			
		}catch(Exception e){e.printStackTrace();}
		
		double[][][] newmap = buffconvert.getDoubleArr(bim) ; 
		int mapscalefac = 1 ; 
		double[][][] bigmap = new double[newmap.length*mapscalefac][newmap[0].length*mapscalefac][4] ; 
		
		for(int i=0;i<bigmap.length;i++)
			for(int j=0;j<bigmap[0].length;j++){
				bigmap[i][j] = newmap[i/mapscalefac][j/mapscalefac] ; 	
		}
		
 		return bigmap ; 
		
		
	}
	
	public double[][][] getMapFast(String target){	
		target = "C:\\Users\\Acer\\Pictures\\improc\\forests\\forestseed.png" ; 
		BufferedImage bim = null ; 
		try{
			bim = ImageIO.read(new File(target)) ; 			
		}catch(Exception e){e.printStackTrace();} 		

		return getFast(bim)  ; 
		 
	}
	
	 public double[][][] getFast(BufferedImage bim){
		 double[][][] pixels = new double[bim.getWidth()][bim.getHeight()][4] ; 
		 int[] pixel;
		 WritableRaster rast = bim.getRaster() ;
		 for (int y = 0; y < bim.getHeight(); y++) {
		     for (int x = 0; x < bim.getWidth(); x++) {
		         pixel = rast.getPixel(x, y, new int[3]);
		         //System.out.println(pixel[0] + " - " + pixel[1] + " - " + pixel[2] + " - " + (bim.getWidth() * y + x));
		         pixels[x][y][0] = pixel[0] ; 
		         pixels[x][y][1] = pixel[1] ; 
		         pixels[x][y][2] = pixel[2] ; 
		         pixels[x][y][3] = 255 ; 

		     }
		 }
		 return pixels ;
		 
	 }

	public ArrayList<double[][][]>setupTrees(double[][][] heatmap, globalarr garr){
		int xfact = 1 ;
		System.out.println("init terrain") ; 
		double[][][] terrain = new double[heatmap.length*xfact][heatmap[0].length*xfact][4] ;
		System.out.println("init constructs") ; 

		double[][][] constructs = new double[heatmap.length*xfact][heatmap[0].length*xfact][4] ;
		System.out.println("init players") ; 

		double[][][] players = new double[heatmap.length*xfact][heatmap[0].length*xfact][4] ;
		System.out.println("init occupied") ; 

		boolean[][] occupied = new boolean[heatmap.length*xfact][heatmap[0].length*xfact] ;
		System.out.println("init loop") ; 

		
		for(int i=0;i<terrain.length;i++)
			for(int j=0;j<terrain[0].length;j++){
				double[] currentpixel = {Math.random()*60,125+Math.random()*80,Math.random()*60,255} ; 
				//double[] currentpixel = {j%39,i%165,j%30,255} ; 
				//terrain[i][j] = heatmap[i][j] ; 
				terrain[i][j] = currentpixel ; 
				if(heatmap[i][j][0]==255)
					addTree(constructs,occupied,i,j) ; 
			//	System.out.println(heatmap[i/10][j/10][0]) ; 		
			}
		
		ArrayList<double[][][]> allmaps = new ArrayList<double[][][]>() ; 
		allmaps.add(terrain) ; 
		allmaps.add(constructs) ; 
		allmaps.add(players) ; 
		garr.setOccupied(occupied);
		return allmaps ; 
		
	}
	
	public void loadTerrains(){
	// load terrain objects
			ArrayList<String>images = new ArrayList<String>() ;
			images.add("C:\\Users\\Acer\\Pictures\\improc\\trees\\trans_pine1.png") ; 
			images.add("C:\\Users\\Acer\\Pictures\\improc\\trees\\trans_pine2.png") ; 
			images.add("C:\\Users\\Acer\\Pictures\\improc\\trees\\trans_pine3.png") ; 
		
			for(int i=0;i<images.size();i++){
				try {
				    img = ImageIO.read(new File(images.get(i)));
				} catch (IOException e) {
				}
				img = buffconvert.getScaledImage(img, 20, 20) ; 
				trees.add( buffconvert.getDoubleArr(img)) ; 
				
			}
	}
	
	public void addTree(double[][][]constructs,boolean[][]occupied, int x, int y){//
		
		if(x > constructs.length-100 || y > constructs[0].length-100)
			return ; 
		
		double[][][]currtree = null ; 
		if(Math.random() > .66)
			currtree = trees.get(0) ;
		else if(Math.random() <.33)	
			currtree = trees.get(1) ;
		else currtree = trees.get(2) ; 
				
				for(int w=0;w<currtree.length;w++)
					for(int h=0;h<currtree[0].length;h++)
						if(currtree[w][h][3] != 0)
							constructs[w+x][h+y] = currtree[w][h] ; 			
				for(int rx=x+25;rx<x+75;rx++)
					for(int ry=y+25;ry<y+75;ry++)
						occupied[rx][ry] = false ; 				
				
	}
	
	
	public void initLevel(double[][][]constructs, boolean[][]occupied){
		
		levelconstructor lc = new levelconstructor(constructs) ; 		
		
		for(int tree=0;tree<1;tree++)
			for(int i=0;i<1;i++){
				int randx = (int)(Math.random()*350+50) ;
				int randy = (int)(Math.random()*350+50) ;
				lc.addItem(trees.get(tree), randx,randy) ;
				for(int rx=randx+25;rx<randx+75;rx++)
					for(int ry=randy+25;ry<randy+75;ry++)
						occupied[rx][ry] = true; 				
			}	
	}
	

	
	// get water which is actually get multiple tiles of any png file
	public ArrayList<BufferedImage> getWater(String path){
		path = "C:\\Users\\Acer\\Pictures\\improc\\terraintest\\stream\\" ; 
		ArrayList<BufferedImage> bims = new ArrayList<BufferedImage>() ; 
		for(int i=1;i<=22;i++){
			String name = path + "stream_" + i + ".png"; 
			System.out.println(name) ; 
			try{
				bims.add(ImageIO.read(new File(name))) ; 
			}catch(Exception e){e.printStackTrace();}
			
		}
		return bims ; 
		
		
		
	}
	
	public short[][] getDLM(String path){
		//path = "C:\\Users\\Acer\\Pictures\\improc\\terraintest\\watermap.txt" ; 
		short[][] dlmvals = new short[5000][5000] ;
		try {	
			BufferedReader in = new BufferedReader(new FileReader(path));
			System.out.println("reading "+path) ;
			String x = null ; 
			for(int row=0;row<dlmvals.length;row++){
				x = in.readLine() ;
				String[] currRow = x.split(" ") ; 
				for(int col=0;col<currRow.length;col++)
					dlmvals[row][col] = (short)Double.parseDouble(currRow[col]) ; 				
			}	    
			in.close() ;
		} catch (IOException e) {System.out.println("File I/O error!");}		
		
		System.out.println("returning DLM values, size = " + dlmvals.length + " height = " + dlmvals[0].length) ; 
		
		return dlmvals ; 
	}
	
	public void refreshMaps(){ //refresh all maps with new matlab arrays
		
		
		
	}
	
	
	
	
	public static void main(String[]args){
		
		//new MapLoader().getWater("") ;
		short[][] vals = new MapLoader().getDLM("C:\\Users\\Acer\\Pictures\\improc\\terraintest\\watertrans.txt");
		for(int i=0;i<vals.length;i++){
			System.out.println() ; 

			for(int j=0;j<vals[0].length;j++)
				System.out.print(vals[i][j] + " ") ; 

		}
	}
	
}
