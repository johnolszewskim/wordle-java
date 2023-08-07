import java.util.*;
import java.io.*;
import wordlejava.*;

/**
 *
 * Console-based game based that allows a user to play a single game of Wordle. Should
 * be run in an ANSI console to get color-coded results. Game finishes and prints the results
 * and then prints the WordleSolver results for the user to compare.
 *
 * @author johnmatthew
 *
 */
public class PlayWordle {

	private static final String ANSI_RESET = "\033[0m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_RED = "\033[0;31m";

	private static Hashtable<Character, String> resultsKeyboard;
	private static String ALPHA_KEYBOARD = "qwertyuiopasdfghjklzxcvbnm";

	public static void main(String []args) {

		System.out.print(ANSI_RESET);
		resultsKeyboard = initResultsKeyboard();

		LinkedList<String> library = GuessManager.importWordsToLibraryFromWeb(5);
		LinkedList<String> libraryCopy = new LinkedList<>();
		libraryCopy.addAll(library);

		WordleGame randomStandardGame = WordleGame.getRandomStandardGame(library);
		WordleRecord result = play(randomStandardGame, libraryCopy);
		String wordle = randomStandardGame.wordle();

		System.out.println(result);
		WordleSolver ws = new WordleSolver(new WordleGame(wordle, WordleGame.STANDARD_GUESSES), libraryCopy);
		System.out.println("WordleSolver:\n" + ws.solve());
	}

	/**
	 *
	 * Plays the WordleGame and returns the WordleResult
	 *
	 * @param game the WorldGame instance to be played
	 * @return result when the game is over
	 */
	private static WordleRecord play(WordleGame game, LinkedList<String> library) {

		LinkedList<String> guesses = new LinkedList<>();

		while (!game.gameIsOver()) {

			printResultsGrid(game);
			printKeyboard();
			String nextGuess = getNextGuess(game, library);
			game.guess(nextGuess);
			guesses.add(nextGuess);
			System.out.print("\033[H\033[2J");
			System.out.flush();
		}

		printResultsGrid(game);

		return new WordleRecord(game.wordle(), guesses, game.getWin());
	}

	/**
	 *
	 * Gets the next guess from the use. Checks the validity of the word based on the game.
	 *
	 * @param game the game for which make the guess.
	 * @return the guess that the user inputs
	 */
	private static String getNextGuess(WordleGame game, LinkedList<String> library) {

		Scanner scan = new Scanner(System.in);

		System.out.print("Guess word: ");
		String guess = scan.nextLine().toLowerCase();
		System.out.println();
		while(!WordleGame.isValidWordleGuess(guess, game.getWordleLength(), library)) {

			System.out.print("Guess word: ");
			guess = scan.nextLine().toLowerCase();
			System.out.println();
		}

		return guess;
	}

	/**
	 *
	 * Prints the grid based on the results of the guess for user information.
	 * Green text means the letter is correct. Yellow text means the letter is
	 * in the word but in the wrong index. Standard text means the letter is
	 * not in the wordle.
	 *
	 * @param game the WorldGame whose grid is to be printed
	 */
	private static void printResultsGrid(WordleGame game) {
		System.out.println("    WORDLE");

		for(int i = 0; i < game.getNumGuesses(); i++) {

			char[] guess = game.getGuess(i);

			if(game.nextGuessIndex() == i) {
				System.out.print("> ");
			} else {
				System.out.print("  ");
			}

			for(int j = 0; j < game.getWordleLength(); j++) {

				if(guess[j] == Character.MIN_VALUE) {
					System.out.print("_ ");
				} else {
					printLetter(game.getResult(i, j), guess[j]);
				}
			}
			System.out.println(); // formatting
		}
		System.out.println(); // formatting
	}

	/**
	 * Prints a standard US English keyboard with colors relating to
	 * the result of a guessed character if they've been guessed.
	 */
	private static void printKeyboard() {

		for(int i = 0; i < 10; i++) {

			char iChar = ALPHA_KEYBOARD.charAt(i);
			System.out.print(resultsKeyboard.get(iChar) + Character.toString(iChar) + " " + ANSI_RESET);
		}

		System.out.print("\n ");

		for(int i = 10; i < 19; i++) {

			char iChar = ALPHA_KEYBOARD.charAt(i);
			System.out.print(resultsKeyboard.get(iChar) + Character.toString(iChar) + " " + ANSI_RESET);
		}

		System.out.print("\n  ");

		for(int i = 19; i < 26; i++) {

			char iChar = ALPHA_KEYBOARD.charAt(i);
			System.out.print(resultsKeyboard.get(iChar) + Character.toString(iChar) + " " + ANSI_RESET);
		}

		System.out.println();
	}

	/**
	 *
	 * Prints a letter in the grid with the correct color based on the related
	 * result.
	 *
	 * @param result determines the color to print the letter
	 * @param letter the letter to print
	 */
	private static void printLetter(int result, char letter) {

		switch(result) {
			case 1:
				System.out.print(ANSI_GREEN + Character.toString(letter) + ANSI_RESET + " ");
				resultsKeyboard.replace(letter, ANSI_GREEN);
				break;
			case 2:
				System.out.print(ANSI_YELLOW + Character.toString(letter) + ANSI_RESET + " ");
				resultsKeyboard.replace(letter, ANSI_YELLOW);
				break;
			case 0:
				System.out.print(ANSI_RED + Character.toString(letter) + ANSI_RESET + " ");
				resultsKeyboard.replace(letter, ANSI_RED);
				break;
			default:
				System.out.print(ANSI_RESET + Character.toString(letter) + " ");
				resultsKeyboard.replace(letter, ANSI_RESET);
		}

		System.out.print(ANSI_RESET);
	}

	/**
	 * Initializes the Hashtable for the keyboard that will keep track of each
	 * letter and the result if it guess. Called in the constructor.
	 *
	 * @return a hashtable with all letters of the alphabet paiared with
	 * 			"ANSI)_RESET" as a value.
	 */
	private static Hashtable<Character,String> initResultsKeyboard() {

		Hashtable<Character,String> keyboard = new Hashtable<>();

		for(int i = 0; i < GuessManager.ALPHABET_STR.length(); i++) {

			keyboard.put(GuessManager.ALPHABET_STR.charAt(i), ANSI_RESET);
		}

		return keyboard;
	}
}