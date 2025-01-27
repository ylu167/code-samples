package exam3;

import java.util.*;

public class MemberMapper {

	String[] members = {"Alice", "Bob", "Charles", "David", "Frank"};
	String[][] memberFriends = 
		{{"Bob", "Joe", "Sara", "Amy", "Frank", "Nancy"}, 				//Alice's friends
				{"Alice", "Nancy", "Peter", "Steve", "Frank", "Tim", "Amy"}, 	//Bob's friends
				{"Sara", "Kevin", "Peter", "Steve"}, 				//Charles friends
				{"Steve", "Amy"}, 						//David's friends
				{"Alice", "Bob", "Mary"}};					//Frank's friends

	public static void main(String[] args) {
		// write your code here
		MemberMapper mm = new MemberMapper();

		List<Member> memberList = mm.setupMemberList();

		mm.getMemberPopularityList(memberList);
		mm.printPopularityList(memberList);

		Map<String, List<String>> memberMap = mm.setupMemberMap(memberList);

		System.out.println();
		System.out.println("------ Member Map ------");
		mm.printMap(memberMap);

		Map<String, List<String>> reversedMap = mm.setupReversedMap(memberList, memberMap);

		System.out.println();
		System.out.println("------ Reversed Map ------");
		mm.printMap(reversedMap);

		System.out.println();
		System.out.println("----- Find Common Friends -----");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter first name");
		String name1 = scanner.nextLine();
		System.out.println("Enter second name");
		String name2 = scanner.nextLine();

		List<String> commonFriends = mm.findCommonFriends(memberMap, reversedMap, name1, name2);

		if (commonFriends.isEmpty()) {
			System.out.println("Sorry! No common friends found!");
		} else {
			System.out.println("The common friends are: ");
			for (String friend : commonFriends) {
				System.out.println(friend);
			}
		}

		System.out.println();
		System.out.println("------ Thank you! ------");
	}

	void printPopularityList(List<Member> memberList) {
		// write your code here
		System.out.println("------ Member Popularity ------");
		for (Member member : memberList) {
			System.out.println("Member: " + member.name + " has " + member.friends.size() + " friends");
		}
	}

	void printMap(Map<String, List<String>> map) {
		// write your code here
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			System.out.print(key + ": ");
			for (String friend : value) {
				System.out.print(friend + " ");
			}
			System.out.println();
		}
	}

	/** setupMemberList() uses the data in the two arrays - members and memberFriends - 
	 * given above to create a memberList that has Member objects. 
	 * Each member object has a name and a list of friends
	 * In terms of indexing, member[i]'s friends are in memberFriends[i] array. 
	 */
	List<Member> setupMemberList() {
		//write your code here
		List<Member> memberList = new ArrayList<>();

		for (int i = 0; i < members.length; i++) {
			String memberName = members[i];
			Member member = new Member(memberName);
			memberList.add(member);
		}

		for (int i = 0; i < memberFriends.length; i++) {
			String[] friendsArray = memberFriends[i];
			Member member = memberList.get(i);
			List<String> friendsList = new ArrayList<>();
			for (String friendName : friendsArray) {
				friendsList.add(friendName);
			}
			member.friends = friendsList;
		}

		return memberList;
	}

	/**getMemberPopularityList() sorts the membersList in the order of 
	 * member's number of friends in descending order 
	 */
	List<Member> getMemberPopularityList(List<Member> memberList) {
		//write your code here
		Collections.sort(memberList);
		return memberList;
	}

	/**setupMemberMap() takes memberList as a parameter
	 * and converts it into a map. It returns the map.
	 * The map has member's name as the Key and the list of his/her friends's names as the value.
	 * There is no case-conversion, i.e. 'Alice' is stored as a key and not 'alice'. 
	 * Note that the keys as well as the friends' names list should be
	 * alphabetically sorted
	 */
	Map<String, List<String>> setupMemberMap(List<Member> memberList) {
		//write your code here
		Map<String, List<String>> memberMap = new TreeMap<>();
		for (Member member : memberList) {
			List<String> friendsList = new ArrayList<>(member.friends);
			Collections.sort(friendsList);
			memberMap.put(member.name, friendsList);
		}
		return memberMap;
	}

	/**setReversedMap() uses the memberList and memberMap to create a reversedMap
	 * You may or may not use both memberList and memberMap depending on your approach.
	 * The method should return reversed map with non-member names as the keys 
	 * and the list of their member-friends as their values.
	 * Note that the keys as well as the friends' names list should be
	 * alphabetically sorted
	 */
	Map<String, List<String>> setupReversedMap(List<Member> memberList, Map<String, List<String>> memberMap) {
		//write your code here
		Set<String> memberNames = memberMap.keySet();
		Map<String, List<String>> reversedMap = new TreeMap<>();

		for (Member member : memberList) {
			for (String friend : member.friends) {
				if (!memberNames.contains(friend)) {
					List<String> membersForFriend = reversedMap.get(friend);
					if (membersForFriend == null) {
						membersForFriend = new ArrayList<>();
						reversedMap.put(friend, membersForFriend);
					}
					membersForFriend.add(member.name);
				}
			}
		}

		// Sort the lists in reversedMap
		for (Map.Entry<String, List<String>> entry : reversedMap.entrySet()) {
			List<String> membersForFriend = entry.getValue();
			Collections.sort(membersForFriend);
		}

		return reversedMap;
	}

	/**findCommonFriends() takes two names: name1 and name2, and finds common friends, if any, using the two maps - memberMap and reversedMap. 
	 * Depending on your approach, you may or may not use both maps to find common friends. 
	 * The method returns a list of common friends, if found. Else it returns an empty list. 
	 */
	List<String> findCommonFriends(Map<String, List<String>> memberMap, Map<String, List<String>> reversedMap, String name1, String name2) {
		//write your code here
		List<String> list1 = null;
		List<String> list2 = null;

		if (memberMap.containsKey(name1)) {
			list1 = memberMap.get(name1);
		} else if (reversedMap.containsKey(name1)) {
			list1 = reversedMap.get(name1);
		}

		if (memberMap.containsKey(name2)) {
			list2 = memberMap.get(name2);
		} else if (reversedMap.containsKey(name2)) {
			list2 = reversedMap.get(name2);
		}

		if (list1 == null || list2 == null) {
			return new ArrayList<>();
		}

		Set<String> set1 = new HashSet<>(list1);
		set1.retainAll(list2);

		List<String> commonFriends = new ArrayList<>(set1);
		Collections.sort(commonFriends);
		return commonFriends;
	}
}
