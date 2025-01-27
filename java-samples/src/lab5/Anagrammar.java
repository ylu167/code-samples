package lab5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Anagrammar {
	String[] words;		//stores all words read from words.txt
	boolean isInDictionary; //true if the clue word exists in words.txt
	boolean hasAnagrams;	//true if the clue word has anagrams
	String[] anagramArray;	//stores all anagrams of clue-word, if found
	
	/**loadWords method reads the file and loads all words 
	 * into the words[] array */
	void loadWords(String filename) {
		//write your code here
		try {
			Scanner fileContent = new Scanner(new File(filename));
			int count = 0;
			while (fileContent.hasNextLine()) {
				count++;
				fileContent.nextLine();
			}
			words = new String[count];
			fileContent = new Scanner(new File(filename));
			int index = 0;
			while (fileContent.hasNextLine()) {
				words[index++] = fileContent.nextLine().trim();
			}
			fileContent.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/** findAnagrams method traverses the words array and looks 
	 * for anagrams of the clue. While doing so, if the clue-word itself is found in the 
	 * words array, it sets the isInDictionary to true.
	 * If it finds any anagram of the clue, it sets the hasAnagram to true. 
	 * It loads the anagram into the anagramArray. 
	 * If no anagrams found, then anagramArray remains an array with size 0. 
	 * */
	void findAnagrams(String clue) {
		//write your code here
		isInDictionary = false;
		hasAnagrams = false;
		StringBuilder anagrams = new StringBuilder();

		for (String word : words) {
			if (clue.length() == word.length()) {
				char[] clueChars = clue.toCharArray();
				char[] wordChars = word.toCharArray();

				java.util.Arrays.sort(clueChars);
				java.util.Arrays.sort(wordChars);

				if (java.util.Arrays.equals(clueChars, wordChars)) {
					if (clue.equals(word)) {
						isInDictionary = true;
					} else {
						hasAnagrams = true;
						anagrams.append(word).append("\n");
					}
				}
			}
		}

		anagramArray = anagrams.length() > 0 ? anagrams.toString().split("\n") : new String[0];
	}

}
