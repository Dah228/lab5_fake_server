package common;


import server.commands.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoker {


    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }
    public void executeCommand(String commandName, List<String> list, Vehicle vehicle, Boolean isLaud) {
        Command command = commands.get(commandName);
        if (command != null) command.execute(list, vehicle, isLaud);
        else {
            System.out.println("Неизвестная команда: " + commandName);
            System.out.println("Введите 'help' для списка доступных команд");
        }
    }
    public Map<String, Command> getCommands() {
        return commands;
    }





}