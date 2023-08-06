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

	public static final String ANSI_RESET = "\033[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	
	
	
	public static void main(String []args) {
		
		System.out.print(ANSI_RESET);
		
		LinkedList<String> library = GuessManager.importWordsToLibraryFromWeb(5);
		LinkedList<String> libraryCopy = new LinkedList<>();
		libraryCopy.addAll(library);
		
		WordleGame randomStandardGame = WordleGame.getRandomStandardGame(library);
		WordleRecord result = play(randomStandardGame);
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
	private static WordleRecord play(WordleGame game) {
		
		LinkedList<String> guesses = new LinkedList<>();
		
		
		
		while (!game.gameIsOver()) {
			
			printResultsGrid(game);
			String nextGuess = getNextGuess(game);
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
	private static String getNextGuess(WordleGame game) {
		
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Guess word: ");
		String guess = scan.nextLine().toLowerCase();
		System.out.println();
		while(!WordleGame.isValidWordleGuess(guess, game.getWordleLength())) {
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
				break;
			case 2:
				System.out.print(ANSI_YELLOW + Character.toString(letter) + ANSI_RESET + " ");
				break;
			default:
				System.out.print(ANSI_RESET + Character.toString(letter) + " ");
		}
		
		System.out.print(ANSI_RESET);
	}

}
