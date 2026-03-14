package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.ReturnCode;
import common.Vehicle;
import server.collection.VehicleManager;

import java.util.List;

public class ClearCommand implements Command {
    private final VehicleManager manager;
    private final CommandType type = CommandType.NOARGS;
    private final ResponseSender responseSender;



    public ClearCommand(VehicleManager manager, ResponseSender responseSender) {
        this.manager = manager;
        this.responseSender = responseSender;
    }

    @Override
    public ReturnCode execute(List<String> param, Vehicle vehicle, Boolean isLaud) {
        if (param.size() != 1) return ReturnCode.FAILED;
        manager.clearCollection();
        if (isLaud) responseSender.send("Коллекция очищена");
        return ReturnCode.OK;
    }


    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }


}
