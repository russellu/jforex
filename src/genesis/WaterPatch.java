package genesis;

public class WaterPatch {
	
	int xloc ; //0-size(map)
	int yloc ; // 0-size(map)
	int rotation ; //-pi:pi
	int speed ; //0-1
	int brightness ; //0-255
	DirectionArrow dir ; 
	
	
	public WaterPatch(int xloc, int yloc, int rotation, int speed, int brightness){
		this.xloc = xloc ; 
		this.yloc = yloc ; 
		this.rotation = rotation ; 
		this.speed = speed ; 
		this.brightness = brightness ;
	}
	
	
	
	
	
	
}
