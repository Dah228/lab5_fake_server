package server.collection;


import common.Vehicle;
import common.VehicleType;
import server.message.ResponseSender;


public class VehicleManager {
    private final VehicleCollection collection;
    private final ResponseSender responseSender;  // Добавляем поле

    // Конструктор с внедрением зависимости
    public VehicleManager(VehicleCollection collection, ResponseSender responseSender) {
        this.collection = collection;
        this.responseSender = responseSender;
    }

    // Логика вывода всей коллекции
    public void showCollection() {
        for (Vehicle v : collection.getVehicles()) {
            Vehicle.printVehicle(v);
        }
    }

    // Логика информации
    public void getInfo() {
        responseSender.send("Размер коллекции : " + collection.size());
        responseSender.send("Тип коллекции : " + collection.getVehicles().getClass().getName());
        responseSender.send("Дата инициализации : " + collection.getInitTime());
        responseSender.send("Список имен : ");
        float summa = 0;
        for (Vehicle v : collection.getVehicles()) {
            summa += v.getEnginePower();
            responseSender.send(v.getName() + "   ");
        }
        responseSender.send("-----------------");
        responseSender.send("Общая мощность двигателей : " + summa);
        if (!collection.isEmpty()) {
            responseSender.send("Средняя мощность двигателя : " + (summa / collection.size()));
        } else {
            responseSender.send("Средняя мощность двигателя : 0 (коллекция пуста)");
        }
    }

    // Логика фильтрации
    public void filterByEnginePower(Float power) {
        for (Vehicle v : collection.getVehicles()) {
            if (v.getEnginePower() >= power) {
                Vehicle.printVehicle(v);
            }
        }
    }

    // Логика очистки
    public void clearCollection() {
        collection.clear();
    }


    public void filterLessThanType(VehicleType type) {
        for (Vehicle v : collection.getVehicles()) {
            if (v.getType().compareTo(type) < 0) {
                Vehicle.printVehicle(v);
            }
        }
    }

}