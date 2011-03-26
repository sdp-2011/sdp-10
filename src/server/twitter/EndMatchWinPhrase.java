package server.twitter;
import java.util.ArrayList;
import java.util.Random;


public class EndMatchWinPhrase extends Phrase {
	
	private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for winning the match.
	 */
	public EndMatchWinPhrase() {
		myPhrases.add("Victory is mine!");
		myPhrases.add("I'm a winner. You're a loser. That simple.");
		myPhrases.add("I made you eat your parents! (wait...am I channeling some #southpark there?)");
		myPhrases.add("Celebration cake? Only if you've got some cool hwip!");
		
		// myPhrases.add();
	}
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
	
}
