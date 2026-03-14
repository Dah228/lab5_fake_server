package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.ReturnCode;
import common.Vehicle;
import server.collection.GroupingField;
import server.collection.ValidateParams;
import server.collection.VehicleAdder;
import server.collection.VehicleManager;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GroupByCommand implements Command{
    private final VehicleAdder vehicleAdder;
    private final CommandType type = CommandType.DETECTPARAM;
    private final ResponseSender responseSender;
    public GroupByCommand(VehicleAdder vehicleAdder, ResponseSender responseSender){
        this.vehicleAdder = vehicleAdder;
        this.responseSender = responseSender;
    }
    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        try {
            vehicleAdder.groupByParam(args,responseSender);
            return ReturnCode.OK;
        } catch (Exception e) {
            return ReturnCode.FAILED;
        }
    }

    @Override
    public String getDescription() {
        return "сгруппировать элементы по заданному типу";
    }


    @Override
    public CommandType getType() {
        return this.type;
    }



}
