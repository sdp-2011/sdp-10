package plan.path.pathfinding.heuristics;

import plan.path.pathfinding.Heuristics;
import plan.path.pathfinding.Robot;
import plan.path.pathfinding.WorldMap;

public class DiagonalHeuristic implements Heuristics {

	public float getCost(WorldMap map, Robot robot, int x, int y, int gx, int gy) {

		float h_diagonal = (float) (Math.min(Math.abs(gx - x), Math.abs(gy - y)) );
		float h_straight = (float) (( Math.abs(gx - x) +  Math.abs(gy - y) ));

		float result = h_diagonal * (h_straight - 2 * h_diagonal);

		return result;
	}
}
