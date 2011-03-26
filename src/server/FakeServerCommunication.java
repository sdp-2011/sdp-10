package server;



/**
 * Fake server for testing purposes.
 * @author shearn89
 *
 */
public class FakeServerCommunication extends ServerCommunication {
	public boolean send(Integer message){
		System.out.println(message);
		return true;
	}
	
	public void close(){
		System.out.println("Connection closed");
	}
}
