package edu.duke.zg73.Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class GuiApp extends Application {
    private GuiPlayer player1;
    private GuiPlayer player2;

    private AiPlayer aiPlayer;

    @Override
    public void start(Stage primaryStage) {
        List<String> choices = Arrays.asList("Play with local player", "Player with computer");
        ChoiceDialog<String> gameModeDialog = new ChoiceDialog<>("Play with local player", choices);
        gameModeDialog.setTitle("Game mode");
        gameModeDialog.setHeaderText("Choose your Game mode");
        gameModeDialog.setContentText("Please choose:");

        Optional<String> gameModeResult = gameModeDialog.showAndWait();

        gameModeResult.ifPresent(gameMode -> {
            if (gameMode.equals("Play with local player")) {
                promptForPlayerNames(true, primaryStage);
            } else {
                promptForPlayerNames(false, primaryStage);
            }
        });
    }

    private void promptForPlayerNames(boolean isLocalMultiplayer, Stage primaryStage) {
        TextInputDialog dialog = new TextInputDialog("Player");
        dialog.setTitle("Player configuration");
        dialog.setHeaderText(isLocalMultiplayer ? "player 1 configuration" : "player configuration");
        dialog.setContentText("Please enter Player name:");

        Optional<String> result = dialog.showAndWait();

        if (isLocalMultiplayer) {
            result.ifPresent(name1 -> {
                TextInputDialog dialog2 = new TextInputDialog("Player 2");
                dialog2.setTitle("player configuration");
                dialog2.setHeaderText("player 2 configuration");
                dialog2.setContentText("Please enter 2 name:");

                Optional<String> result2 = dialog2.showAndWait();
                result2.ifPresent(name2 -> setupGame(name1, name2, primaryStage));
            });
        } else {
            result.ifPresent(name -> setupAiGame(name, primaryStage));
        }
    }

    private void setupAiGame(String name1, Stage primaryStage){
        Platform.runLater(() -> {
            player1 = new GuiPlayer();
            player1.name = name1;
            aiPlayer = new AiPlayer();
            aiPlayer.name = "AI";


            aiPlayer.doPlacementPhase();




            player1.start(primaryStage);
            player1.doPlacementPhase().thenRun(this::startAiGame);

            player1.doPlacementPhase().thenRun(() -> {
                System.out.println("this is start AI GAME");
                startAiGame();
                System.out.println("this is flag2");
            });
        });

    }



    private void startAiGame() {
        try{
            System.out.println("this is Ai startGame");
            doAiAttackingPhase();
        }catch (IOException e) {
            System.out.println("this is Ai startGame error");
            System.out.println(e.getMessage());
        }

    }

    private void doAiAttackingPhase() throws IOException {
        System.out.println("this is Ai doAttackingPhase");
        doAiPlayerTurn(player1, aiPlayer);
    }

    private void doAiPlayerTurn(GuiPlayer guiPlayer, AiPlayer aiPlayer) throws IOException {
        System.out.println("this is doPlayerTurn");
        guiPlayer.setEnemyInfo(aiPlayer.theBoard, aiPlayer.view);
        guiPlayer.playOneTurn().thenRun(() -> {
            try {
                if(guiPlayer.theBoard.is_lost()){
                    guiPlayer.win_message();
                    return;
                }

                aiPlayer.playOneTurn(guiPlayer.theBoard, guiPlayer.view);


                if (aiPlayer.theBoard.is_lost()) {
                    Platform.runLater(guiPlayer::win_message);
                } else {
                    doAiPlayerTurn(guiPlayer, aiPlayer);
                }
            } catch (IOException e) {
                System.out.println("this is doPlayerTurn error");
                System.out.println(e.getMessage());
            }
        });
    }

    private void setupGame(String name1, String name2, Stage primaryStage) {
        Platform.runLater(() -> {
            player1 = new GuiPlayer();
            player1.name = name1;
            player2 = new GuiPlayer();
            player2.name = name2;

            Stage stageForPlayer2 = new Stage();

            player1.start(primaryStage);
            if (!"AI".equals(name2)) {
                player2.start(stageForPlayer2);
            }

            doPlacementPhase().thenRun(this::startGame);
        });
    }



    private CompletableFuture<Void> doPlacementPhase() {
        CompletableFuture<Void> player1Placement = player1.doPlacementPhase();
        CompletableFuture<Void> player2Placement = player2.doPlacementPhase();


        player1.setEnemyInfo(player2.theBoard, player2.view);
        player2.setEnemyInfo(player1.theBoard, player1.view);
        player2.endTurn();
        return CompletableFuture.allOf(player1Placement, player2Placement);
    }

    private void startGame() {
        System.out.println("this is startGame");
        doAttackingPhase();
    }

    private void doAttackingPhase() {
        System.out.println("this is doAttackingPhase");
        doPlayerTurn(player1, player2);
    }

    private void doPlayerTurn(GuiPlayer attacker, GuiPlayer defender) {
        System.out.println("this is doPlayerTurn");
        if (!attacker.theEnemyBoard.is_lost()) {
            attacker.setEnemyInfo(defender.theBoard, defender.view);
            attacker.playOneTurn().thenRun(() -> {
                Platform.runLater(() -> {
                    defender.startNewTurn();
                    attacker.endTurn();
                    doPlayerTurn(defender, attacker);
                });
            });
        } else {
            Platform.runLater(defender::win_message);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
