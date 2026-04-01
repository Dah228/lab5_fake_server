package server.commands;

import common.ReturnCode;
import common.Vehicle;
import server.service.NetworkResponseSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoker {
    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    // ← Создаём CommandParams внутри и передаём команде
    public ReturnCode executeCommand(
            String commandName,
            List<String> args,
            Vehicle vehicle,
            Boolean isLaud,
            NetworkResponseSender responseSender  // ← отправитель
    ) {
        Command command = commands.get(commandName);

        if (command != null) {
            // ← Создаём record с параметрами
            CommandParams params = new CommandParams(args, vehicle, isLaud, responseSender);
            return command.execute(params);
        } else {
            System.out.println("Неизвестная команда: " + commandName);
            return ReturnCode.FAILED;
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}