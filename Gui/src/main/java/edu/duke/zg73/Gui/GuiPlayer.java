package edu.duke.zg73.Gui;

import edu.duke.zg73.battleship.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class GuiPlayer extends Application implements Serializable {
    public Board<Character> theBoard;
    public BoardTextView view;

    public Board<Character> theEnemyBoard;
    public BoardTextView enemyView;
    private V2ShipFactory shipFactory;
    public String name = "Player 1";
    private ArrayList<String> shipsToPlace;
    private HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;
    private HashMap<String, Integer> skill_count;
    private Label messageLabel;

    private Label playerNameLabel;
    public GridPane boardGrid;
    public GridPane enemyGrid;
    private Stage primaryStage;

    Button fireButton;
    Button moveButton;
    Button sonarButton;
    private boolean hasFired = false;
    private boolean hasMoved = false;
    private boolean hasScanned = false;


    public boolean isMyTurn = true;
    public boolean isReady = false;

    public GuiPlayer() {
        // 初始化工厂、放置列表、创建函数和技能计数
        shipFactory = new V2ShipFactory();
        shipsToPlace = new ArrayList<>();
        shipCreationFns = new HashMap<>();
        skill_count = new HashMap<>();
        setupShipCreationList();
        System.out.println(shipsToPlace);
        setupShipCreationMap();
        initSkillCount();

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEnemyInfo(Board<Character> enemyBoard, BoardTextView enemyView) {
        this.theEnemyBoard = enemyBoard;
        this.enemyView = enemyView;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialization code for factories, boards, views, and game settings
        shipFactory = new V2ShipFactory();
        theBoard = new BattleShipBoard<>(10, 20, 'X');
        view = new BoardTextView(theBoard);
        theEnemyBoard = new BattleShipBoard<>(10, 20, 'X');
        enemyView = new BoardTextView(theEnemyBoard);

        shipsToPlace = new ArrayList<>();
        shipCreationFns = new HashMap<>();
        skill_count = new HashMap<>();
        setupShipCreationList();
        setupShipCreationMap();
        initSkillCount();

        // UI Component Initialization
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        enemyGrid = new GridPane();
        enemyGrid.setAlignment(Pos.CENTER);

        messageLabel = new Label();
        messageLabel.setMinHeight(50);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setStyle("-fx-font-size: 16px;");

        playerNameLabel = new Label("Player Name: " + name);
        playerNameLabel.setAlignment(Pos.CENTER);
        playerNameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        fireButton = new Button("Fire");
        moveButton = new Button("Move (" + skill_count.get("M Move a ship to another square") + ")");
        sonarButton = new Button("Sonar (" + skill_count.get("S Sonar scan") + ")");

        HBox buttonBox = new HBox(20, fireButton, moveButton, sonarButton);
        buttonBox.setAlignment(Pos.CENTER);

        initializeEmptyBoard(boardGrid, theBoard);
        initializeEmptyBoard(enemyGrid, theEnemyBoard);

        // Creating row labels (0-9)
        VBox rowLabelsLeft = createRowLabels(theBoard.getHeight());
        VBox rowLabelsRight = createRowLabels(theEnemyBoard.getHeight());

        // Creating column labels (A-T)
        HBox columnLabelsTop = createColumnLabels(theBoard.getWidth());
        HBox columnLabelsBottom = createColumnLabels(theEnemyBoard.getWidth());

        // Combine board and labels
        HBox boardWithLabels = new HBox(rowLabelsLeft, boardGrid);
        boardWithLabels.setAlignment(Pos.CENTER_LEFT);
        HBox enemyBoardWithLabels = new HBox(rowLabelsRight, enemyGrid);
        enemyBoardWithLabels.setAlignment(Pos.CENTER_LEFT);

        // Create a VBox with column labels on top and the board with row labels below
        VBox boardWithAllLabels = new VBox(columnLabelsTop, boardWithLabels);
        VBox enemyBoardWithAllLabels = new VBox(columnLabelsBottom, enemyBoardWithLabels);

        // Main layout of the scene
        HBox boardsBox = new HBox(30, boardWithAllLabels, enemyBoardWithAllLabels);
        boardsBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20, playerNameLabel, buttonBox, boardsBox, messageLabel);
        mainLayout.setAlignment(Pos.CENTER);

        // Final scene and stage setup
        BorderPane root = new BorderPane(mainLayout);
        Scene scene = new Scene(root, 1200, 800); // Width may need to be adjusted to fit labels
        primaryStage.setScene(scene);
        primaryStage.setTitle("Battleship Game");
        primaryStage.show();
    }

    private VBox createRowLabels(int numRows) {
        VBox rowLabels = new VBox();
        for (int i = 0; i < numRows; i++) {
            Label label = new Label(Integer.toString(i));
            label.setMinSize(30, 30);
            label.setAlignment(Pos.CENTER_RIGHT);
            rowLabels.getChildren().add(label);
        }
        return rowLabels;
    }

    private HBox createColumnLabels(int numColumns) {
        HBox columnLabels = new HBox();
        for (int i = 0; i < numColumns; i++) {
            Label label = new Label(Character.toString((char) ('A' + i % 26)));
            label.setMinSize(30, 30);
            label.setAlignment(Pos.BOTTOM_CENTER);
            columnLabels.getChildren().add(label);
        }
        return columnLabels;
    }
    public boolean isReady() {
        return isReady;
    }



    private void configureButtons() {
        fireButton.setOnAction(e -> doaction_F());
        moveButton.setOnAction(e -> doaction_M());
        sonarButton.setOnAction(e -> doaction_S());

    }



    private void initializeEmptyBoard(GridPane boardGrid, Board<Character> board) {
        boardGrid.getChildren().clear();
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Label cellLabel = new Label();
                cellLabel.setMinSize(30, 30);
                cellLabel.setAlignment(Pos.CENTER);
                cellLabel.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: lightblue;");
                GridPane.setConstraints(cellLabel, col, row);
                boardGrid.getChildren().add(cellLabel);
            }
        }
    }


    public void updateBoardDisplay(GridPane boardGrid, Board<Character> theBoard, boolean isSelf) {
        Platform.runLater(() -> {
            boardGrid.getChildren().clear();

            for (int row = 0; row < theBoard.getHeight(); row++) {
                for (int col = 0; col < theBoard.getWidth(); col++) {
                    Character cellContent;
                    if(isSelf){
                        cellContent = theBoard.whatIsAtForSelf(new Coordinate(row, col));
                    }else{
                        cellContent = theBoard.whatIsAtForEnemy(new Coordinate(row, col));
                    }

                    Label cellLabel = new Label(cellContent == null ? "" : cellContent.toString());
                    cellLabel.setMinSize(30, 30);
                    cellLabel.setAlignment(Pos.CENTER);
                    cellLabel.setStyle("-fx-border-color: black; -fx-border-width: 0.5;"); // 设置边框样式


                    if (cellContent == null) {
                        cellLabel.setStyle(cellLabel.getStyle() + "-fx-background-color: lightblue;"); // 空水域
                    } else {
                        switch (cellContent) {
                            case 'S': // 船只
                                cellLabel.setStyle(cellLabel.getStyle() + "-fx-background-color: lightgray;");
                                break;
                            case '*': // 被击中的船只
                                cellLabel.setStyle(cellLabel.getStyle() + "-fx-background-color: red;");
                                break;
                            case ' ': // 未探测的区域
                            default:
                                cellLabel.setStyle(cellLabel.getStyle() + "-fx-background-color: lightblue;");
                                break;
                        }
                    }

                    // 将单元格添加到 GridPane
                    GridPane.setConstraints(cellLabel, col, row);
                    boardGrid.getChildren().add(cellLabel);
                }
            }
        });
    }


    private void updateSkillButtons() {
        Platform.runLater(() -> {
            moveButton.setText("Move (" + skill_count.get("M Move a ship to another square") + ")");
            sonarButton.setText("Sonar (" + skill_count.get("S Sonar scan") + ")");
        });
    }




    private void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(0, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(0, "Destroyer"));
        shipsToPlace.addAll(Collections.nCopies(0, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(1, "Carrier"));
    }

    private void setupShipCreationMap() {
        shipCreationFns.put("Submarine", shipFactory::makeSubmarine);
        shipCreationFns.put("Destroyer", shipFactory::makeDestroyer);
        shipCreationFns.put("Battleship", shipFactory::makeBattleship);
        shipCreationFns.put("Carrier", shipFactory::makeCarrier);
    }

    private void initSkillCount() {
        skill_count.put("M Move a ship to another square", 3);
        skill_count.put("S Sonar scan", 3);
    }


    public CompletableFuture<Void> doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) {
        CompletableFuture<Void> placementComplete = new CompletableFuture<>();

        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Place Your " + shipName);
            dialog.setHeaderText("Placing " + shipName);
            dialog.setContentText("Enter the position and orientation (e.g., A0V):");

            dialog.showAndWait().ifPresent(input -> {
                try {
                    Placement placement = new Placement(input);
                    Ship<Character> ship = createFn.apply(placement);
                    String error = theBoard.tryAddShip(ship);
                    if (error == null) {
                        updateBoardDisplay(boardGrid, theBoard, true);
                        placementComplete.complete(null);
                    } else {
                        showAlert("Error placing " + shipName, error);
                        doOnePlacement(shipName, createFn);
                    }
                } catch (IllegalArgumentException e) {
                    showAlert("Invalid Input", "Please enter a valid position and orientation (e.g., A0V).");
                    doOnePlacement(shipName, createFn);
                }
            });
        });

        return placementComplete;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showCustomDialog(String title, String content) {
        // Create a new stage for the custom dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);

        // Set up the layout for the dialog
        VBox vbox = new VBox(new Label(content));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        // Create and set the scene for the dialog
        Scene dialogScene = new Scene(vbox, 300, 200);
        dialogStage.setScene(dialogScene);

        // Show the custom dialog and wait for it to be closed
        dialogStage.showAndWait();
    }
    public CompletableFuture<Void> doPlacementPhase() {
        CompletableFuture<Void> placementComplete = new CompletableFuture<>();
        Platform.runLater(() -> {
            messageLabel.setText("Starting placement phase for " + name + "...");
        });
        placeShipsAsync(0, placementComplete);
        return placementComplete;
    }

    private void placeShipsAsync(int index, CompletableFuture<Void> placementComplete) {
        if (index < shipsToPlace.size()) {
            String shipName = shipsToPlace.get(index);
            doOnePlacement(shipName, shipCreationFns.get(shipName))
                    .thenRun(() -> placeShipsAsync(index + 1, placementComplete));
        } else {
            Platform.runLater(() -> {
                messageLabel.setText("All ships placed. Ready to start the game!");
                placementComplete.complete(null); // 标记放置完成
            });
        }
    }


    public void doaction_F() {
        System.out.println("doaction_F");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Fire!");
        dialog.setHeaderText("Attack");
        dialog.setContentText("Player " + name + ", Please input attack place:");
        System.out.println("doaction_F by show dialog");
        dialog.showAndWait().ifPresent(input -> {
            System.out.println("doaction_F by showAndWait");
//            Platform.runLater(() -> {
                try {
                    System.out.println("doaction_F by try");
                    Coordinate attackPlace = new Coordinate(input);
                    executeAttack(attackPlace);
                    System.out.println("execute attack with enemy miss: " + theEnemyBoard.get_enemyMiss());
                    System.out.println("finish doation F");
                } catch (IllegalArgumentException ex) {
                    showAlert("Invalid coordinate", "Invalid coordinate. Please try again.");
                    doaction_F(); // Recursively call doaction_F to ask for input again
                }
            });
//        });
    }

    private void executeAttack(Coordinate attackPlace) {
//        Platform.runLater(() -> {
            Character hitDisplay = theEnemyBoard.whatIsAtForSelf(attackPlace);
            Ship<Character> hitShip = theEnemyBoard.fireAt(attackPlace);
            System.out.println("execute is running");
            System.out.println("enemy miss is: " + theEnemyBoard.get_enemyMiss());
            if (hitShip == null) {
                if (hitDisplay == null) {
                    showAlert("Missed", "You missed. No ship at " + attackPlace + ".");
                } else {
                    showAlert("Already Hit", "You've already hit this location: " + attackPlace + ".");
                }
            } else {
                if (hitDisplay == '*') {
                    showAlert("Already Hit", "You've already hit this location: " + attackPlace + ".");
                } else {
                    // Successful hit.
                    showAlert("Hit", "Hit at " + attackPlace + "! You hit a " + hitShip.getName() + "!");
                }
            }

            updateBoardDisplay(boardGrid, theBoard, true);
            updateBoardDisplay(enemyGrid, theEnemyBoard, false);

            System.out.println("update board in execute attack");

//        });
    }
    protected void doaction_M() {
        ArrayList<Ship<Character>> availableShips = theBoard.get_avaliable();
        if (availableShips.isEmpty()) {
            showAlert("No Ship Available", "No ship to move.");
            return;
        }

        StringBuilder shipsInfo = new StringBuilder("Available ships to move:\n");
        for (Ship<Character> ship : availableShips) {
            shipsInfo.append(ship.getName()).append("\n");
        }
        showAlert("Available Ships", shipsInfo.toString());

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Move Ship");
        dialog.setHeaderText("Moving Ship");
        dialog.setContentText("Please input the coordinate of the ship you want to move:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                Coordinate chosenCoord = new Coordinate(input);
                Ship<Character> chosenShip = null;
                for (Ship<Character> ship : availableShips) {
                    if (ship.occupiesCoordinates(chosenCoord)) {
                        chosenShip = ship;
                        break;
                    }
                }
                if (chosenShip == null) {
                    showAlert("Error", "No Ship in your input location.");
                    return;
                }

                final Ship<Character>[] chosenShipContainer = new Ship[1];
                chosenShipContainer[0] = chosenShip;

                TextInputDialog newPlacementDialog = new TextInputDialog();
                newPlacementDialog.setTitle("New Placement");
                newPlacementDialog.setHeaderText("Placing " + chosenShip.getName());
                newPlacementDialog.setContentText("Please input the new place and orientation (e.g., A0V):");

                newPlacementDialog.showAndWait().ifPresent(newPlacementInput -> {
                    try {
                        Placement newPlacement = new Placement(newPlacementInput);

                        theBoard.moveShip(chosenShipContainer[0], newPlacement);
                        skill_count.put("M Move a ship to another square", skill_count.get("M Move a ship to another square") - 1);
                        updateSkillButtons();
                        updateBoardDisplay(boardGrid, theBoard, true);
                        updateBoardDisplay(enemyGrid, theEnemyBoard, false);
                    } catch (IllegalArgumentException e) {
                        showAlert("Error Moving Ship", e.getMessage());
                        // 无需重新尝试，因为用户可以再次点击“Move”按钮
                    }
                });
            } catch (IllegalArgumentException e) {
                showAlert("Invalid Coordinate", e.getMessage());
            }
        });
    }

    protected void doaction_S() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sonar Scan");
        dialog.setHeaderText("Sonar Scan Action");
        dialog.setContentText("Please enter the center of the area you want to scan: [Coordinate]");

        dialog.showAndWait().ifPresent(input -> {
            try {
                Coordinate scanCoord = new Coordinate(input);
                performSonarScan(scanCoord);
            } catch (IllegalArgumentException ex) {
                showAlert("Invalid Coordinate", "Invalid coordinate. Please try again.");
                doaction_S();
            }
        });
    }

    private void performSonarScan(Coordinate scanCoord) {
        // Assume performSonarScan runs on the FX Application thread, otherwise wrap with Platform.runLater
        int[] scanResult = {0, 0, 0, 0};
        int maxDistance = 3;

        for (int i = scanCoord.getRow() - maxDistance; i <= scanCoord.getRow() + maxDistance; i++) {
            for (int j = scanCoord.getColumn() - maxDistance; j <= scanCoord.getColumn() + maxDistance; j++) {
                if (i >= 0 && i < theEnemyBoard.getHeight() && j >= 0 && j < theEnemyBoard.getWidth()) {



                    Character result = theEnemyBoard.whatIsAtForSelf(new Coordinate(i, j));
                    Character result2 = theEnemyBoard.whatIsAtForEnemy(new Coordinate(i, j));


                    if (result == null) continue;

                    if(result == '*'){
                        result = result2;
                    }

                    System.out.println("this is result for coordinate: " + i + " " + j + ", this is for self" + result + ", this is result for enemy: " + result2);


                    switch (result) {
                        case 's': scanResult[0]++; break;
                        case 'd': scanResult[1]++; break;
                        case 'b': scanResult[2]++; break;
                        case 'B': scanResult[2]++; break;
                        case 'c': scanResult[3]++; break;
                        case 'C': scanResult[3]++; break;
                        default: break; // 不计算找不到或为空的情况
                    }


                }
            }
        }

        System.out.println("this is scan result: " + scanResult[0] + " " + scanResult[1] + " " + scanResult[2] + " " + scanResult[3]);

        // Display the results in a dialog instead of the messageArea
        String scanResultsMessage = String.format(
                "Sonar scan results:\n" +
                        "Submarines occupy %d square(s).\n" +
                        "Destroyers occupy %d square(s).\n" +
                        "Battleships occupy %d square(s).\n" +
                        "Carriers occupy %d square(s).\n",
                scanResult[0], scanResult[1], scanResult[2], scanResult[3]
        );

        showCustomDialog("Sonar Scan Results", scanResultsMessage);


        // Update the sonar scan count
        skill_count.put("S Sonar scan", skill_count.get("S Sonar scan") - 1);
        updateSkillButtons();
    }


    private void disableAllButtons() {
//        Platform.runLater(() -> {
            fireButton.setDisable(true);
            moveButton.setDisable(true);
            sonarButton.setDisable(true);
//        });
    }


    public CompletableFuture<Void> playOneTurn() {
        CompletableFuture<Void> turnComplete = new CompletableFuture<>();
        messageLabel.setText("This is your turn");
        updateBoardDisplay(boardGrid, theBoard, true);
        updateBoardDisplay(enemyGrid, theEnemyBoard, false);
        System.out.println("palyer one turn in gui");
//        Platform.runLater(() -> {
            if(theEnemyBoard.is_lost()){
                showCustomDialog("Game Over", "Player " + name + ", You win!");
                turnComplete.complete(null);
                return turnComplete;
            }
            if(theBoard.is_lost()){
                showCustomDialog("Game Over", "Player " + name + ", You lose!");
                turnComplete.complete(null);
                return turnComplete;
            }
            hasFired = false;
            hasMoved = false;
            hasScanned = false;
            isMyTurn = true;
            updateButtons();

            fireButton.setOnAction(e -> {
                if (isMyTurn && !hasFired) {
                    doaction_F();

                    hasFired = true;
                    turnComplete.complete(null);
                    updateBoardDisplay(boardGrid, theBoard, true);
                    updateBoardDisplay(enemyGrid, theEnemyBoard, false);
                    System.out.println("update board in play one turn");
                    messageLabel.setText("Waiting for enemy action");
                    System.out.println(enemyView.displayMyOwnBoard());
                    System.out.println(view.displayEnemyBoard());

                }
            });

            moveButton.setOnAction(e -> {
                if (isMyTurn && !hasMoved) {
                    doaction_M(); // 执行移动操作
                    hasMoved = true; // 标记已经移动过
                    turnComplete.complete(null); // 标记回合结束
                    updateBoardDisplay(boardGrid, theBoard, true);
                    updateBoardDisplay(enemyGrid, theEnemyBoard, false);
                    messageLabel.setText("Waiting for enemy action");
                    System.out.println("update board in play one turn");
                }
            });

            sonarButton.setOnAction(e -> {
                if (isMyTurn && !hasScanned) {
                    doaction_S(); // 执行扫描操作
                    hasScanned = true; // 标记已经扫描过
                    turnComplete.complete(null); // 标记回合结束
                    updateBoardDisplay(boardGrid, theBoard, true);
                    updateBoardDisplay(enemyGrid, theEnemyBoard, false);
                    messageLabel.setText("Waiting for enemy action");
                    System.out.println("update board in play one turn");
                }
            });



//        });

        return turnComplete;
    }


    public void endTurn() {
        // 结束回合的逻辑
        isMyTurn = false; // 假设有另一个方法来处理回合交换
        disableAllButtons(); // 禁用所有按钮
    }

    public void startNewTurn() {
        Platform.runLater(() -> {
            // 重置动作状态
            hasFired = false;
            hasMoved = false;
            hasScanned = false;
            isMyTurn = true; // 允许当前玩家开始操作
            System.out.println("update buttons");
            updateButtons(); // 重新启用按钮
        });
    }

    public void updateButtons() {
        System.out.println("update buttons with " + name + " turn");

        if (!isMyTurn) {
            fireButton.setDisable(true);
            moveButton.setDisable(true);
            sonarButton.setDisable(true);
        } else {
            fireButton.setDisable(hasFired);
            moveButton.setDisable(hasMoved || skill_count.get("M Move a ship to another square") <= 0);
            sonarButton.setDisable(hasScanned || skill_count.get("S Sonar scan") <= 0);

        }
    }





    public void win_message() {
        Platform.runLater(() -> {

            showCustomDialog("Game Over", "Player " + name + ", Congratulations! You win!");

        });
    }

    public void loss_message(){
        Platform.runLater(() -> {
            showCustomDialog("Game Over", "Player " + name + ", You lose!");
        });

    }

    public void setMyBoard(Board<Character> board, BoardTextView view) {
        theBoard = board;
        this.view = view;

    }


    public static void main(String[] args) {
        launch(args);
    }
}