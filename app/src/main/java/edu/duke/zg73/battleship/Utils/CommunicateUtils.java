package edu.duke.zg73.battleship.Utils;


import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * A class used to communicate between client and server
 */
public class CommunicateUtils {

    /**
     * Send an encrypted obj between client and server and handle errors for client/server.
     * It will be more user-friendly.
     * @param resultDescripString prints the sendEncryptedObject result to client/server terminal.
     * @param socket
     * @param object obj to be sended
     */
    public static void send(String resultDescripString, Socket socket, Serializable object) {
        while(true){
            String resultOfSend = CommunicateUtils.sendEncryptedObject(socket, object);
            //System.out.println("resultOfSend: " + resultOfSend);
            if(resultOfSend == null){
                System.out.println(resultDescripString);
                break;
            }else{
                System.out.println("Error: " + resultDescripString + " Trying again.");
                try {
                    // pause the current thread's execution for 3 seconds
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    System.out.println("Error: " + resultDescripString + " Trying again.");

                }
            }
        }
    }



    /**
     * Recv an encrypted obj between client and server and handle errors for client/server.
     * It will be more user-friendly.
     * @param resultDescripString prints the receiveDecryptedObject result to client/server terminal.
     * @param socket
     */
    public static Object recv(String resultDescripString, Socket socket) {
        int count = 0;
        int max_try = 3;
        while(true){
            Object objRecved = CommunicateUtils.receiveDecryptedObject(socket);

            if(count >= max_try){
                return "Recv Error";
            }
            if(objRecved instanceof String && "Recv Error".equals(objRecved)){
                System.out.println("Error: " + resultDescripString + " Trying again.");
                count += 1;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    System.out.println("Error: " + resultDescripString + " Trying again.");
                }
            }else {
                System.out.println(resultDescripString);
                return objRecved;
            }
        }
    }

    /**
     * A helper class to sleep
     */



    /**
     *
     * @param socket is the socket used for connection
     * @return received decrypted object
     */
    public static Object receiveDecryptedObject(Socket socket) {
        try {
            InputStream myInputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(myInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("Recv Error");
            return "Recv Error";
        }
    }

    /**
     *
     * @param socket is the socket used for connection
     * @param object is the encrypted object we want to send
     * @return Send Error if the sending fails, or null if it succeeds
     */
    public static String sendEncryptedObject(Socket socket, Serializable object) {
        try {
            OutputStream myOutputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(myOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();

        } catch (Exception e) {
//                e.printStackTrace();
            System.out.println("Send Error");
            return "Send Error";
        }
        return null;

    }

}
