package server.service;

import common.CommandRequest;

import java.nio.channels.SocketChannel;
import java.util.Map;

public class ServerLoop {

    private final ServerContext context;
    private final NetworkRequestHandler requestHandler;
    private volatile boolean running = true;

    public ServerLoop(ServerContext context, NetworkRequestHandler requestHandler) {
        this.context = context;
        this.requestHandler = requestHandler;
    }

    public void stop() {
        running = false;
    }

    public void run() {
        ServerNetworkService network = context.getNetworkService();

        while (running) {
            int eventsProcessed = network.processEvents();

            if (eventsProcessed > 0) {
                for (Map.Entry<SocketChannel, ServerNetworkService.ClientData> entry :
                        network.getClients().entrySet()) {

                    SocketChannel clientChannel = entry.getKey();
                    var key = clientChannel.keyFor(network.getSelector());

                    if (key != null && key.attachment() instanceof CommandRequest request) {
                        key.attach(null);
                        requestHandler.processRequest(clientChannel, request);
                    }
                }
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}