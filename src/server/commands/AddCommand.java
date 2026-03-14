package server.commands;

import common.CommandType;
import common.Vehicle;
import server.collection.VehicleAdder;
import common.ReturnCode;

import java.util.List;

public class AddCommand implements Command {
    private final CommandType type = CommandType.WITHMODEL;
    private final VehicleAdder vehicle;

    public AddCommand(VehicleAdder vehicle) {
        this.vehicle = vehicle;
    }


    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 1) {
            if (isLaud) System.out.println("Add required 1 args, got = " + args.size());
            return ReturnCode.FAILED;
        }
        try {
            this.vehicle.addElement(vehicle,isLaud);
            return ReturnCode.OK;
        } catch (IllegalArgumentException e){
            System.out.println("произошла ошибка, в параметры команды add были введены не валидные данные");
            return ReturnCode.FAILED;
        }
    }

    @Override
    public String getDescription() {
        return "добавить элемент в колллекцию";
    }

    @Override
    public CommandType getType(){
        return this.type;
    }
}
