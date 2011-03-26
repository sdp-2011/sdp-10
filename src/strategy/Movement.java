package strategy;
import java.awt.geom.Point2D;

import server.ServerCommunication;

/**
 * The Movement class for Stewie's code.
 * @author Diana Bicazan
 *
 */
public class Movement {
	
	Point2D.Float[] stewiePosition;
	Point2D.Float ballPosition;
	Point2D.Float[] loisPosition;
	
	Point2D.Float stewieCenter;
	Point2D.Float loisCenter;
	Point2D.Float goalPosition;
	Point2D.Float ourGoal;
	
	double stewieAngle;
	double loisAngle;
	
	ServerCommunication server;

	public Movement(ServerCommunication server, double stewieAngle, Point2D.Float ballPosition, Point2D.Float goalPosition,  Point2D.Float stewieCenter, Point2D.Float loisCenter,Point2D.Float ourGoal){
		this.server = server;
		this.ballPosition = ballPosition;
		this.stewieCenter = stewieCenter;
		this.loisCenter = loisCenter;
		this.goalPosition = goalPosition;
		this.stewieAngle = stewieAngle;
		this.ourGoal = ourGoal;
		
	}
	
	public boolean closeToBall() {
			if (stewieCenter.distance(ballPosition)<45 ) {
				return true;
			}
		return false;
	}
	
	public boolean loisCloseToBall() {
			if (loisCenter.distance(ballPosition)< 30 ) {
				return true;
			}
		return false;
	}

	public boolean facingBall() {
			System.err.println("----\n\n---");
			int angle = (int) Compute.angleToTurn(stewieCenter,ballPosition,stewieAngle);
			System.err.println("The angle is " +angle);
			if (Math.abs(angle) < 40 ) {
				return true;
			}

		
		return false;
	
	}
	public boolean loisFacingBall() {
			System.err.println("----\n\n---");
			int angle = (int) Compute.angleToTurn(loisCenter,ballPosition,loisAngle);
			System.err.println("The angle is " +angle);
			if (Math.abs(angle) < 20 ) {
				return true;
			}

		
		return false;
	
	}
	public boolean canKick(){
		// HERE - see below
		if (this.closeToBall()&& this.facingBall()) {
			server.send(3);
			return true;
		}
		server.send(4);
		return false;
	}

	public boolean canScore() {
			if (canKick()){
				/* added this plus if clause, so it definitely can't kick when it's facing its own goal
				* two angles can't be miscalculated this badly, can they?
				* STILL NEEDS TESTING!!!!!!!!!!
				* if it's not working, move it to canKick() where it says here but that might cause a few problems
				* in the rest of the strategy when canKick() is simply called to check if we can move
				* --Denes
				*/
				int angleToOurGoal = (int) Compute.angleToTurn(stewieCenter,ourGoal,stewieAngle);
				if (!(Math.abs(angleToOurGoal) < 60)) {
					int angle = (int) Compute.angleToTurn(stewieCenter,goalPosition,stewieAngle);
					System.err.println("Angle to score " + angle);
					if (Math.abs(angle) < 40) {
						return true;
					}
				}
			}
			return false;
		
	}

	public boolean loisCanKick(){
		if (loisCloseToBall() && loisFacingBall()) {
			return true;
		}
		return false;
	}
	
	
	public boolean loisCanScore() {
			if (loisCanKick()){
					int angle = (int) Compute.angleToTurn(loisCenter,ourGoal,loisAngle);
					if (Math.abs(angle) < 30) {
						return true;
					}
			}
			return false;
		
	}

	public void kick(){
		server.send(5);
		server.send(6);
	}
	
	public void goTo(Point2D.Float point){
		int angleToTurn = (int) Compute.angleToTurn(stewieCenter, point, stewieAngle);
		if (angleToTurn > 13 || angleToTurn < -13){
			server.send(Compute.robotAngle(angleToTurn));
			server.send(1);
		} else {
			server.send(1);
		}
	}
	public void setAccelerate(){
		server.send(3);
	}
	public void setNotAccelerate(){
		server.send(4);}
	
	public void penaltyKick(){
		server.send(2);
		if (loisCenter.x <= 320){
			if ((loisCenter.y-90)>=(250 -loisCenter.y)){
				server.send(10);
				server.send(5);
			}
			else {
				server.send(-10);
				server.send(5);
			}
		} else {
			if ((loisCenter.y-90)>=(250 -loisCenter.y)){
				server.send(-10);
				server.send(5);
			}
			else {
				server.send(10);
				server.send(5);
			}
		}
	}

	public void defendPenalty(){
		server.send(7);
	}
	
	public void defendPenalty2(){
		Point2D.Float ballproj = ballProjection(slope(stewiePosition[1],stewiePosition[0])); 
		int distanceToTravel=(int)( stewieCenter.y - ballproj.y);
		if(ballproj.y>100 && ballproj.y < 240 && loisCanScore() && distanceToTravel > 12 ){ 
			server.send(distanceToTravel + 110000);
			server.send(7);
			}		
	} 
	//returns the point where the ball will arrive in the goal
	public Point2D.Float ballProjection(float slope){
		//computes the intercept of the line on the y axis
		float b= ballPosition.y - slope * ballPosition.x;
		Point2D.Float projection = new Point2D.Float();
		projection.x = goalPosition.x;
		projection.y = slope*projection.x + b;
		return projection;
	
	}	 
	//return the slope of a line
	public Float slope(Point2D.Float a,Point2D.Float b){
		// the line formed by a and b is :y=mx+n 
		float x1 = a.x;
		float y1 = a.y;
		float x2 = b.x;
		float y2 = b.y;	 
		float m = (y2-y1)/(x2-x1);
		return m; 
	}		

	public boolean inOwnHalf() {
		if (stewieCenter.x < goalPosition.x && stewieCenter.x > 320){
				return true;
			} else if (stewieCenter.x > goalPosition.x && stewieCenter.x < 320) {
				return true;
			}
		
		
		return false;
	}

	//find the coordinates of a virtualBall on the line y=ax+b given by the ball and the goal position
	public Point2D.Float virtualBall(){
		Point2D.Float virtualPoint = new Point2D.Float();
		

		double o = Math.abs((double) goalPosition.y - ballPosition.y);
		double a = Math.abs((double) goalPosition.x - ballPosition.x);
		double angle = Math.atan2(o,a);
		float h = 50; // hypothe something
		
		// attack
		if (goalPosition.x == 0 ) {
		
			if (ballPosition.x <= 320 && ballPosition.y <170) {
				//Q1
	
				virtualPoint.x = ballPosition.x + (float) Math.cos(angle)*h;
				virtualPoint.y = ballPosition.y - (float) Math.sin(angle)*h;
			} else if (ballPosition.x <= 320 && ballPosition.y >= 170) {
				//Q2
				virtualPoint.x = ballPosition.x + (float) Math.cos(angle)*h;
				virtualPoint.y = ballPosition.y + (float) Math.sin(angle)*h;
			} else if (ballPosition.x > 320 && ballPosition.y >= 170) {
				//Q3
				virtualPoint.x = ballPosition.x + (float) Math.cos(angle)*h;
				virtualPoint.y = ballPosition.y; //+ (float) Math.sin(angle)*h;
			} else if (ballPosition.x > 320 && ballPosition.y < 170) {
				//Q4
				virtualPoint.x = ballPosition.x + (float) Math.cos(angle)*h;
				virtualPoint.y = ballPosition.y; //- (float) Math.sin(angle)*h;
			}
		} else {

			if (ballPosition.x <= 320 && ballPosition.y <170) {
				//Q1
	
				virtualPoint.x = ballPosition.x - (float) Math.cos(angle)*h;
				virtualPoint.y = ballPosition.y; //- (float) Math.sin(angle)*h;
			} else if (ballPosition.x <= 320 && ballPosition.y >= 170) {
				//Q2
				virtualPoint.x = ballPosition.x - (float) Math.cos(angle)*h;
				virtualPoint.y = ballPosition.y; //+ (float) Math.sin(angle)*h;
			} else if (ballPosition.x > 320 && ballPosition.y >= 170) {
				//Q3
				virtualPoint.x = ballPosition.x - (float) Math.cos(angle)*h;
				virtualPoint.y = ballPosition.y + (float) Math.sin(angle)*h;
			} else if (ballPosition.x > 320 && ballPosition.y < 170) {
				//Q4
				virtualPoint.x = ballPosition.x - (float) Math.cos(angle)*h;
				virtualPoint.y = ballPosition.y - (float) Math.sin(angle)*h;
			}		
		}
 
		// if we're in the middle of the pitch
		if (ballPosition.x >= 200 && ballPosition.x <= 440) {
			virtualPoint.x = ballPosition.x;
			virtualPoint.y = ballPosition.y;
		}

		/*
		if ((goalPosition.x-ballPosition.x) > 0){
                virtualPoint.x = ballPosition.x - 20;
		}
		else { virtualPoint.x = ballPosition.x + 20;}
		
		float y1=goalPosition.y;
		float y2=ballPosition.y;
		float x1=goalPosition.x;
		float x2=ballPosition.x;
		int a = (int)((y2-y1)/(x2-x1));
		int b = (int)((y1*x2-y2*x1)/(x2-x1));
		virtualPoint.y = a*virtualPoint.x + b;
		*/

		// check if vball out of bounds
		if (virtualPoint.x < 10 || virtualPoint.x>630) {
			virtualPoint.x = ballPosition.x;
		}
		else if (virtualPoint.y<10 || virtualPoint.y>330){
			virtualPoint.y = ballPosition.y;
			
		}


		return virtualPoint;
	}


	
	


}
