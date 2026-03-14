package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.Vehicle;
import server.collection.VehicleManager;
import common.ReturnCode;

import java.util.List;

public class InfoCommand implements Command{
    private final VehicleManager vehicleCollection;
    private final CommandType type = CommandType.NOARGS;
    private final ResponseSender responseSender;



    public InfoCommand(VehicleManager vehicleCollection, ResponseSender responseSender) {
        this.vehicleCollection = vehicleCollection;
        this.responseSender = responseSender;
    }

    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 1) return ReturnCode.FAILED;
        else {
            vehicleCollection.getInfo();
            return ReturnCode.OK;
        }
    }

    @Override
    public String getDescription() {
        return " вывести в стандартный поток информацию о коллекции";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }

}
