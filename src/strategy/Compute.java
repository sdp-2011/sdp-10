package strategy;
import java.awt.geom.Point2D;

public class Compute {
	/**
	 * Compute the Euclidean distance between a and b.
	 * @param a The first point in 2d
	 * @param b The second point in 2d
	 * @return The distance as a double.
	 */
	public static double distance(Point2D.Float a ,Point2D.Float b ){
		return PixelToMM(Math.sqrt(Math.pow((a.x-b.x),2)+Math.pow((a.y-b.y),2)));
	}
	
	/**
	 * Finds the angle between 2 points and the x-axis.
	 * @param a first 2d point
	 * @param b second 2d point
	 * @return angle from a->b, in radians.
	 */
	public static double angleInRadians(Point2D.Float a, Point2D.Float b){
		return Math.atan2(b.y-a.y,b.x-a.x );
	}
	
	/**
	 * Same as angleInRadians(), but in degrees.
	 * @param a First 2d point
	 * @param b Second 2d point
	 * @return angle.
	 */
	public static double angleInDegrees(Point2D.Float a, Point2D.Float b){
		return Math.toDegrees(Math.atan2(b.y-a.y, b.x-a.x));
	}
	
	/**
	 * Compute the angle to turn in order to arrive at a certain point
	 * @param a first point
	 * @param b second point
	 * @param currentAngle The robot's current angle to the x-axis.
	 * @return The angle in degrees.
	 */
	public static double angleToTurn(Point2D.Float a, Point2D.Float b, double currentAngle){
		// System.err.println("Getting angle to turn: ");
		double angleToTurn = angleInRadians(a,b)- currentAngle; 
		if (angleToTurn < -Math.PI) {
			angleToTurn = 2*Math.PI + angleToTurn;
		}
		if (angleToTurn > Math.PI) {
			angleToTurn = angleToTurn - 2*Math.PI;
		}
		return radToDegree(angleToTurn);
	}
	
	/**
	 * Hack to convert the actual angle into the rotation required to turn the robot to that heading.
	 * @param angle The original angle in degrees.
	 * @return The fudged angle for the robot.
	 */
	public static int robotAngle(double angle){
		return (int) (angle*(90*1.0/126));
		// (440, 240) (417, 240)
		// (452, 232) (465, 214)
	}
	
	/**
	 * Transform distances from pixels to mm.
	 * @param distanceInPixels The pixel distance
	 * @return the mm distance.
	 */
	public static double PixelToMM(double distanceInPixels){
		return distanceInPixels*(2438.4/640);
		}
	
	/**
	 * Transforms from radians to degree
	 * @param angleInRad the angle in radians
	 * @return the angle in degrees
	 */
	public static double radToDegree(double angleInRad){
	   return angleInRad*180/Math.PI ;	
	}
	
	/**
	 * Decide whether to turn left or right. May need work.
	 * @param angleToTurn The angle to turn in radians.
	 * @return 0 for right, 1 for left.
	 */
	public static int getRotationDirection(double angleToTurn ){
			if (Math.abs(angleToTurn) < Math.PI) {
				//turn right
				return 0;
			} else {
				//turn left
			    return 1;
			}
			
		}

	/**
	 * The robot is localized by 2 points. This method finds the center of gravity
	 * @param a First point of it's T
	 * @param b Second point of it's T
	 * @return the center of gravity as a float.
	 */
	public static Point2D.Float centerOfGravity(Point2D.Float a, Point2D.Float b){
		Point2D.Float center = new Point2D.Float();
		center.x = (a.x + b.x)/2;
		center.y = (a.y + b.y)/2;
		return center;
	}
}
