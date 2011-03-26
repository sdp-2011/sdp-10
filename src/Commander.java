import java.io.IOException;
import java.net.InetAddress;

import server.*;
import strategy.*;

public class Commander {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		/**
		 * Start the bluetooth (or fake BT) server.
		 */
		//ServerCommunication server = new FakeServerCommunication();
		ServerCommunication server = new RealServerCommunication();
		System.out.println("I am alive!");
		InetAddress addr = InetAddress.getLocalHost();
		System.out.println("Host is: "+addr.getHostAddress());
		
		/**
		 * Start the vision socket receiver.
		 */
		System.out.println("Getting socket...");
		Thread t = new Thread (new SingleClientServer());
		t.start();
		
		/**
		 * Initialise our listener class.
		 */
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
		
		/**
		 * Create the gui frame for commands
		 */
		CommandReader console = new CommandReader();
		console.create();
		
		/** 
		 * Start strategy planning
		 */
		Strategy strategy = new Strategy(server);
		Output output = new Output();
		while(!currentState.quit){
			strategy.decideStrategy(output);
		}
		server.send(2);
		/**
		 * End section, interrupt threads
		 */
		server.close();
	}

}
