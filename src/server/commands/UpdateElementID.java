package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.Vehicle;
import server.CommandParams;
import server.collection.VehicleAdder;
import common.ReturnCode;

import java.util.List;

public class UpdateElementID implements Command {
    VehicleAdder vehicleManager;
    private final CommandType type = CommandType.WITHARGSMODEL;



    public UpdateElementID(VehicleAdder vehicleManager) {
        this.vehicleManager = vehicleManager;
    }

    @Override
    public ReturnCode execute(CommandParams params) {
        if (params.args().size() != 2){
            return ReturnCode.FAILED;
        }
        try {
            long identifier = Long.parseLong(params.args().get(1));
            if (vehicleManager.updateElementByID(identifier, params.vehicle())) params.responseSender().send("Элемент успешно обновлен");
            return ReturnCode.OK;
        } catch (IllegalArgumentException e) {
            params.responseSender().send("Ошибка: неверный тип! Введите число");
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
