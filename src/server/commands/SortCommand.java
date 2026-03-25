package server.commands;

import common.CommandType;
import common.Vehicle;
import server.CommandParams;
import server.collection.VehicleRandom;
import common.ReturnCode;


import java.util.List;

import static server.VehicleFormatter.printVehicleList;

public class SortCommand implements Command{
    private final VehicleRandom vehicleRandom;
    private final CommandType type = CommandType.NOARGS;

    public SortCommand(VehicleRandom vehicleRandom){
        this.vehicleRandom = vehicleRandom;
    }

    @Override
    public ReturnCode execute(CommandParams params) {
        if (params.args().size() != 1) return ReturnCode.FAILED;
        else {
            printVehicleList(vehicleRandom.sortByID(), params.responseSender());
            return ReturnCode.OK;
        }
    }

    @Override
    public String getDescription(){
        return " отсортировать коллекцию в естественном порядке";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }


}
