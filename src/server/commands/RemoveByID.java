package server.commands;

import common.CommandType;
import server.CommandParams;
import server.collection.VehicleAdder;
import common.ReturnCode;

public class RemoveByID implements Command{
    VehicleAdder vehicleManager;
    private final CommandType type = CommandType.WITHARGS;


    public RemoveByID(VehicleAdder vehicleManager){
        this.vehicleManager = vehicleManager;
    }

    @Override
    public ReturnCode execute(CommandParams params) {
        if(params.args().size() != 2) return ReturnCode.FAILED;
        try {
            long number = Long.parseLong(params.args().get(1));
            if (vehicleManager.rmByID(number)) params.responseSender().send("Успешно удален");
            return ReturnCode.OK;
        } catch (IllegalArgumentException e) {
            if(params.isLaud()) params.responseSender().send("Ошибка: неверный тип! Введите число");
            return ReturnCode.FAILED;
        }
    }


    @Override
    public String getDescription(){
        return " удалить элемент из коллекции по его id";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }
}
