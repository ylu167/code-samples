package finals;

public class TrafficLight implements Runnable {
    static final int TRAFFIC_LIGHT_DELAY = 100;
    TrafficResource resource;

    public TrafficLight(TrafficResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (Vehicle.vehicleCount < Road.maxVehicles) {
            try {
                Thread.sleep(TRAFFIC_LIGHT_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            resource.changeLight();
        }
    }
}
