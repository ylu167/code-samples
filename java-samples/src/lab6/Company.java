package lab6;

public class Company implements Comparable<Company> {
	static int overallHiredCount;
	int rank;
	String name;
	int hiredCount;

	public Company(int rank, String name, int hiredCount) {
		this.rank = rank;
		this.name = name;
		this.hiredCount = hiredCount;
	}

	@Override
	public int compareTo(Company other) {
		return Integer.compare(this.rank, other.rank);
	}
}
