package server.commands;


import common.CommandType;
import common.Vehicle;
import server.collection.VehicleManager;
import common.ReturnCode;
import common.VehicleType;

import java.util.List;

public class FilterLessThatType implements Command{

    private final CommandType type = CommandType.WITHARGS;
    VehicleManager vehicleManager;
    public FilterLessThatType(VehicleManager vehicleCollection){
        this.vehicleManager = vehicleCollection;
    }

    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 2) return ReturnCode.FAILED;
        try {
            VehicleType type = VehicleType.valueOf(args.get(1).toUpperCase());
            vehicleManager.filterLessThanType(type);
            return ReturnCode.OK;
        } catch (IllegalArgumentException e) {
            if(isLaud) System.out.println("Ошибка: неверный тип! Доступные: PLANE, HELICOPTER, BOAT, SHIP, HOVERBOARD");
            return ReturnCode.FAILED;
        }

    }

    @Override
    public String getDescription() {
        return "вывести элементы, значение поля enginePower которых больше заданного";
    }


    @Override
    public CommandType getType() {
        return this.type;
    }


}
