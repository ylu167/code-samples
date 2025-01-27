package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CCModel {
	Case[] cases;
	String[] fileData;

	/**loadData() takes filename as a parameter,
	 * reads the file and loads all 
	 * data as a String for each row in 
	 * fileData[] array
	 * @param filename
	 */
	void loadData(String filename) {
		//write your code here
		//load the content of the file and split into individual lines
		//add code to handle exceptions
		Scanner input = null;
		try {
			input = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder stringData = new StringBuilder();
		while (input.hasNextLine()) {
			stringData.append(input.nextLine() + "\n");
		}
		fileData = stringData.toString().split("\n");
	}

	/**loadCases() uses the data stored in fileData array
	 * and creates Case objects for each row.
	 * These cases are loaded into the cases array.
	 * Note that you may have to traverse the fileData array twice
	 * to be able to initialize the cases array's size.
	 */
	void loadCases() {
		//write your code here
		//initialize the cases array to match the number of rows in fileData
		cases = new Case[fileData.length];

		//parse the fileData and create Case objects
		for (int i = 0; i < fileData.length; i++) {
			String[] parts = fileData[i].split("\t");

			//extract the date, title, case type, and case number
			String caseDate = parts[0];
			String caseTitle = parts[1];

			String caseType = null;
			String caseNumber = null;

			//extract caseType from the title (enclosed in parentheses)
			int typeStartIndex = caseTitle.lastIndexOf("(");
			int typeEndIndex = caseTitle.lastIndexOf(")");

			if (typeStartIndex != -1 && typeEndIndex != -1) {
				caseType = caseTitle.substring(typeStartIndex + 1, typeEndIndex).trim();
				//remove caseType from the title
				caseTitle = caseTitle.substring(0, typeStartIndex).trim();
			}

			//case number is the third part if present
			if (parts.length > 2) {
				caseNumber = parts[2].trim();
			}

			//create a new Case object and store it in the cases array
			cases[i] = new Case(caseDate, caseTitle, caseType, caseNumber);
		}
	}


}
