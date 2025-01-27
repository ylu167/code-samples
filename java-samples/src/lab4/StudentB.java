package lab4;

import java.util.Random;

public class StudentB extends Student {

    static double studentBDonations; //total money donated by Section B students
    static final int MAX_MONEY_DONATION = 1000;

    StudentB(char section, String lastName, String firstName, double income) {
        super(section, lastName, firstName, income);
    }

    @Override
    void donate() {
        Random random = new Random();
        donation = 1 + random.nextInt(MAX_MONEY_DONATION);
        Student.totalMoneyDonations += donation;
        StudentB.studentBDonations += donation;
    }
}
