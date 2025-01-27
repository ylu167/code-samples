package exam1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Starbucks {

	public static void main(String[] args) {
		Starbucks starbucks = new Starbucks();
		String[] fileData = starbucks.loadData("breakfast.txt");
		String[] items = starbucks.getItems(fileData);
		String[][] customerItems = starbucks.getCustomerItems(fileData);
		String[][] itemCustomers = starbucks.getItemCustomers(items, customerItems);

		starbucks.printItems(items);
		starbucks.printCustomerItems(customerItems);
		starbucks.printItemCustomers(itemCustomers);
	}

	/** loadData reads the data from filename
	 * and return a String array, say fileData, that has one element
	 * for each row in the file.
	 *  
	 * @param filename
	 * @return
	 */
	String[] loadData(String filename) {
		//write your code here
		Scanner input = null;
		String[] fileData = null;
		try {
			input = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new String[0];
		}
		StringBuilder stringData = new StringBuilder();
		while (input.hasNextLine()) {
			stringData.append(input.nextLine() + "\n");
		}
		input.close();
		fileData = stringData.toString().split("\n");
		return fileData;
	}

	/**getCustomerItems() reads the fileData array created in 
	 * loadData() method and populates a 2D array, say customerItems array.
	 * Each row in the fileData array represents one row in customerItems array
	 * First element in each row is customer name, followed by each element
	 * having one item ordered by that customer. 
	 * @param fileData
	 * @return
	 */
	String[][] getCustomerItems(String[] fileData) {
		//write your code here
		int numCustomers = fileData.length;
		String[][] customerItems = new String[numCustomers][];

		for (int i = 0; i < numCustomers; i++) {
			String line = fileData[i];
			String[] parts = line.split(",");
			for (int j = 0; j < parts.length; j++) {
				parts[j] = parts[j].trim();
			}
			customerItems[i] = parts;
		}

		return customerItems;
	}

	/**getItems() takes the fileData array created in loadData() method 
	 * and returns a 1D array, say items.
	 * The items array must have all the items ordered
	 * by all the customers without repeating any item
	 * @param fileData
	 * @return
	 */
	String[] getItems(String[] fileData) {
		//write your code here
		int totalItems = 0;
		for (String line : fileData) {
			String[] parts = line.split(",");
			totalItems += (parts.length - 1);
		}

		String[] tempItems = new String[totalItems];
		int uniqueCount = 0;

		for (String line : fileData) {
			String[] parts = line.split(",");
			for (int j = 1; j < parts.length; j++) {
				String item = parts[j].trim();
				boolean exists = false;
				for (int k = 0; k < uniqueCount; k++) {
					if (tempItems[k].equals(item)) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					tempItems[uniqueCount++] = item;
				}
			}
		}

		String[] items = new String[uniqueCount];
		for (int i = 0; i < uniqueCount; i++) {
			items[i] = tempItems[i];
		}

		return items;
	}

	/** getItemCustomers() uses items and customerItems arrays
	 * to create a reversed 2D array that has one row for each item
	 * and names of customers who ordered that item as the columns. 
	 * @param items
	 * @param customerItems
	 * @return
	 */
	String[][] getItemCustomers(String[] items, String[][] customerItems) {
		//write your code here
		int numItems = items.length;
		String[][] itemCustomers = new String[numItems][];

		for (int i = 0; i < numItems; i++) {
			String currentItem = items[i];
			int count = 0;
			for (String[] customer : customerItems) {
				for (int j = 1; j < customer.length; j++) {
					if (customer[j].equals(currentItem)) {
						count++;
						break;
					}
				}
			}

			itemCustomers[i] = new String[1 + count];
			itemCustomers[i][0] = currentItem;

			int index = 1;
			for (String[] customer : customerItems) {
				for (int j = 1; j < customer.length; j++) {
					if (customer[j].equals(currentItem)) {
						itemCustomers[i][index++] = customer[0];
						break;
					}
				}
			}
		}

		return itemCustomers;
	}

	//-------------------------------------------------------//
	void printItems(String[] items) {
		System.out.println("*** Starbucks Item List ***");
		System.out.println(Arrays.toString(items));
		System.out.println();
	}

	void printCustomerItems(String[][] customerItems) {
		System.out.println("*** Customer Order List ***");
		for (int i = 0; i < customerItems.length; i++) {
			System.out.printf("%s ordered: ", customerItems[i][0]);
			for (int j = 1; j < customerItems[i].length; j++) {
				System.out.printf(" %s", customerItems[i][j]);
				if (j < customerItems[i].length - 1) {  // no comma in the end
					System.out.print(", ");
				}
			}
			System.out.println();
		} 
		System.out.println();
	}

	void printItemCustomers(String[][] itemCustomers) {
		System.out.println("*** Items Order List ***");
		for (int i = 0; i < itemCustomers.length; i++) {
			System.out.printf("%s ordered by: ", itemCustomers[i][0]);
			for (int j = 1; j < itemCustomers[i].length; j++) {
				System.out.printf(" %s", itemCustomers[i][j]);
				if (j < itemCustomers[i].length - 1) {  // no comma in the end
					System.out.print(", ");
				}
			}
			System.out.println();
		} 
	}
}
