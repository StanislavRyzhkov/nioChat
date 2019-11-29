package company.ryzhkov.server.repository;

import com.mongodb.reactivestreams.client.Success;
import company.ryzhkov.server.model.User;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserRepository {
    Mono<Success> insert(User user);
    Mono<Optional<User>> findByUsername(String username);
}
