package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.Vehicle;
import server.CommandParams;
import server.collection.VehicleAdder;
import common.ReturnCode;

import java.util.List;
//import server.collection.VehicleComaperator;

public class AddIfMax implements Command{
    VehicleAdder vehicleAdder;
    private final CommandType type = CommandType.WITHMODEL;


    public AddIfMax(VehicleAdder vehicleComaperator){
        this.vehicleAdder = vehicleComaperator;
    }


    @Override
    public ReturnCode execute(CommandParams params){
        if (params.args().size() != 1){
            return ReturnCode.FAILED;
        }

        try {
            vehicleAdder.addIfMax(params.vehicle());
            if (params.isLaud()) params.responseSender().send("У элемента максимальное значение пройденной дистанции. Добавлен.");
            return ReturnCode.OK;
        } catch (IllegalArgumentException e) {
            params.responseSender().send("Ошибка: неверный тип! Введите число");
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

