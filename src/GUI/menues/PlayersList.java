package GUI.menues;

import Controller.CellTower;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Player;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PlayersList {
    public static Scene scene;
    ArrayList<Text> texts= new ArrayList<>();


    public PlayersList build(ArrayList<Player> players) {


        VBox box = new VBox();

        for (Player player : players) {
            texts.add(new Text(player.getName()));
        }



        box.getChildren().addAll(texts);


        String addr = System.getProperty("user.dir") + "/src/pics/green2.gif";
        File file = new File(addr);
        Image image = new Image(file.toURI().toString());
        StackPane pane = new StackPane();
        pane.getChildren().addAll(new ImageView(image));

        box.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(box);

        scene = new Scene(pane, 600, 600);
        scene.getStylesheets().add("/GUI/CSS/MainMenuStyle.css");

        return this;

    }

    public Scene getScene() {
        return scene;
    }
    public void setBox(ArrayList<Player> players){
       texts=null;
        for (Player player : players) {
            texts.add(new Text(player.getName()));
        }

    }

}
