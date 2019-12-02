package company.ryzhkov.server.dispatcher;

import company.ryzhkov.server.model.User;
import company.ryzhkov.server.profiling.Watch;
import company.ryzhkov.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("reg")
public class RegistrationController extends UserParser implements Controller {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Watch
    @Override
    public Mono<String> dispatch(String message) {
        User user = parseUser(message);
        return userService.register(user);
    }
}
