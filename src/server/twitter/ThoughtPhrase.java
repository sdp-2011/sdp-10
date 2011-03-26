package server.twitter;

import java.util.ArrayList;
import java.util.Random;

public class ThoughtPhrase extends Phrase {

private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for missing the goal.
	 */
	public ThoughtPhrase() {
		myPhrases.add("This will be evil! Check it: ");
		myPhrases.add("No one can defeat me now: ");
		myPhrases.add("I can't keep my thoughts to myself: ");
		myPhrases.add("Best plan for world domination: ");
		myPhrases.add("This will bring world domination: ");
		myPhrases.add("I can't think of anything else: ");
		myPhrases.add("The newest way to world domination: ");
	
	}
	
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
	
}
