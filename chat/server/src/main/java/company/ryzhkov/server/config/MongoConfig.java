package company.ryzhkov.server.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
public class MongoConfig {
    private MongoCollection<Document> userCollection;

    @PostConstruct
    public void init() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost");
        MongoDatabase database = mongoClient.getDatabase("chat");
        userCollection = database.getCollection("users");
    }
}
