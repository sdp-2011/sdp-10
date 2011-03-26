package plan.path.pathfinding.heuristics;

import plan.path.pathfinding.Heuristics;
import plan.path.pathfinding.Robot;
import plan.path.pathfinding.WorldMap;


public class ClosestHeuristic implements Heuristics{

	public float getCost(WorldMap map, Robot robot, int x, int y, int gx, int gy) {
		float dx = gx - x;
		float dy = gy - y;
		
		float result = (float) (Math.sqrt((dx * dx) + (dy * dy )));
		
		return result;
	}
}
