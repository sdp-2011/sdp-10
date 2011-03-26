public class Defend extends Thread {
	Controller control;
	
	public Defend(Controller c) {
		this.control = c;
	}
	
	void loop(){
		for (int i=0; i<15; i++){
			control.travel(10);
			control.travel(-10);
		}
		control.stop();
	}
	
	public void run(){
		loop();
	}
}
