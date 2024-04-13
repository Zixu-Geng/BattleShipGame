package edu.duke.zg73.Server;

import edu.duke.zg73.battleship.Utils.CommunicateUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int SERVER_PORT = 1234;
    private static final int THREAD_POOL_SIZE = 10;
    private ExecutorService executorService;
    private ServerSocket serverSocket;

    private boolean isRunning = true;

    public Server() {
        try {
            this.serverSocket = new ServerSocket(SERVER_PORT);
            this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        System.out.println("begin run ");
        while (isRunning) {
            System.out.println("Server is running...");
            match();
        }
    }

    public void match(){
        try {
            System.out.println("Waiting for the first client to connect...");
            Socket clientSocket1 = serverSocket.accept();
            System.out.println("First client connected: " + clientSocket1.getInetAddress().getHostAddress());

            System.out.println("Waiting for the second client to connect...");
            Socket clientSocket2 = serverSocket.accept();
            System.out.println("Second client connected: " + clientSocket2.getInetAddress().getHostAddress());
            CommunicateUtils.send("send Game starts to player1", clientSocket1, "Game starts!!");
            CommunicateUtils.send("send Game starts to player2", clientSocket2, "Game starts!!");
            GuiGameSession guigameSession = new GuiGameSession(clientSocket1, clientSocket2);

            executorService.submit(guigameSession);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        try {

            this.executorService.shutdown();
            // Close the server socket
            this.serverSocket.close();
            System.out.println("Server stops serving.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println("Hello, World!");
        Server myServer = new Server();
        myServer.run();
        myServer.stop();

    }
}
