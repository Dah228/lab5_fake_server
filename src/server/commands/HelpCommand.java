package server.commands;

import common.CommandType;
import common.ResponseSender;
import common.ReturnCode;
import common.Vehicle;

import java.util.List;
import java.util.Map;


public class HelpCommand implements Command {
    private final Map<String, Command> allCommands;
    private final CommandType type = CommandType.NOARGS;
    private final ResponseSender responseSender;


    public HelpCommand(Map<String, Command> allCommands, ResponseSender responseSender){
        this.allCommands = allCommands;
        this.responseSender = responseSender;
    }

    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 1) return ReturnCode.FAILED;
        responseSender.send("=== Доступные команды ===");
        for (Map.Entry<String, Command> entry : allCommands.entrySet()) {
            String name = entry.getKey();
            String description = entry.getValue().getDescription();
            responseSender.send(name + " - " + description);
        }
        responseSender.send("=========================");
        return ReturnCode.OK;
    }

    @Override
    public String  getDescription(){
        return "вывести справку по доступным командам";
    }


    @Override
    public CommandType getType() {
        return this.type;
    }

}
