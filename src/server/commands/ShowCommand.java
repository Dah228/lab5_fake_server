package server.commands;

import common.CommandType;
import common.Vehicle;
import server.collection.VehicleManager;
import common.ReturnCode;

import java.util.List;

public class ShowCommand implements Command {
    private final VehicleManager manager;
    private final CommandType type = CommandType.NOARGS;

    public ShowCommand(VehicleManager manager) {
        this.manager = manager;
    }

    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 1) return ReturnCode.FAILED;
        else {
            manager.showCollection();
            return ReturnCode.OK;
        }
    }

    @Override
    public String getDescription() {
        return " вывести все элементы";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }
}