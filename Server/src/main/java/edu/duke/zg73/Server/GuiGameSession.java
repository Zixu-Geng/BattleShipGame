package edu.duke.zg73.Server;

import edu.duke.zg73.battleship.Board;
import edu.duke.zg73.battleship.BoardTextView;
import edu.duke.zg73.battleship.Utils.CommunicateUtils;

import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class GuiGameSession implements Runnable {
    private volatile boolean player1Turn = true; // 初始设定为玩家1开始
    private volatile boolean gameOver = false; // 游戏结束标志



    private Socket player1Socket;
    private Socket player2Socket;

    private Board<Character> player1Board;
    private Board<Character> player2Board;
    private BoardTextView player1View;
    private BoardTextView player2View;

    public GuiGameSession(Socket player1, Socket player2) {
        this.player1Socket = player1;
        this.player2Socket = player2;
    }

    @Override
    public void run() {
        process();
    }

    public void process() {
        System.out.println("Begin process");
        sendGameStarts();

        CommunicateUtils.send("send doAttackingPhase to player1", player1Socket, "doAttackingPhase");
        CommunicateUtils.send("send doAttackingPhase to player2", player2Socket, "doAttackingPhase");

        doAttackingPhase();


    }


    public void doAttackingPhase(){

        if(player1Turn) {
            handleplayer1Turn();
        }else{
            handleplayer2Turn();
        }

        player1Turn = !player1Turn;
        doAttackingPhase();

    }



    private void sendGameStarts() {
        CommunicateUtils.recv("receive Game starts from player1", player1Socket);
        CommunicateUtils.send("send name to player1", player1Socket, "Player 1");

        CommunicateUtils.recv("receive Game starts from player2", player2Socket);
        CommunicateUtils.send("send name to player2", player2Socket, "Player 2");

        player1Board = (Board<Character>) CommunicateUtils.recv("receive player1", player1Socket);
        player1View = (BoardTextView) CommunicateUtils.recv("receive player1 view", player1Socket);

        player2Board = (Board<Character>) CommunicateUtils.recv("receive player2", player2Socket);
        player2View = (BoardTextView) CommunicateUtils.recv("receive player2 view", player2Socket);


        CommunicateUtils.send("send board of player1 to player2", player2Socket, player1Board);
        CommunicateUtils.send("send view of player1 to player2", player2Socket, player1View);

        CommunicateUtils.send("send board of player2 to player1", player1Socket, player2Board);
        CommunicateUtils.send("send view of player2 to player1", player1Socket, player2View);
        System.out.println("display board");
        System.out.println(player1View.displayMyOwnBoard());
        System.out.println(player2View.displayMyOwnBoard());
        System.out.println("finish display");
        CommunicateUtils.send("receive board of player1", player1Socket, "received board1");
        CommunicateUtils.send("receive board of player2", player2Socket, "received board2");


    }
    public void handleplayer1Turn(){
        CommunicateUtils.send("send player1 board to player1", player1Socket, player1Board);
        CommunicateUtils.send("send player1 view to player1", player1Socket, player1View);

        CommunicateUtils.send("send player2 board to player1", player1Socket, player2Board);
        CommunicateUtils.send("send player2 view to player1", player1Socket, player2View);


        //tag002
        player1Board = (Board<Character>) CommunicateUtils.recv("receive player1 board", player1Socket);
        player1View = (BoardTextView) CommunicateUtils.recv("receive player1 view", player1Socket);

        //tag003
        player2Board = (Board<Character>) CommunicateUtils.recv("receive player2 board", player1Socket);
        player2View = (BoardTextView) CommunicateUtils.recv("receive player2 view", player1Socket);


        String confrim_msg = (String) CommunicateUtils.recv("receive confirm finish player1 turn", player1Socket);
        System.out.println(confrim_msg);
        System.out.println("display player 1 board");
        System.out.println(player1View.displayMyOwnBoard());
        System.out.println("display player2 board");
        System.out.println(player2View.displayEnemyBoard());


    }

    public void handleplayer2Turn(){
        CommunicateUtils.send("send player2 board to player2", player2Socket, player2Board);
        CommunicateUtils.send("send player2 view to player2", player2Socket, player2View);

        CommunicateUtils.send("send player1 board to player2", player2Socket, player1Board);
        CommunicateUtils.send("send player1 view to player2", player2Socket, player1View);

        player2Board = (Board<Character>) CommunicateUtils.recv("receive player2 board", player2Socket);
        player2View = (BoardTextView) CommunicateUtils.recv("receive player2 view", player2Socket);


        //tag003
        player1Board = (Board<Character>) CommunicateUtils.recv("receive player1 board", player2Socket);
        player1View = (BoardTextView) CommunicateUtils.recv("receive player1 view", player2Socket);


        String confrim_msg = (String) CommunicateUtils.recv("receive confirm finish player2 turn", player2Socket);

        System.out.println(confrim_msg);
        System.out.println("display player 2 board");
        System.out.println(player2View.displayMyOwnBoard());
        System.out.println("display player2 board");
        System.out.println(player2View.displayEnemyBoard());
    }




}
