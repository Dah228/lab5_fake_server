package server.commands;

import common.ConsoleResponseSender;
import common.ResponseSender;
import common.Invoker;
import server.collection.*;

import java.util.HashMap;
import java.util.Map;

public class CommandsList {

    VehicleCollection collection = new VehicleCollection();
    Invoker invoker = new Invoker();
    ResponseSender responseSender = new ConsoleResponseSender();
    VehicleManager manager = new VehicleManager(collection, responseSender);
    VehicleRandom random = new VehicleRandom(collection);
    VehicleSaver saver = new VehicleSaver(collection, responseSender);
    VehicleAdder adder = new VehicleAdder(collection, responseSender);

    private final Map<String, Command> commandList = new HashMap<>();

    public CommandsList() {
        registratedCommand();
    }

    private void registratedCommand() {
        register("clear", new ClearCommand(manager, responseSender));
        register("filter_greater_than_engine_power", new CompareByEnginePowerCommand(manager, responseSender));
        register("info", new InfoCommand(manager, responseSender));
        register("show", new ShowCommand(manager));
        register("remove_by_id", new RemoveByID(adder, responseSender));
        register("print_descending", new PrintDescendingCommand(random));
        register("save", new SaveCommand(saver, responseSender));
        register("shuffle", new ShuffleCommand(random));
        register("sort", new SortCommand(random));
        register("filter_less_than_type", new FilterLessThatType(manager, responseSender));
        register("add", new AddCommand(adder, responseSender));
        register("add_if_max", new AddIfMax(adder, responseSender));
        register("update", new UpdateElementID(adder, responseSender));
        register("help", new HelpCommand(invoker.getCommands(),responseSender));
        register("group_by", new GroupByCommand(adder,responseSender));
    }

    private void register(String name, Command command) {
        commandList.put(name, command);
        invoker.registerCommand(name, command);
    }

    public Map<String, Command> getCommandList() {
        return commandList;
    }


    public Invoker getInvoker() {
        return invoker;
    }
}