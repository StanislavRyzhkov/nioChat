package company.ryzhkov.server.repository;

import com.mongodb.reactivestreams.client.Success;
import company.ryzhkov.server.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<Success> insert(User user);
}
