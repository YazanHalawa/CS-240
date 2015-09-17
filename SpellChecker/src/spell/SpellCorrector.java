package spell;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector {
	private Trie myTrie;
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

	public SpellCorrector() {
		myTrie = new Trie();
	}

	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner myScanner = new Scanner(new BufferedInputStream(new FileInputStream(dictionaryFileName)));
		while (myScanner.hasNext()){
			myTrie.add(myScanner.next());
		}
		myScanner.close();
	}

	@Override
	public String suggestSimilarWord(String inputWord)
			throws NoSimilarWordFoundException {
		String inputWordLower = inputWord.toLowerCase();
		if (myTrie.find(inputWordLower) != null)
			return inputWord;
		ArrayList<String> correctWords = new ArrayList<String>();
		ArrayList<String> editedWords = new ArrayList<String>();
		boolean isFirstTimeThrough = true;
		deletionDistance(correctWords, editedWords, inputWordLower, isFirstTimeThrough);
		transpositionDistance(correctWords, editedWords, inputWordLower, isFirstTimeThrough);
		alterationDistance(correctWords, editedWords, inputWordLower, isFirstTimeThrough);
		insertionDistance(correctWords, editedWords, inputWordLower, isFirstTimeThrough);
		if (correctWords.size() == 0){
			for (String word: editedWords){
				deletionDistance(correctWords, editedWords, word, isFirstTimeThrough);
				transpositionDistance(correctWords, editedWords, word, isFirstTimeThrough);
				alterationDistance(correctWords, editedWords, word, isFirstTimeThrough);
				insertionDistance(correctWords, editedWords, word, isFirstTimeThrough);
			}
			if (correctWords.size() == 0)
				throw new NoSimilarWordFoundException();
		}
		Collections.sort(correctWords, new myComp());
		ArrayList <String> alphaWords = new ArrayList<String>();
		for (int i = 0; i < correctWords.size(); i++){
			Node first = (Node) myTrie.find(correctWords.get(0));
			Node second = (Node) myTrie.find(correctWords.get(i));
			if (first.getValue() == second.getValue())
				alphaWords.add(correctWords.get(i));
		}
		Collections.sort(alphaWords);
		return alphaWords.get(0);
	}

	class myComp implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
			Node first = (Node) myTrie.find(o1);
			Node second = (Node) myTrie.find(o2);
			if (first.getValue() < second.getValue())
				return 1;
			else
				return -1;
		}
		
	}
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
