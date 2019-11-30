package company.ryzhkov.server.repository;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
import company.ryzhkov.server.config.MongoConfig;
import company.ryzhkov.server.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static reactor.core.publisher.Mono.from;

@Component
public class UserRepositoryImpl implements UserRepository {
    private MongoConfig mongoConfig;
    private MongoCollection<Document> userCollection;

    @Autowired
    public void setUserCollection(MongoCollection<Document> userCollection) {
        this.userCollection = userCollection;
    }

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
        return from(mongoConfig.getUserCollection().insertOne(document));
    }

    @Override
    public Mono<Optional<User>> findByUsername(String username) {
        return from(userCollection.find(eq("username", username))
                .first())
                .map(document -> {
                    User user = new User();
                    user.setId((ObjectId) document.get("_id"));
                    user.setUsername((String) document.get("username"));
                    user.setPassword((String) document.get("password"));
                    return Optional.of(user);
                })
                .defaultIfEmpty(Optional.empty());
    }
}
