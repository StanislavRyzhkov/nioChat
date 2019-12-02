package company.ryzhkov.server.repository;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
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
    private MongoCollection<Document> userCollection;

    @Autowired
    public void setUserCollection(MongoCollection<Document> userCollection) {
        this.userCollection = userCollection;
    }

    @Override
    public Mono<Success> insert(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        Document document = new Document("username", username)
                .append("password", password);
        return from(userCollection.insertOne(document));
    }

    @Override
    public Mono<Optional<User>> findByUsername(String username) {
        return from(userCollection.find(eq("username", username))
                .first())
                .map(document -> Optional.of(new User(
                        ((ObjectId) document.get("_id")).toHexString(),
                        (String) document.get("username"),
                        (String) document.get("password")
                )))
                .defaultIfEmpty(Optional.empty());
    }
}
