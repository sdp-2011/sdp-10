package intercept;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import server.ServerCommunication;

public class InterceptMovement {
	
	Point2D.Float[] stewiePosition;
	Point2D.Float ballPosition;
	Point2D.Float[] loisPosition;
	
	Point2D.Float stewieCenter;
	Point2D.Float loisCenter;
	Point2D.Float goalPosition;
	Point2D.Float ourGoal;
	Point2D.Float previousBallPosition;
	
	int distanceConstant = 20;
	
	double stewieAngle;
	double loisAngle;
	
	ServerCommunication server;

	public  InterceptMovement(ServerCommunication server, double stewieAngle, Point2D.Float ballPosition,Point2D.Float previousBallPosition, Point2D.Float goalPosition,  Point2D.Float stewieCenter, Point2D.Float loisCenter,Point2D.Float ourGoal){
		this.server = server;
		this.ballPosition = ballPosition;
		this.previousBallPosition = previousBallPosition;
		this.stewieCenter = stewieCenter;
		this.loisCenter = loisCenter;
		this.goalPosition = goalPosition;
		this.stewieAngle = stewieAngle;
		this.ourGoal = ourGoal;
		
	}
	
	public boolean closeToBall() {
			if (stewieCenter.distance(ballPosition)< 50 ) {
				return true;
			}
		return false;
	}
	
	public boolean facingBall() {
			System.err.println("----\n\n---");
			int angle = (int) InterceptCompute.angleToTurn(stewieCenter,ballPosition,stewieAngle);
			System.err.println("The angle is " +angle);
			if (Math.abs(angle) < 30 ) {
				return true;
			}

		
		return false;
	
	}

	public boolean canKick(){
		if (this.closeToBall()&& this.facingBall()) {
			return true;
		}
		return false;
	}
	
	public void kick(){
		server.send(5);
		server.send(6);
	}
	
	public void travel(Point2D.Float point){
		int angleToTurn = (int) InterceptCompute.angleToTurn(stewieCenter, point, stewieAngle);
		int distance = (int)InterceptCompute.PixelToCM(stewieCenter.distance(point));
		System.out.println(">>distance to travel " + distance);
		if (angleToTurn < -10 || angleToTurn > 10){
//		server.send(2);
		server.send(InterceptCompute.robotAngle(angleToTurn));
		}
//		server.send(2);
		server.send(distance+110000);
	}

	public void goTo(Point2D.Float point){
		int angleToTurn = (int) InterceptCompute.angleToTurn(stewieCenter, point, stewieAngle);
		if (angleToTurn < -10 || angleToTurn > 10){
//		server.send(2);
		server.send(InterceptCompute.robotAngle(angleToTurn));
		}
//		server.send(2);
		server.send(1);
	}


	public void goToBackwards(Point2D.Float point){
		int angleToTurn = (int) InterceptCompute.angleToTurn(stewieCenter, point, stewieAngle);
		if (angleToTurn > 170 && angleToTurn < 180 ){
			server.send(2);
		//	server.send( // travel
			}
		server.send(2);
		server.send(InterceptCompute.robotAngle(angleToTurn));
		server.send(2);
		server.send(1);
	}
	public void setAccelerate(){
		server.send(3);
	}
	public void setNotAccelerate(){
		server.send(4);
	}
	public boolean inOwnHalf() {
		if (stewieCenter.x < goalPosition.x && stewieCenter.x > 320){
				return true;
			} else if (stewieCenter.x > goalPosition.x && stewieCenter.x < 320) {
				return true;
			}
		
		
		return false;
	}

	public boolean canScore() {
		if (canKick()){
			int angle = (int) InterceptCompute.angleToTurn(stewieCenter,goalPosition,stewieAngle);
			System.err.println("Angle to score " + angle);
			if (Math.abs(angle) < 30) {
				return true;
			}
		}
		return false;	
	}

	public Point2D.Float getPredictedBall(int constant, Point2D.Float ballPos, Point2D.Float prevBallPos) { 
		
		Point2D.Float predictedBall = new Point2D.Float(); 
		predictedBall.x = ballPos.x + constant*(ballPos.x - prevBallPos.x);
		predictedBall.y = ballPos.y + constant*(ballPos.y - prevBallPos.y);
		return predictedBall;
	}

	public Point2D.Float interceptBall(){

	/*	if (ballPosition.distance(previousBallPosition) < 20) {
			this.distanceConstant = 25;
		} else if (ballPosition.distance(previousBallPosition) < 40) {
			this.distanceConstant = 35;
		} else if (ballPosition.distance(previousBallPosition) < 60) {
			this.distanceConstant = 70;
		} else {
			this.distanceConstant = 90;
		}

	*/	Point2D.Float predictedBall = getPredictedBall(this.distanceConstant, ballPosition, previousBallPosition);

		System.out.println(">>>>\n>>> prevball: "+previousBallPosition+"\n>>> ball: "+ballPosition+"\n>>> stewie: "+stewieCenter+"\n>>>>");

		Rectangle2D.Float  pitch = new Rectangle2D.Float(0,0,640,340);
		//the angle of the line formed by 2 consecutives positions of the ball with the x axis
//		int ballAngle = (int) InterceptCompute.angleInRadians(ballPosition,previousBallPosition);
//		System.out.println("_______ballPosition = " + ballPosition);
//		System.out.println("_______previous ball Position = " + previousBallPosition); 

/*		float y1=ballPosition.y;
            	float y2=previousBallPosition.y;
            	float x1=ballPosition.x;
            	float x2=previousBallPosition.x;
            	float a = (y2-y1)/(x2-x1);
            	float b = (y1*x2-y2*x1)/(x2-x1);
*/
		System.err.println(">>>>>> BEGIN UBER DEBUG MODE <<<<<<\n\n\n");
		
		float a = previousBallPosition.x;
		System.err.println(a);
		float b = previousBallPosition.y;
		System.err.println(b);
		float a1 = ballPosition.x;
		System.err.println(a1);
		float b1 = ballPosition.y;
		System.err.println(b1);
		
		float a2 = stewieCenter.x;
		System.err.println(a2);
		float b2 = stewieCenter.y;
		System.err.println(b2);
		Point2D.Float intersection = new Point2D.Float();

		if ((a-a1) == 0){
			intersection.x = a;
			intersection.y = 320-ballPosition.y;
			return intersection;
		}
		float m = (b-b1)/(a-a1);
		System.err.println(m);
		
		float x = ( m*a1 - b1 + a2/m +b2 ) / ( m + 1/m );
		
		float y = m*(x-a1) + b1;

		intersection.x = x;
		intersection.y = y;
		
		System.err.println("hopeful intersection point = "+intersection);

		System.err.println("\n\n\n>>>>>> END UBER DEBUG MODE <<<<<<");

		if (intersection.y > 340){
			System.err.println(" BALL IS OFF THE PITCH OHNOES" );
			intersection.y = 320;
			intersection.x = (intersection.y - b1)/m + a1;
		} else if (intersection.y < 0){
			System.err.println(" BALL IS OFF THE PITCH OHNOES" );
			intersection.y = 20;
			intersection.x = (intersection.y - b1)/m + a1;
		} else if (intersection.x < 0){
			System.err.println(" BALL IS OFF THE PITCH OHNOES" );
			intersection.x = 20;
			intersection.y = m*(intersection.x-a1) + b1;
		} else if (intersection.x > 640){
			System.err.println(" BALL IS OFF THE PITCH OHNOES" );
			intersection.x = 620;
			intersection.y = m*(intersection.x-a1) + b1;
		}
		return intersection;
	/*	if (pitch.contains(intersection)){
			System.out.println(">>> predicted ball is in the pitch" );
			return intersection;
		} else {
			System.out.println(">> predicted ball is not in the pitch");
			return ballPosition;
		}
		
            //find the 4 intersactions with the pitch sides 
	        Point2D.Float intersect1 = new Point2D.Float();
	        Point2D.Float intersect2= new Point2D.Float();
	        Point2D.Float intersect3 = new Point2D.Float();
	        Point2D.Float intersect4 = new Point2D.Float();
	        // intersection with y = 0
			intersect1.y = 10;
			intersect1.x = (intersect1.y-b)/a;
			//intersection with y =340
			intersect2.y = 330;
			intersect2.x = (intersect2.y-b)/a;
			//intersection with x = 0
			intersect3.x = 10;
			intersect3.y = a*intersect3.x +b;
			//intersection with x = 640
			intersect4.x = 630;
		 	intersect4.y= a*intersect4.y +b;
		 	Point2D.Float[] intersections = { intersect1,intersect2,intersect3,intersect4};
		 	for (int i=0; i<intersections.length;i++){
		 		if (InterceptCompute.angleToTurn(ballPosition,intersections[i],ballAngle)== 0){
		 			return intersections[i];
		 		}
		 	}
		}
		return ballPosition; */
//		return intersection;
	}
}
