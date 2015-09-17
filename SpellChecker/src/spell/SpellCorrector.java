/**
 * 
 */
package spell;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * @author Yazan Halawa
 *
 */
public class SpellCorrector implements ISpellCorrector {
	Trie myTrie;
	/**
	 * 
	 */
	public SpellCorrector() {
		myTrie = new Trie();
	}

	/* (non-Javadoc)
	 * @see spell.ISpellCorrector#useDictionary(java.lang.String)
	 */
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner myScanner = new Scanner (new 
				BufferedInputStream(new FileInputStream(dictionaryFileName)));
		while(myScanner.hasNext()){
			String newWord = myScanner.next();
			myTrie.add(newWord);
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

	/* (non-Javadoc)
	 * @see spell.ISpellCorrector#suggestSimilarWord(java.lang.String)
	 */
	@Override
	public String suggestSimilarWord(String inputWord)
			throws NoSimilarWordFoundException {
		ArrayList<String> correctWords = new ArrayList<String>();
		ArrayList<String> EditedWords = new ArrayList<String>();
		String inputWordLower = inputWord.toLowerCase();
		if (myTrie.find(inputWord)!=null){//if the word exists in the Trie
			return inputWordLower;
		}
		//Distance 1
		boolean isFirstTimeThrough = true;
		//Deletion Distance
		deletionDistance(correctWords, EditedWords, inputWordLower, isFirstTimeThrough);
		//Transposition Distance
		transpositionDistance(correctWords, EditedWords, inputWordLower, isFirstTimeThrough);
		//Alteration Distance
		alterationDistance(correctWords, EditedWords, inputWordLower, isFirstTimeThrough);
		//Insertion Distance
		insertionDistance(correctWords, EditedWords, inputWordLower, isFirstTimeThrough);

		if (correctWords.isEmpty()){
			//Distance 2
			isFirstTimeThrough = false;
			for (int i = 0; i < EditedWords.size(); i++){
				//Deletion Distance
				deletionDistance(correctWords, EditedWords, EditedWords.get(i), isFirstTimeThrough);
				//Transposition Distance
				transpositionDistance(correctWords, EditedWords, EditedWords.get(i), isFirstTimeThrough);
				//Alteration Distance
				alterationDistance(correctWords, EditedWords, EditedWords.get(i), isFirstTimeThrough);
				//Insertion Distance
				insertionDistance(correctWords, EditedWords, EditedWords.get(i), isFirstTimeThrough);
			}
			if (correctWords.isEmpty())
				throw new NoSimilarWordFoundException();
		}
		//Sort CorrectWords by count
		Collections.sort(correctWords, new MyCountComp());
		ArrayList<String> correctAlphabitizedWords = new ArrayList<String>();
		//Add words equal in count to a new array
		for (int i = 0; i < correctWords.size(); i++){
			Node tempFirst = (Node)myTrie.find(correctWords.get(0));
			Node tempSecond = (Node)myTrie.find(correctWords.get(i));
			if (tempFirst.count == tempSecond.count){
				correctAlphabitizedWords.add(correctWords.get(i));
			}
		}
		//Sort CorrectWords alphabetically
		Collections.sort(correctAlphabitizedWords);
		return correctAlphabitizedWords.get(0);
	}

	class MyCountComp implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
			Node firstNode = (Node)myTrie.find(o1);
			Node secondNode = (Node)myTrie.find(o2);
			if (firstNode.count < secondNode.count){
				return 1;
			} else{
				return -1;
			}
		}

	}
	/**
	 * @param correctWords
	 * @param EditedWords
	 * @param inputWordLower
	 */
	private void insertionDistance(ArrayList<String> correctWords,
			ArrayList<String> EditedWords, String inputWordLower, boolean isFirstTime) {
		for (int i = 0; i < inputWordLower.length(); i++){
			char myChar = 'a';
			for (int j = 0; j < 26; j++){
				StringBuilder myBuilder = new StringBuilder();
				String word;
				if (i == 0){
					myBuilder.append(myChar);
					myBuilder.append(inputWordLower);
				}
				else {
					myBuilder.append(inputWordLower.substring(0, i));
					myBuilder.append(myChar);
					myBuilder.append(inputWordLower.substring(i, inputWordLower.length()));
				}
				word = myBuilder.toString();
				if (myTrie.find(word)!=null){
					correctWords.add(word);
				}
				else{
					if (isFirstTime)
						EditedWords.add(word);
				}
				myChar++;
			}
		}
		StringBuilder mySecondBuilder = new StringBuilder();
		for (int j = 0; j < 26; j++){
			char myChar = 'a';
			mySecondBuilder.append(inputWordLower);
			mySecondBuilder.append(myChar);
			String word = mySecondBuilder.toString();
			myChar++;
			if (myTrie.find(word)!=null){
				correctWords.add(word);
			}
			else{
				if (isFirstTime)
					EditedWords.add(word);
			}
		}
	}

	/**
	 * @param correctWords
	 * @param EditedWords
	 * @param inputWordLower
	 */
	private void alterationDistance(ArrayList<String> correctWords,
			ArrayList<String> EditedWords, String inputWordLower, boolean isFirstTime) {
		for (int i = 0; i < inputWordLower.length(); i++){
			char myChar = 'a';
			for (int j = 0; j < 26; j++){
				char [] tempArray = inputWordLower.toCharArray();
				tempArray[i] = myChar;
				String word = new String(tempArray);
				myChar++;
				if (myTrie.find(word)!=null){
					correctWords.add(word);
				}
				else{
					if (isFirstTime)
						EditedWords.add(word);
				}
			}
		}
	}

	/**
	 * @param correctWords
	 * @param EditedWords
	 * @param inputWordLower
	 */
	private void transpositionDistance(ArrayList<String> correctWords,
			ArrayList<String> EditedWords, String inputWordLower, boolean isFirstTime) {
		for (int i = 0; i < inputWordLower.length()-1; i++){
			char [] tempArray = inputWordLower.toCharArray();
			char temp = tempArray[i];
			tempArray[i] = tempArray[i+1];
			tempArray[i+1] = temp;
			String word = new String(tempArray);
			if (myTrie.find(word)!=null){
				correctWords.add(word);
			}
			else{
				if (isFirstTime)
					EditedWords.add(word);
			}
		}
	}

	/**
	 * @param correctWords
	 * @param EditedWords
	 * @param inputWordLower
	 */
	private void deletionDistance(ArrayList<String> correctWords,
			ArrayList<String> EditedWords, String inputWordLower, boolean isFirstTime) {
		for (int i = 0; i <= inputWordLower.length(); i++){
			String firstPart;
			String secondPart;
			String word;
			if (i == 0){
				if (inputWordLower.length() <= 1 )
					word = "";
				else
					word = inputWordLower.substring(1, inputWordLower.length());
			}
			else if (i == inputWordLower.length())
				word = inputWordLower.substring(0, i);
			else{
				firstPart = inputWordLower.substring(0, i);
				secondPart = inputWordLower.substring(i+1, inputWordLower.length());
				StringBuilder myBuilder = new StringBuilder();
				myBuilder.append(firstPart);
				myBuilder.append(secondPart);
				word = myBuilder.toString();
			}
			if (myTrie.find(word)!=null){
				correctWords.add(word);
				}
			else{
				if (isFirstTime)
					EditedWords.add(word);
			}
		}
	}

}
