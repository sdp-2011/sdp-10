package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

/**
 * The BlueTooth server module. Can send command to the robot.
 * Create it, and it initializes connections. You then set the command
 * to send with setMessage(command), and send it with send().
 * @author shearn89
 *
 */
public class RealServerCommunication extends ServerCommunication {
	NXTConnector conn;
	DataInputStream dis;
	DataOutputStream dos;
	int m = 5;
	int m2 = 0;
	
	/**
	 * Constructor method, initializes connections.
	 */
	public RealServerCommunication(){
		conn = connect();
		dos = conn.getDataOut();
		dis = conn.getDataIn();
	}
	/**
	 * Connect method, initializes bluetooth connection to the NXT.
	 * @return The connection object.
	 */
	private NXTConnector connect(){
		NXTConnector conn = new NXTConnector();
		
		System.out.println("Connecting to Stewie...");
		boolean connected = conn.connectTo("btspp://group10");
			
		if (!connected) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
			return null;
		} else {
			System.out.println("Connection initialized. Good morning, Dave... \n");
			return conn;
		}
	}
	
	/**
	 * The public sender method. Sends commands.
	 * @param message The command to send, 1=go, 2=stop, 3=travel (with arg),
	 * 4=rotate (with arg), 5=kick.
	 */
	public boolean send(Integer message){
		try {
			// System.err.println("sending: "+message);
			boolean out = sendreceive(dis, dos, message);
			return out;
		} catch (IOException e){
			System.err.println("Sending failed, IOException: ");
			System.err.println(e.getMessage());
			close();
			return false;
			// System.exit(1);
		}
	}
	
	/**
	 * Sends and receives data, checking for whether the robot processed everything correctly.
	 * @param dataIn The DataInputStream from the bluetooth connection.
	 * @param dataOut The DataOutputStream from the bluetooth connection.
	 * @param message The integer string to send to the robot.
	 * @return A boolean value for okay or not: true if all went okay and the robot is waiting
	 * for another command, and false if it closed.
	 * @throws IOException if something went wrong.
	 */
	private boolean sendreceive(DataInputStream dataIn, DataOutputStream dataOut, Integer message) throws IOException{
		dataOut.writeInt(message);
		dataOut.flush();
		int code = dataIn.readInt();
		// System.err.println("receive code is: "+code);
		if (code == 0) {
			// robot is waiting for another command.
			return true;
		} else {
			// robot has closed connection.
			return false;
		}
	}
	
	/**
	 * Closes the data connection, required if the robot is still waiting for a command.
	 * @param dataIn The DataInputStream from the bluetooth connection.
	 * @param dataOut The DataOutputStream from the bluetooth connection.
	 */
	public void close(){
		try {
			dis.close();
			dos.writeInt(-1);
			dos.flush();
			dos.close();
			conn.close();
		} catch (IOException e){
			System.err.println(e);
		}
	}
}
