package lab8;

import java.util.LinkedList;
import java.util.Queue;

public class Mixer {
    public static final int MAX_STUDENTS = 20;
    public static Queue<CMUStudent> studentsQ = new LinkedList<>();
    public Administrator admin1;
    public Administrator admin2;
    public static final Object lock = new Object();

    public static void main(String[] args) {
        Mixer mixer = new Mixer();
        mixer.startEvent();
    }

    public void startEvent() {
        // Initialize studentsQ
        for (int i = 0; i < MAX_STUDENTS; i++) {
            studentsQ.add(new CMUStudent());
        }

        // Create admins
        admin1 = new Administrator("Andy");
        admin2 = new Administrator("Sean");

        // Create threads
        Thread t1 = new Thread(admin1);
        Thread t2 = new Thread(admin2);

        System.out.println("*** Mixer Event ***");
        t1.start();
        t2.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printReport();
    }

    public void printReport() {
        System.out.println("\n*** Event Report ***\n");
        System.out.println("Total students welcomed by " + admin1.name + ": " + admin1.adminWelcomeCount);
        System.out.println("Total talk time by " + admin1.name + ": " + admin1.adminTalkTime + " ms");
        System.out.println("---------------------------------\n");
        System.out.println("Total students welcomed by " + admin2.name + ": " + admin2.adminWelcomeCount);
        System.out.println("Total talk time by " + admin2.name + ": " + admin2.adminTalkTime + " ms");
        System.out.println("---------------------------------\n");
        System.out.println("*** End Report ***");
    }
}

