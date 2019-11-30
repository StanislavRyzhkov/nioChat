package company.ryzhkov.server.core;

import company.ryzhkov.server.manage.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Component
public class ReadHandler {
    private Dispatcher dispatcher;
    private ResponseHandler responseHandler;

    @Autowired
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Autowired
    public void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Watch
    public void read(SelectionKey key, ByteBuffer buff) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();
        buff.clear();
        int read = 0;
        while ((read = ch.read(buff)) > 0) {
            buff.flip();
            byte[] bytes = new byte[buff.limit()];
            buff.get(bytes);
            sb.append(new String(bytes, StandardCharsets.UTF_8));
            buff.clear();
        }
        String msg;
        if (read < 0) {
            msg = key.attachment() + " left the chat\n";
            ch.close();
        } else {
            System.out.println(key.attachment());
            msg = sb.toString();
        }
        dispatcher.handleMessage(msg)
                .doOnNext(e -> responseHandler.sendMessage(e, key))
                .doOnError(System.out::println)
                .subscribe();
    }
}
