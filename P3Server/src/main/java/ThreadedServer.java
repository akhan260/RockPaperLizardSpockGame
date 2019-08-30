import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ThreadedServer{

    private static boolean isServer = false;

    public static void main(String[] args)throws IOException, ClassNotFoundException{
        // TODO Auto-generated method stub

        ThreadedServer myNet = new ThreadedServer();

        if(isServer) {
            myNet.serverCode();

        }
        else {

            myNet.clientCode();
        }

    }

    public void serverCode(){

        int count = 1;

        try(ServerSocket mysocket = new ServerSocket(5555);){
            System.out.println("Server is waiting for a client!");

            while(true) {

                ClientThread c = new ClientThread(mysocket.accept(), count);
                System.out.println("Server has a client!!!");
                c.start();


        	/*
        	ClientRunnable cr = new ClientRunnable(mysocket.accept(), count);
        	Thread t = new Thread(cr);
        	t.start();
        	*/
                count++;
            }
        }
        catch(Exception e) {
            System.out.println("Server socket did not launch");
        }

    }























    public void clientCode() throws IOException, ClassNotFoundException{

        Socket socketClient;
        Scanner scanner;
        ObjectOutputStream out;
        ObjectInputStream in;

        socketClient= new Socket("127.0.0.1",5555);
        System.out.println("Client: "+"Connection Established");

        System.out.println("This is the remote address client is connected to: " +socketClient.getRemoteSocketAddress());
        System.out.println("And the remote port: " + socketClient.getPort());


        scanner = new Scanner(System.in);
        out = new ObjectOutputStream(socketClient.getOutputStream());
        in = new ObjectInputStream(socketClient.getInputStream());
        socketClient.setTcpNoDelay(true);

        while(scanner.hasNextLine()) {
            out.writeObject(scanner.nextLine());
            String data = in.readObject().toString();
            System.out.println("client received: " + data);
        }

    }

}

class ClientThread extends Thread{

    Socket connection;
    int count;
    ObjectInputStream in;
    ObjectOutputStream out;

    ClientThread(Socket s, int count){
        this.connection = s;
        this.count = count;


    }

    public void run(){

        try {
            in = new ObjectInputStream(connection.getInputStream());
            out = new ObjectOutputStream(connection.getOutputStream());
            connection.setTcpNoDelay(true);

        }
        catch(Exception e) {
            System.out.println("Streams not open");
        }



        while(true) {
            try {
                String data = in.readObject().toString();
                System.out.println("Server received: " + data + " from client: " + count);
                out.writeObject(data.toUpperCase());
            }
            catch(Exception e) {
                System.out.println("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                break;
            }
        }
    }
}

class ClientRunnable implements Runnable{

    Socket connection;
    int count;
    ObjectInputStream in;
    ObjectOutputStream out;

    ClientRunnable(Socket s, int count){
        this.connection = s;
        this.count = count;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            in = new ObjectInputStream(connection.getInputStream());
            out = new ObjectOutputStream(connection.getOutputStream());
            connection.setTcpNoDelay(true);
        }
        catch(Exception e) {
            System.out.println("Streams not open");
        }



        while(true) {
            try {
                String data = in.readObject().toString();
                System.out.println("Server received: " + data + " from client: " + count);
                out.writeObject(data.toUpperCase());
            }
            catch(Exception e) {
                System.out.println("OOOOPPs...Something wrong with the socket from client: " + count +"....closing down!");
                break;
            }
        }

    }

}

