import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SimpleServerClient {

    private static boolean isServer = false;

    public static void main(String[] args)throws IOException, ClassNotFoundException{
        // TODO Auto-generated method stub

        SimpleServerClient myNet = new SimpleServerClient();

        if(isServer) {

            myNet.serverCode();

        }
        else {

            myNet.clientCode();
        }


    }

    public void serverCode() throws IOException {
        System.out.println("Server is waiting for a client!");
        ServerSocket mysocket = new ServerSocket(5555);
        Socket connection = mysocket.accept();


        System.out.println("Server has a client!!!");
        System.out.println("This is the remote port the client is using: " + connection.getPort());

        ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
        connection.setTcpNoDelay(true);


        while(true) {

            try {
                InfoPass info = (InfoPass)in.readObject();

                System.out.println("Server received this InfoPass object: ");
                System.out.println("p1: " + info.p1Choic + " p2: " + info.p2Choice);
                info.p1Score=0;
                info.p2Score=1;
                out.writeObject(info);
            }
            catch(Exception e) {

            }
	    	
	    	/*try with arraylist: works because arraylist is serializable
	    	try {
	    	ArrayList<Integer> list = (ArrayList<Integer>)in.readObject();
	    	
	    	System.out.println("Server received this arraylist: ");
	    	list.forEach(i->System.out.println(i));
	    	list.add(500);
	    	out.writeObject(list);
	    	}
	    	catch(Exception e) {

	    	}
	    	*/
	    /*	
	    	try {
	    	String data = in.readObject().toString();
	    	System.out.println("Server received: " + data);
	    	out.writeObject(data.toUpperCase());
	    	}
	    	catch(Exception e) {
	    		System.out.println("OOOOPPs...Something wrong with the socket...closing down!");
	    		break;
	    	}
	    	*/
        }

    }

    public void clientCode() throws IOException, ClassNotFoundException{

        Socket socketClient= new Socket("127.0.0.1",5555);
        System.out.println("Client: "+"Connection Established");

        System.out.println("This is the remote address client is connected to: " +socketClient.getRemoteSocketAddress());
        System.out.println("And the remote port: " + socketClient.getPort());

        Scanner scanner = new Scanner(System.in);
        ObjectOutputStream out = new ObjectOutputStream(socketClient.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socketClient.getInputStream());
        socketClient.setTcpNoDelay(true);

        //try with a class that is serializable

        while(scanner.hasNextLine()) {
            InfoPass info = new InfoPass();
            info.p1Choic = "rock";
            info.p2Choice = "spock";
            info.p1Score = 0;
            info.p2Score = 0;

            out.writeObject(info);
            info = (InfoPass)in.readObject();
            System.out.println("score: " + info.p1Score + " " + info.p2Score);

            break;

        }
	    
	    /*try with arrayList. works because arraylist is serializable
	    while(scanner.hasNextLine()) {
	    	ArrayList<Integer> list = new ArrayList<Integer>();
	    	
	    	list.add(20);
	    	list.add(30);
	    	list.add(40);
	    	
	    	out.writeObject(list);
	    	list = (ArrayList<Integer>)in.readObject();
	    	list.forEach(e->System.out.println(e));
	    	
	    	break;
	    	
	    }
	   */ 

	    while(scanner.hasNextLine()) {
	    	out.writeObject(scanner.nextLine());
	    	String data = in.readObject().toString();
	    	System.out.println("client received: " + data);
	    }

        scanner.close();
        socketClient.close();

    }

}

class InfoPass implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int players,p1Score, p2Score;
    String p1Choic, p2Choice;

}