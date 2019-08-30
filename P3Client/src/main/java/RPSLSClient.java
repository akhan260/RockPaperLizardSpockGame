import com.sun.corba.se.spi.activation.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;



public class RPSLSClient extends Application{


    //Setting fields and buttons and images
    TextField tf_port,tf_ip,s3,s4;
    Button clientChoice,PlayAgain, Quit, submitPort, submitIP;
    HashMap<String, Scene> sceneMap;
    GridPane grid;
    HBox line1, line2;
    VBox stackup;
    VBox clientBox;
    Scene startScene;
    BorderPane startPane;
    Client clientConnection;
    Server serverConnection;
    Label lb_port, lb_ip, lb_score;
    Image rock = new Image("rock.png");
    Image paper = new Image("paper.png");
    Image scissors = new Image("scissors.png");
    Image lizard = new Image("lizard.png");
    Image spock = new Image("spock.png");
    ImageView rock_ = new ImageView(rock);
    ImageView paper_ = new ImageView(paper);
    ImageView scissors_ = new ImageView(scissors);
    ImageView lizard_ = new ImageView(lizard);
    ImageView spock_ = new ImageView(spock);


    //list view
    ListView<String> listItems;

     int portNum;
     String ip;




    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub

        //initalizing logging in port
        lb_port = new Label("Enter Port");
        tf_port = new TextField("");
        submitPort = new Button("Submit");

        //initializing logging in ip address
        lb_ip = new Label("Enter IP Address");
        tf_ip = new TextField("");
        submitIP = new Button("Submit");


        primaryStage.setTitle("This is the Client");



        this.clientChoice = new Button("Enter Game"); //button to start the game

        //submitIP is used so the program can get the IP
        submitIP.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                ip = tf_ip.getText();
                submitIP.setText("Submitted");
            }
        });
        //submitPort is used to get the port
        submitPort.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                portNum = Integer.parseInt(tf_port.getText());
                submitPort.setText("Submitted");
            }
        });

        //clientChoice forgot to change it but
        this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
            clientConnection = new Client(
                    data -> {Platform.runLater(()->{listItems.getItems().add(data.toString());});

                    }, portNum, ip);
            clientConnection.start();
        });


        //setting up the design
        line1 = new HBox(5, lb_port, tf_port, submitPort);
        line2 = new HBox(5, lb_ip, tf_ip, submitIP);
        stackup = new VBox(5, line1,line2, clientChoice);
        startPane = new BorderPane();
        startPane.setPadding(new Insets(70));
        startPane.setCenter(stackup);

        startScene = new Scene(startPane, 550, 200);



        sceneMap = new HashMap<String, Scene>();

        sceneMap.put("client",  createClientGui(primaryStage));





        primaryStage.setScene(startScene);
        primaryStage.show();

    }

    public Scene createClientGui(Stage primaryStage) throws IOException, ClassNotFoundException {

        //play again and quit buttons
        PlayAgain = new Button("Play Again");
        Quit = new Button("Quit");
        HBox temp = new HBox(PlayAgain, Quit);

        //setting the images disable before the game starts
        rock_.setDisable(true);
        paper_.setDisable(true);
        scissors_.setDisable(true);
        lizard_.setDisable(true);
        spock_.setDisable(true);
        PlayAgain.setDisable(true);

        //setting up the listview to know the game information
        listItems = new ListView<String>();
        listItems.setCellFactory(lst ->
                new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        //when it is a players turn the other player images will be disable
                        if(clientConnection.temp.equals("Wait"))
                        {
                            rock_.setDisable(true);
                            paper_.setDisable(true);
                            scissors_.setDisable(true);
                            lizard_.setDisable(true);
                            spock_.setDisable(true);
                        }
                        if(clientConnection.temp.equals("Play"))
                        {
                            rock_.setDisable(false);
                            paper_.setDisable(false);
                            scissors_.setDisable(false);
                            lizard_.setDisable(false);
                            spock_.setDisable(false);
                        }
                        //giving the option to play again
                        if(clientConnection.temp.equals("Would you like to play again?"))
                        {
                            PlayAgain.setDisable(false);
                        }
                        super.updateItem(item, empty);
                        if (empty) {
                            setPrefHeight(45.0);
                            setText(null);
                        } else {
                            setPrefHeight(Region.USE_COMPUTED_SIZE);
                            setText(item);
                        }
                    }
                });




        HBox line3 = new HBox(5, rock_, paper_, scissors_, lizard_, spock_);



        //sending data to the server
        rock_.setOnMouseClicked(e -> {
                clientConnection.send("rock");

        });
        paper_.setOnMouseClicked(e -> {
                clientConnection.send("paper");

        });
        scissors_.setOnMouseClicked(e -> {
                clientConnection.send("scissors");
        });
        lizard_.setOnMouseClicked(e -> {
                clientConnection.send("lizard");
        });
        spock_.setOnMouseClicked(e -> {
                clientConnection.send("spock");
        });

        //quit the game
        Quit.setOnAction(e -> {

                clientConnection.send(" wants to quit");
                primaryStage.close();


        });
        //play the game again
        PlayAgain.setOnAction(event -> {

                clientConnection.send(" wants to play again");
        });

        //setting up the design
        clientBox = new VBox(10, temp, line3, listItems);
        return new Scene(clientBox);

    }



}
