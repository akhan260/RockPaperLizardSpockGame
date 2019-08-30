import com.sun.corba.se.spi.activation.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;


public class Client extends Thread{

    //all the necessary things we need
    Socket socketClient;
    Scanner scanner;
    ObjectOutputStream out;
    ObjectInputStream in;
    int port;
    String ip;
    private Consumer<Serializable> callback;
    ArrayList<ServerSocket> clients = new ArrayList<ServerSocket>();

    GameInfo info;
    String temp;




    //Client constructor
    Client(Consumer<Serializable> call, int portNum, String ipAd){
        port = portNum;
        callback = call;
        info = new GameInfo();
        ip = ipAd;

    }




    public void run() {
        try {
            //socketClient
            socketClient= new Socket("127.0.0.1",port);
            //incoming data and outgoing data
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);

            while(true) {
                System.out.print("start\n");
                Serializable data = (Serializable) in.readObject(); //taking the incoming data and printing to the client
                temp = data.toString();
                System.out.print(data + "\n");
                callback.accept(data);//accepting the data
                System.out.print("end\n");
            }
        }
        catch(Exception e) {}
    }

    //data sending to the server
    public void send(String data) {

        try {
            out.writeObject(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
