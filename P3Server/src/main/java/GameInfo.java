//Passing info between clients and server:
//        You must implement the GameInfo class. class GameInfo implements
//        Serializable{}
//    You will add serializable data members to this class that keep track of the state of
//        the game( i.e. int p1Points, int p2Points, String p1Plays, String p2Plays ,
//        Boolean have2players…..). This class will be used to send information back and
//        forth between the server and two clients. This is the only way you are allowed to
//        send and receive information.
import java.io.Serializable;

public class GameInfo implements Serializable {
    /**
     *
     */

    private static final long serialVersionUID = 1L;
    //points
    int p1Points, p2Points;
    //move
    String p1Plays, p2Plays;
    //if server has to players
    Boolean have2Players;
    //if players will play again
    Boolean p1Again;
    Boolean p2Again;
    //if players have left
    Boolean p1Left;
    Boolean p2Left;

    GameInfo()
    {
        p1Points = 0;
        p2Points = 0;
        have2Players = false;
        p1Plays = "";
        p2Plays = "";
        p1Again = false;
        p2Again = false;
        p1Left = false;
        p2Left = false;

    }



}

/*
For the server logic:
- It will only allow a game to start if there are two clients connected.
- It will notify a client if they are the only one connected.
- It will keep track of what each player played.
- It will evaluate who won each hand.
- It will evaluate if a client has won the game.
- It will update each client with the above items in time.
- It will do all things necessary to run the game.
It is expected that your server code will open, manage and close all resources needed
and handle all exceptions in a graceful way. For game play, each client will chose to
play either Rock, Paper, Scissors, Lizard or Spock and send that choice to the server.
The server will determine who won and then update each client with what the other
played and the resulting state of the game. If a client has won the hand and has
reached three points, the server will send what the other player played, the resulting
state of the game and require each client to make a choice as to play again or quit. If a
player quits, the server will end that connection. If one player quits and the other wants
to play again, the server will notify the client that they must wait for another person to
connect. If both want to play again, the server will start a new game.
 */