package wordlejava;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 *
 * Manages the logic and the wordle possibilities of a given game. Contains the logic to reduce the 
 * possible guesses. Contains a library of words and, based on results, refines the library to 
 * only include possible wordles. Logic within the class allows for the ability to expand and 
 * increase the likelihood of guessing relevant words and words with the the highest strategic
 * impact on future guesses.
 *
 * @author johnmatthew
 *
 */
public class GuessManager {

	public static String ALPHABET_STR = "abcdefghijklmnopqrstuvwxyz";

	private List<String> library;
	private int wordleLength;
	private char[] garbage;
	private int garbageIndex;
	private char[] wordleKnown;
	private int lettersKnown;
	private char[] wordleIncludes;
	private int wordleIncludesIndex;
	int[] charCounts;


	/**
	 *
	 * Only constructor intended to begin the guessing from a full library of words of a 
	 * single length.
	 *
	 * @param wordleLength length of the wordle in the game that is being solved
	 * @param library the full library to begin with that will be reduced based on results
	 */
	public GuessManager(int wordleLength, List<String> library) {

//		System.out.println("GuessManager() library.size(): " + library.size()); // for testing

		this.wordleLength = wordleLength;
		this.library = library;
		this.garbage = new char[26];
		this.garbageIndex = 0;
		this.wordleKnown = new char[wordleLength];
		this.lettersKnown = 0;
		this.wordleIncludes = new char[wordleLength];
		this.wordleIncludesIndex = 0;
		this.charCounts = new int[26];


	}

	/**
	 *
	 * Method to create library from a local file of words. Called by importWordToBliraryFromWeb()
	 * in the case that an exception is thrown.
	 *
	 * @param wordleLength length of the wordle used to create library without unnecessarily importing 
	 * 			words of the incorrect length
	 * @return library of appropriately sized words
	 */
	public static LinkedList<String> importWordsToLibrary(int wordleLength) {

//		System.out.println("IMPORTING FROM FILE"); // for testing

		LinkedList<String> library = new LinkedList<>();

		try {
			String path = System.getProperty("user.dir");
			File file = new File(path + "/src/wordlejava/ANC-token-count.txt");
			Scanner sc = new Scanner(file);

			while(sc.hasNext()) {

				String word = sc.nextLine();
				word = word.substring(0, word.indexOf("\t"));
//				System.out.println(word); // for testing
				if(WordleGame.isValidWordleGuess(word, wordleLength)) {
					library.add(word);
				}
			}

		} catch (FileNotFoundException e) {

			library = null;

			e.printStackTrace();

		}

		return library;
	}

	/**
	 *
	 * Method that pulls in word frequency data from the American National Corpus. This library is
	 * used so the algorithm can use word frequency as a metric to determine guesses. If the 
	 * connection is bad or is unable to pull data, importWordsToLibary() is called in order to continue
	 * function by pulling data from a local file.
	 *
	 * Static in nature in order to ensure library consistency throughout all package objects.
	 *
	 * @param wordleLength length of the wordle used to create library without unnecessarily importing 
	 * 			words of the incorrect length
	 * @return library of appropriately sized words
	 */
	public static LinkedList<String> importWordsToLibraryFromWeb(int wordleLength) {

//		System.out.println("IMPORTING FROM WEB"); // for testing

		LinkedList<String> library = new LinkedList<>();

		try {

			URL frequencyData = new URL("https://www.anc.org/SecondRelease/data/ANC-token-count.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(frequencyData.openStream()));

			String word;
			while (((word = in.readLine()) != null) && (word.contains("\t"))) {

				word = word.substring(0, word.indexOf("\t"));
				if(WordleGame.isValidWordleGuess(word, wordleLength)) {
//	        		System.out.println(word); // for testing
					library.add(word);
				}
			}
			in.close();

		} catch(Exception e) {

			e.printStackTrace();

			library = importWordsToLibrary(wordleLength);
		}

//		System.out.println("IMPORTED LIBRARY FROM WEB SIZE: " + library.size()); // for testing
		return library;
	}

	/**
	 *
	 * Loops through all of the letter of a guess and calls the appropriate method based on the
	 * appropriate method to refine the library based on the results. 
	 *
	 * @param guess the guess that was made. Should correlate with results param
	 * @param results results correlated with the passed guess
	 */
	public void refineLibrary(char[] guess, int[] results) {

		for(int i = 0; i < guess.length; i++) {

			switch(results[i]) {

				case 0:
					this.refineByWrong(guess[i]);
					break;
				case 1:
					this.refineByCorrect(guess[i], i);
					break;

				case 2:
					this.refineByIndex(guess[i], i);
					break;
			}

		}
	}

	/**
	 *
	 * Refines possible words in library based on a correct placing of a letter.
	 * Removes all potential words that don't have the correct word at the
	 * specific index.
	 *
	 * @param c the correct letter
	 * @param index the correct index
	 */
	private void refineByCorrect(char c, int index) {

//		System.out.println(c + ": CORRECT"); // for testing

		for(int i = 0; i < this.library.size(); i++) {

			// if word doesn't have c at specific index
			if(this.library.get(i).charAt(index) != c) {
				this.library.remove(i);
				i--;

			}
		}
		this.wordleKnown[index] = c;
		this.lettersKnown++;
	}

	/**
	 * Refines possible words in the library based on an incorrect placing of
	 * a letter that in in the wordle at a different index. Removes words 
	 * that have the incorrectly placed letter at the same index.
	 *
	 * Only adds to wordleIncludes if not already in.
	 *
	 * @param c the correct letter
	 * @param index the incorrect index
	 */
	private void refineByIndex(char c, int index) {

//		System.out.println(c + ": WRONG INDEX"); // for testing

		for(int i = 0; i < this.library.size(); i++) {

			// if word has c in the same index
			if((this.library.get(i).indexOf(c) == index) ||
					(this.library.get(i).indexOf(c) == -1)) {
				this.library.remove(i);
				i--;

			}
		}

		// check if c is already in wordleIncludes
		if(Arrays.toString(this.wordleIncludes).indexOf(c) == -1) {

			this.wordleIncludes[this.wordleIncludesIndex] = c;
			this.wordleIncludesIndex++;
		}

	}

	/**
	 *
	 * Refines possible words in the library based on an incorrect letter.
	 * Removes all words that have the incorrect letter and adds that  letter
	 * to the garbage.
	 *
	 * @param c the incorrect letter
	 */
	private void refineByWrong(char c) {

//		System.out.println(c + ": WRONG"); // for testing

		if(Arrays.toString(this.garbage).indexOf(c) != -1) {
			return;
		}

		// if word includes c
		for(int i = 0; i < this.library.size(); i++) {

			if(this.library.get(i).indexOf(c) != -1) {
				this.library.remove(i);
				i--;
			}
		}
		this.garbage[this.garbageIndex] = c;
		this.garbageIndex++;
	}

	/**
	 *
	 * Used to return the next word to be guessed. This method is where a change
	 * in logic should take place to test the best algorithm.
	 *
	 * @return the next word to be guessed based on the included logic
	 */
	public String getNextWord() {

		LinkedList<String> copy = new LinkedList<>();
		copy.addAll(this.library);

		String highestImpactWord = this.getHighestImpactWord(0, copy);
//		System.out.println("highestImpactWord: " + highestImpactWord + "\n"); // for testing

		return highestImpactWord;
	}

	/**
	 *
	 * Counts the characters of the words found in the word list. destination array counts are 
	 * in alphabetical order. Sets all values to 0 then increments as it loops through every 
	 * letter of every word.
	 *
	 * Sets the count of words already in the known word to -1.
	 * POTENTIAL TO CHANGE TO REMEDY DOUBLE LETTERS. NEED TO FIND WHERE IS USED
	 *
	 * @param wordList the list of words whose letters are to be counted.
	 * @param destination the array to hold the counts.
	 */
	private void countChars(List<String> wordList, int[] destination) {

		Arrays.fill(destination, 0);
//		System.out.println("chountChars() destination before: " + Arrays.toString(destination)); // for testing

		for(int i = 0; i < wordList.size(); i++) {

			for(int j = 0; j < this.wordleLength; j++) {

				String currentWord = wordList.get(i);
				char currentChar = currentWord.charAt(j);
				int currentCharIndex = ALPHABET_STR.indexOf(currentChar);
				destination[currentCharIndex]++;

			}
		}

		for(int i = 0; i < this.wordleKnown.length; i++) {

			if(this.wordleKnown[i] != Character.MIN_VALUE) {

				destination[ALPHABET_STR.indexOf(this.wordleKnown[i])] = -1;
			}
		}

//		System.out.println("countChars() destination after: " + Arrays.toString(destination)); // for testing
	}

	/**
	 *
	 * Finds the nth greatest number in an array of word counts. 
	 *
	 * @param n the nth greatest number. 0 finds the maximum number
	 * @param charCounts the character counts to be compared
	 * @return the nth greatest character in the array of counts
	 */
	private char getNthMaxChar(int n, int[] charCounts) {

		List<Integer> countsSortedList = new ArrayList<>();

		// put counts in List and sort
		for (int i : charCounts)
		{
			countsSortedList.add(i);
		}
		countsSortedList.sort(Collections.reverseOrder());

//		System.out.println("getNthMaxChar() countsSortedList: " + countsSortedList); // for testing

		// find index of the nth greatest count
		int nthIndexCount = countsSortedList.get(n);
		int countAlphabeticalIndex = 0;

		// find the index of that count in the alphabetical array
		while(nthIndexCount != charCounts[countAlphabeticalIndex]) {

			countAlphabeticalIndex++;
		}

		// return the character at that alphabetical index
		return ALPHABET_STR.charAt(countAlphabeticalIndex);

	}

	/**
	 *
	 * Method that recursively finds the word that includes the most highest
	 * count letter words as possible. Finds the nthMax character in the remaining
	 * words. Eliminates words without that letter then recursively calls itself
	 * by increasing n and passing the refined library. If all the words are eliminated
	 * reverts to the list of previous possible words.
	 *
	 * The goal is to use this method to eliminate the greatest amount of possible words if 
	 * an incorrect letter is guessed.
	 *
	 * @param nthMax The number of highest count
	 * @param remaining the remaining possible words
	 * @return the single word that contains the highest amount of high count letters
	 */
	private String getHighestImpactWord(int nthMax, List<String> remaining) {

//		System.out.println("getHighestImpactWord() n = " + nthMax); // for testing
//		System.out.println("library.size(): " + library); // for testing

		// count letters in library
		int[] tempCounts = new int[26];
		this.countChars(library, tempCounts);

		// get nth max character
		char nthMaxChar = this.getNthMaxChar(nthMax, tempCounts);
//		System.out.println(nthMax + "thMaxChar: " + Character.toString(nthMaxChar)); // for testing

		// rules out letters that have already been guessed
		if(Arrays.toString(this.wordleKnown).indexOf(nthMaxChar) != -1) {
//			System.out.println("ALREADY KNOW ABOUT " + Character.toString(nthMaxChar) + "\n"); // for testing
			return getHighestImpactWord(nthMax+1, remaining);
		}

		if(Arrays.toString(this.wordleIncludes).indexOf(nthMaxChar) != -1) {
//			System.out.println("ALREADY KNOW ABOUT " + Character.toString(nthMaxChar) + "\n"); // for testing
			return getHighestImpactWord(nthMax+1, remaining);
		}

		//save possible words before elimination in the case there are zero left
		List<String> lastRemaining = new LinkedList<>(remaining);

		// remove words that don't contain nth most frequent letter
		for(int i = 0; i < remaining.size(); i++) {

			if(remaining.get(i).indexOf(nthMaxChar) == -1) {
				remaining.remove(i);
				i--;
			}
		}

//		System.out.println("this.library.size(): " + this.library.size()); // for testing
//		System.out.println("remaining.size(): " + remaining.size()); // for testing
//		System.out.println(remaining + "\n"); // for testing

		// if there is only one possibility
		if(remaining.size() == 1) {

//			System.out.println("ONE WORD REMAINING"); // for testing
			return remaining.get(0);
		}

		// if all the words were eliminated return the most linguistically frequent word
		// from the previous list
		if(remaining.size() == 0) {
//			System.out.println("NONE LEFT AT " + nthMax + "\n"); // for testing
			return lastRemaining.get(0);
		}

		// if the iteration is the most letters there can be pick the most linguistically
		// frequent one left
		if(nthMax == this.wordleLength-1) {
//			System.out.println("BASE CASE"); // for testing
			return remaining.get(0);
		}

		// recursive call
//		System.out.println("RECURSIVE CALL"); // for testing
		return this.getHighestImpactWord(nthMax+1, remaining);
	}

	/**
	 *
	 * Used to get the size of the local library
	 *
	 * @return int of the size of the current state of the library
	 */
	public int size() {

		return library.size();
	}

	/**
	 *
	 * Gets a specific index in the library
	 *
	 * @param index the int of the index to be fetched
	 * @return String at that index
	 */
	public String get(int index) {

		return this.library.get(index);
	}

	/**
	 *
	 * Removes String at a specific index in the library
	 *
	 * @param index int of index to be removed
	 */
	public void remove(int index) { this.library.remove(index); }

	/**
	 *
	 * Prints the size and words included in the library
	 *
	 */
	public String toString() { return this.size() + ": " + this.library.toString(); }

	/**
	 *
	 * returns the garbage of letters that have been guessed that are not in the wordle
	 *
	 * @return char array of incorrectly guessed letters
	 */
	public char[] garbage() { return this.garbage; }

	/**
	 *
	 * returns the letters that have been guessed that are in the wordle but in the 
	 * incorrect index
	 *
	 * @return char array of wrongly indexed letters
	 */
	public char[] wordleIncludes() { return this.wordleIncludes; }

	/**
	 *
	 * returns the elements of the wordle that are known at the instant of calling.
	 * letters are correctly indexed. Unknown letters are Character.MIN_VALUE
	 *
	 * @return char array of known elements of the wordle
	 */
	public char[] wordleKnown() { return this.wordleKnown; }
}
