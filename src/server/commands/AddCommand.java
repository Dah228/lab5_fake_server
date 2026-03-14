package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.Vehicle;
import server.collection.VehicleAdder;
import common.ReturnCode;

import java.util.List;

public class AddCommand implements Command {
    private final CommandType type = CommandType.WITHMODEL;
    private final VehicleAdder vehicle;
    private final ResponseSender responseSender;

    public AddCommand(VehicleAdder vehicle, ResponseSender responseSender) {
        this.vehicle = vehicle;
        this.responseSender = responseSender;
    }


    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 1) {
            if (isLaud) responseSender.send("Add required 1 args, got = " + args.size());
            return ReturnCode.FAILED;
        }
        try {
            this.vehicle.addElement(vehicle,isLaud);
            return ReturnCode.OK;
        } catch (IllegalArgumentException e){
            responseSender.send("произошла ошибка, в параметры команды add были введены не валидные данные");
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
