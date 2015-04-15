package players;

public interface ControllablePlayer {

	public void moveStep(double xIncr, double yIncr) ; // move a certain step (current position + x/yIncr)
	public void moveJump(int newX, int newY) ; // jump to a new Location on the map 
	public void attack(int attackCode) ; // display a certain type of attack animation (pierce, crush, slash) 
	public void haltAttack() ;//stop attacking
	
	
}
