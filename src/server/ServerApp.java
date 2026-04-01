package server;

import common.CommandRequest;
import common.CommandResponse;
import common.CommandType;
import common.ReturnCode;
import server.commands.CommandsList;

import java.util.List;
import java.util.Map;

public class ServerApp {
    public static void main(String[] args) {
        // ─── 1. ИНИЦИАЛИЗАЦИЯ ──────────────────────────────────────
        CommandsList commandsList = new CommandsList();
        Invoker invoker = commandsList.getInvoker();
        AllCommands allCommands = new AllCommands(commandsList);

        ServerNetworkService network = new ServerNetworkService(7301);
        if (!network.start()) {
            System.err.println("❌ Не удалось запустить сервер");
            return;
        }

        System.out.println("✅ Сервер запущен. Команды: "
                + commandsList.getCommandList().size());

        // ─── 2. ГЛАВНЫЙ ЦИКЛ: приём клиентов ───────────────────────
        while (true) {
            if (!network.acceptClient()) {
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                continue;
            }

            System.out.println("🔗 Клиент подключён");

            // === ОТПРАВКА КАРТЫ КОМАНД КЛИЕНТУ ===
            Map<String, CommandType> commandsInfo = allCommands.getAll();
            CommandResponse initResponse = new CommandResponse(
                    true,
                    "connected",
                    commandsInfo
            );
            network.send(initResponse);  // ← ОТПРАВЛЯЕМ ОДИН РАЗ (было дважды!)
            System.out.println("📤 Отправлена карта команд: " + commandsInfo.size());

            // === ЦИКЛ ОБРАБОТКИ ЗАПРОСОВ ===
            while (network.isClientConnected()) {
                try {
                    // ── ПРИЁМ ЗАПРОСА ──
                    CommandRequest request = network.receive();
                    if (request == null) {
                        System.out.println("⚠ Клиент отключился");
                        break;
                    }

                    // ── ИЗВЛЕЧЕНИЕ ДАННЫХ ──
                    String commandName = request.getCommandName();
                    List<String> arguments = request.getArguments();
                    common.Vehicle vehicle = request.getVehicle();
                    Boolean isLaud = request.getBoolean();

                    System.out.printf("📩 Запрос: %s | args=%s | isLaud=%s%n",
                            commandName, arguments, isLaud);

                    // ── ПОДГОТОВКА: создаём отправщик для этого запроса ──
                    NetworkResponseSender networkSender = new NetworkResponseSender();

                    // ── ВЫПОЛНЕНИЕ: передаём sender через invoker ──
                    ReturnCode statusCode = invoker.executeCommand(
                            commandName,
                            arguments,
                            vehicle,
                            isLaud,
                            networkSender  // ← передаём отправщик!
                    );

                    // ── ПОЛУЧЕНИЕ ВЫВОДА ──
                    String commandOutput = networkSender.getOutput();

                    // ── ФОРМИРОВАНИЕ ОТВЕТА ──
                    CommandResponse response = new CommandResponse(
                            statusCode == ReturnCode.OK,
                            commandOutput.isEmpty() ? "Команда выполнена" : commandOutput,
                            null
                    );

                    // ── ОТПРАВКА КЛИЕНТУ ──
                    if (!network.send(response)) {
                        System.out.println("❌ Не удалось отправить ответ");
                        break;
                    }
                    System.out.println("📤 Ответ отправлен");

                } catch (Exception e) {
                    System.err.println("💥 Ошибка обработки: " + e.getMessage());
                    e.printStackTrace();

                    CommandResponse error = new CommandResponse(
                            false,
                            "Ошибка сервера: " + e.getMessage(),
                            null
                    );
                    network.send(error);
                }
            }

            // ── КЛИЕНТ ОТКЛЮЧИЛСЯ ──
            network.closeClientConnection();
            System.out.println("🔌 Клиент отключён, ожидаем следующего...\n");
        }
    }
}