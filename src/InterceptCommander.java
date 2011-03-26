import intercept.InterceptStrategy;

import java.io.IOException;

import server.RealServerCommunication;
import server.ServerCommunication;
import strategy.CurrentGlobalState;
import strategy.SingleClientServer;

public class InterceptCommander {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
	//	ServerCommunication server = new FakeServerCommunication();
		ServerCommunication server = new RealServerCommunication();
		System.out.println("I am alive!");
		
		
		Thread t = new Thread (new SingleClientServer());
		t.start();
		
		CurrentGlobalState currentState = CurrentGlobalState.getInstance();
		
		/**
		 * Waits for vision to connect before running commands.
		 */
		while (currentState.connected == false){
			try {
				Thread.sleep(100);
			} catch (Exception e){
			}
			currentState = CurrentGlobalState.getInstance();
		}
		
		CommandReader console = new CommandReader();
		console.create();	

		boolean okay = true;
		InterceptStrategy strategy = new InterceptStrategy(server);
		while(okay){
			try {
				Thread.sleep(500);
			} catch (Exception e){
			}
			strategy.decideStrategy();
		}
		
		
		/**
		 * End section, interrupt threads
		 */
		server.close();
		t.interrupt();
	}

}
