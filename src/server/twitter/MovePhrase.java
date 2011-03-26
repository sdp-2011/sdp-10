package server.twitter;

import java.util.ArrayList;
import java.util.Random;


public class MovePhrase extends Phrase {
	
	private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for moving around.
	 */
	public MovePhrase() {
		myPhrases.add("I like to move it, move it!");
		myPhrases.add("Move, b*tch, get out the way, get out the way!");
		myPhrases.add("I just moved!");
		myPhrases.add("Movin', crusin', oh yeah...");
		// myPhrases.add();
	}
	
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
	
}
