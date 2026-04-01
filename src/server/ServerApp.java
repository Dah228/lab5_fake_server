package server;

import common.CommandRequest;
import common.CommandResponse;
import common.CommandType;
import common.ReturnCode;
import server.commands.AllCommands;
import server.commands.CommandsList;
import server.commands.Invoker;
import server.service.NetworkResponseSender;
import server.service.ServerNetworkService;

import java.nio.channels.SocketChannel;
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
            System.err.println("Не удалось запустить сервер");
            return;
        }

        System.out.println("Сервер запущен. Команды: "
                + commandsList.getCommandList().size());

        // ─── 2. ГЛАВНЫЙ ЦИКЛ: обработка событий (NIO Selector) ─────
        while (true) {
            // Обрабатываем все готовые события (подключения, чтение)
            int eventsProcessed = network.processEvents();
            
            if (eventsProcessed > 0) {
                // Проверяем все подключения на наличие готовых запросов
                for (Map.Entry<SocketChannel, ServerNetworkService.ClientData> entry : 
                     network.getClients().entrySet()) {
                    
                    SocketChannel clientChannel = entry.getKey();
                    
                    // Проверяем, есть ли прикреплённый запрос в SelectionKey
                    var key = clientChannel.keyFor(network.getSelector());
                    if (key != null && key.attachment() instanceof CommandRequest) {
                        CommandRequest request = (CommandRequest) key.attachment();
                        key.attach(null); // Очищаем attachment
                        
                        try {
                            // === ИЗВЛЕЧЕНИЕ ДАННЫХ ===
                            String commandName = request.getCommandName();
                            List<String> arguments = request.getArguments();
                            common.Vehicle vehicle = request.getVehicle();
                            Boolean isLaud = request.getBoolean();

                            System.out.printf("📩 Запрос от %s: %s | args=%s | isLaud=%s%n",
                                    clientChannel.getRemoteAddress(), commandName, arguments, isLaud);

                            // === ПОДГОТОВКА: создаём отправщик для этого запроса ===
                            NetworkResponseSender networkSender = new NetworkResponseSender();

                            // === ВЫПОЛНЕНИЕ: передаём sender через invoker ===
                            ReturnCode statusCode = invoker.executeCommand(
                                    commandName,
                                    arguments,
                                    vehicle,
                                    isLaud,
                                    networkSender  // ← передаём отправитель!
                            );

                            // === ПОЛУЧЕНИЕ ВЫВОДА ===
                            String commandOutput = networkSender.getOutput();

                            // === ФОРМИРОВАНИЕ ОТВЕТА ===
                            CommandResponse response = new CommandResponse(
                                    statusCode == ReturnCode.OK,
                                    commandOutput.isEmpty() ? "Команда выполнена" : commandOutput,
                                    null
                            );

                            // === ОТПРАВКА КЛИЕНТУ ===
                            if (!network.sendTo(clientChannel, response)) {
                                System.out.println("❌ Не удалось отправить ответ клиенту " + clientChannel.getRemoteAddress());
                            } else {
                                System.out.println("📤 Ответ отправлен клиенту " + clientChannel.getRemoteAddress());
                            }

                        } catch (Exception e) {
                            System.err.println("💥 Ошибка обработки запроса: " + e.getMessage());
                            e.printStackTrace();

                            CommandResponse error = new CommandResponse(
                                    false,
                                    "Ошибка сервера: " + e.getMessage(),
                                    null
                            );
                            network.sendTo(clientChannel, error);
                        }
                    }
                }
            }
            
            // Небольшая пауза чтобы не нагружать CPU
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
    }
}