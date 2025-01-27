package exam2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameParlor {

	WordGame wordGame;  //this must hold the instance of WordGame to be played (CountGame or TimeGame).
	String[] words;		//will contain words read from the data file
	String filename = "miniwords.txt"; //word file to be used for the sames
	Scanner input = new Scanner(System.in);

	//do not change this method
	public static void main(String[] args) {
		GameParlor game = new GameParlor();
		game.words = game.readWords(game.filename);
		game.runGame();
	}


	/** readWords() reads words from filename and loads into words[] array */
	String[] readWords(String filename) {
		//write your code here
		try {
			Scanner fileScanner = new Scanner(new File(filename));
			StringBuilder wordsBuilder = new StringBuilder();
			while (fileScanner.hasNextLine()) {
				wordsBuilder.append(fileScanner.nextLine()).append(",");
			}
			fileScanner.close();
			return wordsBuilder.toString().split(",");
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filename);
			return new String[]{};
		}
	}

	/**runGame() prints menu options, takes user choice,
	 * initializes wordGame with appropriate WordGame instance, and then
	 * invokes play() on it polymorphically.
	 */
	void runGame() {
		//write your code here
		System.out.println("*** Enter your choice ***");
		System.out.println("1. Guess the Word in 10 counts");
		System.out.println("2. Guess the Word in 10 seconds");

		int choice = input.nextInt();
		input.nextLine();

		if (choice == 1) {
			wordGame = new CountGame(words);
		} else if (choice == 2) {
			wordGame = new TimeGame(words);
		}

		wordGame.play();
	}
}
