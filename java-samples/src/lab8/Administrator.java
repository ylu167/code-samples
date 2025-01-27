package lab8;

public class Administrator implements Runnable {
    public static volatile int totalWelcomeCount = 0;
    public int adminWelcomeCount = 0;
    public int adminTalkTime = 0;
    public String name;

    public Administrator(String name) {
        this.name = name;
    }

    public void run() {
        while (true) {
            CMUStudent student = null;

            synchronized (Mixer.lock) {
                if (totalWelcomeCount >= Mixer.MAX_STUDENTS) {
                    break;
                }
                if (!Mixer.studentsQ.isEmpty()) {
                    student = Mixer.studentsQ.poll();
                    totalWelcomeCount++;
                } else {
                    break;
                }
            }

            if (student == null) {
                break;
            }

            // Simulate talking to the student
            if (name.equals("Andy")) {
                System.out.println("\t" + name + " talking to Student " + student.id + " for " + student.studentTalkTime + " ms");
            } else {
                System.out.println(name + " talking to Student " + student.id + " for " + student.studentTalkTime + " ms");
            }

            try {
                Thread.sleep(student.studentTalkTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update counts
            adminWelcomeCount++;
            adminTalkTime += student.studentTalkTime;
        }
    }
}
