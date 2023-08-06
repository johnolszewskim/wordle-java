# wordle-java

This project is an avenue to exercise Java coding abilities in-addition to learning new
concepts. The wordlejava package is used to emulate the New York Times word game Wordle. The package
includes a variety of different files for difference uses. Following terminology is used:

- wordle: the unknown word that is being guessed by the player
- guess: a word that the player guesses as a turn
- game: is one single instance of WordleGame, that has a wordle and can be interacted with
		by the use or other classes

To compile the package run in the folder containing /wordlejava:

~ javac wordlejava/*.java -Xlint 

The WordleGame.java is the model for a game of Wordle. WorldGame flexible in that it isn't
restricted to numbers of a specific length or a specific number of guesses for the user.
These can be modified as parameters at initialization.

PlayWordle.java, when run allows the user to play a game of Wordle based on a randomly selected
word in the library of words. It then prints the results of the game and a instance of
WordleSolver is run with the same game so the user can compare their effort. To play Wordle
run the following command from the same folder from which it was compiled:

~ java wordlejava/PlayWordle.java

WordleSolver.java solves a specific WordleGame instance. It begins when solve() is called and 
continues until the game is solved or there are no guesses left. It uses a GuessManager
instance to manage the possibilities.

GuessManager.java uses a list of possible words to make each guess for a WordleGame.
The game keeps track of the results of each guess and refines the exclude words
that are not possible. GuessManager uses the letter frequencies primarily to determine which
word to pick. GuessManager is where expanded functionality would be found for 
smarter algorithms.

WordleRecord.java is a class intended to package the results of a WordleGame in an easy
passable and save-able format. 

main.java is a testing class that can run a set number, 5 iterations are run if unspecified,
of iterations of WordleSolver.solve(). To run with a number of iterations run the following
command:

~ java wordlejava/main.java {iterations}

Where {iterations} is the first command line argument, an integer.
