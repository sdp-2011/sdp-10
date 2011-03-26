package plan.path;

import java.awt.geom.Point2D;

import plan.path.pathfinding.AStar;
import plan.path.pathfinding.Path;
import plan.path.pathfinding.PathFinder;

/**
 * Computes an A* Search path and simplifies it to a set of waypoints such taht
 * it can send the robot a smaller number of points.
 *
 * @author Daniel Stanoescu
 */

public class Finder {

	/** Creates an abstract representation of the pitch on which the robots must move.*/
	public PitchMap map = new PitchMap();
	
	/** Creates a Pathfinder which uses A* search to find a path.*/
	private PathFinder finder;
	
	/** Creates a data structure that holds all the information about the path. */
	private Path path;
		
	/**
	 * Fill the area of the map with a type of terrain with height h and width w
	 * starting from coordinates (x,y)
	 *
	 * @param x The x coordinate of the point used to start the fill-up
	 * @param y The y coordinate of the point used to start the fill-up
	 * @param w The width of the rectangle that will fill the map
	 * @param h The height of the rectangle that will fill the map
	 * @param t The type of terrain used to fill the map
	 * @return Fills the map with the specified terrain
	 */
	public void fill(int x, int y, int w, int h, int t){
		map.fillArea(x, y, w, h, t);
	}
	
	/**
	 * Runs an A* search on the pitchmap given an origin and a destination
	 *
	 * @param selectedx The x coordinate of the origin of the path
	 * @param selectedy The y coordinate of the origin of the path
	 * @param dx The x coordinate of the path destination
	 * @param dy The x coordinate of the path destination
	 * @return The steps which make up the path
	 */

	public Path answer(int selectedx, int selectedy, int dx, int dy){
		
		finder = new AStar(map, 65, true);
		path = finder.findPath(new Unit(map.getUnit(selectedx, selectedy)), selectedx, selectedy, dx, dy);
		return path;
	}
	
// =================================================================================
// ====================== Path Simplification (Waypoints) =========================
// =================================================================================

	/**
	 * Takes a full path computed by the A* Search and calculates points
	 * where the vectors that make up the path are changing
	 *
	 * @param pC Takes as input an A* search found path\
	 * @return Returns a Point2D.Float list of waypoints. 
	 */

	public Point2D.Float[] simplePath(Path pC) {	

		Path pS = new Path();
		
		pS.appendStep(pC.getStep(0).getX(), pC.getStep(0).getY());

		int paxcx = 0;		
		int paycy = 0;
		int check = 0;

		for(int i = 1; i < pC.getLenght()-1; i++){

			int ax = pC.getStep(i-1).getX();
			int ay = pC.getStep(i-1).getY();
			

			int bx = pC.getStep(i).getX();
			int by = pC.getStep(i).getY();
	
			int cx = pC.getStep(i+1).getX();
			int cy = pC.getStep(i+1).getY();
	
		
				if ( (((ax + cx)*10)/2 != bx * 10) || (((ay + cy)*10)/ 2 != by * 10)){
					pS.appendStep(pC.getStep(i).getX(), pC.getStep(i).getY());
				}
		}	
		

		pS.appendStep(pC.getStep(pC.getLenght()-1).getX(), pC.getStep(pC.getLenght()-1).getY());

// =================================================================================
// ============================== Path Correction ==================================
// =================================================================================		
		
		System.out.println("Initial number of steps: " + (pS.getLenght()));

		for(int j = 0; j<pS.getLenght()-1; j++){
		
		if (((pS.getStep(j+1).getX()-pS.getStep(j).getX() == 1)
				&&
			(pS.getStep(j+1).getY()-pS.getStep(j).getY() == 1))
						||
			((pS.getStep(j).getX()-pS.getStep(j+1).getX() == 1)
				&&
			 (pS.getStep(j+1).getY()-pS.getStep(j).getY() == 1))
						||
			((pS.getStep(j+1).getX()-pS.getStep(j).getX() == 1)
				&&
			 (pS.getStep(j+1).getY()-pS.getStep(j).getY() == 1)))
		{
				System.out.println("Close Points !!");
				pS.removeStep(j+1);
		}

		}
		Point2D.Float [] wayPoints = new Point2D.Float [pS.getLenght()];

		System.out.println("The number of simplified steps: " + (pS.getLenght()));

// ====================================================================================
		
		for (int k = 0; k< pS.getLenght(); k++){
		
			wayPoints[k] = new Point2D.Float(pS.getStep(k).getX() * 10, pS.getStep(k).getY() * 10);
		//	System.out.println("Corected (x,y) [" + k + "] : " + wayPoints[k].x + " | " + wayPoints[k].y);
		}
		return wayPoints;
		
	}
	
}
