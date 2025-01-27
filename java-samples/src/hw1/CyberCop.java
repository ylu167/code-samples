package hw1;

import java.util.Scanner;

public class CyberCop {

	public static final String DATAFILE = "data/FTC-cases-TSV.txt";
	CCModel ccModel = new CCModel();
	SearchEngine searchEngine = new SearchEngine();

	Scanner input = new Scanner(System.in);

	/**main() instantiates CyberCop and then invokes dataManager's loadData
	 * and loadCases() methods
	 * It then invokes showMenu to get user input
	 * @param args
	 */
	//Do not change this method
	public static void main(String[] args) {
		CyberCop cyberCop = new CyberCop();
		cyberCop.ccModel.loadData(DATAFILE);
		cyberCop.ccModel.loadCases();
		cyberCop.showMenu();
	}

	/**showMenu() shows the menu. 
	 * Based on the user choice, it invokes one of the methods:
	 * printSearchResults(), printCaseTypeSummary(), or printYearwiseSummary()
	 * The program exits when user selects Exit option. 
	 * See the hand-out for the expected layout of menu-UI
	 */
	void showMenu() {
		//write your code here
		//infinite loop to keep showing the menu until user chooses to exit
		while (true) {
			System.out.println("*** Welcome to CyberCop! ***");
			System.out.println("1. Search cases for a company");
			System.out.println("2. Search cases in a year");
			System.out.println("3. Search case number");
			System.out.println("4. Print Case-type Summary");
			System.out.println("5. Print Year-wise Summary");
			System.out.println("6. Exit");
			//get the user's choice
			int choice = input.nextInt();
			//consume the newline
			input.nextLine();

			switch (choice) {
				case 1:
					System.out.println("---------------------------------------------------------------------------");
					System.out.println("Enter search string");
					//get the caseTitle
					String company = input.nextLine();
					Case[] companyCases = searchEngine.searchTitle(company, ccModel.cases);
					printSearchResults(company, companyCases);
					break;
				case 2:
					System.out.println("---------------------------------------------------------------------------");
					System.out.println("Enter search year as YYYY");
					//get the caseDate
					String year = input.nextLine();
					Case[] yearCases = searchEngine.searchYear(year, ccModel.cases);
					printSearchResults(year, yearCases);
					break;
				case 3:
					System.out.println("---------------------------------------------------------------------------");
					System.out.println("Enter case number");
					//get the caseNumber
					String caseNumber = input.nextLine();
					Case[] caseNumberCases = searchEngine.searchCaseNumber(caseNumber, ccModel.cases);
					printSearchResults(caseNumber, caseNumberCases);
					break;
				case 4:
					printCaseTypeSummary();
					break;
				case 5:
					printYearwiseSummary();
					break;
				case 6:
					// Exit the program
					input.close();
					return;
			}
		}
	}

	/**printSearcjResults() takes the searchString and array of cases as input
	 * and prints them out as per the format provided in the handout
	 * @param searchString
	 * @param cases
	 */
	void printSearchResults(String searchString, Case[] cases) {
		//write your code here
		System.out.println("---------------------------------------------------------------------------");
		if (cases.length == 0) {
			System.out.println("Sorry, no search results found for "+searchString);
		}
		else {
			System.out.println(cases.length+" case(s) found for "+searchString);
			System.out.println("---------------------------------------------------------------------------");
			System.out.print(" #. Last update	");
			System.out.printf("%-72s %-16s %s\n", "Case Title", "Case Type", "Case/File Number");
			System.out.println("---------------------------------------------------------------------------");
			int count = 1;

			for (Case c : cases) {
				System.out.print(" " + count + ". "+c.caseDate + "\t");
				System.out.printf("%-72s %-24s %s\n",
						c.caseTitle,
						c.caseType != null ? c.caseType : "",
						c.caseNumber != null ? c.caseNumber : "");
				count++;
			}

		}
		System.out.println("---------------------------------------------------------------------------");
	}

	/**printCaseTypeSummary() prints a summary of
	 * number of cases of different types as per the 
	 * format given in the handout.
	 */
	void printCaseTypeSummary() {
		//write your code here
		int federalCount = 0;
		int administrativeCount = 0;
		int unknownCount = 0;
		//access the cases directly from the model
		for (Case c : ccModel.cases) {
			if (c.caseType != null) {
				if (c.caseType.equalsIgnoreCase("Federal")) {
					federalCount++;
				} else if (c.caseType.equalsIgnoreCase("Administrative")) {
					administrativeCount++;
				}
			}
		}
		unknownCount=ccModel.cases.length-federalCount-administrativeCount;

		System.out.println("---------------------------------------------------------------------------");
		System.out.println("*** Case Type Summary Report ***");
		System.out.println("No. of Administrative cases: " + administrativeCount);
		System.out.println("No. of Federal cases: " + federalCount);
		System.out.println("No. of unknown case types: " + unknownCount);
		System.out.println("---------------------------------------------------------------------------");
	}
	
	/**printYearWiseSummary() prints number of cases in each year
	 * as per the format given in the handout
	 */
	void printYearwiseSummary() {
		//write your code here
		int minYear = Integer.MAX_VALUE;
		int maxYear = Integer.MIN_VALUE;

		//find the minimum and maximum years in the data
		for (Case c : ccModel.cases) {
			int year = c.getYear();
			if (year < minYear) {
				minYear = year;
			}
			if (year > maxYear) {
				maxYear = year;
			}
		}

		//create a dynamic array to store case counts for each year
		int[] yearCount = new int[maxYear - minYear + 1];

		//populate the yearCount array with case counts
		for (Case c : ccModel.cases) {
			int year = c.getYear();
			//adjust for dynamic indexing
			yearCount[year - minYear]++;
		}

		//print the year-wise summary
		System.out.println("---------------------------------------------------------------------------");
		System.out.printf("%56s", "*** Year-wise Summary Report ***");
		System.out.println();
		System.out.printf("%60s", "*** Number of FTC cases per year ***");
		System.out.println();

		int count = 5;
		for (int i = 0; i < yearCount.length; i++) {
			if (i%count==0) {
				System.out.println();
			}
			System.out.printf("\t\t"+"%-8s", (maxYear - i) + ": " + yearCount[yearCount.length-1-i]);
		}
		System.out.println();
		System.out.println("---------------------------------------------------------------------------");
	}


}
