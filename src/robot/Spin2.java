import lejos.nxt.Motor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;

public class Spin2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pilot pilot = new TachoPilot(6, 15, Motor.A, Motor.C,true);
		Controller control = new Controller(pilot);
		
		control.rotate(360);
		control.rotate(-360);
		control.rotate(360);
		control.rotate(-360);
	}

}
