package company.ryzhkov.server.dispatcher;

import reactor.core.publisher.Mono;

public interface Controller {
    Mono<String> dispatch(String message);

//    @Watch
//    public Mono<String> handleMessage(String message) {
//        if (message.startsWith("/reg") || message.startsWith("/auth")) {
//            String[] elements = message.split(" ");
//            if (elements.length < 5) throw new RuntimeException("Неправильный ввод!");
//            String username = elements[2];
//            String password = elements[4];
//            User user = new User();
//            user.setUsername(username);
//            user.setPassword(password);
//            if (elements[0].equals("/reg")) return userService.register(user);
//            else return userService.authenticate(user);
//        }
//        return Mono.just("FAIL");
//    }
}
