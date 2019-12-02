package company.ryzhkov.server.dispatcher;

import company.ryzhkov.server.profiling.Watch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GenericDispatcher {
    private Map<String, Controller> map;

    @Autowired
    public void setMap(Map<String, Controller> map) {
        this.map = map;
    }

    @Watch
    public Mono<String> handleMessage(String message) {
        int spaceIndex = message.indexOf(0x0020);
        String beanName = message.substring(1, spaceIndex);
        String command = message.substring(spaceIndex);
        Controller bar = map.get(beanName);
        return bar.dispatch(command);
    }
}
