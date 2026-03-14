package client;

import common.FuelType;
import common.Vehicle;
import common.VehicleType;
import common.ResponseSender;
//import server.collection.VehicleCollection;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

//import static java.util.Collections.max;

public class DataValidator {
    Scanner scanner;
    Boolean isLaud;
//    private final VehicleCollection vehicleCollection = new VehicleCollection();

    public DataValidator(Scanner scanner, Boolean isLaud) {
        this.scanner = scanner;
        this.isLaud = isLaud;
    }


    /**
     * Универсальный метод для чтения и валидации данных.
     *
     * @param prompt   текст подсказки
     * @param isLaud   режим (true = консоль с циклом, false = файл с одной попыткой)
     * @param parser   функция, которая превращает String в T или бросает IllegalArgumentException
     * @param emptyMsg сообщение, если введена пустая строка (для консоли)
     * @param <T>      тип возвращаемого значения
     * @return валидное значение типа T
     */
    private <T> T readValidatedInput(
            String prompt,
            Boolean isLaud,
            java.util.function.Function<String, T> parser,
            String emptyMsg,
            String errorMsg
    ) {
        if (isLaud) {
            // Консольный режим: цикл с повторным запросом
            while (true) {
                System.out.println(prompt);
                String input = scanner.nextLine().trim();

                try {
                    return parser.apply(input);
                } catch (IllegalArgumentException e) {
                    System.out.println(errorMsg);
                } catch (NoSuchElementException e) {
                    System.out.println("\n[Ввод прерван, ожидаем новые данные...]");
                }
            }
        } else {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    continue;
                }

                try {
                    return parser.apply(input);
                } catch (IllegalArgumentException e) {
                    System.out.println(errorMsg + ": '" + input + "'");
                }
            }
            throw new IllegalArgumentException("Файл закончился: " + prompt);
        }
    }

    public Integer readValidInteger(String prompt, Boolean isLaud) {
        return readValidatedInput(
                prompt, isLaud,
                Integer::valueOf,
                "Поле не может быть пустым!",
                "Ошибка ввода, ожидалось целое число"
        );
    }

    public Float readValidFloat(String prompt, Float min, Boolean isLaud) {
        return readValidatedInput(
                prompt, isLaud,
                s -> {
                    float val = Float.parseFloat(s);
                    if (val <= min) throw new IllegalArgumentException("Некорректное значение!");
                    return val;
                },
                "Поле не может быть пустым!",
                "Ошибка ввода, ожидалось число"
        );
    }

    public VehicleType readVehicleType(String prompt, Boolean isLaud) {
        return readValidatedInput(
                prompt, isLaud,
                s -> s.isEmpty() ? null : VehicleType.valueOf(s.toUpperCase()),
                "Введите тип или пустую строку для пропуска",
                "Неверный тип! Доступны: " + Arrays.toString(VehicleType.values())
        );
    }

    public FuelType readFuelType(String prompt, Boolean isLaud) {
        return readValidatedInput(
                prompt, isLaud,
                s -> FuelType.valueOf(s.toUpperCase()),
                "Тип топлива не может быть пустым!",
                "Неверный тип топлива! Доступны: " + Arrays.toString(FuelType.values())
        );
    }

    private boolean isValidForXml(String text) {
        for (char c : text.toCharArray()) {
            if (c == '<' || c == '>' || c == '&' || c == '"' || c == '\'') {
                return false;
            }
        }
        return true;
    }

    public String readValidName(String prompt, Boolean isLaud) {
        return readValidatedInput(
                prompt, isLaud,
                s -> {
                    if (!isValidForXml(s) || s.isEmpty()) {
                        throw new IllegalArgumentException("XML-unsafe символы");
                    }
                    return s;
                },
                "Имя не может быть пустым!",
                "Имя содержит недопустимые символы для XML: < > & \" '"
        );
    }


    public Vehicle parseVehicle(Scanner scanner, Boolean isLaud) {
        DataValidator validator = new DataValidator(scanner, isLaud);
        Vehicle veh = new Vehicle();

//        var Id = vehicleCollection.getAllID();
//        if (!Id.isEmpty()) {
//            veh.setId((long) (max(vehicleCollection.getAllID())) + 1);
//        } else veh.setId(1);

        String name = validator.readValidName("ВВедите имя: ", isLaud);
        veh.setName(name);
        veh.setCreationDate(); // автогенерация

        // Координаты X
        Integer x = validator.readValidInteger("Введите X (целое число): ", isLaud);

        // Координаты Y
        Float y = validator.readValidFloat("Введите Y (> -668): ", -668F, isLaud);

        veh.setCoordinates(x, y);

        // Engine Power
        Float enginePower = validator.readValidFloat("Введите мощность двигателя (> 0): ", 0f, isLaud);
        veh.setEnginePower(enginePower);

        // Distance Traveled
        Float distance = validator.readValidFloat("Введите пройденное расстояние (> 0): ", 0f, isLaud);
        veh.setDistanceTravelled(distance);

        // Vehicle Type (может быть null)
        VehicleType type = validator.readVehicleType("\"Введите тип (PLANE, HELICOPTER, BOAT, SHIP, HOVERBOARD) или пустую строку для null:\"", isLaud);
        veh.setType(type);

        // Fuel Type (не может быть null)
        FuelType fuelType = validator.readFuelType("\"Введите тип топлива (GASOLINE, KEROSENE, ELECTRICITY, DIESEL, NUCLEAR):\"", isLaud);
        veh.setFuelType(fuelType);
        return veh;
    }

}


