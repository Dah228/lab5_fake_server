package common;

public class ConsoleResponseSender implements ResponseSender {
    @Override
    public void send(String message) {
        System.out.println(message);
    }
}