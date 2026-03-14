package server.commands;

import common.CommandType;
import common.Vehicle;
import server.collection.VehicleManager;
import common.ReturnCode;

import java.util.List;

public class CompareByEnginePowerCommand implements Command{
    private final VehicleManager vehicleManager;
    private final CommandType type = CommandType.WITHARGS;


    public CompareByEnginePowerCommand(VehicleManager vehicleCollection) {
        this.vehicleManager = vehicleCollection;
    }

    @Override
    public ReturnCode execute(List<String> parameter, Vehicle vehicle, Boolean isLaud) {
        if (parameter.size() != 2) {
            return ReturnCode.FAILED;
        }
        try {
            Float number = Float.parseFloat(String.valueOf(parameter.get(1)));
            vehicleManager.filterByEnginePower(number);
            return ReturnCode.OK;
            } catch (IllegalArgumentException e) {
                if(isLaud) System.out.println("Ошибка: неверный тип! Введите число");
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
