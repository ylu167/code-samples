package hw2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		//write your code here
		CaseReader caseReader = CaseReaderFactory.createReader(filename);
		List<Case> cases = caseReader.readCases();

		caseList.setAll(cases);
		caseMap.clear();

		for (Case c : cases) {
			caseMap.put(c.getCaseNumber(), c);
		}
	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		//write your code here
		yearMap.clear();
		yearList.clear();

		// Group cases by year and populate yearMap
		Map<String, List<Case>> groupedByYear = caseList.stream().collect(Collectors.groupingBy(c -> c.getCaseDate().substring(0, 4)));

		yearMap.putAll(groupedByYear);
		yearList.addAll(groupedByYear.keySet());
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		//write your code here
		List<Case> matchingCases = new ArrayList<>();
		for (Case c : caseList) {
			boolean match = true;

			// Title comparison using 'contains' after trimming and ignoring case
			if (title != null && !title.isEmpty()) {
				if (!c.getCaseTitle().toLowerCase().contains(title.toLowerCase().trim())) {
					match = false;
				}
			}

			// Case type comparison after trimming and ignoring case
			if (caseType != null && !caseType.isEmpty()) {
				if (!c.getCaseType().toLowerCase().contains(caseType.toLowerCase().trim())) {
					match = false;
				}
			}

			// Year comparison using date parsing
			if (year != null && !year.isEmpty()) {
				try {
					// Assume date is in format 'YYYY-MM-DD' or 'MM/DD/YYYY'
					String caseDate = c.getCaseDate().trim();
					String caseYear;
					if (caseDate.contains("-")) {
						caseYear = caseDate.split("-")[0];
					} else if (caseDate.contains("/")) {
						caseYear = caseDate.split("/")[2];
					} else {
						caseYear = caseDate; // If date is just the year
					}
					if (!caseYear.equals(year.trim())) {
						match = false;
					}
				} catch (Exception e) {
					match = false;
				}
			}

			// Case number comparison after trimming
			if (caseNumber != null && !caseNumber.isEmpty()) {
				if (!c.getCaseNumber().trim().contains(caseNumber.trim())) {
					match = false;
				}
			}

			if (match) {
				matchingCases.add(c);
			}
		}
		return matchingCases;
	}
}
