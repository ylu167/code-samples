package hw1;

public class SearchEngine {
	
	/**searchTitle() takes a searchString and array of cases,
	 * searches for cases with searchString in their title,
	 * and if found, returns them in another array of cases.
	 * If no match is found, it returns null.
	 * Search is case-insensitive
	 * @param searchString
	 * @param cases
	 * @return
	 */
	Case[] searchTitle(String searchString, Case[] cases) {
		//write your code here
		int count = 0;
		for (Case c : cases) {
			//case-insensitive
			if (c.caseTitle.toLowerCase().contains(searchString.toLowerCase())) {
				count++;
			}
		}

		//create an array to store the matching cases
		Case[] result = new Case[count];
		int index = 0;
		//add matching cases to the array
		for (Case c : cases) {
			if (c.caseTitle.toLowerCase().contains(searchString.toLowerCase())) {
				result[index++] = c;
			}
		}
		return result;
	}
	
	/**searchYear() takes year in YYYY format as search string,
	 * searches for cases that have the same year in their date,
	 * and returns them in another array of cases.
	 * If not found, it returns null.
	 * @param year
	 * @param cases
	 * @return
	 */
	Case[] searchYear(String year, Case[] cases) {
		//write your code here
		int count = 0;
		for (Case c : cases) {
			if (String.valueOf(c.getYear()).equals(year)) {
				count++;
			}
		}

		//create an array to store the matching cases
		Case[] result = new Case[count];
		int index = 0;
		//add matching cases to the array
		for (Case c : cases) {
			if (String.valueOf(c.getYear()).equals(year)) {
				result[index++] = c;
			}
		}
		return result;
	}
	
	/**searchCaseNumber() takes a caseNumber,
	 * searches for those cases that contain that caseNumber, 
	 * and returns an array of cases that match the search.
	 * If not found, it returns null.
	 * Search is case-insensitive.
	 * @param caseNumber
	 * @param cases
	 * @return
	 */
	Case[] searchCaseNumber(String caseNumber, Case[] cases) {
		//write your code here
		int count = 0;
		for (Case c : cases) {
			if (c.caseNumber != null && c.caseNumber.contains(caseNumber)) {
				count++;
			}
		}

		//create an array to store the matching cases
		Case[] result = new Case[count];
		int index = 0;
		//add matching cases to the array
		for (Case c : cases) {
			if (c.caseNumber != null && c.caseNumber.contains(caseNumber)) {
				result[index++] = c;
			}
		}
		return result;
	}
}
