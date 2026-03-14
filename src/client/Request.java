//package client.mainpart;
//
//import common.parser.CommandType;
//import common.parser.ReturnCode;
//import common.parser.Vehicle;
//
//import java.io.FileInputStream;
//import java.util.List;
//import java.util.Scanner;
//
//public class Request {
//
//
//
//    if (allCommands.getCommandType(commandName) == CommandType.WITHMODEL) {
//        // Создаем scanner из inputStream (если еще не создан)
//        Scanner commandScanner = new Scanner(stream);
//
//        // Создаем DataValidator
//        DataValidator validator = new DataValidator(commandScanner, !(stream instanceof FileInputStream), responseSender);
//
//        try {
//            // Считываем Vehicle через валидатор
//            Vehicle vehicle = validator.parseVehicle(commandScanner, !(stream instanceof FileInputStream));
//
//            // Сериализуем vehicle в строку (или сразу отправляй объект, зависит от твоей архитектуры)
//            String vehicleData = serializeVehicle(vehicle);
//
//            // Выполняем команду с данными vehicle
//            invoker.executeCommand(commandName, List.of(vehicleData), commandScanner, !(stream instanceof FileInputStream));
//
//        } catch (Exception e) {
//            if (!(stream instanceof FileInputStream)) {
//                System.out.println("Ошибка при вводе данных: " + e.getMessage());
//            } else {
//                System.out.println("Ошибка в скрипте: " + e.getMessage());
//                return ReturnCode.FAILED;
//            }
//        }
//        continue;
//    }
//}
