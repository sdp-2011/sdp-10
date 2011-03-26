package plan.path.pathfinding;

/**
 * An interface which makes it possible for a robot to 
 * find a path from one point on the tile map to another one
 * based on that tile map.
 *
 * @author Daniel Stanoescu
 */

public interface PathFinder {

	/**
	 * Finds a path from (sx, sy) to (dx, dy) on a tile map
	 * taking into account the obstacles defined and also
	 * the cost of moving from one tile to the other
	 *
	 * @param robot The unit that we want to move along the path
	 * @param sx The x coordinate of the origin of the path
	 * @param sy The y coordinate of the origin of the path
	 * @param dx The x coordinate of the destination of the path
	 * @param dy The y coordinate of the destination of the path
	 * @return The path from (sx, sy) to (dx, dy) or Null if no path was found
	 */

	public Path findPath(Robot robot, int sx, int sy, int dx, int dy);
	
}
