package wordlejava;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * 
 * Class that handles the solving of a WordleGame. Only functionality is to solve a passed game
 * using the libray given. 
 * 
 * @author johnmatthew
 *
 */
public class WordleSolver {
	
	private WordleGame game;
	private GuessManager guessManager;

	/**
	 * 
	 * Single constructor using required parameters. 
	 * 
	 * @param game The WorldeGame that is to be solved. Random creation should be done during
	 * 			constructor call or prior to ensure the same library is used.
	 * @param library List of words that the GeussManager will use to find the wordle. Intent
	 * 			to be the same library of words as the list of words that was used to select
	 * 			the WordleGame in cases of random wordle selection and WorldeGame creation.
	 */
	public WordleSolver(WordleGame game, List<String> library) {
		
		this.game = game;
		this.guessManager = new GuessManager(this.game.getWordleLength(), library);
//		System.out.println("WordleSolver() library.size(): " + library.size()); // for testing

	}
	
	/**
	 * 
	 * Solves the WordleGame by continuing to make guesses until the game is over until the
	 * condition that there are no guesses remaining or the game has been won. Maintains
	 * record of the guesses made.
	 * 
	 * @return The WordleRecord summarizing the game that was solved and the result
	 */
	public WordleRecord solve() {
		
		LinkedList<String> guesses = new LinkedList<>();
		
		while (!this.game.gameIsOver()) {
			
			char[] previousGuess = this.makeNextGuess();
			guesses.add(new String(previousGuess));
			this.guessManager.refineLibrary(previousGuess, this.game.getLastResults());
			
//			System.out.println(Arrays.toString(this.guessManager.garbage())); // for testing
//			System.out.println(Arrays.toString(this.guessManager.wordleKnown()) + " " + Arrays.toString(this.guessManager.wordleIncludes())); // for testing
//			System.out.println("POSSIBLE WORDS: " + this.guessManager.size()); // for testing
//			System.out.println(this.guessManager.toString() + "\n"); // for testing
//			new Scanner(System.in).nextLine(); // for testing
		}
//		System.out.println("gameIsOver"); // for testing
		return new WordleRecord(this.game.wordle(), guesses, this.game.getWin());
	}
	
	/**
	 * 
	 * Gets the next guess by calling the GuessManager and makes the guess on the WordleGame
	 * returns the result of calling the WordleGame in order to ensure the guess was 
	 * successfully mad and and to use that structure for use in future meethods.
	 * 
	 * @return char array of successfully made guess
	 */
	public char[] makeNextGuess() {
		
		String nextGuess = this.guessManager.getNextWord();
		
		this.game.guess(nextGuess);
//		this.game.printGrid(); // for testing
		
		return this.game.getLastGuess();
	}
	
	
	
	
	
	
	
}
