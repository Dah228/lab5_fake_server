package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.Vehicle;
import server.collection.VehicleAdder;
import common.ReturnCode;

import java.util.List;

public class UpdateElementID implements Command {
    VehicleAdder vehicleManager;
    private final CommandType type = CommandType.WITHARGSMODEL;
    private final ResponseSender responseSender;



    public UpdateElementID(VehicleAdder vehicleManager, ResponseSender responseSender) {
        this.vehicleManager = vehicleManager;
        this.responseSender = responseSender;
    }

    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 2){
            return ReturnCode.FAILED;
        }
        try {
            long identifier = Long.parseLong(args.get(1));
            vehicleManager.updateElementByID(identifier, vehicle, isLaud);
            return ReturnCode.OK;
        } catch (IllegalArgumentException e) {
            responseSender.send("Ошибка: неверный тип! Введите число");
            return ReturnCode.FAILED;
        }
    }

    public String getDescription(){
        return " обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }

}
