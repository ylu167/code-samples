package lab8;

import java.util.Random;

public class CMUStudent {
    public static final int MIN_TALK_TIME = 5;
    public static final int MAX_TALK_TIME = 15;
    public static volatile int cmuStudentCount = 0;

    public int id;
    public int studentTalkTime;

    public CMUStudent() {
        synchronized (CMUStudent.class) {
            cmuStudentCount++;
            id = cmuStudentCount;
        }
        // Assign studentTalkTime
        Random rand = new Random();
        studentTalkTime = rand.nextInt(MAX_TALK_TIME - MIN_TALK_TIME + 1) + MIN_TALK_TIME;
    }
}
