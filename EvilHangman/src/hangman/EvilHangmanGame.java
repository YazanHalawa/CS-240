/**
 * 
 */
package hangman;

import java.io.File;
import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.Set;

/**
 * @author Yazan
 *
 */
public class EvilHangmanGame implements IEvilHangmanGame {
	private Set<String> mySet;
	private ArrayList<Character> guessesMade;
	private int _guessesLeft;
	private Map<String, Set<String>> myMap;
	private String wordKey;
	private String blankString;
	/**
	 * @return the mySet
	 */
	public Set<String> getMySet() {
		return mySet;
	}

	/**
	 * @param mySet the mySet to set
	 */
	public void setMySet(Set<String> mySet) {
		this.mySet = mySet;
	}

	/**
	 * @return the guessesMade
	 */
	public ArrayList<Character> getGuessesMade() {
		return guessesMade;
	}

	/**
	 * @param guessesMade the guessesMade to set
	 */
	public void setGuessesMade(ArrayList<Character> guessesMade) {
		this.guessesMade = guessesMade;
	}

	/**
	 * @return the _guessesLeft
	 */
	public int get_guessesLeft() {
		return _guessesLeft;
	}

	/**
	 * @param _guessesLeft the _guessesLeft to set
	 */
	public void set_guessesLeft(int _guessesLeft) {
		this._guessesLeft = _guessesLeft;
	}

	/**
	 * @return the myMap
	 */
	public Map<String, Set<String>> getMyMap() {
		return myMap;
	}

	/**
	 * @param myMap the myMap to set
	 */
	public void setMyMap(Map<String, Set<String>> myMap) {
		this.myMap = myMap;
	}

	/**
	 * @return the wordKey
	 */
	public String getWordKey() {
		return wordKey;
	}

	/**
	 * @param wordKey the wordKey to set
	 */
	public void setWordKey(String guessedKey) {
		if (wordKey == "")
			wordKey = guessedKey;
		else{
			for (int i = 0; i < guessedKey.length(); i++){
				if (wordKey.charAt(i) == '-'){
					char[] tempArray = new char[guessedKey.length()];
					tempArray = wordKey.toCharArray();
					tempArray[i] = guessedKey.charAt(i);
					wordKey = new String(tempArray);
				}
			}
		}
	}

	/**
	 * @return the blankString
	 */
	public String getBlankString() {
		return blankString;
	}

	/**
	 * @param blankString the blankString to set
	 */
	public void setBlankString(String blankString) {
		this.blankString = blankString;
	}
	/**
	 * 
	 */
	public EvilHangmanGame() {

	}

	/* (non-Javadoc)
	 * @see hangman.IEvilHangmanGame#startGame(java.io.File, int)
	 */
	@Override
	public void startGame(File dictionary, int wordLength) {
		mySet = new HashSet<String>();
		guessesMade = new ArrayList<Character>();
		myMap = new HashMap<String, Set<String>>();
		StringBuilder myBuilder = new StringBuilder();
		for (int i = 0; i < wordLength; i++){
			myBuilder.append('-');
		}
		wordKey = myBuilder.toString();
		blankString = myBuilder.toString();
		if (dictionary.isFile()){
			if (dictionary.canRead()){
				try {
					Scanner myScanner = new Scanner(new BufferedInputStream(new FileInputStream(dictionary.getPath())));
					while(myScanner.hasNext()){
						String newWord = myScanner.next();
						if (newWord.length() == wordLength){
							mySet.add(newWord);
						}
					}
					myScanner.close();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
			else{
				System.out.println("Usage: Input file cannot be read. Try again\n");
			}
		}
		else{
			System.out.println("Usage: Input file is not a valid file. Try again\n");
		}


	}

	/* (non-Javadoc)
	 * @see hangman.IEvilHangmanGame#makeGuess(char)
	 */
	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		if (Character.isUpperCase(guess))
			guess += 32;
		if (guessesMade.contains(guess)){
			throw new GuessAlreadyMadeException();
		}
		else{
			guessesMade.add(guess);
			int length = 0;
			//partitioning the list
			partitionList(guess);
			if (myMap.size() == 1 && myMap.containsKey(blankString)){
				changeSet(myMap);
			}
			else{
				//Choosing the largest set
				Map<String, Set<String>> tempMap = new HashMap<String, Set<String>>();
				int maxLength = 0;
				for (String tempKey: myMap.keySet()){
					if (myMap.get(tempKey).size() > maxLength){
						tempMap.clear();
						tempMap.put(tempKey, myMap.get(tempKey));
						maxLength = myMap.get(tempKey).size();
					}
					if (myMap.get(tempKey).size() == maxLength){
						tempMap.put(tempKey, myMap.get(tempKey));
					}
				}
				if (tempMap.size() == 1){
					changeSet(tempMap);
				}
				else{
					Map<String, Set<String>> duplicatesMap = new HashMap<String, Set<String>>();
					//(first Priority):no appearances
					boolean hasOccurences = false;
					for (String tempKey: tempMap.keySet()){
						for (int i = 0; i < tempKey.length(); i++){
							if (tempKey.charAt(i)!= '-'){
								hasOccurences = true;
							}
						}
						if (!hasOccurences){
							duplicatesMap.put(tempKey, tempMap.get(tempKey));
						}
					}
					if (duplicatesMap.size() == 1){
						changeSet(duplicatesMap);
					}
					else{//(Second Priority): fewest letters
						int minOccurences = 100;
						for (String tempKey: tempMap.keySet()){
							int tempOccurences = 0;
							for (int i = 0; i < tempKey.length(); i++){
								if (tempKey.charAt(i)!= '-'){
									tempOccurences++;
								}
							}
							if (tempOccurences < minOccurences){
								minOccurences = tempOccurences;
								duplicatesMap.clear();
								duplicatesMap.put(tempKey, tempMap.get(tempKey));
							}
							if (tempOccurences == minOccurences){
								duplicatesMap.put(tempKey, tempMap.get(tempKey));
							}
						}
						Map<String, Set<String>> thirdMap = new HashMap<String, Set<String>>();
						if (duplicatesMap.size() != 1){//(third Priority): rightmost guessed letter
							int wordLength = 0;
							for (String tempKey: duplicatesMap.keySet()){
								wordLength = tempKey.length();
								break;
							}
							for (int i = wordLength-1; i>=0; i--){
								for (String tempKey: duplicatesMap.keySet()){
									if (tempKey.charAt(i) != '-'){
										thirdMap.put(tempKey, duplicatesMap.get(tempKey));
									}
								}
								if (thirdMap.size() == 1)
									break;
								else if (thirdMap.size() > 0){
									Map<String, Set<String>> tmp = new HashMap<String, Set<String>>();
									tmp.keySet().removeAll(thirdMap.keySet());
									duplicatesMap.putAll(tmp);
								}
							}
						}
						else{
							thirdMap = duplicatesMap;
						}
						//At this point our third map should hold one item
						changeSet(thirdMap);
					}
				}
			}
			return mySet;
		}
	}

	/**
	 * @param guess
	 */
	private void partitionList(char guess) {
		int length;
		for (String currentWord: mySet){
			length = currentWord.length();
			String lowerCaseWord = currentWord.toLowerCase();
			StringBuilder oldBuilder = new StringBuilder();
			oldBuilder.append(lowerCaseWord);
			StringBuilder newBuilder = new StringBuilder();
			for (int i = 0; i < length; i++){
				if (oldBuilder.charAt(i) != guess){
					newBuilder.append('-');
				}
				else
					newBuilder.append(guess);
			}
			if (myMap.containsKey(newBuilder.toString())){
				myMap.get(newBuilder.toString()).add(currentWord);
			}
			else{
				Set<String> tempSet = new HashSet<String>();
				tempSet.add(currentWord);
				myMap.put(newBuilder.toString(), tempSet);
			}
			oldBuilder.delete(0, oldBuilder.length());
			newBuilder.delete(0, newBuilder.length());
		}
	}
	/**
//	 * @param tempMap
//	 */
	private void changeSet(Map<String, Set<String>> tempMap) {
		String guessedKey = "";
		for (Map.Entry<String, Set<String>> myEntry: tempMap.entrySet()){
			mySet = myEntry.getValue();
			guessedKey = myEntry.getKey();
		}
		myMap.clear();
		setWordKey(guessedKey);
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Initiating the Game
		String dictionaryPath = "";
		int wordLength = 0;
		int guesses = 0;
		if (args.length == 3){
			try{
				dictionaryPath = args[0];
				wordLength = Integer.parseInt(args[1]);
				guesses = Integer.parseInt(args[2]);
			}
			catch(Exception e){
				System.out.println("Usage: java hangman.Main dictionary wordLength guesses");
			}
		}
		else{
			System.out.println("Usage: java hangman.Main dictionary wordLength guesses");
		}
		File dictionary = new File(dictionaryPath);
		Scanner myScanner = new Scanner(System.in);
		while (!dictionary.isFile() || !dictionary.canRead()){
			System.out.println("Invalid dictionary Input. Reenter dictionary Path");
			dictionaryPath = myScanner.next();
			dictionary = new File(dictionaryPath);
		}
		EvilHangmanGame myGame = new EvilHangmanGame();
		myGame.set_guessesLeft(guesses);
		myGame.startGame(dictionary, wordLength);
		//RUNNING THE GAME
		boolean isWon = false;
		while (myGame.get_guessesLeft()!=0){
			System.out.print("Number of Guesses Left: ");
			System.out.println(myGame.get_guessesLeft());
			System.out.print("Used Letters: ");
			for (char myChar: myGame.getGuessesMade()){
				System.out.print(myChar);
				System.out.print(" ");
			}
			System.out.println();
			System.out.print("Word: ");
			System.out.println(myGame.getWordKey());
			char guess = 0;
			try {
				guess = promptGuess(guess);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (!Character.isUpperCase(guess) && !Character.isLowerCase(guess)){
				System.out.println("Invalid input");
				try {
					guess = promptGuess(guess);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while (myGame.getGuessesMade().contains(guess)){
				System.out.println("You already used that letter");
				try {
					guess = promptGuess(guess);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while (!Character.isUpperCase(guess) && !Character.isLowerCase(guess)){
					System.out.println("Invalid input");
					try {
						guess = promptGuess(guess);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			Set<String> guessedWords = new HashSet<String>();
			try {
				guessedWords = myGame.makeGuess(guess);
			} catch (GuessAlreadyMadeException e) {
				e.printStackTrace();
			}
			if (guessedWords.size() == 1){
				String winningWord = null;
				for (String word: guessedWords){
					winningWord = word;
					break;
				}
				//check that he guessed all the letters
				boolean guessedAll = true;
				for (int i = 0; i < winningWord.length(); i++){
					if (!myGame.getGuessesMade().contains(winningWord.charAt(i)))
						guessedAll = false;
				}
				if (guessedAll){
					System.out.println("You Won!");
					System.out.println("The correct word was " + winningWord);
					isWon = true;
					break;
				}
			}
			int num = 0;
			for (int i = 0; i < myGame.getWordKey().length(); i++){
				if (myGame.getWordKey().charAt(i) == guess)
					num++;
			}
			if (num == 0){
				System.out.println("Sorry, there are no " + guess + "\'s");
				myGame.set_guessesLeft(myGame.get_guessesLeft()-1);
			}
			else{
				System.out.println("Yes, there exists " + num + " " + guess + "\'s");
			}	
		}
		if (!isWon){
			String winningWord = null;
			for (String word: myGame.getMySet()){
				winningWord = word;
				break;
			}
			System.out.println("You lost!");
			System.out.println("The winning word was " + winningWord);
		}
		myScanner.close();

	}
	/**
	 * @param guess
	 * @return
	 * @throws IOException 
	 */
	private static char promptGuess(char guess) throws IOException {
		System.out.print("Enter guess: ");
		BufferedReader myReader = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		input = myReader.readLine();
		if (input.length()!= 1){
			System.out.println("Invalid guess.");
			guess = promptGuess(guess);
		}
		guess = input.charAt(0);
		return guess;
	}

}

