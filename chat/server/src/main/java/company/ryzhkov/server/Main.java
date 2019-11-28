package company.ryzhkov.server;

import company.ryzhkov.server.config.AppConfig;
import company.ryzhkov.server.core.NioServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        NioServer server = context.getBean("nioServer", NioServer.class);
        new Thread(server).start();
    }
}
