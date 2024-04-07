package edu.duke.zg73.Client;

import edu.duke.zg73.battleship.*;
import edu.duke.zg73.battleship.Utils.CommunicateUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket clientSocket;

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 1234;
    private BufferedReader inputReader;

    public void setInputReader(BufferedReader inputReader) {
        this.inputReader = inputReader;
    }


    public Client() throws Exception {

    }

    public void setup(){
        this.clientSocket = connectToServer();
        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
        if (this.clientSocket == null) {
            System.out.println("Server connection failed. Exiting.");
            System.exit(0);
        }
        System.out.println("Connected to server. Enjoy our services.\n");
    }

    private Socket connectToServer() {
        final int MAX_RETRY = 2;
        int retryCount = 0;
        while (retryCount < MAX_RETRY) {
            try {
                return new Socket(SERVER_HOST, SERVER_PORT);
            } catch (IOException e) {
                System.out.println("Failed to connect to server. Trying again.");
                retryCount++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        System.out.println("Connection failed after " + MAX_RETRY + " attempts.");
        return null;
    }

    public TextPlayer createTexPlayer() throws IOException {
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
        V2ShipFactory shipFactory = new V2ShipFactory();
        Board<Character> board = new BattleShipBoard<>(10, 20, 'X');
        return new TextPlayer("Player", board, bufferReader, System.out, shipFactory);
    }

    public void initializeGame() throws IOException {
        System.out.println("\n\nWelcome to the Battleship game\n");
        TextPlayer player = createTexPlayer();
        player.doPlacementPhase();

        CommunicateUtils.recv("receive start sign", clientSocket);
        CommunicateUtils.send("send player", clientSocket, player);
    }

    public void gameSetup() throws IOException, ClassNotFoundException {
        TextPlayer player = (TextPlayer) CommunicateUtils.recv("receive player", clientSocket);
        TextPlayer enemy = (TextPlayer) CommunicateUtils.recv("receive enemy", clientSocket);
        resetIO(player, enemy);
        System.out.println("Game setup complete.");
    }

    public void gameLoop() throws IOException, ClassNotFoundException {
        TextPlayer player, enemy;
        while (true) {
            player = (TextPlayer) CommunicateUtils.recv("receive player in loop", clientSocket);
            enemy = (TextPlayer) CommunicateUtils.recv("receive enemy in loop", clientSocket);
            resetIO(player, enemy);
            player.playOneTurn(enemy.theBoard, enemy.view);

            if (enemy.theBoard.is_lost()) {
                player.win_message();
                break;
            }
            CommunicateUtils.send("send player", clientSocket, player);
            CommunicateUtils.send("send enemy", clientSocket, enemy);
        }
    }



    private void resetIO(TextPlayer player, TextPlayer enemy) {
        player.reset_IO();
        enemy.reset_IO();
    }

    public static void main(String[] args) throws Exception {
        Client myClient = new Client();
        myClient.setup();
        myClient.initializeGame();
        myClient.gameSetup();
        myClient.gameLoop();
    }
}

