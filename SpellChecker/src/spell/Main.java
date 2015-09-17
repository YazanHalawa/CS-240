package spell;

import java.io.IOException;

import spell.ISpellCorrector.NoSimilarWordFoundException;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws NoSimilarWordFoundException, IOException {
		
		String dictionaryFileName = args[0];
		String inputWord = args[1];
		
		/**
		 * Create an instance of your corrector here
		 */
		SpellCorrector myCorrector = new SpellCorrector();
		SpellCorrector mytempCorrector = new SpellCorrector();
		mytempCorrector.useDictionary(dictionaryFileName);
		myCorrector.useDictionary(dictionaryFileName);
		System.out.println(mytempCorrector.myTrie.equals(myCorrector.myTrie));
		String suggestion = myCorrector.suggestSimilarWord(inputWord);
		
		System.out.println("Suggestion is: " + suggestion);
	}

}
