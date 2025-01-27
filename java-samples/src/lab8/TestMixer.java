package lab8;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMixer {

    static Mixer mixer;

    @BeforeAll
    public static void setup() {
        // Reset static variables before the simulation
        Administrator.totalWelcomeCount = 0;
        CMUStudent.cmuStudentCount = 0;

        mixer = new Mixer();
        mixer.startEvent();
    }

    @Test
    public void testTotalWelcomeCountEqualsCmuStudentCount() {
        assertEquals(
                CMUStudent.cmuStudentCount,
                Administrator.totalWelcomeCount,
                "CMUStudent.cmuStudentCount should equal Administrator.totalWelcomeCount"
        );
    }

    @Test
    public void testSumOfAdminWelcomeCountEqualsMaxStudents() {
        int totalAdminWelcomeCount = mixer.admin1.adminWelcomeCount + mixer.admin2.adminWelcomeCount;
        assertEquals(
                Mixer.MAX_STUDENTS,
                totalAdminWelcomeCount,
                "Sum of adminWelcomeCount should equal Mixer.MAX_STUDENTS"
        );
    }
}

