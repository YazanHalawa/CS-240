package client.qualityChecker;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the interface for the Spell Corrector
 * @author Yazan Halawa
 *
 */
public interface ISpellCorrector {
	
	@SuppressWarnings("serial")
	public static class NoSimilarWordFoundException extends Exception {
	}
	
	/**
	 * Tells this <code>ISpellCorrector</code> to use the given file as its dictionary
	 * for generating suggestions. 
	 * @param dictionaryFileName File containing the words to be used
	 * @throws IOException If the file cannot be read
	 */
	public void useDictionary(String dictionaryFileName) throws IOException;
		
	/**
	 * Suggest a word from the dictionary that most closely matches
	 * <code>inputWord</code>
	 * @param inputWord
	 * @return The suggestion
	 * @throws NoSimilarWordFoundException If no similar word is in the dictionary
	 */

	public ArrayList<String> suggestSimilarWords(String inputWord)
			throws NoSimilarWordFoundException;

}
