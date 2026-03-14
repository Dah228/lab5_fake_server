package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.Vehicle;
import server.collection.VehicleRandom;
import common.ReturnCode;

import java.util.List;

public class PrintDescendingCommand implements Command{
    private final VehicleRandom vehicleRandom;
    private final CommandType type = CommandType.NOARGS;

    public PrintDescendingCommand(VehicleRandom vehicleRandom){
        this.vehicleRandom = vehicleRandom;
    }

    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud){
        if (args.size() != 1) return ReturnCode.FAILED;
        else{
            vehicleRandom.sortByIDDescending();
            return ReturnCode.OK;
        }

    }

    @Override
    public String getDescription(){
        return "вывести элементы коллекции в порядке убывания";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }
}
