package lab4;

import java.util.Random;

public class StudentC extends Student implements Serviceable{

    static double studentCDonations; //total money donated by Section C students
    static final int MAX_MONEY_DONATION = 10;
    int timeDonation;

    StudentC(char section, String lastName, String firstName, double income) {
        super(section, lastName, firstName, income);
    }

    @Override
    void donate() {
        Student.totalMoneyDonations += MAX_MONEY_DONATION;
        StudentC.studentCDonations += MAX_MONEY_DONATION;
    }

    @Override
    public void serve() {
        Random random = new Random();
        timeDonation = random.nextInt(MAX_SERVICE_HOURS) + 1;
        Student.totalTimeDonations += timeDonation;
    }
}
