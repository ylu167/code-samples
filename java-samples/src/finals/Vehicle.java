package finals;

public class Vehicle implements Comparable<Vehicle> {
    public static volatile int vehicleCount = 0;
    public final int id;
    public final int priority; // 1 for emergency, 2 for regular

    public Vehicle(int priority) {
        this.priority = priority;
        this.id = ++vehicleCount;
    }

    @Override
    public int compareTo(Vehicle other) {
        // Emergency vehicles (priority=1) have higher priority than regular (priority=2)
        // If same priority, lower id (earlier created) comes first
        if (this.priority != other.priority) {
            return this.priority - other.priority;
        } else {
            return this.id - other.id;
        }
    }
}
