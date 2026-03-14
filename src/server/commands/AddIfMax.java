package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.Vehicle;
import server.collection.VehicleAdder;
import common.ReturnCode;

import java.util.List;
//import server.collection.VehicleComaperator;

public class AddIfMax implements Command{
    VehicleAdder vehicleAdder;
    private final CommandType type = CommandType.WITHMODEL;
    private final ResponseSender responseSender;


    public AddIfMax(VehicleAdder vehicleComaperator, ResponseSender responseSender){
        this.vehicleAdder = vehicleComaperator;
        this.responseSender = responseSender;
    }


    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud){
        if (args.size() != 1){
            return ReturnCode.FAILED;
        }

        try {
            vehicleAdder.addIfMax(vehicle, isLaud);
            return ReturnCode.OK;
        } catch (IllegalArgumentException e) {
            responseSender.send("Ошибка: неверный тип! Введите число");
            return ReturnCode.FAILED;
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }

    @Override
    public CommandType getType(){
        return this.type;
    }

}

