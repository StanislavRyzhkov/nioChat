package company.ryzhkov.server.service;

import company.ryzhkov.server.model.User;
import company.ryzhkov.server.profiling.Watch;
import company.ryzhkov.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<String> register(User user) {
        return userRepository.insert(user).map(success -> "USER CREATED");
    }

    @Watch
    public Mono<String> authenticate(User user) {
        return userRepository
                .findByUsername(user.getUsername())
                .doOnNext(System.out::println)
                .map(opt -> opt.orElseThrow(RuntimeException::new)).map(e -> "HUY");
    }

    private Mono<Boolean> checkUsernameUnique(String username) {
        return userRepository.findByUsername(username)
                .map(optionalUser -> !optionalUser.isPresent());
    }
}
