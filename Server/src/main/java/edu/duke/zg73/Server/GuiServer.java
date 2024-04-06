//package edu.duke.zg73.Server;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.channels.*;
//import java.util.Iterator;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class GuiServer{
//    private static final int SERVER_PORT = 1234;
//    private static final int THREAD_POOL_SIZE = 10;
//    private ExecutorService executorService;
//    private ServerSocketChannel serverSocketChannel;
//    private Selector selector;
//
//    private boolean isRunning = true;
//
//    public GuiServer() {
//        try {
//            this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
//            this.serverSocketChannel = ServerSocketChannel.open();
//            this.serverSocketChannel.bind(new InetSocketAddress(SERVER_PORT));
//            this.serverSocketChannel.configureBlocking(false);
//            this.selector = Selector.open();
//            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void run() {
//        System.out.println("Server is running on port " + SERVER_PORT);
//        SocketChannel player1SocketChannel = null;
//        SocketChannel player2SocketChannel = null;
//        while (isRunning) {
//            try {
//                selector.select();
//                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//                while (iterator.hasNext()) {
//                    SelectionKey key = iterator.next();
//                    iterator.remove();
//                    if (!key.isValid()) {
//                        continue;
//                    }
//                    if (key.isAcceptable()) {
//                        SocketChannel clientSocketChannel = serverSocketChannel.accept();
//                        if (clientSocketChannel != null) {
//                            clientSocketChannel.configureBlocking(false);
//                            System.out.println("Client connected: " + clientSocketChannel.getRemoteAddress());
//                            if (player1SocketChannel == null) {
//                                player1SocketChannel = clientSocketChannel;
//                                System.out.println("Player 1 has connected.");
//                            } else if (player2SocketChannel == null) {
//                                player2SocketChannel = clientSocketChannel;
//                                System.out.println("Player 2 has connected.");
//                                // Start game session for these two players
//                                GuiGameSession gameSession = new GuiGameSession(player1SocketChannel, player2SocketChannel);
//                                executorService.submit(gameSession);
//                                // Reset for the next pair of players
//                                player1SocketChannel = null;
//                                player2SocketChannel = null;
//                            }
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        stop();
//    }
//
//    public void stop() {
//        isRunning = false;
//        try {
//            if (selector != null) selector.close();
//            if (serverSocketChannel != null) serverSocketChannel.close();
//            if (executorService != null) executorService.shutdown();
//            System.out.println("Server stopped.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        Server server = new Server();
//        new Thread(server::run).start();
//    }
//}
