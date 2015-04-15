package improc;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class TestMaps extends myframe {

	
	
	double[][][]map = null ;
	
	
	public TestMaps(){
		System.out.println("getting map") ;
		
		//map = new MapLoader().GetMap(""); 
		////bim = new BufferedImage(map.length,map[0].length,BufferedImage.TYPE_INT_ARGB) ; 
		map = new MapLoader().getMapFast("") ; 
		
		System.out.println("map gotten") ; 
	}
	
	
	
	public void render(){
		
		WritableRaster rast = bim.getRaster() ;
		//System.out.println(" map width = " + map.length + " map height = " + map[0].length) ; 
		for(int i=0;i<500;i++)
			for(int j=0;j<500;j++){
				rast.setSample(j, i, 0, map[i][j][0]);
				rast.setSample(j, i, 1, map[i][j][1]);
				rast.setSample(j, i, 2, map[i][j][2]);
				rast.setSample(j, i, 3, map[i][j][3]);
			
			}	
		
	}
	

	
	public void run(){
		
		while(true){
			System.out.println("rendering") ; 
			render() ; 
			System.out.println("Repainting") ;
			repaint() ; 
		}
			
		
	}
	
	public static double[][] mapping = {{1,2,3,4},{1,2,3,4},{1,2,3,4}} ;
	public static double[] getVal(int b){
		return mapping[b] ; 
		
		
		
	}
	
	
	
	public static void main(String[]args){
		
		byte[][] b = new byte[5000][5000] ; 
		double[][][] dub = new double[500][500][4] ; 
		double startT = System.nanoTime() ;
		for(int t=0;t<100;t++)
			for(int i=0;i<dub.length;i++)
				for(int j=0;j<dub[0].length;j++){
					dub[i][j] = getVal(2) ;		
			}
			
		System.out.println("total time = " + (System.nanoTime() - startT)/1000000000) ; 
		
		//new Thread(new TestMaps()).start(); 
		/*
		double[][][] large = new double[5000][5000][4];
		System.out.println("large 1 init") ; 
		double[][][] large2 = new double[5000][5000][4] ;
		for(int i=0;i<large.length;i++)
			for(int j=0;j<large[0].length;j++){
				large2[i][j] = large[i][j] ; 
				System.out.println(large[i][j][1]) ;
			}
		*/
	}
}
