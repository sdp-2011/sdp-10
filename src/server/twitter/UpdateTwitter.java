package server.twitter;

import java.io.File;

import twitter4j.TwitterException;



// import twitter4j.TwitterException;

public class UpdateTwitter {

	private static UpdateTwitter myUpdateTwitter = new UpdateTwitter();
	
	/**
	 * Private constructor for singleton use
	 */
	private UpdateTwitter() {
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
	public static UpdateTwitter getInstance() {
		return myUpdateTwitter;
	}
	
	
	private TwitterBot myConnection = TwitterBot.getInstance();
	
	private TwitPicBot myPicBot = TwitPicBot.getInstance();

	/**
	 * Pass one of the following: move, score, attempt, beginMatch, winMatch, loseMatch
	 * and a phrase will be selected to update to Twitter
	 * @param event
	 * @throws TwitterException 
	 */
	public void stewieDid(SoccerEvent event){
		Phrase myPhrase = new Phrase();
		
		switch (event) {
			case move: 
				myPhrase = new MovePhrase();
				break;
			case score:
				myPhrase = new ScorePhrase();
				break;
			case attempt:
				myPhrase = new AttemptPhrase();
				break;
			case kick:
				myPhrase = new KickPhrase();
				break;
			case beginMatch:
				myPhrase = new BeginMatchPhrase();
				break;
			case winMatch:
				myPhrase = new EndMatchWinPhrase();
				break;
			case loseMatch:
				myPhrase = new EndMatchLosePhrase();
				break;
			
		}
		
		try {
			myConnection.updateMyStatus(myPhrase.getPhrase());
		} catch (TwitterException e) {
			System.err.println(e);
		}
				
	}
	
	public void stewieThought() {
		Phrase myPhrase = new ThoughtPhrase();
		
		String url = null;
		
		try {
			// if it's not working change the classpath for screenshot.
			String path = new File(".").getCanonicalPath();
			path += "/screenshot.jpg";
			url = myPicBot.uploadPic(path, "A plan");
			if (url != null) {
				String status = myPhrase.getPhrase()+" "+url;
				myConnection.updateMyStatus(status);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		
		
	}
	
	
	
	/////////////
	///////////			EXAMPLE!
	/////////
	
	/*
	public static void main(String[] args) {
		UpdateTwitter twitter = UpdateTwitter.getInstance();
		SoccerEvent event = SoccerEvent.move;
		twitter.stewieDid(event);
		
	} */
	
}
