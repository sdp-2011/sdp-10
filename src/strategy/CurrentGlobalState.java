package strategy;

import java.awt.geom.Point2D;

public class CurrentGlobalState {

	/**
	 * Location data from vision that needs to get through to the observers
	 */
	private Point2D.Float[] stewieLocation = new Point2D.Float[2];
	private Point2D.Float[] loisLocation = new Point2D.Float[2];
	private Point2D.Float[] ballLocation = new Point2D.Float[5];
	private Point2D.Float targetGoalLocation = new Point2D.Float();
	private Point2D.Float ourGoalLocation = new Point2D.Float();
	private double StewieAngle = 0;
	private double LoisAngle = 0;
	private boolean paused = false;
	private char penalty = 'e';
	
	public boolean connected = false;
	public boolean quit = false;
	
	private static CurrentGlobalState myCurrentState = new CurrentGlobalState();
	
	/**
	 * Private constructor for singleton use
	 */
	private CurrentGlobalState() {
	} 
	
	/**
	 * Method to prevent cloning, for singleton use.
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	
	/**
	 * a getInstance method for LocationListener following the Singleton pattern.
	 * @return LocationListener
	 */
	public static CurrentGlobalState getInstance() {
		return myCurrentState;
	}
	
	
	/**
	 * Is execution paused for goals etc?
	 * @return
	 */
	public boolean isPaused(){
		return paused;
	}
	/**
	 * sets the pause value.
	 * @param input
	 */
	public void pause(boolean input){
		paused = input;
		System.out.println("input has been set to " + input + "in CGS");	

	}
	
	public void penalty(char input){
		penalty=input;
		System.out.println("penalty has been set to: " + input + " in CGS");	

	}
	
	public char getPenalty(){
		return penalty;
	}

	/**
	 * Function for getting Stewie's location
	 * @return
	 */
	public Point2D.Float[] getLocationStewie() {
		return stewieLocation;
	}
	
	/**
	 * Function for getting the opponents location (Lois)
	 * @return
	 */
	public Point2D.Float[] getLocationLois() {
		return loisLocation;
	}
	
	/**
	 * Function for getting the location of the ball
	 * @return
	 */
	public Point2D.Float[] getLocationBall() {
		return ballLocation;
	}
	
	/**
	 * Function for getting the location of our target goal
	 * @return
	 */
	public Point2D.Float getLocationTargetGoal() {
		return targetGoalLocation;
	}

	/**
	 * Function for getting the goal to defend
	 * @return
	 */
	public Point2D.Float getLocationOurGoal() {
		return ourGoalLocation;
	}
	/**
	 * Function for getting the angle of Stewie with the x axis 
	 * @return
	 */
	
	public double getStewieAngle(){
		// System.err.println("about to calculate stewie angle in CBS");
		double angle = Compute.angleInRadians(stewieLocation[0],stewieLocation[1]);
		return angle;
    }
	
	public double getLoisAngle(){
		// System.err.println("about to calculate lois angle in CBS");
		double angle = Compute.angleInRadians(loisLocation[0],loisLocation[1]);
		return angle;
    }
	public void updateBallLocation(Point2D.Float latest){
		this.ballLocation[4] = this.ballLocation[3];
		this.ballLocation[3] = this.ballLocation[2];
		this.ballLocation[2] = this.ballLocation[1];
		this.ballLocation[1] = this.ballLocation[0];
		this.ballLocation[0] = latest;
	}
	public void resetBallLocation(){
		for(int i = 0; i < 5; i++) {
			this.ballLocation[i] = null;
		}
	}
	/**
	 * setter function for the data handler that will update observers as soon as new data has been set. 
	 */
	public void setLocations(Point2D.Float[] locations) {
		//System.out.print("Set Locations has been called");
		this.stewieLocation[0] = locations[1];
		this.stewieLocation[1] = locations[2];
		this.loisLocation[0] = locations[3];
		this.loisLocation[1] = locations[4];
		if(locations[0].x!=-1){
			if (ballLocation[0] != null) {
				if(Math.abs(locations[0].x - ballLocation[0].x) < 160) {
					this.updateBallLocation(locations[0]);
				} else {
					System.out.println("> Ball x difference too great, assuming frame error");
				}
			} else {
				this.updateBallLocation(locations[0]);
			}
		}
		this.ourGoalLocation = locations[5];
		this.targetGoalLocation = locations[6];
	
	}
	
}
