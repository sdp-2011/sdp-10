package plan.path.pathfinding;

/**
 * An interface used by the A* Search to implement a number of heuristics,
 * all heuritstics have to satisfy the interface framework
 *
 * @author Daniel Stanoescu
 */

public interface Heuristics {

	/**
	 * An interface which supports pluggable heuristics for the A* search:
	 *
	 * @param map The map on which we compute the path
	 * @param robot The robot that needs to move along the path
	 * @param x The 'x' coordinate of the current tile
	 * @param y The 'y' coordinate of the current tile
	 * @param gx The 'x' coordinate of the goal tile
	 * @param gy the 'y' coordinate of the goal tile
	 * @return Returns the heuristic cost associated with the current tile.
	 */


	public float getCost(WorldMap map, Robot robot, int x, int y, int gx, int gy);

}
