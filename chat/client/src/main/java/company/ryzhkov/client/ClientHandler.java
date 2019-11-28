package company.ryzhkov.client;

public interface ClientHandler {

    void init();

    void sendMessage(String message);

    void disconnect();
}
