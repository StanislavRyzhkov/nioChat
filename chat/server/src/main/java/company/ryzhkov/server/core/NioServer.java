package company.ryzhkov.server.core;

import company.ryzhkov.server.manage.Dispatcher;
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
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Component
public class NioServer implements Runnable {
    private ByteBuffer welcomeBuff = ByteBuffer.wrap("Welcome!".getBytes());
    private ServerSocketChannel ssc;
    private Selector selector;
    private ByteBuffer buff = ByteBuffer.allocate(256);
    private Dispatcher dispatcher;

    @Value("${server.port}")
    private int defaultPort;

    @Autowired
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        this.ssc = ServerSocketChannel.open();
        this.ssc.socket().bind(new InetSocketAddress(this.defaultPort));
        this.ssc.configureBlocking(false);
        this.selector = Selector.open();
        this.ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            System.out.printf("Server started on port %d\n", defaultPort);
            Iterator<SelectionKey> iterator;
            SelectionKey key;
            while (ssc.isOpen()) {
                selector.select();
                iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) handleAccept(key);
                    if (key.isReadable()) handleRead(key);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException, server of port " + this.defaultPort + " terminating. Stack trace:");
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

    private void handleRead(SelectionKey key) throws IOException {
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
//            msg = key.attachment() + ": " + sb.toString();
            msg = sb.toString();
        }
        dispatcher.handleMessage(msg)
                .doOnNext(e -> this.sendMessage(e, key))
                .doOnError(System.out::println)
                .subscribe();
//        broadcast(msg);
    }

    @SneakyThrows
    private void sendMessage(String message, SelectionKey key) {
        if (key.isValid() && key.channel() instanceof SocketChannel) {
            ByteBuffer bb = ByteBuffer.wrap(message.getBytes());
            ((SocketChannel) key.channel()).write(bb);
            bb.rewind();
        }
    }

    private void broadcast(String msg) throws IOException {
        ByteBuffer msgBuffer = ByteBuffer.wrap(msg.getBytes());
        selector.keys().forEach(e -> sendMessage(msg, e));
//        for (SelectionKey key : selector.keys()) {
//            if (key.isValid() && key.channel() instanceof SocketChannel) {
//                SocketChannel ch = (SocketChannel) key.channel();
//                ch.write(msgBuffer);
//                msgBuffer.rewind();
//            }
//        }
    }
}
