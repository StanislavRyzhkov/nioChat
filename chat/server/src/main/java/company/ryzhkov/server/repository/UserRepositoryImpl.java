package company.ryzhkov.server.repository;

import com.mongodb.reactivestreams.client.Success;
import company.ryzhkov.server.config.MongoConfig;
import company.ryzhkov.server.model.User;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRepositoryImpl implements UserRepository {
    private MongoConfig mongoConfig;

    @Autowired
    public void setMongoConfig(MongoConfig mongoConfig) {
        this.mongoConfig = mongoConfig;
    }

    @Override
    public Mono<Success> insert(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        Document document = new Document("username", username)
                .append("password", password);
        return Mono.from(mongoConfig.getUserCollection().insertOne(document));
    };
}
