package company.ryzhkov.server.service;

import company.ryzhkov.server.model.User;
import company.ryzhkov.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private UserRepository userRepository;
    BeanPostProcessor beanPostProcessor;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<String> register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.insert(user).map(success -> "USER CREATED");
    }

    public Mono<String> authenticate(String username, String password) {
        return null;
    }
}
