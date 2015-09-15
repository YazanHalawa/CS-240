package client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import client.qualityChecker.ISpellCorrector.NoSimilarWordFoundException;
import client.qualityChecker.SpellCorrector;

/**
 * This class will test all the methods in the Spell Corrector
 * @author Yazan Halawa
 *
 */
public class QualityChecker {

	private SpellCorrector spellCorrector;

	/**
	 * This method will be called before each test to set up spellCorrector
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		spellCorrector = new SpellCorrector();
		String dictionaryFileName = "test/client/knownValues.txt";
		spellCorrector.useDictionary(dictionaryFileName);
	}

	/**
	 * This method tests the UseDictionary method in the SpellCorrector class
	 * @throws IOException
	 */
	@Test
	public void testUseDictionary() throws IOException {
		assertEquals(7, spellCorrector.getMyTrie().getWordCount());
	}

	/**
	 * This method tests whether a correct word is identified as such
	 * @throws NoSimilarWordFoundException
	 */
	@Test
	public void testWordInKnownValues() throws NoSimilarWordFoundException {
		ArrayList<String> similarWords = spellCorrector.suggestSimilarWords("Liza");
		assertFalse(spellCorrector.isNotInKnownValues());
		assertTrue(similarWords == null);
	}
	
	/**
	 * This method tests whether a word that is not in the known words is recognized as such
	 * @throws NoSimilarWordFoundException
	 */
	@Test(expected=NoSimilarWordFoundException.class)
	public void testWordNotInKnownValues() throws NoSimilarWordFoundException {
		ArrayList<String> similarWords = spellCorrector.suggestSimilarWords("Malcom");
		assertTrue(spellCorrector.isNotInKnownValues());
	}
	
	/**
	 * This method tests suggestions given for an invalid word
	 * @throws NoSimilarWordFoundException
	 */
	@Test
	public void testSuggestions() throws NoSimilarWordFoundException{
		ArrayList<String> similarWords = spellCorrector.suggestSimilarWords("Lize");
		
		//First test the size
		assertEquals(3, similarWords.size());
		
		//Now make sure the suggestions are correct
		ArrayList<String> correctSuggestions = new ArrayList<String>();
		correctSuggestions.add("liz");
		correctSuggestions.add("liza");
		correctSuggestions.add("lizzy");
		assertEquals(correctSuggestions, similarWords);
	}
	
	/**
	 * This method tests a word that generates no suggestions
	 * @throws NoSimilarWordFoundException
	 */
	@Test(expected=NoSimilarWordFoundException.class)
	public void testNoSuggestions() throws NoSimilarWordFoundException{
		ArrayList<String> similarWords = spellCorrector.suggestSimilarWords("Aslan");
		assertEquals(null, similarWords.size());
	}

}
