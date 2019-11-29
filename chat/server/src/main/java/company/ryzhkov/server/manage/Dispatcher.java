package company.ryzhkov.server.manage;

import company.ryzhkov.server.model.User;
import company.ryzhkov.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Dispatcher {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Mono<String> handleMessage(String message) {
        if (message.startsWith("/reg") || message.startsWith("/auth")) {
            String[] elements = message.split(" ");
            if (elements.length < 5) throw new RuntimeException("Неправильный ввод!");
            String username = elements[2];
            String password = elements[4];
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            if (elements[0].equals("/reg")) return userService.register(user);
            else return userService.authenticate(user);
        }
        return Mono.just("FAIL");
    }
}
