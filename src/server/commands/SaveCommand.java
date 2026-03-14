package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.Vehicle;
import server.collection.VehicleSaver;
import common.ReturnCode;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.List;

public class SaveCommand implements Command{
    VehicleSaver vehicleSaver;
    private final CommandType type = CommandType.NOARGS;
    private final ResponseSender responseSender;


    public SaveCommand(VehicleSaver vehicleSaver, ResponseSender responseSender){
        this.vehicleSaver = vehicleSaver;
        this.responseSender = responseSender;
    }

@Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud){
        if (args.size()!= 1) return ReturnCode.FAILED;
        try {
            vehicleSaver.saveToFile();
            return ReturnCode.OK;
        } catch (ParserConfigurationException e) {
            responseSender.send("Нарушена поддержка парсера");
            return ReturnCode.FAILED;
        } catch (TransformerException e) {
            responseSender.send("Ошибка записи файла");
            return ReturnCode.FAILED;
        }
    }

    @Override
    public String getDescription() {
        return " сохранить коллекцию в файл";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }
}
