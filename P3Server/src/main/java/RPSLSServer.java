
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RPSLSServer extends Application{


    //Setting the feilds and buttons
    TextField tf_port,s3,s4, c1;
    Button serverChoice,b1, submitPort;
    HashMap<String, Scene> sceneMap;
    GridPane grid;
    HBox line1;
    VBox stackup;
    VBox clientBox;
    Scene startScene;
    BorderPane startPane;
    Server serverConnection;
    Label lb_port;

    ListView<String> listItems;



    int portNum; //here is the port number


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("This is the Server Gui");

        //Initializing the port
        lb_port = new Label("Enter Port");
        tf_port = new TextField("");
        submitPort = new Button("Submit");

        this.serverChoice = new Button("Start Server");


        //Setting the listView
        listItems = new ListView<String>();

        listItems.setCellFactory(lst ->
                new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) { //updatin the server every time an activity happends
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

        this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server")); // new scene user has logged in
            serverConnection = new Server(
                    data -> {Platform.runLater(()->{listItems.getItems().add(data.toString());}); //initalizing the port

            }, portNum);
        });


        submitPort.setOnAction(new EventHandler<ActionEvent>(){ //submitting the port
            public void handle(ActionEvent event){
                portNum = Integer.parseInt(tf_port.getText());
                submitPort.setText("Submitted");
            }
        });


        //desigining the scene
        line1 = new HBox(5, lb_port, tf_port, submitPort);
        stackup = new VBox(5, line1, serverChoice);
        startPane = new BorderPane();
        startPane.setPadding(new Insets(70));
        startPane.setTop(stackup);

        startScene = new Scene(startPane, 500, 200);




        sceneMap = new HashMap<String, Scene>();

        sceneMap.put("server",  createServerGui());





        primaryStage.setScene(startScene);
        primaryStage.show();

    }

    public Scene createServerGui() {

        //Server activity set up
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(70));

        pane.setCenter(listItems); //list Items in it

        return new Scene(pane, 600, 600);


    }

}
