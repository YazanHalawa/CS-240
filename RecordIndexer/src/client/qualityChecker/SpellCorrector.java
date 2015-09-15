
package client.qualityChecker;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This class contains all the methods for the Spell Corrector
 * @author Yazan Halawa
 *
 */
public class SpellCorrector implements ISpellCorrector {
	// Init the variables
	private Trie myTrie;
	private boolean notInKnownValues;

	/**
	 * The constructor for the Spell Corrector class
	 */
	public SpellCorrector() {
		myTrie = new Trie();
	}

	/* (non-Javadoc)
	 * @see spell.ISpellCorrector#useDictionary(java.lang.String)
	 */
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner myScanner;
		// If it is a local file
		if (!dictionaryFileName.substring(0, 4).equals("http")){
			myScanner = new Scanner(new BufferedInputStream(new FileInputStream(dictionaryFileName)));
		}
		else{// If it is not a local file
			URL url = new URL(dictionaryFileName);
			myScanner = new Scanner(url.openStream());
		}

		// Grab the text from the dictionary 
		StringBuilder tempWord = new StringBuilder();
		String allWords = null;
		while(myScanner.hasNext()){
			allWords = myScanner.next();
		}

		// Parse the dictionary
		if (allWords != null){
			for (int i = 0; i < allWords.length(); i++){
				if (allWords.charAt(i) != ','){
					tempWord.append(allWords.charAt(i));
				}
				else{
					myTrie.add(tempWord.toString().toLowerCase());
					tempWord.delete(0, tempWord.length());
				}
			}
			// Add the last word
			myTrie.add(tempWord.toString().toLowerCase());
		}
		myScanner.close();
	}

	/**
	 * @return the myTrie
	 */
	public Trie getMyTrie() {
		return myTrie;
	}

	/**
	 * @param myTrie the myTrie to set
	 */
	public void setMyTrie(Trie myTrie) {
		this.myTrie = myTrie;
	}

	@Override
	public ArrayList<String> suggestSimilarWords(String inputWord)
			throws NoSimilarWordFoundException {

		String inputWordLower = inputWord.toLowerCase();
		if (myTrie.find(inputWordLower) != null){// If the word exists in the trie
			notInKnownValues = false;
			return null;
		}
		else
			notInKnownValues = true;

		// Init variables
		ArrayList<String> correctWords = new ArrayList<String>();
		ArrayList<String> editedWords = new ArrayList<String>();
		boolean isFirstTimeThrough = true;

		// Create the list of distance one words
		deletionDistance(correctWords, editedWords, inputWordLower, isFirstTimeThrough);
		transpositionDistance(correctWords, editedWords, inputWordLower, isFirstTimeThrough);
		alterationDistance(correctWords, editedWords, inputWordLower, isFirstTimeThrough);
		insertionDistance(correctWords, editedWords, inputWordLower, isFirstTimeThrough);

		isFirstTimeThrough = false;
		// Create the list of distance two words
		for (String word: editedWords){
			deletionDistance(correctWords, editedWords, word, isFirstTimeThrough);
			transpositionDistance(correctWords, editedWords, word, isFirstTimeThrough);
			alterationDistance(correctWords, editedWords, word, isFirstTimeThrough);
			insertionDistance(correctWords, editedWords, word, isFirstTimeThrough);
		}
		if (correctWords.size() == 0)
			throw new NoSimilarWordFoundException();

		// Sort by closeness
		Collections.sort(correctWords, new myComp(myTrie));
		Set<String> noduplicates = new HashSet<String>();
		for (String word: correctWords){
			noduplicates.add(word);
		}
		correctWords = new ArrayList<String>();
		for (String word: noduplicates){
			correctWords.add(word);
		}

		// Sort alphabetically
		Collections.sort(correctWords);
		return correctWords;
	}

	/**
	 * @return the notInKnownValues
	 */
	public boolean isNotInKnownValues() {
		return notInKnownValues;
	}

	/**
	 * @param notInKnownValues the notInKnownValues to set
	 */
	public void setNotInKnownValues(boolean notInKnownValues) {
		this.notInKnownValues = notInKnownValues;
	}

	/**
	 * This method creates similar words by inserting a letter at each possible place
	 * @param correctWords
	 * @param editedWords
	 * @param inputWordLower
	 * @param isFirstTimeThrough
	 */
	private void insertionDistance(ArrayList<String> correctWords,
			ArrayList<String> editedWords, String inputWordLower,
			boolean isFirstTimeThrough) {

		for (int i = 0; i < inputWordLower.length(); i++){
			char myChar = 'a';
			for (int j = 0; j < 26; j++){
				StringBuilder myBuilder = new StringBuilder();
				if (i==0){
					myBuilder.append(myChar + inputWordLower);
				}
				else {
					myBuilder.append(inputWordLower.substring(0, i) + myChar + 
							inputWordLower.substring(i, inputWordLower.length()));
				}
				String word = myBuilder.toString();
				if (myTrie.find(word)!=null)
					correctWords.add(word);
				else{
					if (isFirstTimeThrough)
						editedWords.add(word);
				}
				myChar++;
			}
		}
		char myChar = 'a';
		for (int i = 0; i < 26; i++){
			StringBuilder myBuilder = new StringBuilder();
			myBuilder.append(inputWordLower + myChar);
			String word = myBuilder.toString();
			if (myTrie.find(word)!=null)
				correctWords.add(word);
			else{
				if (isFirstTimeThrough)
					editedWords.add(word);
			}
			myChar++;
		}

	}

	/**
	 * This method creates similar words by altering letters at each place
	 * @param correctWords
	 * @param editedWords
	 * @param inputWordLower
	 * @param isFirstTimeThrough
	 */
	private void alterationDistance(ArrayList<String> correctWords,
			ArrayList<String> editedWords, String inputWordLower,
			boolean isFirstTimeThrough) {
		for (int i = 0; i < inputWordLower.length(); i++){
			char myChar = 'a';
			for (int j = 0; j < 26; j++){
				char [] myChars = inputWordLower.toCharArray();
				myChars[i] = myChar;
				String word = new String(myChars);
				if (myTrie.find(word)!=null)
					correctWords.add(word);
				else{
					if (isFirstTimeThrough)
						editedWords.add(word);
				}
				myChar++;
			}
		}

	}

	/**
	 * This method creates similar words by transpositioning at each possible place
	 * @param correctWords
	 * @param editedWords
	 * @param inputWordLower
	 * @param isFirstTimeThrough
	 */
	private void transpositionDistance(ArrayList<String> correctWords,
			ArrayList<String> editedWords, String inputWordLower,
			boolean isFirstTimeThrough) {
		for (int i = 0; i < inputWordLower.length()-1; i++){
			char [] myChars = inputWordLower.toCharArray();
			char tmp = myChars[i];
			myChars[i] = myChars[i+1];
			myChars[i+1] = tmp;
			String word = new String(myChars);
			if (myTrie.find(word)!=null)
				correctWords.add(word);
			else{
				if (isFirstTimeThrough)
					editedWords.add(word);
			}
		}

	}

	/**
	 * This method creates similar words by deleting letters at each possible place
	 * @param correctWords
	 * @param editedWords
	 * @param inputWordLower
	 * @param isFirstTimeThrough
	 */
	private void deletionDistance(ArrayList<String> correctWords,
			ArrayList<String> editedWords, String inputWordLower,
			boolean isFirstTimeThrough) {
		for (int i = 0; i < inputWordLower.length(); i++){
			StringBuilder myBuilder = new StringBuilder();
			if (i == 0)
				myBuilder.append(inputWordLower.substring(1, inputWordLower.length()));
			else if ( i == inputWordLower.length()-1)
				myBuilder.append(inputWordLower.substring(0, i));
			else{
				myBuilder.append(inputWordLower.substring(0, i) + inputWordLower.substring(i+1, inputWordLower.length()));
			}
			String word = myBuilder.toString();
			if (myTrie.find(word)!=null)
				correctWords.add(word);
			else{
				if (isFirstTimeThrough)
					editedWords.add(word);
			}
		}

	}

}
