package lab6;

public class Student implements Comparable<Student> {
	String lastName;
	String firstName;
	String andrewID;
	int companyRank;
	String companyName;

	public Student(String lastName, String firstName, String andrewID, int companyRank, String companyName) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.andrewID = andrewID;
		this.companyRank = companyRank;
		this.companyName = companyName;
	}

	@Override
	public int compareTo(Student other) {
		return this.andrewID.compareTo(other.andrewID);
	}
}
