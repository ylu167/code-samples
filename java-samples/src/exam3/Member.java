package exam3;

import java.util.ArrayList;
import java.util.List;

public class Member implements Comparable<Member> {
    String name;
    List<String> friends;

    public Member(String name) {
        this.name = name;
        this.friends = new ArrayList<>();
    }

    @Override
    public int compareTo(Member other) {
        return Integer.compare(other.friends.size(), this.friends.size());
    }
}
