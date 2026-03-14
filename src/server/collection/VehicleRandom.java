package server.collection;

import common.Vehicle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VehicleRandom {
    private final VehicleCollection collection;
    public VehicleRandom(VehicleCollection collection) {
        this.collection = collection;
    }

    public void sortByID(){
        List<Vehicle> vehicles = collection.getVehicles();
        vehicles.sort(Comparator.comparingLong(Vehicle::getId));
        for(Vehicle v : vehicles){
            Vehicle.printVehicle(v);
        }
    }

    public void sortByIDDescending(){
        List<Vehicle> vehicles = collection.getVehicles();
        vehicles.sort(Comparator.comparingLong(Vehicle::getId).reversed());
        for(Vehicle v : vehicles){
            Vehicle.printVehicle(v);
        }
    }

    public void shuffle(){
        List<Vehicle> vehicles = collection.getVehicles();
        Collections.shuffle(vehicles);
        for(Vehicle v : vehicles){
            Vehicle.printVehicle(v);
        }
    }



}
