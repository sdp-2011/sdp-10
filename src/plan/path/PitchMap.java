package plan.path;

import java.awt.geom.Point2D;

import plan.path.pathfinding.Robot;
import plan.path.pathfinding.WorldMap;

/**
 * A class which defines the abstract representation of the real pitch, and reflects
 * all the changes of that occur in the real world. 
 *
 * @author Daniel Stanoescu
 */

public class PitchMap implements WorldMap{

	/** Setting the size of the search space for the A* algorithm  */

	public static final int width = 65;
	public static final int height = 65;
	
	/** We define all the different types of terrains we are going to use on the pitch  */

	public static final int grass = 0;
	public static final int water = 1;
	
	/** Define all the objects on the pitch, this is used to mark the existance of our bot and different obstacles */	
	public static final int stewie = 2;
	public static final int lois = 3;
	public static final int loisCenter = 4;
	
	/** The terrain matrix which holds all the different terrain information */
	private int[][] terrain = new int[width][height];
	
	/** The unit matrix which holds information about the position of differen units */
	private int[][] units = new int[width][height];
	
	/** The visited matrix which keeps track of all the visited nodes throughout the PitchMap */
	private boolean[][] visited = new boolean[width][height];
	
			/**
			 * Method for filling an area with a constrain type on the map
			 * 
			 * @param x : The x coordinate where you want to start filling
			 * @param y : The y coordinate where you want to start filling
			 * @param witdh : The width in tiles of the size of the area you want to fill
			 * @param height : The height in tiles of the size of the area you want to fill
			 * @param type : The type of field you want to use. Use 1 by default
			 * @return Fills the appropiate area with the specified type of terrain
			 */
	
			public void fillArea(int x, int y, int width, int height, int type) {
				for (int xp=x;xp<x+width;xp++) {
					for (int yp=y;yp<y+height;yp++) {
						terrain[xp][yp] = type;
					}
				}
			}

			/**
			 * Methdo used for surrounding the center of the robot with water so we don't
			 * bump into it and plan arround it.
			 *
			 * @param center The center of the robot is used to padd the area around it with a terrain type.
			 * @return Fills the robot area on the terrain matrix.
			 */
			
			public void fillRobot(Point2D.Float center){
				int xC = (int) center.x/10;
				int yC = (int) center.y/10;
				int x0 = xC-5;
				int y0 = yC-5;
				
				if (x0 < 0){
					x0 = 0;
				}
				if (xC >= 60){
					xC = 59;
				}
				if (y0 < 0){
					y0 = 0;
				}
				if (yC >= 30){
					yC = 29;
				}

				for (int x=x0; x<xC+6; x++){
					for (int y=y0; y<yC+6; y++){
						terrain[x][y] = 3;
					}
				}
				terrain[xC][yC] = 4;
			}
			
			/** Method used to clear the visited tiles on the map */
			public void clearVisited() {
				for (int x=0;x<getWidthInTiles();x++) {
					for (int y=0;y<getHeightInTiles();y++) {
						visited[x][y] = false;
					}
				}
			}
					
			/** Checks if the tile at coordinates (x,y) has been visited
			 *
			 * @param x The x coordinates of the tile 
			 * @param y The y coordinates of the tile
			 * @return The tile at (x,y)
			 */

			public boolean visited(int x, int y) {
				return visited[x][y];
			}
			
			/** Returns the terain type at coordinates (x,y)
			 *
			 * @param x The x coordiante of the tile
			 * @param y The y coordinate of the tile
			 * @return The type of the tile (x,y)
			 */

			public int getTerrain(int x, int y) {
				return terrain[x][y];
			}
			
			/** Returns the unit type at coordinates (x,y)
			 *
			 * @param x The x coordinate of the unit
			 * @param y The y coordinate of the unit
			 * @return The unit at coordinates (x,y)
			 */
			public int getUnit(int x, int y) {
				return units[x][y];
			}
			
			/** Sets a tile to a unit of a mentioned type at (x,y)
			 *
			 * @param x The x coordinate of the unit
			 * @param y The y coordinate of the unit
			 * @param unit The specified unit type
			 * @return Sets the unit at coordinate(x,y) on the unit map
			 */

			public void setUnit(int x, int y, int unit) {
				units[x][y] = unit;
			}

			/**
			 * Constraints the robot from moving onto that tile and specifies
			 * the type of terrain that the robot can't move onto.
			 *
			 * @param robot The unit which you want to move on a tile
			 * @param x The x coordinates of the tile where you want to move the robot
			 * @param y The y coordinates of the tile where you want to move the robot
			 */

			public boolean blocked(Robot robot, int x, int y) {
				// if there's a unit at the location, then it's blocked
				if (getUnit(x,y) != 0) {
					return true;
				}
					
				int unit = ((Unit) robot).getType();
				
				// constrains stewie to only walk on grass
				if (unit == stewie) {
					return terrain[x][y] != grass;
				}
				
				// unknown unit so everything blocks
				return true;
			}
			
			/** Sets the cost of the robot moving from one point to the other */
			public float getCost(Robot robot, int sx, int sy, int tx, int ty) {
				return 1;
			}
			
			/** Gets the hight of the pitch map */	
			public int getHeightInTiles() {
				return width;
			}

			/** Gets the width of the pitch map */
			public int getWidthInTiles() {
				return height;
			}
			
			/** Marks the tile at (x,y) as visited */
			public void pathFinderVisited(int x, int y) {
				visited[x][y] = true;
			}		
		
	
	
}
