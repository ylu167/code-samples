package prereq;

import java.util.Scanner;

public class WordFilter {
	
	//do not change main method
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter a word");
		String word = input.next();
		System.out.println("Enter a filter");
		String filter = input.next();
		WordFilter wordFilter = new WordFilter();
		wordFilter.filter(word, filter);
		input.close();
	}
	
	private void filter(String word, String filter) {
		//write your code here
        char[] filterArray = filter.toCharArray();
		boolean[] filterFlags = new boolean[26];

		for (char c : filterArray) {
			filterFlags[c - 'a'] = true;
		}

		StringBuilder filteredWord = new StringBuilder();

		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (!filterFlags[c - 'a']) {
				filteredWord.append(c);
			}
		}

		System.out.println("Filtered word: " + filteredWord.toString());
	}
}
