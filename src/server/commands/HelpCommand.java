package server.commands;

import common.CommandType;
import common.ReturnCode;
import common.Vehicle;

import java.util.List;
import java.util.Map;


public class HelpCommand implements Command {
    private final Map<String, Command> allCommands;
    private final CommandType type = CommandType.NOARGS;

    public HelpCommand(Map<String, Command> allCommands){
        this.allCommands = allCommands;
    }

    @Override
    public ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud) {
        if (args.size() != 1) return ReturnCode.FAILED;
        System.out.println("=== Доступные команды ===");
        for (Map.Entry<String, Command> entry : allCommands.entrySet()) {
            String name = entry.getKey();
            String description = entry.getValue().getDescription();
            System.out.println(name + " - " + description);
        }
        System.out.println("=========================");
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
