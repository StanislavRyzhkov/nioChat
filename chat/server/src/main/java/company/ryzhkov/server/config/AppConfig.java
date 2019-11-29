package company.ryzhkov.server.config;

import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("company.ryzhkov.server")
@PropertySource("classpath:config.properties")
public class AppConfig {
    private MongoConfig mongoConfig;

    @Autowired
    public void setMongoConfig(MongoConfig mongoConfig) {
        this.mongoConfig = mongoConfig;
    }

    @Bean("userCollection")
    MongoCollection<Document> userCollection() {
        return mongoConfig.getUserCollection();
    }
}
