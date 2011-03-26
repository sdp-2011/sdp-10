package plan.path.pathfinding.heuristics;

import plan.path.pathfinding.Heuristics;
import plan.path.pathfinding.Robot;
import plan.path.pathfinding.WorldMap;

public class ManhattanHeuristic implements Heuristics{

	public float getCost(WorldMap map, Robot robot, int x, int y, int gx, int gy){

		float result = (float) (Math.abs(gx-x) + Math.abs(gy-y));
		return result;

	}
}
