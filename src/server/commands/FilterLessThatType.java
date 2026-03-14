package server.commands;


import common.*;
import server.collection.VehicleManager;

import java.util.List;

public class FilterLessThatType implements Command{

    private final CommandType type = CommandType.WITHARGS;
    VehicleManager vehicleManager;
    private final ResponseSender responseSender;

    public FilterLessThatType(VehicleManager vehicleCollection, ResponseSender responseSender){
        this.vehicleManager = vehicleCollection;
        this.responseSender = responseSender;
    }

    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 2) return ReturnCode.FAILED;
        try {
            VehicleType type = VehicleType.valueOf(args.get(1).toUpperCase());
            vehicleManager.filterLessThanType(type);
            return ReturnCode.OK;
        } catch (IllegalArgumentException e) {
            if(isLaud) responseSender.send("Ошибка: неверный тип! Доступные: PLANE, HELICOPTER, BOAT, SHIP, HOVERBOARD");
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
