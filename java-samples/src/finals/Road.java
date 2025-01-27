package finals;

public class Road implements Runnable {
    static int maxVehicles;
    TrafficResource resource = new TrafficResource();
    long startTime;
    long endTime;

    Thread trafficThread; // producer
    Thread lightThread;   // traffic light
    Thread roadThread;    // consumer (this)

    // counters
    int totalVehiclesPassed = 0;
    int emergencyVehiclesPassed = 0;
    int totalConsumerLoopExecution = 0;

    public static void main(String[] args) {
        Road road = new Road();
        road.startRoad();
        road.printReport();
        road.checkAssertions();
    }

    void startRoad() {
        try {
            System.out.println("How many vehicles?");
            java.util.Scanner sc = new java.util.Scanner(System.in);
            maxVehicles = sc.nextInt();
            sc.close();
        } catch (Exception e) {
            maxVehicles = 0; // default if no input
        }

        // If maxVehicles is 0, we still run briefly
        startTime = System.currentTimeMillis();

        trafficThread = new Thread(new Traffic(resource, maxVehicles));
        lightThread = new Thread(new TrafficLight(resource));
        roadThread = new Thread(this);

        // start threads
        trafficThread.start();
        lightThread.start();
        roadThread.start();

        // Wait for threads to finish
        try {
            trafficThread.join();
            lightThread.join();
            roadThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        endTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (Vehicle.vehicleCount < maxVehicles || !resource.vehicleQ.isEmpty()) {
            resource.releaseVehicle(maxVehicles);
        }

        // store final counters for reporting
        totalVehiclesPassed = resource.totalVehiclesPassed;
        emergencyVehiclesPassed = resource.emergencyVehiclesPassed;
        totalConsumerLoopExecution = resource.totalConsumerLoopExecution;
    }

    void printReport() {
        System.out.println("-----------TRAFFIC REPORT---------------------------");
        System.out.printf("The program ran for %d ms%n", (endTime - startTime <= 0 ? 1 : endTime - startTime));
        System.out.printf("Max Q length at traffic light was %d%n", resource.maxQLength);
        System.out.printf("Final Q length at traffic light was %d%n", resource.vehicleQ.size());
        System.out.printf("Total Vehicles passed: %d%n", totalVehiclesPassed);
        System.out.printf("Emergency Vehicles passed: %d%n", emergencyVehiclesPassed);
        System.out.printf("Number of consumer thread loop execution: %d%n", totalConsumerLoopExecution);
    }

    void checkAssertions() {
        // We assume simple checks instead of a testing framework:
        if (maxVehicles != (totalVehiclesPassed + resource.vehicleQ.size())) {
            throw new AssertionError("Assertion failed: maxVehicles != totalVehiclesPassed + final Q size");
        }

        if (!(resource.maxQLength >= resource.vehicleQ.size())) {
            throw new AssertionError("Assertion failed: maxQLength < final Q size");
        }

        if (!(Vehicle.vehicleCount == maxVehicles)) {
            throw new AssertionError("Assertion failed: Vehicle.vehicleCount != maxVehicles");
        }

        if (!(totalConsumerLoopExecution == totalVehiclesPassed)) {
            throw new AssertionError("Assertion failed: totalConsumerLoopExecution != totalVehiclesPassed");
        }
    }
}
