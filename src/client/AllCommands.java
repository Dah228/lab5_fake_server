package client;

import common.CommandType;
import common.Invoker;
import server.commands.Command;
import server.commands.CommandsList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AllCommands {

    private final Map<String, CommandType> commandsInfo;
    private final CommandsList commandsList;
    private final Invoker invoker;

    public AllCommands(CommandsList commandsList) {
        this.commandsList = commandsList;
        this.invoker = commandsList.getInvoker();
        this.commandsInfo = new HashMap<>();
        initializeCommandsFromList(commandsList);
    }

    private void initializeCommandsFromList(CommandsList commandsList) {
        Map<String, Command> allCommands = commandsList.getCommandList();

        for (Map.Entry<String, Command> entry : allCommands.entrySet()) {
            String commandName = entry.getKey();
            if (commandName.equals("execute_script")) {
                commandsInfo.put(commandName, CommandType.WITHARGS);
                continue;
            }
            Command command = entry.getValue();
            CommandType type = command.getType();
            commandsInfo.put(commandName, type);
        }
    }

    // Getter для CommandsList
    public CommandsList getCommandsList() {
        return commandsList;
    }


    public CommandType getCommandType(String commandName) {
        return commandsInfo.get(commandName);
    }

    public Set<String> getAllCommandNames() {
        return commandsInfo.keySet();
    }

    public boolean commandExists(String commandName) {
        return commandsInfo.containsKey(commandName);
    }
}