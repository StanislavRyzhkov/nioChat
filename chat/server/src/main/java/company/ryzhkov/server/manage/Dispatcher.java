package company.ryzhkov.server.manage;

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
        if (message.startsWith("/reg")) {
            String body = message.substring(4);
            String[] elements = body.trim().split(" ");
            String username = elements[1];
            String password = elements[3];
            return userService.register(username, password);
        }
        if (message.startsWith("/auth")) {
            String body = message.substring(4);
            String[] elements = body.trim().split(" ");
            String username = elements[1];
            String password = elements[3];
        }
        return Mono.just("FAIL");
    }
}
