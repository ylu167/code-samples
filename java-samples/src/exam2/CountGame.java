package exam2;

import java.util.Scanner;

public class CountGame extends WordGame {
    public static final int MAX_COUNT = 10;

    public CountGame(String[] words) {
        super(words);
    }

    @Override
    public void play() {
        Scanner input = new Scanner(System.in);
        int count = 1;
        String currentClue = clue;

        System.out.println("[" + currentClue + "]");
        System.out.println("Enter your guess!");

        while (count < MAX_COUNT && !isGameOver) {
            String guessStr = input.nextLine();
            char guess = guessStr.charAt(0);
            currentClue = getNextClue(currentClue, guess);
            System.out.println(count + ". [" + currentClue + "]");
            count++;

            if (isGameOver) {
                System.out.println("Great! You got it right in " +(count-1)+ " guesses");
                return;
            }
        }

        if (!isGameOver) {
            System.out.println("Sorry! The word is " + puzzleWord);
        }
    }
}
