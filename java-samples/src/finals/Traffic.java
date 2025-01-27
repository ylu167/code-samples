package finals;

import java.util.Random;

public class Traffic implements Runnable {
    static final int MIN_VEHICLE_DELAY = 5;
    static final int MAX_VEHICLE_DELAY = 10;
    TrafficResource resource;
    int maxVehicles;
    Random rand = new Random();

    public Traffic(TrafficResource resource, int maxVehicles) {
        this.resource = resource;
        this.maxVehicles = maxVehicles;
    }

    @Override
    public void run() {
        while (Vehicle.vehicleCount < maxVehicles) {
            // 75% chance for regular vehicle, 25% for emergency
            boolean isEmergency = rand.nextInt(100) < 25;
            Vehicle v = isEmergency ? new EmergencyVehicle() : new Vehicle(2);
            resource.joinVehicle(v);

            // Sleep random interval
            int delay = MIN_VEHICLE_DELAY + rand.nextInt(MAX_VEHICLE_DELAY - MIN_VEHICLE_DELAY + 1);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
