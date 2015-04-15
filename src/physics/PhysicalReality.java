package physics;

import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public interface PhysicalReality {
	
	
	public double[][][] getWorld() ;
	public int[][] getIdentifiers() ; 
	public Map<Integer,Occupying> getBodies() ;
	public JFrame getFrame() ; 
	public JPanel getPanel() ; 
}
