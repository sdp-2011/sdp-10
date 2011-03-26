import lejos.nxt.Motor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;

public class RunBasic {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pilot pilot = new TachoPilot(8, 16, Motor.A, Motor.C,true);
		Controller control = new Controller(pilot);
		Sensors sensor = new Sensors(control);
		
		sensor.start();
		
		while(true){	
			control.go();
			try {
				Thread.sleep(1000);
			} catch (Exception e){
				// 
			}
			control.rotate(90);
		}
	}

}
