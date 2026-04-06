package server.collection;

import common.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VehicleRandom {
    private final VehicleCollection collection;

    public VehicleRandom(VehicleCollection collection) {
        this.collection = collection;
    }

    public ArrayList<Vehicle> sortByID() {
        ArrayList<Vehicle> vehicles = collection.getVehicles();
        vehicles.sort(Comparator.comparingLong(Vehicle::getId));
        return vehicles;
    }

    public ArrayList<Vehicle> sortByIDDescending() {
        ArrayList<Vehicle> vehicles = collection.getVehicles();
        vehicles.sort(Comparator.comparingLong(Vehicle::getId).reversed());
        return vehicles;
    }

    public ArrayList<Vehicle> shuffle() {
        ArrayList<Vehicle> vehicles = collection.getVehicles();
        Collections.shuffle(vehicles);
        return vehicles;
    }


}
