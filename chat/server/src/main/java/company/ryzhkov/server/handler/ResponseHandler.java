package company.ryzhkov.server.handler;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

@Component
public class ResponseHandler {

    @SneakyThrows
    public void sendMessage(String message, SelectionKey key) {
        if (key.isValid() && key.channel() instanceof SocketChannel) {
            ByteBuffer bb = ByteBuffer.wrap(message.getBytes());
            ((SocketChannel) key.channel()).write(bb);
            bb.rewind();
        }
    }
}
