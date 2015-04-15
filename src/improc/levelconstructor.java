package improc;

import java.util.ArrayList;

public class levelconstructor {
	
	double[][][] constructs ;
	ArrayList<double[]> constrlocs = new ArrayList<double[]>() ;
	ArrayList<double[]> constrsizes = new ArrayList<double[]>() ;
	
	public levelconstructor(double[][][]constructs){

		this.constructs = constructs ; 
		
	}
	public void addItem(double[][][] item,int x,int y){
		int w = item.length ; 
		int h = item[0].length ; 
		double[] iloc = new double[2] ; 
		double[] isize = new double[2] ;
		iloc[0] = y ; isize[0] = item.length ; 
		iloc[1] = x ; isize[1] = item[0].length ; 
		constrlocs.add(iloc) ; 
		constrsizes.add(isize) ; 
		for(int i=0;i<w;i++)
			for(int j=0;j<h;j++)
				if(item[i][j][3] != 0)
					constructs[i+x][j+y] = item[i][j] ; 	
	}
	
	
	
}
