package strategy;
import java.awt.geom.Point2D;

import plan.path.Finder;
import plan.path.pathfinding.Path;
import server.ServerCommunication;

public class Strategy {

	Point2D.Float[] stewiePosition;
	Point2D.Float ballPosition;
	Point2D.Float[] loisPosition;
	Point2D.Float goalPosition;
	Point2D.Float ourGoal;	
	Point2D.Float stewieCenter;
	Point2D.Float loisCenter;
	
	double stewieAngle;
	double loisAngle;
	boolean frameDrawn = false;
	boolean startGame = true;

	ServerCommunication server;
	Movement m;
	
	public Strategy(ServerCommunication server){
		this.server = server;
	}
     
	public boolean decideStrategy(Output output) {
		CurrentGlobalState currentState = CurrentGlobalState.getInstance();
		
		Point2D.Float[] stewiePosition = currentState.getLocationStewie();
  	    Point2D.Float[] loisPosition = currentState.getLocationLois();
  	    Point2D.Float ballPosition = currentState.getLocationBall()[0];
  	    Point2D.Float goalPosition = currentState.getLocationTargetGoal();
		Point2D.Float ourGoal = currentState.getLocationOurGoal();
  	    boolean paused = currentState.isPaused();
		char penalty = currentState.getPenalty();
			// System.err.println("getting angles...");
  	    if (stewiePosition == null || ballPosition == null) {
			return true;
		}
	double stewieAngle = currentState.getStewieAngle();
        double loisAngle = currentState.getLoisAngle();
	
		Point2D.Float stewieCenter = Compute.centerOfGravity(stewiePosition[0], stewiePosition[1] );
		Point2D.Float loisCenter = Compute.centerOfGravity(loisPosition[0], loisPosition[1] );
		double angleToTurn = Compute.angleToTurn(stewieCenter,ballPosition ,stewieAngle);
		// System.out.println("goal=" + goalPosition.x + "," + goalPosition.y);
		this.m = new Movement(server, stewieAngle, ballPosition, goalPosition, stewieCenter, loisCenter, ourGoal);

  		if (paused){
			if (penalty == 'e'){
				server.send(2);
				System.out.println("robot is paused, sending stop and waiting... [in strategy.java]");
//				okaySig = true; add this back in if we get robo errors
			}
			
			if (penalty == 'd'){
				m.defendPenalty();
			}
			
			if (penalty == 'a'){
				m.penaltyKick();
			}
			return true;
	    }
  	   
	/*	if (startGame) {
		//	server.send(9000);
		//	m.goTo(ballPosition);
			
			if (ballPosition.distance(stewieCenter) < 25){
				server.send(2);                                         
				server.send(Compute.robotAngle(70));
				server.send(5);
				startGame = false;
			}

		
	   	}else{ */
		
//======================  Path Finding Code Snippets ====================================	
		Finder finder = new Finder();
		Path q = new Path();

		// Fills the pitchmap with allowable grids. 
		finder.map.fillArea(0, 0, 64, 34, 0);	
		
		// Fill the corners of the map with water of the size of one tile
//			finder.map.fillArea(0, 0, 7, 7, 1); // for the upper right corner
//			finder.map.fillArea(0,27, 7, 7, 1); // for the lower right corner
//			finder.map.fillArea(57,0, 7, 7, 1); // upper left corner
//			finder.map.fillArea(57,27,7, 7, 1); // lower left corner
	
		finder.map.fillRobot(loisCenter);
		
	//	/* Keeps the coordinates of either the goalPosition or the ballPosition depending where we want to go */
		Point2D.Float whereToGo = new Point2D.Float(0,0);

		if (!m.canKick()){
			whereToGo = ballPosition;
		} else {	
			System.out.println("<<<<<<<<<<<<<<<<I Can Kick>>>>>>>>>>>>>>");	 
			whereToGo.x = Math.abs(goalPosition.x-10);
			whereToGo.y = goalPosition.y;
		}

		finder.map.setUnit((int)(stewieCenter.x/10), (int)(stewieCenter.y/10), 2);
		q = finder.answer((int)(stewieCenter.x/10), (int)(stewieCenter.y/10), (int)(whereToGo.x/10), (int)(whereToGo.y/10));
		

//=========================================================================================
		if (q != null){

			/** Convering the path to as set of simple Way Points */
			Point2D.Float[] wayPoints;	
			wayPoints = finder.simplePath(q);
		
			/** Outputting the path to the screen */
			output.fullPath(q, finder, ballPosition, wayPoints);
			output.simplePrint(wayPoints);

			if (m.canKick() && m.loisCloseToBall()) {
				server.send(-30+110000);
			} else if (m.canScore()){
				System.err.println(">>> I CAN SCORE/ i'm facing the right way.");
				m.kick();
			} else if (m.canKick()){
				m.goTo(goalPosition);
			} else if (wayPoints.length > 1) {
				System.out.println("distance:"+wayPoints[0].distance(wayPoints[1]));
				if (wayPoints[0].distance(wayPoints[1]) < 30 && wayPoints.length >= 3){
					System.out.println("first waypoint too close, going to second");
					System.out.println(">> At: "+wayPoints[0]+ ", going to: "+wayPoints[2]);
					m.goTo(ballPosition);
				} else {
					System.out.println(">> At: "+wayPoints[0]+ ", going to: "+wayPoints[1]);
					m.goTo(wayPoints[1]);
				} 
			}
			// } else {m.goTo(ballPosition);}
//		return true;	
		} else {
			m.goTo(ballPosition); // was kick()
		}
		return true;
	}
}
	
	
