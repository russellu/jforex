package sprites;

public interface ScrollableMap {

	public void bump(int xIncr, int yIncr) ; // bump the map over a few pixels
	public void jump(int xPos, int yPos) ; // jump to a new location on the map
	public void zoom(int amt) ; //zoom in and out (continuous)
	public void clickLocation(int x,int y) ; // respond to a click on the panel
	public void saveEvents() ; 
	public void setMouseLocation(int x,int y) ; // get the location of the mouse
	
	
	
}
