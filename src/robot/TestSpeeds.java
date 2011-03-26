import lejos.nxt.Motor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;


public class TestSpeeds {

	public static int angleFix(int angle){
		return (int) (angle*(90*1.0/126));
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pilot pilot = new TachoPilot(6, 15, Motor.A, Motor.C,true);
		Controller control = new Controller(pilot);
		
		control.setSpeed(10);
		control.travel(100);
		control.rotate(angleFix(180));
		try { Thread.sleep(2000);}
		catch (Exception e){}
		
		control.setSpeed(20);
		control.travel(100);
		control.rotate(angleFix(180));
		try { Thread.sleep(2000);}
		catch (Exception e){}
		
		control.setSpeed(30);
		control.travel(100);
		control.rotate(angleFix(180));
		try { Thread.sleep(2000);}
		catch (Exception e){}
		
		control.setSpeed(40);
		control.travel(100);
		control.rotate(angleFix(180));
		try { Thread.sleep(2000);}
		catch (Exception e){}
		
		control.setSpeed(50);
		control.travel(100);
		control.rotate(angleFix(180));
	}

}
