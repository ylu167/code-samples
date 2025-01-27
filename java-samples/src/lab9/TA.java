package lab9;

public class TA implements Runnable, Comparable<TA> {
    public static int taCount = 0;
    private static Object totalHelpTimeLock = new Object();
    private int taID;
    private int studentsHelped = 0;
    private int helpTime = 0; // time spent by this TA
    private TARoom taRoom;

    public TA(TARoom taRoom) {
        synchronized (TA.class) {
            taCount++;
            this.taID = taCount;
        }
        this.taRoom = taRoom;
    }

    @Override
    public void run() {
        try {
            while (helpTime < JavaCourse.MAX_HELP_TIME && !JavaCourse.allDone) {
                Student s = taRoom.getStudent();
                if (s == null) {
                    break;
                }
                int questionTime = s.askQuestion();
                studentsHelped++;
                helpTime += questionTime;
                synchronized (totalHelpTimeLock) {
                    JavaCourse.totalHelpTime += questionTime;
                }
                // Simulate discussion
                System.out.println(spacer(taID) + "TA" + taID + ":Student" + s.studentID + ":" + questionTime + "min");
                Thread.sleep(questionTime);
                if (helpTime >= JavaCourse.MAX_HELP_TIME) {
                    synchronized (totalHelpTimeLock) {
                        if (JavaCourse.totalHelpTime >= taCount * JavaCourse.MAX_HELP_TIME && !JavaCourse.allDone) {
                            JavaCourse.allDone = true;
                            System.out.println("**************** All done flag set by TA" + taID);
                        }
                    }
                    System.out.println(spacer(taID) + "---- TA" + taID + " done ----");
                    break;
                }
                synchronized (totalHelpTimeLock) {
                    if (JavaCourse.totalHelpTime >= taCount * JavaCourse.MAX_HELP_TIME && !JavaCourse.allDone) {
                        JavaCourse.allDone = true;
                        System.out.println("******************* All done flag set by TA" + taID);
                        System.out.println(spacer(taID) + "---- TA" + taID + " done ----");
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            // Handle exception
        }
    }

    private String spacer(int id) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < id; i++) {
            sb.append("                ");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(TA other) {
        return Integer.compare(other.helpTime, this.helpTime);
    }

    public int getStudentsHelped() {
        return studentsHelped;
    }

    public int getHelpTime() {
        return helpTime;
    }

    public int getTaID() {
        return taID;
    }
}
