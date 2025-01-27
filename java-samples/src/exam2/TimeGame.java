package exam2;

import java.util.Scanner;

public class TimeGame extends WordGame {
    public static final int MAX_TIME = 10;

    public TimeGame(String[] words) {
        super(words);
    }

    @Override
    public void play() {
        Scanner input = new Scanner(System.in);
        long startTime = System.currentTimeMillis();
        String currentClue = clue;

        System.out.println("[" + currentClue + "]");
        System.out.println("Enter your guess! ");

        while (!isGameOver) {
            Long Time = (System.currentTimeMillis() - startTime) / 1000;
            if (Time > MAX_TIME) {
                System.out.println(Time + " seconds");
                System.out.println("Sorry! The word is " + puzzleWord);
                return;
            }

            System.out.println(Time + " seconds");
            String guessStr = input.nextLine();
            char guess = guessStr.charAt(0);
            currentClue = getNextClue(currentClue, guess);
            System.out.println("[" + currentClue + "]");

            if (isGameOver) {
                System.out.println(Time + " seconds");
                System.out.println("Congratulations! You got it right in " +Time+ " seconds");
            }
        }


    }
}
