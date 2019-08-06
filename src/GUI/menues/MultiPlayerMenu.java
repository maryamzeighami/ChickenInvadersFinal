package GUI.menues;

import Controller.CellTower;
import GUI.MainStageHolder;
import GUI.gameplay.GameSceneBuilder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.DataBase;
import models.Player;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static GUI.menues.choosePlayerScene.ChoosePlayerSceneBuilder.getPlayers;

public class MultiPlayerMenu {
    public static Scene scene;
    TextField numerBox = new TextField();


    public MultiPlayerMenu build(Player player) {

        Button servertBut = new Button("create game");
        servertBut.setStyle("-fx-background-color: #4e804d");
        servertBut.setOnAction(event -> {
            try {
                ServerSocket serverSocket = new ServerSocket(421);
                CellTower cellTower = new CellTower(serverSocket, player);
                // in loop
                for (int i = 0; i < Integer.parseInt(numerBox.getText()); i++) {
                    new Thread(() -> {
                        try {
                            Socket socket = serverSocket.accept();
                            cellTower.addSocket(socket);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        Button clientBut = new Button(" join game");
        clientBut.setStyle("-fx-background-color: #4e804d");
        clientBut.setOnAction(event -> {
            try {
                Socket socket = new Socket("127.0.0.1" ,421);
                CellTower cellTower = new CellTower(socket,player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        VBox box = new VBox();


        box.getChildren().addAll(clientBut, servertBut, numerBox);


        String addr = System.getProperty("user.dir") + "/src/pics/green2.gif";
        File file = new File(addr);
        Image image = new Image(file.toURI().toString());
        StackPane pane = new StackPane();
        pane.getChildren().addAll(new ImageView(image));

        box.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(box);

        scene = new Scene(pane, 300, 200);
        scene.getStylesheets().add("/GUI/CSS/MainMenuStyle.css");

        return this;

    }

    public Scene getScene() {
        return scene;
    }

}
