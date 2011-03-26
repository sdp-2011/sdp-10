package server.twitter;

import java.util.ArrayList;
import java.util.Random;


public class AttemptPhrase extends Phrase {
	
	private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for attempting to score.
	 */
	public AttemptPhrase() {
		myPhrases.add("Feel my wrath...");
		myPhrases.add("Here it goes...!");
		myPhrases.add("Mark my words! When you least expect it, your uppance will come!");
		

		// myPhrases.add();
	}
	
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
}
