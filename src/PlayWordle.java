import java.util.*;
import java.io.*;
import wordlejava.*;

/**
 *
 * Console-based game based that allows a user to play a single game of Wordle. Should
 * be run in an ANSI console to get color-coded results. Game finishes and prints the resuls
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

	private static Hashtable<String, String> resultsKeyboard;

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

	private static void printKeyboard() {

		System.out.print(resultsKeyboard.get("q") + "q " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("w") + "w " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("e") + "e " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("r") + "r " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("t") + "t " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("y") + "y " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("u") + "u " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("i") + "i " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("o") + "o " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("p") + "p " + ANSI_RESET + "\n ");
		System.out.print(resultsKeyboard.get("a") + "a " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("s") + "s " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("d") + "d " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("f") + "f " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("g") + "g " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("h") + "h " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("j") + "j " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("k") + "k " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("l") + "l " + ANSI_RESET + "\n  ");
		System.out.print(resultsKeyboard.get("z") + "z " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("x") + "x " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("c") + "c " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("v") + "v " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("b") + "b " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("n") + "n " + ANSI_RESET);
		System.out.print(resultsKeyboard.get("m") + "m " + ANSI_RESET);
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
				resultsKeyboard.replace(Character.toString(letter), ANSI_GREEN);
				break;
			case 2:
				System.out.print(ANSI_YELLOW + Character.toString(letter) + ANSI_RESET + " ");
				resultsKeyboard.replace(Character.toString(letter), ANSI_YELLOW);
				break;
			case 0:
				System.out.print(ANSI_RED + Character.toString(letter) + ANSI_RESET + " ");
				resultsKeyboard.replace(Character.toString(letter), ANSI_RED);
				break;
			default:
				System.out.print(ANSI_RESET + Character.toString(letter) + " ");
				resultsKeyboard.replace(Character.toString(letter), ANSI_RESET);
		}

		System.out.print(ANSI_RESET);
	}

	private static Hashtable<String,String> initResultsKeyboard() {

		Hashtable<String,String> keyboard = new Hashtable<>();
		keyboard.put("a", ANSI_RESET);
		keyboard.put("b", ANSI_RESET);
		keyboard.put("c", ANSI_RESET);
		keyboard.put("d", ANSI_RESET);
		keyboard.put("e", ANSI_RESET);
		keyboard.put("f", ANSI_RESET);
		keyboard.put("g", ANSI_RESET);
		keyboard.put("h", ANSI_RESET);
		keyboard.put("i", ANSI_RESET);
		keyboard.put("j", ANSI_RESET);
		keyboard.put("k", ANSI_RESET);
		keyboard.put("l", ANSI_RESET);
		keyboard.put("m", ANSI_RESET);
		keyboard.put("n", ANSI_RESET);
		keyboard.put("o", ANSI_RESET);
		keyboard.put("p", ANSI_RESET);
		keyboard.put("q", ANSI_RESET);
		keyboard.put("r", ANSI_RESET);
		keyboard.put("s", ANSI_RESET);
		keyboard.put("t", ANSI_RESET);
		keyboard.put("u", ANSI_RESET);
		keyboard.put("v", ANSI_RESET);
		keyboard.put("w", ANSI_RESET);
		keyboard.put("x", ANSI_RESET);
		keyboard.put("y", ANSI_RESET);
		keyboard.put("z", ANSI_RESET);

		return keyboard;
	}
}