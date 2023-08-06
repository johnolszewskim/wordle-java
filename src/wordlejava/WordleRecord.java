package wordlejava;

import java.util.*;

/**
 *
 * Class to effectively package and pass the relevant data of a single WordleGame. Could be used
 * for future implementation of sharing results of a game with other users or displaying
 * results of WordleGames whos instance is no longer available.
 *
 * @author johnmatthew
 *
 */
public class WordleRecord {

	private String wordle;
	private LinkedList<String> guesses;
	private boolean win;


	/**
	 *
	 * WordleRecord must be created after a specific WordleGame is finished
	 *
	 * @param wordle the worlde of the game
	 * @param guesses List of the guesses that were made
	 * @param win whether the game was won or lost as a result. Necessary because the number of
	 * 			guesses in a game is flexible
	 */
	public WordleRecord(String wordle, LinkedList<String> guesses, boolean win) {

		this.win = win;
		this.guesses = guesses;
		this.wordle = wordle;
	}

	/**
	 *
	 * Returns a String including all information known about the WordleGame.
	 *
	 */
	public String toString() {

		return wordle + "\tWIN: " + this.win + "-> " + this.wordle + " " + this.guesses.toString();
	}

	/**
	 *
	 * Gets the number of the guess that won the WordleGame in the case that the game was won.
	 *
	 * @return int of the number of guess. 0 if the game was not won.
	 */
	public int getWinIndex() {

		if(this.win) {
			return this.guesses.size();
		}

		return 0;
	}
}
