import lejos.nxt.Motor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;

/** 
 * The Control class. Handles the actual driving and movment of the bot, once
 * BotCommunication has processed the commands.
 * @author shearn89
 */
public class Controller{
	TachoPilot pilot;
	public int maxSpeed=30; // 20 for friendlies
	public boolean ballCarrier = false;
	/**
	 * Constructor method: takes the Pilot object from the main program.
	 * @param pilot
	 */
	public Controller(Pilot p){
		pilot = (TachoPilot) p;
		pilot.setMoveSpeed(maxSpeed);
		pilot.setTurnSpeed(35); // 45 has been working fine.
		pilot.regulateSpeed(true);
		Motor.A.smoothAcceleration(true);
		Motor.C.smoothAcceleration(true);
	}
	/**
	 * Makes the robot reverse until it receives a stop() command.
	 */
	public void reverse(){
		pilot.backward();
	}
	/**
	 * Makes the robot go until it receives a stop() command.
	 */
	public void go(){
		pilot.forward();
	}
	/**
	 * Floats the motors, allowing the robot to drift.
	 */
	public void floatWheels(){
		Motor.A.flt();
		Motor.C.flt();
	}
	/**
	 * Makes the robot stop.
	 */
	public void stop(){
		pilot.stop();
	}
	/**
	 * The robot will rotate deg degrees.
	 * @param deg The degree to rotate. +ve is right, -ve left.
	 */
	public void rotate(int deg){
		pilot.rotate(-deg);
	}
	/**
	 * Moves the robot dist distance. Only used by the Sensors class
	 * @param dist the distance to move.
	 */
	public void travel(int dist){
		pilot.travel(dist);
	}
	/**
	 * Sets the motor speed.
	 * @param speed
	 */
	public void setSpeed(int speed){
		pilot.setMoveSpeed(speed);
	}
	/**
	 * Kicks the ball.
	 */
	public void kick(){
		Motor.B.setSpeed(900);
		Motor.B.resetTachoCount();
		Motor.B.rotateTo(60);
		Motor.B.rotateTo(0);
	}
}
