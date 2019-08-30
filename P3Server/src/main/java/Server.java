import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.ListView;


public class Server{

	//initializing
	private int count = 1;
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private TheServer server;
	private Consumer<Serializable> callback;
	private int port = 0;
	private GameInfo info;
	boolean check;
	private String play = "Play";
	private String wait = "Wait";

	Server(Consumer<Serializable> call, int portNum){
	    port = portNum;
		callback = call;
		server = new TheServer();
		server.start();
		info = new GameInfo();
	}


	//Class thread
	public class TheServer extends Thread{

		public void run() {

			//Accepting the socket
			try(ServerSocket mysocket = new ServerSocket(port);){
				callback.accept("Server is waiting for a client!\n");
				//logged in now waiiting or a client

				while(true) {
					//a new client has logged in
					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept("A client has connected to server: " + "client #" + count + "\n");
					clients.add(c);

					//if client has reached the 2 (size) then the game proceeds
					if(clients.size() == 2)
                    {
                    	info.p1Again = false;
                    	info.p2Again = false;
                    	info.have2Players = true;
                        callback.accept("Game is now starting");
                        clients.get(0).out.writeObject("You are Player 1");
                        clients.get(1).out.writeObject("You are Player 2");
						clients.get(0).out.writeObject(play); //alternating turns
						clients.get(1).out.writeObject(wait);
						clients.get(0).start();
						clients.get(1).start();
                    }
					count++;
				}
			}//end of try
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}//end of while
	}





	//evaluating moves here

	//I tried using the if else statement but I couldn't I used the if else
	// switch case and I found an outlet from here online that is very similar
	 public int evaluateMove(String move1,String move2){

		//if player1 is rock then
		switch (move1){
			case "rock":{
				switch (move2){
					//Player 2 move
					case "paper":
						return 2; //rock doesn't beat paper so Player 2 wins
					case "rock":
						return 0; //its a tie
					case "scissors":
						return 1; // rock beats scissors
					case "lizard":
						return 1; //rock beats lizard
					case "spock":
						return 2; //rock beats spcock
				}
			}

			//The pattern follows the same for paper
			case "paper":{
				switch (move2){
					case "paper":
						return 0;
					case "rock":
						return 1;
					case "scissors":
						return 2;
					case "lizard":
						return 2;
					case "spock":
						return 1;
				}
			}
			//The pattern follows the same for scissors
			case "scissors":{
				switch (move2){
					case "scissors":
						return 0; //
					case "rock":
						return 2;
					case "paper":
						return 1;
					case "lizard":
						return 1;
					case "spock":
						return 2;
				}
			}
			//The pattern follows the same for lizard
			case "lizard":{
				switch (move2){
					case "lizard":
						return 0;
					case "rock":
						return 2;
					case "paper":
						return 1; //tie
					case "scissors":
						return 2;
					case "spock":
						return 1;
				}
			}

			//The pattern follows the same for spock
			case "spock":{
				switch (move2){
					case "spock":
						return 0;
					case "rock":
						return 1;
					case "paper":
						return 2;
					case "scissors":
						return 1;
					case "lizard":
						return 2;
				}
			}
		}
		return -9999;

	}

	class ClientThread extends Thread{


		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;

		//Client Thread initalizer
		ClientThread(Socket s, int count) throws IOException {
			System.out.println("------1");
			this.connection = s;
			this.count = count;
			in = new ObjectInputStream(connection.getInputStream());
			out = new ObjectOutputStream(connection.getOutputStream());
		}

		public void run(){

			try {
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}


			while(true) {
				try {
					//reading the clients moves
					String data = in.readObject().toString();
					System.out.print(data);

					//player 1 move
					if(count == 1)
					{
						if(clients.get(0).count == 1 && data.equals(" wants to play again"))
						{
							//player wants to play again
							callback.accept("Player " + count + " wants to play again.");
							clients.get(0).out.writeObject("You want to play again.");
							clients.get(1).out.writeObject("Player " + count + " wants to play again.");
							info.p1Again = true;
							info.p2Points = 0;
						}
						if(clients.get(0).count == 1 && data.equals(" wants to quit"))
						{
							//player wants to quit
							callback.accept("Player " + count + " has left the game");
							//both players receieve information
							clients.get(0).out.writeObject("You have left the game.");
							clients.get(1).out.writeObject("Player " + count + " has left the game.");
							clients.get(1).out.writeObject("Final Score: You: " + info.p2Points + "\t Player 1: " + info.p1Points);
							info.p1Left = true;
						}

					}

					//player 2 move
					if(count == 2)
					{
						//player 2 wants to play again
						if(clients.get(1).count == 2 && data.equals(" wants to play again"))
						{
							callback.accept("Player " + count + " wants to play again.");
							//both players recieve the information
							clients.get(1).out.writeObject("You want to play again.");
							clients.get(0).out.writeObject("Player " + count + " wants to play again.");
							info.p2Again = true;
							info.p2Points = 0;
						}
						if(clients.get(1).count == 2 && data.equals(" wants to quit"))
						{
							//player 2 wants to quit
							callback.accept("Player " + count + " has left the game");
							//both players recieve the information
							clients.get(1).out.writeObject("You have left the game.");
							clients.get(0).out.writeObject("Player " + count + " has left the game.");
							clients.get(0).out.writeObject("Final Score: You: " + info.p1Points + "\t Player 2: " + info.p2Points);
							info.p2Left = true;
						}
					}

					//break out the loop now that player has left
					if(info.p2Left || info.p1Left)
					{
						break;
					}

					//wants to play again
					if(info.p1Again && info.p2Again)
					{
						callback.accept("New round is starting");
						clients.get(0).out.writeObject("New round is starting");
						clients.get(1).out.writeObject("New round is starting");

						info.p1Again = false;
						info.p2Again = false;
					}


					//player 1 move
					if(count == 1){
						info.p1Plays = data;
						//player chooses its option in the game
						if(data.equals("rock") || data.equals("paper") || data.equals("scissors") || data.equals("lizard") || data.equals("spock"))
						{
							if(clients.get(0).count == 1)
							{
								//players and server recieve the information
								callback.accept("Player " + count + " played " + info.p1Plays);
								clients.get(0).out.writeObject("You played " + info.p1Plays);

							}
							clients.get(0).out.writeObject(wait);
							clients.get(1).out.writeObject(play);
						}
					}
					//player 2 move
					if(count == 2)
					{

						info.p2Plays = data;
						//player 2 plays it move
						if(data.equals("rock") || data.equals("paper") || data.equals("scissors") || data.equals("lizard") || data.equals("spock"))
						{
							//server and players recieve information
							callback.accept("Player " + count + " played " + info.p2Plays);
							clients.get(1).out.writeObject("You played " + info.p2Plays);
							clients.get(0).out.writeObject("Player " + count + " played " + info.p1Plays);
							clients.get(1).out.writeObject("Player 1 played " + info.p1Plays);


						}
						//evaluating move if p1 won
						if(evaluateMove(info.p1Plays, info.p2Plays) == 1)
						{
							info.p1Points++;
							callback.accept("Player 1 won the round");
							clients.get(0).out.writeObject("You won the round");
							clients.get(1).out.writeObject("Player 1 won the round");
						}
						//evaluating move if p2 won
						if(evaluateMove(info.p1Plays, info.p2Plays) == 2)
						{
							info.p2Points++;
							callback.accept("Player 2 won the round");
							clients.get(0).out.writeObject("Player 2 won the round");
							clients.get(1).out.writeObject("You won the round");
						}
						//evaluating move if tie
						if(evaluateMove(info.p1Plays, info.p2Plays) == 0)
						{
							callback.accept("Tied");
							clients.get(0).out.writeObject("Tied");
							clients.get(1).out.writeObject("Tied");
						}

						//Score and server and player is showing that information
						callback.accept("Score: Player 1: " + info.p1Points + "\t Player 2: " + info.p2Points);
						clients.get(0).out.writeObject("Score: You: " + info.p1Points + "\t Player 2: " + info.p2Points);
						clients.get(1).out.writeObject("Score: You: " + info.p2Points + "\t Player 1: " + info.p1Points);
						clients.get(0).out.writeObject(play);
						clients.get(1).out.writeObject(wait);

						//if a game reaches 3 points to a player the game either restarts or ends
						if(info.p1Points == 3)
						{
							callback.accept("Player 1 has won the game.");
							clients.get(0).out.writeObject("You won the game.");
							clients.get(0).out.writeObject("Would you like to play again?");
							clients.get(1).out.writeObject("Player 1 won the game.");
							clients.get(1).out.writeObject("Would you like to play again?");

							info.p1Points = 0;
							info.p2Points = 0;
						}

						if(info.p2Points == 3)
						{
							callback.accept("Player 2 has won the game.");
							clients.get(0).out.writeObject("Player 2 won the game.");
							clients.get(0).out.writeObject("Would you like to play again?");
							clients.get(1).out.writeObject("You won the game.");
							clients.get(1).out.writeObject("Would you like to play again?");

							info.p1Points = 0;
							info.p2Points = 0;
						}
					}
				}
				catch(Exception e) {
					callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					break;
				}
			}
		}//end of run
	}//end of client thread


}


	
	

	
