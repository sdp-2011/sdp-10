package server.twitter;

import java.util.ArrayList;
import java.util.Random;


public class ScorePhrase extends Phrase {
	
	private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for when Stewie scores!!
	 */
	public ScorePhrase() {
		myPhrases.add("AND HE SCOOOOOOOOOOOOOOOOOOORESSS!!");
		myPhrases.add("Yes, yes, again, again, oh dear God, please, once more!");
		myPhrases.add("Thank you. When the world is mine, your death shall be quick and painless.");

		// myPhrases.add();
	}
	
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
	
}
