package server.twitter;
import java.util.ArrayList;
import java.util.Random;

public class Phrase {

	public ArrayList<String> myPhrases;
	

	/**
	 * function to add phrases to knowledge base
	 * @param phrase
	 */
	public void setPhrase(String phrase) {
		myPhrases.add(phrase);
	}
	
	/**
	 * function to get a random phrase from the knowledge base
	 * @return
	 */
	public String getPhrase() {
		Random randomIndex = new Random();
		int myIndex = randomIndex.nextInt(myPhrases.size());
		return myPhrases.get(myIndex);
	}
	
	/**
	 * function to get all phrases of the class (the knowledge base)
	 * @return
	 */
	public ArrayList<String> getAllPhrases() {
		return myPhrases;
	}
	
}



