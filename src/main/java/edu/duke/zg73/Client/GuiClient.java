package edu.duke.zg73.Client;

import java.util.concurrent.CompletableFuture;

import edu.duke.zg73.Gui.GuiApp;
import edu.duke.zg73.Gui.GuiPlayer;
import edu.duke.zg73.battleship.App;
import edu.duke.zg73.battleship.BattleShipBoard;
import edu.duke.zg73.battleship.Board;
import edu.duke.zg73.battleship.BoardTextView;
import edu.duke.zg73.battleship.Utils.CommunicateUtils;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

import static javafx.application.Application.launch;

public class GuiClient extends Application {
    public GuiPlayer player;

    public Board<Character> enemyBoard;
    public BoardTextView enemyView;

    public Board<Character> myBoard;
    public BoardTextView myView;
    private Socket clientSocket;

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 1234;

    public GuiClient() throws Exception {

    }



    public void setup(){
        this.clientSocket = connectToServer();
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

    public GuiPlayer createGuiPlayer() throws IOException {

        return new GuiPlayer();
    }
    public void process(Stage stage) throws IOException {
        System.out.println("Game is starting...");
        this.player = createGuiPlayer();
        this.player.start(stage);
        this.player.doPlacementPhase().thenRun(() -> {
            try {
                System.out.println("Placement phase is done.");
                gameSetup(); // 放置阶段完成后执行游戏设置
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }



    public void gameSetup() {
        CompletableFuture.runAsync(() -> {
            try {
                Object a;
                a = CommunicateUtils.recv("receive Game starts", clientSocket);
                a = CommunicateUtils.recv("receive player name", clientSocket);
                System.out.println((String) a);
                player.name = (String) a;

                CommunicateUtils.send("send player board", clientSocket, player.theBoard);
                CommunicateUtils.send("send player view", clientSocket, player.view);



                enemyBoard = (Board<Character>) CommunicateUtils.recv("receive enemy board", clientSocket);
                enemyView = (BoardTextView) CommunicateUtils.recv("receive enemy view", clientSocket);

                a = CommunicateUtils.recv("receive board and view", clientSocket);

                a = CommunicateUtils.recv("start doAttackingPhase", clientSocket);
                doAttackingPhase();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void doAttackingPhase() {
        CompletableFuture.runAsync(() -> {
            try {
                myBoard = (Board<Character>) CommunicateUtils.recv("receive my board", clientSocket);
                myView = (BoardTextView) CommunicateUtils.recv("receive my view", clientSocket);
                enemyBoard = (Board<Character>) CommunicateUtils.recv("receive enemy board", clientSocket);
                enemyView = (BoardTextView) CommunicateUtils.recv("receive enemy view", clientSocket);

                Platform.runLater(() -> {
                    player.setMyBoard(myBoard, myView);
                    player.setEnemyInfo(enemyBoard, enemyView);

                    player.updateBoardDisplay(player.boardGrid, player.theBoard, true);
                    player.updateBoardDisplay(player.enemyGrid, player.theEnemyBoard, false);

                    player.startNewTurn();
                    System.out.println("Start new turn of player: " + player.name);


                    player.playOneTurn().thenRun(() -> {
                        Platform.runLater(() -> {
                            player.endTurn();
                            sendBoardToServer();
                        });
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sendBoardToServer() {
        // 发送棋盘和视图信息回服务器
        CommunicateUtils.send("send player1 board to server", clientSocket, player.theBoard);
        CommunicateUtils.send("send player1 view to server", clientSocket, player.view);
        CommunicateUtils.send("send player2 board to server", clientSocket, enemyBoard);
        CommunicateUtils.send("send player2 view to server", clientSocket, enemyView);

        // 确认当前玩家回合结束
        CommunicateUtils.send("confirm finish player1 turn", clientSocket, "finish player1 turn");

        // 准备开始下一个回合，再次调用doAttackingPhase
        doAttackingPhase();
    }

    public void onlinePlay(Stage stage) throws IOException {
        setup();
        process(stage);
    }



    @Override
    public void start(Stage stage) throws Exception {

        VBox root = new VBox();
        root.setSpacing(10);

        Button btnLocalGame = new Button("start game locally");
        btnLocalGame.setOnAction(event -> {
            try {
                localPlay(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button btnOnlineGame = new Button("start game online");
        btnOnlineGame.setOnAction(event -> {
            try {
                onlinePlay(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        root.getChildren().addAll(btnLocalGame, btnOnlineGame);

        Scene scene = new Scene(root, 1000, 500);
        stage.setTitle("BattleShip Game");
        stage.setScene(scene);
        stage.show();

    }



    public void localPlay(Stage stage) throws IOException {
        System.out.println("Local Game is starting...");
        Platform.runLater(() -> {
            try {
                GuiApp guiApp = new GuiApp();
                Stage newStage = new Stage();
                guiApp.start(newStage); // 使用新的舞台来启动GuiApp
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
