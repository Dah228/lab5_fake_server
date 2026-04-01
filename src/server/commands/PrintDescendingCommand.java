package server.commands;

import common.CommandType;
import common.Vehicle;
import server.CommandParams;
import server.collection.VehicleRandom;
import common.ReturnCode;

import java.util.ArrayList;

import static server.VehicleFormatter.printVehicleList;

public class PrintDescendingCommand implements Command{
    private final VehicleRandom vehicleRandom;
    private final CommandType type = CommandType.NOARGS;

    public PrintDescendingCommand(VehicleRandom vehicleRandom){
        this.vehicleRandom = vehicleRandom;
    }

    @Override
    public ReturnCode execute(CommandParams params){
        if (params.args().size() != 1) return ReturnCode.FAILED;
        else{
            ArrayList<Vehicle> veh = vehicleRandom.sortByIDDescending();
            if (params.isLaud()) printVehicleList(veh, params.responseSender());
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
