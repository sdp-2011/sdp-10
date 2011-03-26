package strategy;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * @author ale
 *
 */

public class SingleClientServer implements Runnable {
	private CurrentGlobalState currentState = CurrentGlobalState.getInstance() ;
	private PrintWriter out;
	private ServerSocket serverSock;
	private Socket clientSocket;
	
	public void run() {
		new SingleClientServer().go();
		
	}

	private void go() {
		try {
			System.out.println("connecting...");
			serverSock = new ServerSocket(4560);
			clientSocket = serverSock.accept();
			System.out.println("got a connection");
			currentState.connected = true;
			currentState.pause(false);
			out = new PrintWriter(clientSocket.getOutputStream());
			InputStreamReader in = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader reader = new BufferedReader(in); 
			ComputeAndAcknowledge(reader);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	private void ComputeAndAcknowledge(BufferedReader _reader){
		String message;
		try{
			while ((message = _reader.readLine()) != null && !currentState.quit){
			//	System.out.print("\r"+message);
			//	out.println(1);
				String[] coordinates = message.split(";");
				if (coordinates[0].equals("pause")){
					System.out.println("paused, continuing.");
					currentState.pause(true);
					continue;
				}
				if (coordinates[0].equals("attack")){
					System.out.println("penalty kick");
					currentState.penalty('a');	
					continue;
				}
				if (coordinates[0].equals("defend")){
					System.out.println("defend");
					currentState.penalty('d');
					continue;
				}
				if (coordinates[0].equals("unpause")){
					currentState.pause(false);
					currentState.penalty('e');
					continue;
				}
				else {
					Point2D.Float[] points = new Point2D.Float[coordinates.length];
					for(int i = 0; i<coordinates.length;i++){
						String[] p = coordinates[i].split(",");
						Float x = new Float(p[0]);
						Float y = new Float(p[1]);
						points[i]= new Point2D.Float(x, y);						
					}

					currentState.setLocations(points);
				}
			}
			System.out.println(">> Connection to vision dropped...");
			currentState.connected = false;
			currentState.pause(true);
			clientSocket.close();
			serverSock.close();
			
			try {
				Thread.sleep(100);
			} catch (Exception e){
				e.printStackTrace();
			}
			if (!currentState.quit){
				go();
			} else{
				System.exit(0);
			}
		}
		catch(IOException ex){
			System.out.println("I/O Exception: "+ ex);
		}
		
	}
}
