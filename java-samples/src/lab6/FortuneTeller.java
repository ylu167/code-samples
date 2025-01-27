package lab6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FortuneTeller {
	List<Student> students = new ArrayList<>();
	List<Company> companies = new ArrayList<>();

	// DO NOT CHANGE THIS METHOD
	public static void main(String[] args) {
		FortuneTeller fortuneTeller = new FortuneTeller();
		fortuneTeller.loadStudentsList("Fortunes.csv");
		fortuneTeller.loadCompaniesList();
		System.out.println("*** Welcome to Fortune Teller! ***");
		System.out.println("1. No. of students hired by Fortune100 Best Employers");
		System.out.println("2. Fortune100 Best Employers by Hired Count");
		System.out.println("3. Employers and Students");
		System.out.println("Please choose an option or any other key to exit");
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		fortuneTeller.printReport(choice);
		input.close();
	}

	void printReport(int choice) {
		switch (choice) {
		case 1: {

			/** Write the appropriate Collections.sort() statement here to sort companies on Rank*/
			Collections.sort(companies);
			System.out.println("*** No. of students hired by Fortune10 Best Employers ***"); 
			System.out.println("--------------------------------------------------------------------");
			System.out.println("Rank. Company\t\t\t\tHired Count");
			System.out.println("--------------------------------------------------------------------");
			for (Company c : companies) {
				System.out.printf("%3d. %-30s: %10d%n", c.rank, c.name, c.hiredCount);
			}
			System.out.println("--------------------------------------------------------------------");
			System.out.printf("Total%43d%n", Company.overallHiredCount);
			System.out.println("====================================================================");
			break;
		}

		case 2: {

			/** Write the appropriate Collections.sort() statement here to sort companies on hiredCount*/
			Collections.sort(companies, new CompanyHiredCountComparator());
			System.out.println("*** Fortune100 Best Employers by Hired Count ***"); 
			System.out.println("--------------------------------------------------------------------");
			System.out.println("Rank. Company\t\t\t\tHired Count");
			System.out.println("--------------------------------------------------------------------");
			for (Company c : companies) {
				System.out.printf("%3d. %-30s: %10d%n", c.rank, c.name, c.hiredCount);
			}
			System.out.println("--------------------------------------------------------------------");
			System.out.printf("Total%43d%n", Company.overallHiredCount);
			System.out.println("====================================================================");

			break;
		}

		case 3: {
			/** Write the appropriate Collections.sort() statement here to sort company and students*/
			Collections.sort(students, new StudentCompanyComparator());
			System.out.println("*** Employers and Students ***");
			System.out.println("--------------------------------------------------------------------");
			int i = 1;
			System.out.printf("#.   %-20s %-20s %-25s %-10s%n", "Employer", "AndrewID", "First name", "Last name" );
			System.out.println("--------------------------------------------------------------------");
			for (Student s: students) {
				System.out.printf("%3d. %-20s %-20s %-25s %-10s%n", i++,  s.companyName, s.andrewID, s.firstName, s.lastName);
			}
			System.out.println("====================================================================");
			break;
		}
		default: break;
		}

	}

	/** loadStudentsList() reads the data from filename 
	 * and loads students arrayList. 
	 */
	void loadStudentsList(String filename) {
		//write your code here
		Scanner input = null;
		StringBuilder studentData = new StringBuilder();
		try {
			input = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (input != null && input.hasNextLine()) {
			studentData.append(input.nextLine()).append("\n");
		}

		String[] studentStrings = studentData.toString().split("\n");
		for (String line : studentStrings) {
			String[] student = line.split(",");
			if (student.length == 5) {
				String lastName = student[0].trim();
				String firstName = student[1].trim();
				String andrewID = student[2].trim();
				int companyRank = Integer.parseInt(student[3].trim());
				String companyName = student[4].trim();
				students.add(new Student(lastName, firstName, andrewID, companyRank, companyName));
			}
		}

	}

	/** loadCompaniesList() uses the data stored in students array list
	 * to create companies array list. In this list, each Company object
	 * must have information about the company that has hired some students
	 */
	void loadCompaniesList() {
		//write your code here
		for (Student student : students) {
			boolean found = false;
			for (Company company : companies) {
				if (company.name.equals(student.companyName)) {
					company.hiredCount++;
					found = true;
					break;
				}
			}
			if (!found) {
				Company newCompany = new Company(student.companyRank, student.companyName, 1);
				companies.add(newCompany);
			}
			Company.overallHiredCount++;
		}
	}

	/*********** write your Comparators below this line ***************/
	public class StudentCompanyComparator implements Comparator<Student> {
		@Override
		public int compare(Student s1, Student s2) {
			int companyComparison = s1.companyName.compareTo(s2.companyName);
			if (companyComparison != 0) {
				return companyComparison;
			}
			return s1.andrewID.compareTo(s2.andrewID);
		}
	}

	public class CompanyHiredCountComparator implements Comparator<Company> {
		@Override
		public int compare(Company c1, Company c2) {
			int hiredCountComparison = Integer.compare(c2.hiredCount, c1.hiredCount); // Descending order
			if (hiredCountComparison != 0) {
				return hiredCountComparison;
			}
			return c1.name.compareTo(c2.name); // Ascending by name
		}
	}
}
