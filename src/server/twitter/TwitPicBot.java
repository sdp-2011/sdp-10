package server.twitter;

import java.util.logging.Logger;
import java.io.File;
import twitter4j.TwitterException;
import twitter4j.media.*;
import twitter4j.conf.*;

public class TwitPicBot {

	/**
	 * Creating TwitPicBot instance
	 */
		private static TwitPicBot myTwitPicBot = new TwitPicBot();

	/**
	 * Initialising Logger
	 */
		private static Logger myLogger = Logger.getLogger(TwitPicBot.class.getName());


	/**
	 * This is not supposed to be hard coded into the program, but I'm sure you guys don't mind. 
	 * Just don't share ;).
	 */
		private final static String consumerKey = "";
		private final static String consumerSecret = "";
		private final static String accessToken = "";
		private final static String accessSecret = "";
	
	/**
	 * Private constructor for singleton use
	 */
	private TwitPicBot() {
		// "I wanna get away... I wanna flyyyyyy awaaaaayy. Yeah, yeah. "
	} 
	
	
	/**
	 * Method to prevent cloning, for singleton use.
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	
	/**
	 * a getInstance method for TwitterBot following the Singleton pattern.
	 * @return TwitPicBot
	 */
	public static TwitPicBot getInstance() {
		return myTwitPicBot;
	}
	
	/**
	 * Connects to Twitter and tries to upload picture. 
	 * @param pic
	 * @throws TwitterException 
	 */
	
	public String uploadPic(String piclocation, String message) throws TwitterException {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(consumerKey);
		cb.setOAuthConsumerSecret(consumerSecret);
		cb.setOAuthAccessToken(accessToken);
		cb.setOAuthAccessTokenSecret(accessSecret);
		
		myLogger.info("Connected with Twitter through OAuth.");

		ImageUpload imUp = new ImageUploaderFactory(cb.build()).getInstance();
		
		String url = null;
		
		try {
			File pic = new File(piclocation);
			url = imUp.upload(pic, message);
			myLogger.info("Successfully uploaded picture here: " + url);
			
		} catch (TwitterException te) {				
			te.printStackTrace();
			myLogger.warning("Did not upload picture!!");
		}
		
		return url;
		
	}
	
	/**
	 * Used for Testing at the moment!
	 * @param args
	 * @throws TwitterException
	 */ 
	/*public static void main(String[] args) throws TwitterException {
		
		System.out.println("Am I here");
		File p = new File("/afs/inf.ed.ac.uk/user/s08/s0808798/SDP/git/sdp10/sdp10/src/server/twitter/logo.jpg");
		
		String msg = "test2";
		TwitPicBot myBot = new TwitPicBot();
		myBot.uploadPic(p, msg);
		//myBot.uploadPic("logo.jpg", msg);
		
	}*/
	
}
