package server.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

public class TwitterBot {
	
	/**
	 * Creating TwitterBot instance
	 */
		private static TwitterBot myTwitterBot = new TwitterBot();

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
	private TwitterBot() {
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
	 * @return TwitterBot
	 */
	public static TwitterBot getInstance() {
		return myTwitterBot;
	}
	
	
	/**
	 * Connects to Twitter and tries to update status. 
	 * @param botStatus
	 * @throws TwitterException 
	 */
	public void updateMyStatus(String botStatus) throws TwitterException {
		
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		AccessToken myAccessToken = new AccessToken(accessToken, accessSecret);
		twitter.setOAuthAccessToken(myAccessToken);
		System.err.println("Connected with Twitter through OAuth.");
		
		// char limit check
		char botStatusChars[] = new char[botStatus.length()];
		botStatus.getChars(0, botStatus.length() -1, botStatusChars, 0);
		
		if (botStatusChars.length <= 140) {
			
			try {
				
				twitter.updateStatus(botStatus);
				System.err.println("Successfully updated status");
				
			} catch (TwitterException te) {
				System.err.println(te);
				// te.printStackTrace();
				System.err.println("Did no update status!!");
			}
		
		}

	}	
	
	
	/////////////////////
	////////////////////
	/////TESTING///////
	//////////////////
	/////////////////
	
	
	/**
	 * Used for Testing at the moment!
	 * @param args
	 * @throws TwitterException
	 */ /*
	public static void main(String[] args) throws TwitterException {
		
		System.out.println("Am I here");
		String testShort = "Hmm...Feeling devious today, muhahar.";
		String testLong = "This is a very long string that I shouldn't be talking about but I am trying to do so anyway. We'll see if this gets through to you guys, because if it would, that would be amazingly terribly tremendously fabulously WRONG. You heard that? Hmm?? WRONG I SAY. Cool - peace out xx";
		TwitterBot myBot = new TwitterBot();
		myBot.updateMyStatus(testShort);
		myBot.updateMyStatus(testLong);
		
		
		} */
	
	
	
}
