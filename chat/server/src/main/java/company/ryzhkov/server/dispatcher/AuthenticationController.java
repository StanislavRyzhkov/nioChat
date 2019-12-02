package company.ryzhkov.server.dispatcher;

import company.ryzhkov.server.model.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationController extends UserParser implements Controller {

    @Override
    public Mono<String> dispatch(String message) {
        User user = parseUser(message);
        return null;
    }
}
