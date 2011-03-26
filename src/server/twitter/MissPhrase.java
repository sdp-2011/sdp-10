package server.twitter;
import java.util.ArrayList;
import java.util.Random;


public class MissPhrase extends Phrase {
	
	private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for missing the goal.
	 */
	public MissPhrase() {
		myPhrases.add("What the hell is this? I said a WIN only! Are you trying to give me a bloody heart attack?!?");
		myPhrases.add("AHHH! DAMN YOU ALL!");
		myPhrases.add("What the deuce?!");
		myPhrases.add("Blast!!");
		myPhrases.add("Damn you vile woman!!");
		myPhrases.add("I hate you now.");
		myPhrases.add("Bloody hell! I'm a woman!");
		

		// myPhrases.add();
	}
	
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
}
