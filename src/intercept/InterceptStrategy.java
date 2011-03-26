package intercept;
import java.awt.geom.Point2D;

import server.ServerCommunication;
import strategy.CurrentGlobalState;

public class InterceptStrategy implements Runnable {

	Point2D.Float[] stewiePosition;
	Point2D.Float ballPosition;
	Point2D.Float[] loisPosition;
	Point2D.Float goalPosition;
	Point2D.Float ourGoal;	
	Point2D.Float stewieCenter;
	Point2D.Float loisCenter;
	
	double stewieAngle;
	double loisAngle;
	boolean okaySig = false;
	
	
	ServerCommunication server;
	InterceptMovement m;
	
	public InterceptStrategy(ServerCommunication server){
		this.server = server;
	}
     
	public boolean decideStrategy() {
		
		
		CurrentGlobalState currentState = CurrentGlobalState.getInstance();
		
		Point2D.Float[] stewiePosition = currentState.getLocationStewie();
  	    Point2D.Float[] loisPosition = currentState.getLocationLois();
  	    Point2D.Float ballPosition = currentState.getLocationBall()[0];
	    Point2D.Float previousBallPosition = currentState.getLocationBall()[2];
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
	
		Point2D.Float stewieCenter = InterceptCompute.centerOfGravity(stewiePosition[0], stewiePosition[1] );
		Point2D.Float loisCenter = InterceptCompute.centerOfGravity(loisPosition[0], loisPosition[1] );
		double angleToTurn = InterceptCompute.angleToTurn(stewieCenter,ballPosition ,stewieAngle);
		// System.out.println("goal=" + goalPosition.x + "," + goalPosition.y);
		if (previousBallPosition == null){
			previousBallPosition = ballPosition;
		}
		this.m = new InterceptMovement(server, stewieAngle, ballPosition,previousBallPosition, goalPosition, stewieCenter, loisCenter, ourGoal);
  		if (paused){
			if (penalty == 'e'){
				server.send(2);
				System.out.println("robot is paused, sending stop and waiting...");
			}
			
/*			if (penalty == 'd'){
				m.defendPenalty();
			}
			
			if (penalty == 'a'){
				m.penaltyKick();
			}
*/			return true;
	    	}
  	   
		// System.out.println(">>> Ball: "+ballPosition);
		System.out.println("##### "+ballPosition.distance(previousBallPosition));
		if(m.canScore()){
			System.out.println(">>>I CAN SCORE");
			m.kick();
		}
		else if (m.canKick()){
			System.out.println(">>> I HAVE THE BALL");
			m.goTo(goalPosition);
		}
		else if (ballPosition.distance(previousBallPosition)> 2){
			System.out.println(">>> I GO TO THE PREDICTED BALL");
			server.send(70*100);
			Point2D.Float predictedBall = m.interceptBall();
			System.out.println(">>> Predicted: "+predictedBall+" Actual: "+ballPosition+ " Stewie: "+stewieCenter);
			m.travel(predictedBall);
		//	try{
		//	Thread.sleep(10);}
		//	catch (Exception e){
	//		}
		}	
		else{ 
			m.goTo(ballPosition);
			System.out.println(">>> I GO TO THE BALL");
		}

		return true;
}
	public void run(){
		while(true){
			decideStrategy();
		}
	}
	
}
	
	
