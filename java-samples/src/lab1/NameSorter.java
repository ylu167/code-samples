package lab1;

import java.util.Arrays;
import java.util.Scanner;

/**NameSorter class takes n number of names in the form of string inputs 
 * from the user. It then asks user for which name to search for. 
 * It sorts the names entered by the user, and then prints the 
 * position of the search-name in the sorted list of the names, 
 * if it is found.
 */

public class NameSorter {
	Scanner input = new Scanner(System.in);

	/**getNameInputs takes an int parameter n and creates an array of size n. 
	 * It then asks user to Enter n names that get stored in the array. 
	 * It uses the helper method toTitleCase() to convert all names to Title case. 
	 * It returns the array filled with names entered by the user.
	 */
	String[] getNameInputs(int n) {
		//write your code here
		String[] names = new String[n];
		for (int i = 0; i < n; i++) {
			System.out.println("Enter Name "+ (i+1));
			String name = input.next();
			names[i] = toTitleCase(name);
		}

		return names;
	}
	
	/**toTitleCase() takes one string argument as name and returns the string in title case. 
	 * If the name is null or the string is empty, it returns null.
	 */
	String toTitleCase(String name) {
		//write your code here
		if (name == null || name.isEmpty()) {
			return null;
		}

		String[] words = name.split("\\s+");
		StringBuilder titleCase = new StringBuilder();

		for (String word : words) {
			if (!word.isEmpty()) {
				// Convert the first character to uppercase and the rest to lowercase
				String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
				// Append the capitalized word to the StringBuilder
				titleCase.append(capitalizedWord).append(" ");
			}
		}

		return titleCase.toString().trim();
	}

	/**sortAndSearch() takes two arguments. The first is an array of strings and the second
	 * is a searchString. The method first sorts the array in increasing alphabetical order, 
	 * and prints it in that order.
	 * It then searches for the searchString in a case-insensitive way. If the searchString is found,
	 * it returns the index of the searchString according to the sorted list. 
	 * If it is not found, then it returns -1.
	 * It also handles nulls as needed.
	 */
	int sortAndsearch(String[] strings, String searchString) {
		//write your code here
		if (strings == null || strings.length == 0 || searchString == null) {
			return -1;
		}

		// Convert the search string to title case
		searchString = toTitleCase(searchString);

		// Sort the array using Arrays.sort
		Arrays.sort(strings);

		// Print sorted array
		System.out.println("*** Names in sorted order ***");
		for (int i = 0; i < strings.length; i++) {
			System.out.println((i+1) + ". " + strings[i]);
		}

		// Search for the name in the sorted array
		for (int i = 0; i < strings.length; i++) {
			if (searchString.equalsIgnoreCase(strings[i])) {
				return i;
			}
		}

		return -1;
	}

	/**DO NOT CHANGE THIS METHOD */
	public static void main(String[] args) {
		NameSorter nameSorter = new NameSorter();
		System.out.println("*** How many names to store? ***");
		int n = nameSorter.input.nextInt();
		if (n > 0) {
			String[] names = nameSorter.getNameInputs(n);
			System.out.println("*** Enter the name to search ***");
			String name = nameSorter.input.next();
			int index = nameSorter.sortAndsearch(names, name);
			if (index >=0 ) System.out.println(name + " found at position " + (index+1));
			else System.out.println("Sorry! " + name + " not found!");
		} else System.out.println("Good Bye!");
	}
}
