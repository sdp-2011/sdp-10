import lejos.nxt.Motor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;


public class MoveAndKick {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pilot pilot = new TachoPilot(6, 15, Motor.A, Motor.C,true);
		Controller control = new Controller(pilot);
		control.go();
		try {
			Thread.sleep(1000);
		} catch (Exception e){
			
		}
		control.floatWheels();
		control.kick();
	}

}
