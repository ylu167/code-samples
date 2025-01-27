package finals;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficResource {
    Queue<Vehicle> vehicleQ = new PriorityQueue<>();
    Lock trafficLock = new ReentrantLock();
    Condition vehicleAvailableInQueue = trafficLock.newCondition();
    Condition vehicleCanGo = trafficLock.newCondition();
    int maxQLength;
    boolean isGreen = true;  // starts with Green

    int totalVehiclesPassed = 0;
    int emergencyVehiclesPassed = 0;
    int totalConsumerLoopExecution = 0;

    // Called by producer (Traffic)
    boolean joinVehicle(Vehicle vehicle) {
        trafficLock.lock();
        try {
            vehicleQ.add(vehicle);
            if (vehicleQ.size() > maxQLength) {
                maxQLength = vehicleQ.size();
            }

            // Print only when light is non-green (RED). Producer prints with a tab.
            if (!isGreen) {
                System.out.printf("\tRED: %s %d in Q. Q length %d%n",
                        vehicle.getClass().getSimpleName(), vehicle.id, vehicleQ.size());
            }

            // Signal that a vehicle is available
            vehicleAvailableInQueue.signal();

            // If it's a red light and we just added an emergency vehicle,
            // the consumer should be able to proceed immediately.
            if (!isGreen && vehicle.priority == 1) {
                vehicleCanGo.signalAll();
            }

        } finally {
            trafficLock.unlock();
        }
        return true;
    }

    // Called by consumer (Road)
    void releaseVehicle(int maxVehicles) {
        trafficLock.lock();
        try {
            // Wait until there's a vehicle or all are created
            while (vehicleQ.isEmpty() && Vehicle.vehicleCount < maxVehicles) {
                vehicleAvailableInQueue.await();
            }

            if (vehicleQ.isEmpty() && Vehicle.vehicleCount >= maxVehicles) {
                // No more vehicles to process
                return;
            }

            // Wait until a vehicle can go (green light or emergency vehicle on red)
            while (!canVehicleGo()) {
                vehicleCanGo.await();
            }

            totalConsumerLoopExecution++;

            Vehicle v = vehicleQ.poll();
            if (v != null) {
                totalVehiclesPassed++;
                if (v.priority == 1) { // emergency
                    emergencyVehiclesPassed++;
                }

                // Consumer prints with no tab.
                // If emergency vehicle passes on red, print "RED: ..." accordingly.
                if (v.priority == 1) {
                    System.out.printf("%s %s %d passed. Q length: %d%n",
                            isGreen ? "Green:" : "RED:", v.getClass().getSimpleName(), v.id, vehicleQ.size());
                } else {
                    // Regular Vehicle
                    System.out.printf("Green: %s %d passed. Q length: %d%n",
                            v.getClass().getSimpleName(), v.id, vehicleQ.size());
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            trafficLock.unlock();
        }
    }

    private boolean canVehicleGo() {
        if (vehicleQ.isEmpty()) {
            return false;
        }
        Vehicle front = vehicleQ.peek();
        if (isGreen) return true;
        return front.priority == 1; // red light allows only emergency
    }

    // Called by TrafficLight
    void changeLight() {
        trafficLock.lock();
        try {
            isGreen = !isGreen;
            vehicleCanGo.signalAll();
        } finally {
            trafficLock.unlock();
        }
    }
}
