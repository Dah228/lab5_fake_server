package server.collection;

import common.Coordinates;
import common.FuelType;
import common.Vehicle;
import common.VehicleType;

import java.util.List;

public class ValidateParams {

    private final List<String> args;

    public ValidateParams(List<String> args) {
        if (args == null || args.isEmpty()) {
            throw new IllegalArgumentException("Нет аргументов");
        }
        this.args = args;
    }

    public GroupingField getGroupingField() {
        String first = args.get(1).trim();

        // 1. VehicleType
        if (isVehicleType(first)) {
            VehicleType type = VehicleType.valueOf(first.toUpperCase());
            return new GroupingField("type", Vehicle::getType, type);
        }

        // 2. FuelType
        if (isFuelType(first)) {
            FuelType fuel = FuelType.valueOf(first.toUpperCase());
            return new GroupingField("fueltype", Vehicle::getFuelType, fuel);
        }

        // 3. Координаты
        if (args.size() == 3 && isCoordinates(args)) {
            int x = Integer.parseInt(args.get(1).trim());
            float y = Float.parseFloat(args.get(2).trim());
            Coordinates coords = new Coordinates();
            coords.setCoord(x,y);
            return new GroupingField("coordinates", Vehicle::getCoordinates, coords);
        }

        throw new IllegalArgumentException("Не распознано: " + first);
    }


    private boolean isVehicleType(String value) {
        try {
            VehicleType.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private boolean isFuelType(String value) {
        try {
            FuelType.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isCoordinates(List<String> args) {
        if (args.size() < 3) return false;
        try {
            Integer.parseInt(args.get(1).trim());
            Float.parseFloat(args.get(2).trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}