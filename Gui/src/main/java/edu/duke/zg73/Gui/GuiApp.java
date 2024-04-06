package edu.duke.zg73.Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class GuiApp extends Application {
    private GuiPlayer player1;
    private GuiPlayer player2;

    @Override
    public void start(Stage primaryStage) {
        List<String> choices = Arrays.asList("本地玩家对战", "与电脑对战");
        ChoiceDialog<String> gameModeDialog = new ChoiceDialog<>("本地玩家对战", choices);
        gameModeDialog.setTitle("选择游戏模式");
        gameModeDialog.setHeaderText("选择游戏模式");
        gameModeDialog.setContentText("请选择:");

        Optional<String> gameModeResult = gameModeDialog.showAndWait();

        gameModeResult.ifPresent(gameMode -> {
            if (gameMode.equals("本地玩家对战")) {
                promptForPlayerNames(true, primaryStage);
            } else {
                promptForPlayerNames(false, primaryStage);
            }
        });
    }

    private void promptForPlayerNames(boolean isLocalMultiplayer, Stage primaryStage) {
        TextInputDialog dialog = new TextInputDialog("Player");
        dialog.setTitle("玩家设置");
        dialog.setHeaderText(isLocalMultiplayer ? "玩家 1 设置" : "玩家设置");
        dialog.setContentText("请输入玩家名字:");

        Optional<String> result = dialog.showAndWait();

        if (isLocalMultiplayer) {
            result.ifPresent(name1 -> {
                TextInputDialog dialog2 = new TextInputDialog("Player 2");
                dialog2.setTitle("玩家设置");
                dialog2.setHeaderText("玩家 2 设置");
                dialog2.setContentText("请输入玩家 2 的名字:");

                Optional<String> result2 = dialog2.showAndWait();
                result2.ifPresent(name2 -> setupGame(name1, name2, primaryStage));
            });
        } else {
            result.ifPresent(name -> setupGame(name, "AI", primaryStage));
        }
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
        if (!defender.theEnemyBoard.is_lost()) {
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
