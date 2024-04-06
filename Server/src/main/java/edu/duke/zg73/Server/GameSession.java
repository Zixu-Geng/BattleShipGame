package edu.duke.zg73.Server;

import edu.duke.zg73.battleship.App;
import edu.duke.zg73.battleship.TextPlayer;
import edu.duke.zg73.battleship.Utils.CommunicateUtils;
import edu.duke.zg73.battleship.Utils.AsynCommunicateUtils;
import org.w3c.dom.Text;

import java.io.*;
import java.net.Socket;

public class GameSession implements Runnable {
    private Socket player1Socket;
    private Socket player2Socket;

    public GameSession(Socket player1, Socket player2) {
        this.player1Socket = player1;
        this.player2Socket = player2;
    }

    @Override
    public void run() {

        process();


    }

    public void process(){
        System.out.println("begin process");

        CommunicateUtils.send("send Game starts to player1", player1Socket, "Game starts!");
        CommunicateUtils.send("send Game starts to player2", player2Socket, "Game starts!");

        TextPlayer player1 =(TextPlayer) CommunicateUtils.recv("receive player1", player1Socket);
        player1.setupShipCreationMap();

        TextPlayer player2 =(TextPlayer) CommunicateUtils.recv("receive player2", player2Socket);
        player2.setupShipCreationMap();



        CommunicateUtils.send("send player1 to player1", player1Socket, player1);
        CommunicateUtils.send("send player2 to player1", player1Socket, player2);

        CommunicateUtils.send("send player2 to player2", player2Socket, player2);
        CommunicateUtils.send("send player1 to player2", player2Socket, player1);

        boolean is_player1 = true;//true for player1, false for player2


        System.out.println("setting is over");
        while(true){
            System.out.println("this is for player " + (is_player1 ? "1" : "2"));

            if(is_player1){
                CommunicateUtils.send("send player1 to player1", player1Socket, player1);
                CommunicateUtils.send("send player2 to player1", player1Socket, player2);
                player1 = (TextPlayer) CommunicateUtils.recv("receive player1", player1Socket);
                player2 = (TextPlayer) CommunicateUtils.recv("receive player2", player1Socket);
            }else{
                CommunicateUtils.send("send player2 to player2", player2Socket, player2);
                CommunicateUtils.send("send player1 to player2", player2Socket, player1);
                player2 = (TextPlayer) CommunicateUtils.recv("receive player2", player2Socket);
                player1 = (TextPlayer) CommunicateUtils.recv("receive player1", player2Socket);
            }

            if(player1.theBoard.is_lost()){
                player1.win_message();
                return;
            } else if (player2.theBoard.is_lost()){
                player2.win_message();
                return;
            }

            is_player1 = !is_player1;

        }


    }


}
