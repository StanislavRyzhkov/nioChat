package company.ryzhkov.server.dispatcher;

import company.ryzhkov.server.model.User;

public abstract class UserParser {

    protected User parseUser(String message) {
        String[] elements = message.split(" ");
        if (elements.length < 4) throw new RuntimeException("Неправильный ввод!");
        String username = elements[1];
        String password = elements[3];
        return new User("", username, password);
    }
}
