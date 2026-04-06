package server;

import common.CommandRequest;
import common.CommandResponse;
import common.ReturnCode;
import server.commands.CommandsList;
import server.commands.Invoker;
import server.service.NetworkResponseSender;
import server.service.ServerNetworkService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ServerApp {
    public static void main(String[] args) {
        System.out.println("Инициализация сервера...");

        CommandsList commandsList = new CommandsList();
        Invoker invoker = commandsList.getInvoker();

        ServerNetworkService network = new ServerNetworkService(7301, commandsList);

        if (!network.start()) {
            System.err.println("Не удалось запустить сервер");
            return;
        }

        System.out.println("Сервер запущен. Команды: " +
                commandsList.getCommandList().size());
        System.out.println("Введите команду в консоль сервера или 'help' для списка.");

        // Поток для чтения команд из консоли сервера
        Thread consoleThread = new Thread(() -> {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String line;
                while ((line = consoleReader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    if (line.equals("exit")) {
                        System.out.println("Остановка сервера...");
                        network.stop();
                        break;
                    }

                    String[] tokens = line.split("\\s+");
                    String commandName = tokens[0];
                    List<String> arguments = Arrays.asList(tokens);

                    System.out.printf("Консоль: %s | args=%s%n", commandName, arguments);

                    NetworkResponseSender consoleSender = new NetworkResponseSender();

                    ReturnCode statusCode = invoker.executeCommand(
                            commandName,
                            arguments,
                            null, // vehicle не нужен для консольных команд сервера
                            true, // isLaud = true, чтобы выводить сообщения
                            consoleSender
                    );

                    String output = consoleSender.getOutput();
                    if (!output.isEmpty()) {
                        System.out.println(output);
                    } else {
                        System.out.println(statusCode == ReturnCode.OK ? "Команда выполнена" : "Ошибка выполнения");
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка чтения консоли: " + e.getMessage());
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();

        // Главный цикл: обработка сетевых событий
        while (true) {
            int eventsProcessed = network.processEvents();

            if (eventsProcessed > 0) {
                for (Map.Entry<SocketChannel, ServerNetworkService.ClientData> entry :
                        network.getClients().entrySet()) {

                    SocketChannel clientChannel = entry.getKey();
                    var key = clientChannel.keyFor(network.getSelector());

                    if (key != null && key.attachment() instanceof CommandRequest request) {
                        key.attach(null);

                        try {
                            String commandName = request.getCommandName();
                            List<String> arguments = request.getArguments();
                            common.Vehicle vehicle = request.getVehicle();
                            Boolean isLaud = request.getBoolean();

                            System.out.printf("Запрос от %s: %s%n",
                                    clientChannel.getRemoteAddress(), commandName);

                            NetworkResponseSender networkSender = new NetworkResponseSender();

                            ReturnCode statusCode = invoker.executeCommand(
                                    commandName,
                                    arguments,
                                    vehicle,
                                    isLaud,
                                    networkSender
                            );

                            String commandOutput = networkSender.getOutput();

                            CommandResponse response = new CommandResponse(
                                    statusCode == ReturnCode.OK,
                                    commandOutput.isEmpty() ? "Команда выполнена" : commandOutput,
                                    null
                            );

                            network.sendTo(clientChannel, response);

                        } catch (Exception e) {
                            System.err.println("Ошибка обработки запроса: " + e.getMessage());
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

            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
    }
}