package company.ryzhkov.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("company.ryzhkov.server")
@PropertySource("classpath:config.properties")
public class AppConfig {
}
