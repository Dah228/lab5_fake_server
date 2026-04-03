package server.service;

import common.CommandRequest;
import common.CommandResponse;
import common.CommandType;
import common.Serializer;
import server.commands.Command;
import server.commands.CommandsList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerNetworkService {
    private ServerSocketChannel serverChannel;
    private final Selector selector;
    private final int port;
    private final CommandsList commandsList;

    private final Map<SocketChannel, ClientData> clients = new ConcurrentHashMap<>();

    public static class ClientData {
        public ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        public ByteBuffer dataBuffer;
        public int expectedSize = -1;
        public boolean readingSize = true;
        public boolean initialized = false;

        public void reset() {
            sizeBuffer.clear();
            dataBuffer = null;
            expectedSize = -1;
            readingSize = true;
        }
    }

    public ServerNetworkService(int port, CommandsList commandsList) {
        this.port = port;
        this.commandsList = commandsList;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать селектор", e);
        }
    }

    public boolean start() {
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Сервер запущен на порту " + port + ", ожидание подключений...");
            return true;
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер: " + e.getMessage());
            return false;
        }
    }

    public int processEvents() {
        try {
            selector.select(100);
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            int processed = 0;

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (!key.isValid()) continue;

                if (key.isAcceptable()) {
                    handleAccept();
                    processed++;
                } else if (key.isReadable()) {
                    handleRead(key);
                    processed++;
                }
            }
            return processed;
        } catch (IOException e) {
            System.out.println("Ошибка обработки событий: " + e.getMessage());
            return 0;
        }
    }

    private void handleAccept() throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            clients.put(clientChannel, new ClientData());

            System.out.println("Клиент подключён: " + clientChannel.getRemoteAddress() +
                    " (всего клиентов: " + clients.size() + ")");

            // Отправляем клиенту карту команд при подключении
            sendCommandsMap(clientChannel);
        }
    }

    private void sendCommandsMap(SocketChannel clientChannel) {
        try {
            Map<String, CommandType> commandsMap = new HashMap<>();
            Map<String, Command> allCommands = commandsList.getCommandList();

            for (Map.Entry<String, Command> entry : allCommands.entrySet()) {
                String commandName = entry.getKey();
                Command command = entry.getValue();
                commandsMap.put(commandName, command.getType());
            }

            CommandResponse initResponse = new CommandResponse(
                    true,
                    "connected",
                    commandsMap
            );

            if (sendTo(clientChannel, initResponse)) {
                System.out.println("Отправлена карта команд клиенту " + clientChannel.getRemoteAddress());
                ClientData data = clients.get(clientChannel);
                if (data != null) {
                    data.initialized = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка отправки карты команд: " + e.getMessage());
            removeClient(clientChannel);
        }
    }

    public CommandRequest readFromClient(SocketChannel clientChannel) {
        ClientData data = clients.get(clientChannel);
        if (data == null) return null;

        try {
            if (data.readingSize) {
                while (data.sizeBuffer.hasRemaining()) {
                    if (clientChannel.read(data.sizeBuffer) == -1) {
                        removeClient(clientChannel);
                        return null;
                    }
                }

                data.sizeBuffer.flip();
                data.expectedSize = data.sizeBuffer.getInt();
                data.sizeBuffer.clear();

                if (data.expectedSize <= 0 || data.expectedSize > 10_000_000) {
                    System.out.println("Некорректный размер сообщения: " + data.expectedSize);
                    removeClient(clientChannel);
                    return null;
                }

                data.dataBuffer = ByteBuffer.allocate(data.expectedSize);
                data.readingSize = false;
            }

            while (data.dataBuffer.hasRemaining()) {
                if (clientChannel.read(data.dataBuffer) == -1) {
                    removeClient(clientChannel);
                    return null;
                }
            }

            data.dataBuffer.flip();
            byte[] bytes = new byte[data.expectedSize];
            data.dataBuffer.get(bytes);

            data.reset();

            return (CommandRequest) Serializer.deserialize(bytes);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка чтения от клиента: " + e.getMessage());
            removeClient(clientChannel);
            return null;
        }
    }

    private void handleRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        CommandRequest request = readFromClient(clientChannel);

        if (request != null) {
            key.attach(request);
        }
    }

    public boolean sendTo(SocketChannel clientChannel, CommandResponse response) {
        if (clientChannel == null || !clientChannel.isOpen()) {
            return false;
        }

        try {
            byte[] data = Serializer.serialize(response);
            ByteBuffer buffer = ByteBuffer.wrap(data);

            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
            sizeBuffer.putInt(data.length);
            sizeBuffer.flip();

            while (sizeBuffer.hasRemaining()) {
                clientChannel.write(sizeBuffer);
            }

            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }

            return true;
        } catch (IOException e) {
            System.out.println("Ошибка отправки ответа: " + e.getMessage());
            removeClient(clientChannel);
            return false;
        }
    }

    public void broadcast(CommandResponse response) {
        for (SocketChannel client : clients.keySet()) {
            sendTo(client, response);
        }
    }

    public void removeClient(SocketChannel clientChannel) {
        if (clientChannel != null) {
            clients.remove(clientChannel);
            try {
                clientChannel.close();
            } catch (IOException ignored) {}
            System.out.println("Клиент отключён (осталось: " + clients.size() + ")");
        }
    }

    public void stop() {
        for (SocketChannel client : clients.keySet()) {
            try {
                client.close();
            } catch (IOException ignored) {}
        }
        clients.clear();

        try {
            if (serverChannel != null && serverChannel.isOpen()) {
                serverChannel.close();
            }
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
            System.out.println("Сервер остановлен");
        } catch (IOException e) {
            System.out.println("Ошибка при остановке сервера: " + e.getMessage());
        }
    }

    public Map<SocketChannel, ClientData> getClients() {
        return clients;
    }

    public int getClientCount() {
        return clients.size();
    }

    public Selector getSelector() {
        return selector;
    }
}