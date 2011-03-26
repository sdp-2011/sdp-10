package server.twitter;
import java.util.ArrayList;
import java.util.Random;


public class BeginMatchPhrase extends Phrase {
	
	private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for indicating the begin of a match.
	 */
	public BeginMatchPhrase() {
		myPhrases.add("No, no, actually it's Stewie, but...well, well, you can call me 'Cookie' if you like.");
		myPhrases.add("Well then, my goal becomes clear: The broccoli must die. Err, Robot.");
		myPhrases.add("u know, opponent, life's like a box of chocolates: u never know what ur going to get. ur life however is more like a box of active grenades!");
		myPhrases.add("Let's get this over with");
		myPhrases.add("Don't worry - we all know what the outcome is going to be already...");
		myPhrases.add("Let's beat em up!");
		myPhrases.add("I offer you one last chance for deliverance. Return my mind control device, or be destroyed.");
		
		// myPhrases.add();
	}
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
	
}
