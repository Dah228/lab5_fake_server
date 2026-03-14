package server.collection;


import common.Vehicle;
import common.ResponseSender;

import java.util.List;

import static java.util.Collections.max;

public class VehicleAdder {
    private final VehicleCollection collection;
    private final ResponseSender responseSender;

    public VehicleAdder(VehicleCollection collection, ResponseSender responseSender) {
        this.responseSender = responseSender;
        this.collection = collection;

    }


    public void updateElementByID(long id, Vehicle vehicle, Boolean isLaud) {
        if (collection.getVehicleByID(id) != null) {
            vehicle.setId(id);
            collection.replaceVehicle(id,vehicle);
            if (isLaud) responseSender.send("Элемент обновлен");
        }
    }


    public void rmByID(long id, Boolean isLaud) {
        List arr = collection.getAllID();
        for (Object i : arr) {
            if ((long) i == id) {
                collection.rmEl(collection.getVehicleByID(id));
            }
        }
        if (isLaud) responseSender.send("Элемент удален");

    }




    public void addElement(Vehicle vehicle, Boolean isLaud) {
        collection.add(vehicle);
        if (isLaud) responseSender.send("Элемент добавлен");
    }


    public void addIfMax(Vehicle veh, Boolean isLaud) {
        if (collection.getVehicles().stream().allMatch(v -> v.getDistanceTravelled() < veh.getDistanceTravelled())) {
            collection.add(veh);
            if (isLaud) responseSender.send("Элемент добавлен");
        } else {
            if (isLaud)
                responseSender.send("Не добавлено: в коллекции есть элементы с distanceTravelled >= " + veh.getDistanceTravelled());
        }

    }


}
