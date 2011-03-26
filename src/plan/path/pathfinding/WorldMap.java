package plan.path.pathfinding;

/**
 * Interface used to implement any number of maps, it standardize
 * the all the maps that you can create and use in your calculations.
 *
 * @author Daniel Stanoescu
 */

public interface WorldMap {
	
	/** Pitch Map Width */
	public int getWidthInTiles();
	
	/** Pitch Map Height */
	public int getHeightInTiles();
	
	/** Marks a tile as being visited */
	public void pathFinderVisited(int x, int y);
	
	/** Checks if the robot can be on that tile */
	public boolean blocked(Robot robot, int x, int y);
	
	/**
	 * Returns teh cost of moving through a tile from the current tile
	 *
	 * @param sx The x coordinate of the origin tile
	 * @param sy The y coordinate of the origin tile
	 * @param tx The x coordinate of the tile to move
	 * @param ty The y coordinate of the tile to move
	 * @return Returns the cost of moving through that tile.
	 */

	public float getCost(Robot robot, int sx, int sy, int tx, int ty);
	
}
