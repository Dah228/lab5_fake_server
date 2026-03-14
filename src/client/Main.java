package client;

import common.Invoker;
import server.collection.*;
import common.Parser;
import common.Vehicle;
import server.commands.CommandsList;




import static common.Vehicle.printVehicle;

public class Main {
    public static void main(String[] arg) {

        VehicleCollection collection = new VehicleCollection();

        CommandsList commandsList = new CommandsList();
//        AllCommands allCommands = new AllCommands(commandsList);

        Invoker invoker = commandsList.getInvoker();



        try {
            String filePath = arg.length > 0 ? arg[0] : "server.collection.xml";
            collection.addList(Parser.parse(filePath));
            for (Vehicle v : collection.getVehicles()) printVehicle(v);
        } catch (Exception e) {
            System.out.println("Указанного файла не существует/ не соответствует заданному формату введите корректный файл");
        }


        Executor executor = new Executor(invoker);


        executor.Execute(0,System.in);


    }
}