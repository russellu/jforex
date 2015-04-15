package improc;

public class ControlMovement {
	
	keys k = new keys("keys") ; 
	StateUpdater iarr =  null ;
	
	public ControlMovement(){
				
	}
	
	public ControlMovement(StateUpdater input){		
		iarr = input ; 
	}
	
	public void update(Wolf p){
		
		boolean moved = false ; 
		int movincr = 2 ; 
		if(k.up&&k.right){
			System.out.println("moving up right") ; 
			p.move(-movincr,movincr) ; 
			moved = true ; 
		}
		else if(k.up&&k.left){
			p.move(-movincr,-movincr) ; 
			moved = true ; 
		}
		else if(k.down&&k.right){
			p.move(movincr,movincr) ; 
			moved = true ; 
		}
		else if(k.down&&k.left){
			p.move(movincr,-movincr) ; 
			moved = true ; 
		}
		else if(k.up){
			p.move(-movincr,0) ; 
			moved = true ; 
		}
		else if(k.down){
			p.move(movincr,0) ; 
			moved = true ; 
		}
		else if(k.left){
			p.move(0,-movincr) ; 
			moved = true ; 
		}
		else if(k.right){
			p.move(0,movincr) ; 
			moved = true ; 
		}
	}
	
	public void update(Viewport p){
		//System.out.println("being updated, current x = " + p.xloc) ; 
			
			int movincr = 2 ; 
			if(k.up&&k.right){
				System.out.println("moving up right") ; 
				p.move(-movincr,movincr) ; 
			}
			else if(k.up&&k.left){
				System.out.println("moving up left") ; 

				p.move(-movincr,-movincr) ; 
			}
			else if(k.down&&k.right){
				p.move(movincr,movincr) ; 
			}
			else if(k.down&&k.left){
				p.move(movincr,-movincr) ; 
			}
			else if(k.up){
				System.out.println("moving up") ; 

				p.move(-movincr,0) ; 
			}
			else if(k.down){
				p.move(movincr,0) ; 
			}
			else if(k.left){
				p.move(0,-movincr) ; 
			}
			else if(k.right){
				p.move(0,movincr) ; 
			}			
	}
	
}
