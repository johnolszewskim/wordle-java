package wordlejava;

import java.util.*;

/**
 *
 * @author johnmatthew
 *
 * Representation of New York Times word game Wordle.
 * Class intended to be used in conjunction with annother
 * classes the holds an instance of this class. Does not 
 * include user interaction.
 *
 * The game is intended to be flexible based on the Wordle and 
 * the number of guess that are specified during instance creation. 
 *
 */
public class WordleGame {



	public static int STANDARD_GUESSES = 6;
	public static int STANDARD_LENGTH = 5;
	private char[][] grid;
	private String wordle;
	private int nextGuessIndex = 0;
	private int lastGuessIndex = -1;
	private boolean gameIsOver = false;

	/**
	 *
	 * Only constructor for WordleGame. Requires minimum amount of
	 * parameters required to play the game.
	 *
	 * @param wordle single word that will be unknown to the person playing
	 * @param numGuesses number of guesses the user will be allowed
	 */
	public WordleGame(String wordle, int numGuesses) {

		this.wordle = wordle.toLowerCase();
		initializeGrid(wordle.length(), numGuesses);
	}

	/**
	 *
	 * initializes the char[] that holds the user guesses
	 *
	 * @param wordleLength the number of letters in the World
	 * @param numGuesses the number of guesses the player will be allowed
	 */
	private void initializeGrid(int wordleLength, int numGuesses) {

		this.grid = new char[numGuesses][wordleLength];

		for(int i = 0; i < numGuesses; i++) {

			char[] guess = new char[wordleLength];
			grid[i] = guess;
		}
	}

	/**
	 *
	 * A single guess for the wordle. Boundary cases and invalid words
	 * are checked for then the word is placed in the grid.
	 *
	 * @param word the word the the player guesses
	 * @return true if the word was successfully placed in the grid.
	 * 			false if the word wasn't placed.
	 */
	public boolean guess(String word) {

		if(word == null) { return false; }
		if(word.length() != this.wordle.length()) { return false; }
		if(this.gameIsOver) {
			return false;
		}
		// TODO: if word isnt a valid word

		this.placeWord(word);
		this.nextGuessIndex++;
		this.lastGuessIndex++;

		if(this.nextGuessIndex == this.grid.length) {
			this.nextGuessIndex--;
			this.gameIsOver = true;
		}

		if(String.valueOf(this.grid[this.lastGuessIndex]).equals(this.wordle)) {

			this.gameIsOver = true;
		}

		return true;
	}

	/**
	 * Places the word in the correct index in the grid.
	 * Checks for boundary cases and invalid words should be done
	 * prior to calling the method.
	 *
	 * @param word
	 */
	private void placeWord(String word) {

		for(int i = 0; i < word.length(); i++) {

			this.grid[this.nextGuessIndex][i] = word.charAt(i);
		}
	}


	/**
	 *
	 * Prints the grid of sequential guesses top to bottom. Blanks
	 * represent unused guesses.
	 *
	 * For debugging, relevant instance variable values are printed.
	 */
	public void printGrid() {

//		System.out.println("Wordle: " + this.wordle); // for testing

		for(int i = 0; i < this.grid.length; i++) {
			for(int j = 0; j < this.grid[i].length; j++) {

				if(this.grid[i][j] == Character.MIN_VALUE) {
					System.out.print("_ ");
				} else {
					System.out.print(this.grid[i][j] + " ");
				}
			}
			System.out.println();
		}
//		System.out.println("LAST GUESS: " + this.lastGuessIndex); // for testing
//		System.out.println("NEXT GUESS: " + this.nextGuessIndex); // for testing
//		System.out.println("GAME IS OVER: " + this.gameIsOver); // for testing
//		System.out.println(); // for testing
	}

	/**
	 *
	 * Determines if the last guess won the game.
	 *
	 * @return true if the last guess was the Wordle.
	 */
	public boolean getWin() {

		return String.valueOf(this.grid[this.lastGuessIndex]).equals(this.wordle);
	}

	/**
	 *
	 * Determines the appropriate response for a single letter. Uses grid information
	 * to compare to the Wordle. Any letter in the grid can be checked, not 
	 * just the last guess.
	 *
	 * @param guess the guess index beginning with 0.
	 * @param letter the letter index beginning with 0.
	 * @return 1 if the letter is correct. 2 If the letter is in the wordle but
	 * 			is in the incorrect index. 0 if the letter is not in the wordle.
	 */
	public int getResult(int guess, int letter) {

		if(this.grid[guess][letter] == this.wordle.charAt(letter)) {
			return 1;
		}

		if(this.wordle.indexOf(this.grid[guess][letter]) != -1) {
			return 2;
		}

		return 0;
	}

	public char[] getGuess(int guess) {

		return this.grid[guess];
	}

	/**
	 *
	 * Gets the results of the last guess taken by the player. If the 
	 * game is over, gets the final guess.
	 *
	 * @return Array representing the results of the last guess. 
	 */
	public int[] getLastResults() {

		int[] results = new int[wordle.length()];

		for(int i = 0; i < this.wordle.length(); i++) {
			results[i] = this.getResult(this.lastGuessIndex, i);
		}


		return results;
	}

	/**
	 *
	 * Used to get the last guess placed in the grid. 
	 *
	 * @return char[] representation of the last guess.
	 */
	public char[] getLastGuess() {

		return this.grid[this.lastGuessIndex];
	}

	/**
	 *
	 * The number of letters in the wordle.
	 *
	 * @return int representing the number of letters in the wordle.
	 */
	public int getWordleLength() { return this.wordle.length(); }

	/**
	 *
	 * Used to check if the game is over.
	 *
	 * @return true if wordle has been guessed or the game is over
	 */
	public boolean gameIsOver() { return this.gameIsOver; }

	public int lastGuessIndex() {

		if(this.gameIsOver && !this.getWin()) {
			return 0;
		}

		return this.lastGuessIndex;

	}

	/**
	 * Returns the index of the next guess.
	 *
	 * @return index of the next guess. The last index if the last guess has
	 * been made
	 */
	public int nextGuessIndex() { return this.nextGuessIndex; }


	/**
	 *
	 * Checks for the validity of a word based on a game.
	 *
	 * @param guess word to be checked
	 * @param wordleLength the length of the wordle to be compared
	 * @return false if the word is not the same length, null or contains 
	 * 			anything but alphabetical characters
	 */
	public static boolean isValidWordleGuess(String guess, int wordleLength) {

		if(guess == null) { return false; }

		if(guess.length() != wordleLength) { return false; }

		if(!guess.matches("^[a-zA-Z]*$")) { return false; }

		return true;
	}

	public static boolean isValidWordleGuess(String guess, int wordleLength, LinkedList<String> library) {

		if(!library.contains(guess)) {
			return false;
		}

		return isValidWordleGuess(guess, wordleLength);
	}

	/**
	 *
	 * Returns a game with the standard number of guess based on the standard length
	 * picked randomly from a library. Restricted to the first 2309 indicies of a 
	 * library sorted by frequency to align with the NYT game.
	 *
	 * @param library word list from which to pick the wordle
	 * @return new standard game instance
	 */
	public static WordleGame getRandomStandardGame(List<String> library) {

		String wordle = library.get(new Random().nextInt(2309));

		return new WordleGame(wordle, STANDARD_GUESSES);
	}

	/**
	 *
	 * Returns the number of guesses in the Wordle game
	 *
	 * @return int of the number of guesses
	 */
	public int getNumGuesses() { return this.grid.length; }

	/**
	 *
	 * Gets the wordle
	 *
	 * @return String of the wordle
	 */
	public String wordle() { return this.wordle; }
}