package server;

import common.CommandRequest;
import common.CommandResponse;
import common.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerNetworkService {
    private ServerSocketChannel serverChannel;
    private SocketChannel clientChannel;
    private final int port;

    public ServerNetworkService(int port) {
        this.port = port;
    }

    /**
     * Запускает сервер и начинает прослушивать порт
     */
    public boolean start() {
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            System.out.println("Сервер запущен на порту " + port + ", ожидание подключения...");
            return true;
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер: " + e.getMessage());
            return false;
        }
    }

    /**
     * Принимает входящее подключение от клиента (блокирующее ожидание в цикле)
     */
    public boolean acceptClient() {
        try {
            // В неблокирующем режиме активно опрашиваем, пока не появится клиент
            while ((clientChannel = serverChannel.accept()) == null) {
                // Ждём подключения
            }

            clientChannel.configureBlocking(false);
            System.out.println("Клиент подключён: " + clientChannel.getRemoteAddress());
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка при приёме клиента: " + e.getMessage());
            return false;
        }
    }

    /**
     * Получает запрос от клиента по протоколу: [4 байта: длина][N байт: данные]
     */
    public CommandRequest receive() {
        if (clientChannel == null || !clientChannel.isOpen()) {
            System.out.println("Нет активного подключения к клиенту");
            return null;
        }

        try {
            // Читаем размер сообщения (4 байта)
            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
            while (sizeBuffer.hasRemaining()) {
                if (clientChannel.read(sizeBuffer) == -1) {
                    System.out.println("Клиент закрыл соединение");
                    return null;
                }
            }
            sizeBuffer.flip();
            int dataSize = sizeBuffer.getInt();

            // Валидация размера для защиты от некорректных данных
            if (dataSize <= 0 || dataSize > 10_000_000) {
                System.out.println("Некорректный размер сообщения: " + dataSize);
                return null;
            }

            // Читаем тело сообщения
            ByteBuffer dataBuffer = ByteBuffer.allocate(dataSize);
            while (dataBuffer.hasRemaining()) {
                clientChannel.read(dataBuffer);
            }
            dataBuffer.flip();

            byte[] data = new byte[dataSize];
            dataBuffer.get(data);

            return (CommandRequest) Serializer.deserialize(data);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка получения запроса: " + e.getMessage());
            return null;
        }
    }

    /**
     * Отправляет ответ клиенту по протоколу: [4 байта: длина][N байт: данные]
     */
    public boolean send(CommandResponse response) {
        if (clientChannel == null || !clientChannel.isOpen()) {
            System.out.println("Нет активного подключения к клиенту");
            return false;
        }

        try {
            byte[] data = Serializer.serialize(response);
            ByteBuffer buffer = ByteBuffer.wrap(data);

            // Отправляем размер
            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
            sizeBuffer.putInt(data.length);
            sizeBuffer.flip();

            while (sizeBuffer.hasRemaining()) {
                clientChannel.write(sizeBuffer);
            }

            // Отправляем данные
            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }

            return true;
        } catch (IOException e) {
            System.out.println("Ошибка отправки ответа: " + e.getMessage());
            return false;
        }
    }

    /**
     * Закрывает подключение к текущему клиенту
     */
    public void closeClientConnection() {
        try {
            if (clientChannel != null && clientChannel.isOpen()) {
                clientChannel.close();
                System.out.println("Подключение к клиенту закрыто");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при закрытии подключения: " + e.getMessage());
        }
    }

    /**
     * Останавливает сервер
     */
    public void stop() {
        closeClientConnection();
        try {
            if (serverChannel != null && serverChannel.isOpen()) {
                serverChannel.close();
                System.out.println("Сервер остановлен");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при остановке сервера: " + e.getMessage());
        }
    }

    /**
     * Проверка: есть ли активное подключение к клиенту
     */
    public boolean isClientConnected() {
        return clientChannel != null && clientChannel.isOpen() && clientChannel.isConnected();
    }

    /**
     * Проверка: запущен ли сервер и слушает ли порт
     */
    public boolean isRunning() {
        return serverChannel != null && serverChannel.isOpen() && serverChannel.socket().isBound();
    }
}