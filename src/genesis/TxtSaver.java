package genesis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TxtSaver {

	
	public static void saveArrows(String path,ArrayList<DirectionArrow>arrows){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(path)) ;
			for(int i=0;i<arrows.size();i++){
				DirectionArrow currentArrow = arrows.get(i) ; 
				String arrowLine = currentArrow.xloc + " " + currentArrow.yloc + " " + currentArrow.angle ;
				out.write(arrowLine);
			    out.newLine();
			}
			out.close() ; 
			
		}catch(Exception e){
			e.printStackTrace() ;
			
		}
		
		
		
		
	}
	
}
