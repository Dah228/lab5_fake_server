package server.commands;

import common.CommandType;
import common.ReturnCode;
import server.CommandParams;
import server.collection.VehicleAdder;

import java.util.List;

public class AddCommand implements Command {
    private final CommandType type = CommandType.WITHMODEL;
    private final VehicleAdder vehicleAdder;

    public AddCommand(VehicleAdder vehicleAdder) {
        this.vehicleAdder = vehicleAdder;
    }

    @Override
    public ReturnCode execute(CommandParams params) {
        try {
            // ← Достаём данные из record
            this.vehicleAdder.addElement(params.vehicle());

            if (params.isLaud()) {
                params.responseSender().send("Транспортное средство успешно добавлено");
                params.responseSender().send("ID: " + params.vehicle().getId());
            }
            return ReturnCode.OK;
        } catch (IllegalArgumentException e) {
            params.responseSender().send("Ошибка валидации: " + e.getMessage());
            return ReturnCode.FAILED;
        } catch (Exception e) {
            params.responseSender().send("Внутренняя ошибка: " + e.getMessage());
            return ReturnCode.FAILED;
        }
    }

    @Override
    public String getDescription() {
        return "Добавить новый элемент в коллекцию";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }
}