package company.ryzhkov.server.core;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Component
public class NioServer implements Runnable {
    private ByteBuffer welcomeBuff = ByteBuffer.wrap("Welcome!".getBytes());
    private ServerSocketChannel ssc;
    private Selector selector;
    private ByteBuffer buff = ByteBuffer.allocate(256);
    private ReadHandler readHandler;

    @Value("${server.port}")
    private int port;

    @Autowired
    public void setReadHandler(ReadHandler readHandler) {
        this.readHandler = readHandler;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        this.ssc = ServerSocketChannel.open();
        this.ssc.socket().bind(new InetSocketAddress(this.port));
        this.ssc.configureBlocking(false);
        this.selector = Selector.open();
        this.ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            System.out.printf("Server started on port %d\n", port);
            Iterator<SelectionKey> iterator;
            SelectionKey key;
            while (ssc.isOpen()) {
                selector.select();
                iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) handleAccept(key);
                    if (key.isReadable()) readHandler.read(key, buff);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException, server of port " + this.port + " terminating. Stack trace:");
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        String address = sc.socket().getInetAddress().toString() + ":" + sc.socket().getPort();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, address);
        sc.write(welcomeBuff);
        welcomeBuff.rewind();
        System.out.printf("Accepted connection from %s\n", address);
    }

//    private void broadcast(String msg) throws IOException {
//        ByteBuffer msgBuffer = ByteBuffer.wrap(msg.getBytes());
//        selector.keys().forEach(e -> sendMessage(msg, e));
//        for (SelectionKey key : selector.keys()) {
//            if (key.isValid() && key.channel() instanceof SocketChannel) {
//                SocketChannel ch = (SocketChannel) key.channel();
//                ch.write(msgBuffer);
//                msgBuffer.rewind();
//            }
//        }
//    }
}
