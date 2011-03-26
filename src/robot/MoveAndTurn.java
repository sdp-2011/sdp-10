import lejos.nxt.Motor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;


public class MoveAndTurn {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pilot pilot = new TachoPilot(6, 15, Motor.A, Motor.C,true);
		Controller control = new Controller(pilot);
		
		control.go();
		try {
			Thread.sleep(2000);
		} catch (Exception e) {}
		control.rotate(120);
		control.go();
		try {
			Thread.sleep(2000);
		} catch (Exception e) {}
		control.rotate(120);
	}

}
