package lab9;

import java.util.concurrent.ThreadLocalRandom;

public class Student {
    public static int totalStudentsCreated = 0;
    public static int totalStudentsHelped = 0;
    public static final int MIN_QUESTION_TIME = 5; // in minutes
    public static final int MAX_QUESTION_TIME = 20; // in minutes

    public int studentID;

    public Student() {
        totalStudentsCreated++;
        this.studentID = totalStudentsCreated;
    }

    public int askQuestion() {
        totalStudentsHelped++;
        return ThreadLocalRandom.current().nextInt(MIN_QUESTION_TIME, MAX_QUESTION_TIME + 1);
    }
}
