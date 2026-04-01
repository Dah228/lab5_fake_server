//package server.service;
//
//import common.CommandRequest;
//import common.CommandResponse;
//import common.ReturnCode;
//import server.commands.AllCommands;
//import server.commands.Invoker;
//
//import java.io.IOException;
//import java.nio.channels.SocketChannel;
//
//public class ClientHandler implements Runnable {
//
//    private final String clientId;
//    private final SocketChannel clientChannel;
//    private final ServerNetworkService serverService;
//
//    // Твои существующие компоненты — передаём их из ServerApp
//    private final Invoker invoker;
//    private final AllCommands allCommands;
//
//    public ClientHandler(String clientId, SocketChannel channel,
//                         ServerNetworkService serverService,
//                         Invoker invoker, AllCommands allCommands) {
//        this.clientId = clientId;
//        this.clientChannel = channel;
//        this.serverService = serverService;
//        this.invoker = invoker;
//        this.allCommands = allCommands;
//    }
//
//    @Override
//    public void run() {
//        try {
//            System.out.println("[" + clientId + "] Подключён: " +
//                    clientChannel.getRemoteAddress());
//
//            // === ОТПРАВКА КАРТЫ КОМАНД (как у тебя в оригинале) ===
//            var commandsInfo = allCommands.getAll();
//            CommandResponse initResponse = new CommandResponse(
//                    true, "connected", commandsInfo);
//            serverService.sendTo(clientChannel, initResponse);
//            System.out.println("[" + clientId + "] 📤 Карта команд отправлена");
//
//            // === ЦИКЛ ОБРАБОТКИ ЗАПРОСОВ ===
//            while (clientChannel.isOpen()) {
//                CommandRequest request = serverService.receiveFrom(clientChannel);
//                if (request == null) {
//                    System.out.println("[" + clientId + "] ⚠ Клиент отключился");
//                    break;
//                }
//
//                // Извлекаем данные (как у тебя)
//                String commandName = request.getCommandName();
//                var arguments = request.getArguments();
//                common.Vehicle vehicle = request.getVehicle();
//                Boolean isLaud = request.getBoolean();
//
//                System.out.printf("[" + clientId + "] 📩 %s | args=%s%n",
//                        commandName, arguments);
//
//                // === Создаём отправщик ДЛЯ ЭТОГО клиента ===
//                var networkSender = new NetworkResponseSender(clientChannel, serverService);
//
//                // === Выполняем команду ===
//                ReturnCode statusCode = invoker.executeCommand(
//                        commandName, arguments, vehicle, isLaud, networkSender);
//
//                // === Формируем и отправляем ответ ===
//                String output = networkSender.getOutput();
//                CommandResponse response = new CommandResponse(
//                        statusCode == ReturnCode.OK,
//                        output.isEmpty() ? "Команда выполнена" : output,
//                        null
//                );
//
//                if (!serverService.sendTo(clientChannel, response)) {
//                    System.out.println("[" + clientId + "] ❌ Не удалось отправить ответ");
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("[" + clientId + "] 💥 Ошибка: " + e.getMessage());
//        } finally {
//            // Убираем клиента из списка и закрываем канал
//            serverService.removeClient(clientId);
//        }
//    }
//}