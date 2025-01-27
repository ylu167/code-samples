package lab9;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaCourse implements Runnable {
    public static volatile boolean allDone = false;
    public static int totalHelpTime = 0;
    public static final int STUDENT_DELAY = 1; // milliseconds
    public static final int MAX_HELP_TIME = 240; // minutes
    public static final int MIN_QUESTION_TIME = 5; // minutes
    public static final int MAX_QUESTION_TIME = 20; // minutes

    public List<TA> taList = new ArrayList<>();
    public TARoom taRoom;
    private List<Thread> taThreads = new ArrayList<>();
    private int maxQLength = 0;
    private long startTime;
    private long endTime;

    public JavaCourse(int numTAs) {
        for (int i = 1; i < numTAs+1; i++) {
            System.out.print("TA"+i+"             ");
        }
        System.out.println();
        for (int i = 0; i < numTAs; i++) {
            System.out.print("----------------");
        }
        System.out.println();
        taRoom = new TARoom();
        for (int i = 0; i < numTAs; i++) {
            TA ta = new TA(taRoom);
            taList.add(ta);
            Thread t = new Thread(ta);
            taThreads.add(t);
        }
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        // Start TA threads
        for (Thread t : taThreads) {
            t.start();
        }
        // Create students
        while (!allDone) {
            Student s = new Student();
            taRoom.putStudent(s);
            int qLength = taRoom.getQueueLength();
            if (qLength > maxQLength) {
                maxQLength = qLength;
            }
            try {
                Thread.sleep(STUDENT_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Wait for TA threads to finish
        for (Thread t : taThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        endTime = System.currentTimeMillis();
        printReport();
    }

    public void printReport() {
        System.out.println("-------------------------TAs-----------------");
        taList.sort(null);
        for (TA ta : taList) {
            System.out.println("TA " + ta.getTaID() + " helped " + ta.getStudentsHelped() + " students for " + ta.getHelpTime() + " min");
        }
        System.out.println("------------------------Time-----------------");
        System.out.println("Total help time:           " + totalHelpTime + " min");
        System.out.println("Max help time:             " + (TA.taCount * MAX_HELP_TIME) + " min");
        System.out.println("Total lapsed time:         " + (endTime - startTime) + " min");
        System.out.println("-----------------------Students--------------");
        System.out.println("Total students created:     " + Student.totalStudentsCreated);
        System.out.println("Total students helped:      " + Student.totalStudentsHelped);
        System.out.println("Max Q length:               " + maxQLength);
        System.out.println("Students left in the Q:     " + taRoom.getQueueLength());
        System.out.println("---------------------------------------------");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of TAs");
        int numTAs = scanner.nextInt();
        JavaCourse course = new JavaCourse(numTAs);
        Thread courseThread = new Thread(course);
        courseThread.start();
    }
}
