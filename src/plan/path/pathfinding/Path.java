package plan.path.pathfinding;

import java.util.ArrayList;

/** 
 * The data structure that models the Path, 
 * it contains a series of steps that has
 * (x,y) coordinates
 *
 * @author Daniel Stanoescu
 */


public class Path {


	/** An Array containing the steps that make up the path */
	private ArrayList steps = new ArrayList();
	
	public Path (){}
	
	/** Gets the lenght of the steps array */
	public int getLenght() {
		return steps.size();
	}
	
	/** Gets the step that lies at the index */
	public Step getStep(int index) {
		return (Step) steps.get(index);
	}
	
	/** Gets the x coordinate of the step at index */
	public int getX(int index) {
		return getStep(index).x;
	}
	
	/** Gets the y coordinate of the step at index */
	public int getY(int index) {
		return getStep(index).y;
	}
	
	/** Adds a new step with coordinates (x,y) to the array */
	public void appendStep(int x, int y) {
		steps.add(new Step(x,y));
	}
	
	/** Adds a new step to position 0 */
	public void prependStep(int x, int y) {
		steps.add(0, new Step(x,y));
	}

	/** Removes a step i from the steps array */
	public void removeStep(int i){
		steps.remove(i);
	}
	
	/** Checks if the step (x,y) is contained in the steps array */
	public boolean contains(int x, int y) {
		return steps.contains(new Step(x,y));
	}

		/** A step instance of the path */
		public class Step{
			
			private int x;
			private int y;
			
			/** Sets the (x,y) coordinates of the step */
			public Step(int x , int y){
				this.x = x;
				this.y = y;
			}

			/** Gets the x coordinate of the step */
			public int getX(){
				return x;
			}
			
			/** Gets the y coordinate of the step */
			public int getY(){
				return y;
			}
			
			/** Returns a hashCode */
			public int hashCode(){
				return x*y;
			}
			
			/** Checks if two steps are equivalent */
			public boolean equals(Object other) {
				if (other instanceof Step){
					Step o = (Step) other;
				
					return (o.x == x) &&(o.y == y);
				}
				return false;
			}
			
			
	}	
		
}
