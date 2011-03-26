package plan.path;

import plan.path.pathfinding.Robot;

/** 
 * A unit class allowing you to set different types
 * to a variety of units
 * 
 * @author Daniel Stanoescu
 */

public class Unit implements Robot {

	private int type;
	
	public Unit(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
}
