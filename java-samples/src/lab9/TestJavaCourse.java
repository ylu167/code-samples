package lab9;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestJavaCourse {

    static JavaCourse course;

    @BeforeAll
    public static void setup() {
        // Run the JavaCourse simulation with a certain number of TAs
        int numTAs = 5;
        course = new JavaCourse(numTAs);
        Thread courseThread = new Thread(course);
        courseThread.start();
        try {
            courseThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTotalStudentsHelped() {
        // Test 1: The number of students helped should tally with the TAs report at the end.
        int totalStudentsHelpedByTAs = 0;
        for (TA ta : course.taList) {
            totalStudentsHelpedByTAs += ta.getStudentsHelped();
        }
        assertEquals(Student.totalStudentsHelped, totalStudentsHelpedByTAs, "Mismatch in total students helped");
    }

    @Test
    public void testTotalHelpTime() {
        // Test 2: The time spent by a TA with each student should add up to the same number as at the total in the TAs report
        int totalHelpTimeByTAs = 0;
        for (TA ta : course.taList) {
            totalHelpTimeByTAs += ta.getHelpTime();
        }
        assertEquals(JavaCourse.totalHelpTime, totalHelpTimeByTAs, "Mismatch in total help time");
    }

    @Test
    public void testTotalStudentsCreated() {
        // Test 3: Total students created equals students helped and students left in the Q
        int totalStudentsCreated = Student.totalStudentsCreated;
        int studentsLeftInQ = course.taRoom.getQueueLength();
        assertEquals(totalStudentsCreated, Student.totalStudentsHelped + studentsLeftInQ, "Mismatch in total students created");
    }

    @Test
    public void testTotalHelpTimeConsistency() {
        // Test 4: Total help time should be same as time spent by each TA
        int totalHelpTimeByTAs = 0;
        for (TA ta : course.taList) {
            totalHelpTimeByTAs += ta.getHelpTime();
        }
        assertEquals(JavaCourse.totalHelpTime, totalHelpTimeByTAs, "Mismatch in total help time");
    }
}
