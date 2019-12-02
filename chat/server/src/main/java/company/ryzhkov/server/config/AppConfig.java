package company.ryzhkov.server.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("company.ryzhkov.server")
@PropertySource("classpath:config.properties")
public class AppConfig {

    @Value("${db.host}")
    private String dbHost;

    @Value("${db.name}")
    private String dbName;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(dbHost);
    }

    @Bean
    public MongoDatabase database() {
        return mongoClient().getDatabase(dbName);
    }

    @Bean("userCollection")
    MongoCollection<Document> userCollection() {
        return database().getCollection("users");
    }
}
