package edu.duke.zg73.battleship.Utils;


import javafx.application.Platform;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class AsynCommunicateUtils{

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void send(String resultDescripString, SocketChannel socketChannel, Serializable object) {
        executor.submit(() -> {
            try {
                // 序列化对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                oos.flush();
                byte[] bytes = baos.toByteArray();

                // 发送对象大小和对象本身
                ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + bytes.length);
                buffer.putInt(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
                Platform.runLater(() -> System.out.println(resultDescripString));
            } catch (IOException e) {
                Platform.runLater(() -> System.out.println("Error: " + resultDescripString + ". " + e.getMessage()));
            }
        });
    }

    public static void recv(String resultDescripString, SocketChannel socketChannel, Consumer<Object> onReceive) {
        executor.submit(() -> {
            try {
                ByteBuffer sizeBuffer = ByteBuffer.allocate(Integer.BYTES);
                while (sizeBuffer.hasRemaining()) {
                    if (socketChannel.read(sizeBuffer) == -1) {
                        throw new IOException("Connection closed by peer");
                    }
                }
                sizeBuffer.flip();
                int size = sizeBuffer.getInt();

                ByteBuffer dataBuffer = ByteBuffer.allocate(size);
                while (dataBuffer.hasRemaining()) {
                    if (socketChannel.read(dataBuffer) == -1) {
                        throw new IOException("Connection closed by peer");
                    }
                }
                dataBuffer.flip();
                ByteArrayInputStream bais = new ByteArrayInputStream(dataBuffer.array());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object object = ois.readObject();

                Platform.runLater(() -> {
                    System.out.println(resultDescripString);
                    onReceive.accept(object);
                });
            } catch (IOException | ClassNotFoundException e) {
                Platform.runLater(() -> System.out.println("Error: " + resultDescripString + ". " + e.getMessage()));
            }
        });
    }
}

