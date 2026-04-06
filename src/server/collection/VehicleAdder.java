package server.collection;

import common.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VehicleAdder {
    private final VehicleCollection collection;

    public VehicleAdder(VehicleCollection collection) {
        this.collection = collection;
    }

    public boolean updateElementByID(long id, Vehicle vehicle) {
        if (collection.getVehicleByID(id) != null) {
            vehicle.setId(id);
            collection.replaceVehicle(id, vehicle);
            return true;
        }
        return false;  // ← не найдено
    }

    public boolean rmByID(long id) {
        List<Long> ids = collection.getAllID();
        for (Long i : ids) {
            if (i.equals(id)) {
                collection.rmEl(collection.getVehicleByID(id));
                return true;
            }
        }
        return false;
    }

    public void addElement(Vehicle vehicle) {
        collection.add(vehicle);
    }

    public boolean addIfMax(Vehicle veh) {
        if (collection.getVehicles().stream().allMatch(v ->
                v.getDistanceTravelled() < veh.getDistanceTravelled())) {
            collection.add(veh);
            return true;
        }
        return false;
    }

    // ← groupByParam возвращает результат, а не печатает
    public Map<Comparable<?>, Long> groupByParam(List<String> args) {
        ValidateParams validator = new ValidateParams(args);
        GroupingField field = validator.getGroupingField();

        return collection.getVehicles().stream()
                .collect(Collectors.groupingBy(
                        field.extractor(),
                        Collectors.counting()
                ));
    }

}