package server.twitter;

import java.util.ArrayList;
import java.util.Random;


public class EndMatchLosePhrase extends Phrase {
	
	private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for losing the match.
	 */
	public EndMatchLosePhrase() {
		myPhrases.add("Oh, try and stop them.");
		myPhrases.add("I will have your head..");
		myPhrases.add("Oh, well, a thousand pardons for disrupting your flatware sanitation ritual, but, you see, I'm in searing pain!");
		
		// myPhrases.add();
	}
	
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
	
	
}
