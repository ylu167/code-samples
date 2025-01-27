package lab7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/** GrammyAnalyst takes Grammys.txt to provide two reports and one search functionality
 */
public class GrammyAnalyst {
	/**initialize these member variables with appropriate data structures **/
	List<Nomination> nominations;  
	Map<String, List<Nomination>> grammyMap;  
	List<Artist> artists;
	
	public static void main(String[] args) {
		GrammyAnalyst ga = new GrammyAnalyst();
		ga.loadNominations();
		ga.loadGrammyMap();
		System.out.println("*********** Grammy Report ****************");
		ga.printGrammyReport();
		System.out.println("*********** Search Artist ****************");
		System.out.println("Enter artist name");
		Scanner input = new Scanner(System.in);
		String artist = input.nextLine();
		ga.searchGrammys(artist);
		ga.loadArtists();
		System.out.println("*********** Artists Report ****************");
		ga.printArtistsReport();
		input.close();
	}
	
	/**loadNominations() reads data from Grammys.txt and 
	 * populates the nominations list, where each element is a Nomination object
	 */
	void loadNominations() {
		//write your code here
		nominations = new ArrayList<Nomination>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Grammys.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				// Skip empty lines
				if (line.trim().isEmpty()) {
					continue;
				}
				String[] tokens = line.split(";");
				if (tokens.length >= 3) {
					String category = tokens[0].trim();
					String title = tokens[1].trim();
					String artist = tokens[2].trim();
					Nomination nomination = new Nomination(category, title, artist);
					nominations.add(nomination);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**loadGrammyMap uses artist name in lower case as the key, and a list of 
	 * all nominations for that artist as its value. Hint: use 'nominations' list 
	 * created in previous method to populate this map.
	 */
	void loadGrammyMap() {
		//write your code here
		grammyMap = new HashMap<String, List<Nomination>>();
		for (Nomination nomination : nominations) {
			List<Nomination> nomList = grammyMap.get(nomination.artist.toLowerCase());
			if (nomList == null) {
				nomList = new ArrayList<Nomination>();
				grammyMap.put(nomination.artist.toLowerCase(), nomList);
			}
			nomList.add(nomination);
		}
	} 
	
	/**loadArtists loads the artists array List. Each Artist object in it should have
	 * artist's name in proper case, i.e., as read from data file, and 
	 * a list of nominations for that artist. Hint: use 'grammyMap' created in 
	 * previous method to populate this list
	 */
	void loadArtists() {
		//write your code here
		artists = new ArrayList<Artist>();
		for (Map.Entry<String, List<Nomination>> entry : grammyMap.entrySet()) {
			List<Nomination> nominationsList = entry.getValue();
			Artist artist = new Artist(entry.getKey(), nominationsList);
			artists.add(artist);
		}
	}
	
	/**printGrammyReport prints report as shown in the handout */
	void printGrammyReport() {
		//write your code here
		Collections.sort(nominations, new Comparator<Nomination>() {
			@Override
			public int compare(Nomination n1, Nomination n2) {
				return n1.artist.compareTo(n2.artist);
			}
		});
		for (Nomination nomination : nominations) {
			System.out.println(nomination);
		}
	}
	
	/**printArtistReport prints report as shown in the handout */
	void printArtistsReport() {
		//write your code here
		Collections.sort(artists);
		for (Artist artist : artists) {
			System.out.println(artist);
		}
	}
	
	/**searchGrammys takes a string as input and makes a case-insensitive
	 * search on grammyMap. If found, it prints data about all nominations
	 * as shown in the handout.
	 */
	void searchGrammys(String artist) {
		//write your code here
		boolean found = false;
		for (Map.Entry<String, List<Nomination>> entry : grammyMap.entrySet()) {
			if (entry.getKey().toLowerCase().contains(artist.toLowerCase())) {
				found = true;
				List<Nomination> nomList = entry.getValue();
				for (Nomination nomination : nomList) {
					System.out.println(nomination.artist + ": " + nomination.category + ": " + nomination.title);
				}
			}
		}
		if (!found) {
			System.out.println(artist + " not found!");
		}
	}
}
