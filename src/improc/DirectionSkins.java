package improc;

import java.util.ArrayList;

public class DirectionSkins {

	ArrayList<double[][][]>skins = new ArrayList<double[][][]>() ; 
	ArrayList<Integer>dirs = new ArrayList<Integer>() ; 
	
	public DirectionSkins(){ // default
		
		
	}
	
	
	public void addDirectionSkin(double[][][]skin,int direction){ //add a skin with a corresponding direction
		skins.add(skin) ; 
		dirs.add(direction) ;		
	}
	
	public double[][][] getDirectionSkin(int dir){ // get the skin corresponding to a certain direction
		for(int i=0;i<dirs.size();i++)
			if(dirs.get(i)==dir)
				return skins.get(i) ; 
			
		return skins.get(0) ; 
	}
	
	
	
}
