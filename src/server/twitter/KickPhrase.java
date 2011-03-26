package server.twitter;
import java.util.ArrayList;
import java.util.Random;


public class KickPhrase extends Phrase {
	
	private ArrayList<String> myPhrases = new ArrayList<String>();
	
	/**
	 * Constructor that instantiates base phrases for this class.
	 * Phrases for missing the goal.
	 */
	public KickPhrase() {
		myPhrases.add("Victory shall yet be mine!");
		myPhrases.add("Stop mocking me!");
		myPhrases.add("Time to be bad!");
		myPhrases.add("Peek-a-boo! I see you!");
		myPhrases.add("I am going to kick... your... ass.");
		myPhrases.add("Did you see that? Kickass, I know..");
		myPhrases.add("The ball is no match for my kicks of excellence..");
	
	}
	
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
	
	
	
}
