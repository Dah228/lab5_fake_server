package server.collection;


import common.Vehicle;
import common.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;


public class VehicleManager {
    private final VehicleCollection collection;

    // Конструктор с внедрением зависимости
    public VehicleManager(VehicleCollection collection) {
        this.collection = collection;
    }

    // Логика вывода всей коллекции
    public ArrayList<Vehicle> showCollection() {
        return collection.getVehicles();
    }


    // Логика информации
    public HashMap<String, String> getInfo() {
        HashMap<String, String> paramList = new HashMap<>();
        paramList.put("Размер коллекции : ", String.valueOf(collection.size()));
        paramList.put("Тип коллекции : ", collection.getVehicles().getClass().getName());
        paramList.put("Дата инициализации : ", String.valueOf(collection.getInitTime()));
        float summa = 0;
        for (Vehicle v : collection.getVehicles()) {
            summa += v.getEnginePower();
        }
        paramList.put("Общая мощность двигателей : ", String.valueOf(summa));

        if (!collection.isEmpty()) {
            paramList.put("Средняя мощность двигателя : ", String.valueOf(summa / collection.size()));
        } else {
            paramList.put("Средняя мощность двигателя", "0 (коллекция пуста)");
        }

        return paramList;
    }

    // Логика фильтрации
    public ArrayList<Vehicle> filterByEnginePower(Float power) {
        ArrayList<Vehicle> filteredByEngine = new ArrayList<>();
        for (Vehicle v : collection.getVehicles()) {
            if (v.getEnginePower() >= power) {
                filteredByEngine.add(v);
            }
        }
        return filteredByEngine;
    }

    // Логика очистки
    public void clearCollection() {
        collection.clear();
    }


    public ArrayList<Vehicle> filterLessThanType(VehicleType type) {
        ArrayList<Vehicle> filteredByEngine = new ArrayList<>();
        for (Vehicle v : collection.getVehicles()) {
            if (v.getType().compareTo(type) < 0) {
                filteredByEngine.add(v);
            }
        }
        return filteredByEngine;
    }

}