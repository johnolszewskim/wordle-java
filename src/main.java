import java.io.File;
import java.util.*;
import wordlejava.*;

public class main {
	
	public static int NUM_ITERATIONS = 5;
	
	public static void main(String[] args) {
		
		int numIterations = NUM_ITERATIONS;
		
		if(args.length == 1) {
			numIterations = Integer.parseInt(args[0]);
		}
		
		LinkedList<WordleRecord> records = runIterations(numIterations);
		
		for(WordleRecord r : records) {
			System.out.println(r.toString());
		}
		
		System.out.println(Arrays.toString(tallyResults(records)));
		
	}
	
	private static LinkedList<WordleRecord> runIterations(int iterations) {
		
		LinkedList<String> libraryFromWeb = GuessManager.importWordsToLibraryFromWeb(WordleGame.STANDARD_LENGTH);
		
		LinkedList<WordleRecord> records = new LinkedList<>();
		
		for(int i = 0; i < iterations; i++) {
			
			LinkedList<String> library = new LinkedList<>();
			library.addAll(libraryFromWeb);
			
//			System.out.println("runIterations() library.size(): " + library.size()); // for testing
			
			WordleSolver randomSolver = new WordleSolver(WordleGame.getRandomStandardGame(library), library);
			WordleRecord r = randomSolver.solve();
			records.add(r);
//			new Scanner(System.in).nextLine(); 
		}
		
		return records;
	}
	
	private static int[] tallyResults(LinkedList<WordleRecord> records) {
		
		int[] results = {0,0,0,0,0,0,0};
		
		for(int i= 0; i < records.size(); i++) {
			
			results[records.get(i).getWinIndex()]++;
			
		}
		
		return results;
	}

}
