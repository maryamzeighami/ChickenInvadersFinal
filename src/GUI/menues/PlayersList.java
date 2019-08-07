package GUI.menues;

import Controller.CellTower;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;

public class PlayersList {
    public static Scene scene;
    private ArrayList<Text> texts = new ArrayList<>();
    private VBox box = new VBox();

    public PlayersList build(ArrayList<Player> players, boolean server, CellTower tower) {



        setBox(players);

        String addr = System.getProperty("user.dir") + "/src/pics/green2.gif";
        File file = new File(addr);
        Image image = new Image(file.toURI().toString());
        StackPane pane = new StackPane();
        pane.getChildren().addAll(new ImageView(image));

        box.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(box);
        if (server){
            Button but = new Button("start");
            but.setStyle("-fx-background-color: #4e804d");
            but.setOnAction(event -> tower.transmitStart());
            but.setTranslateY(50);
            pane.getChildren().add(but);
        }
        scene = new Scene(pane, 600, 600);
        scene.getStylesheets().add("/GUI/CSS/MainMenuStyle.css");

        return this;

    }

    public Scene getScene() {
        return scene;
    }
    public void setBox(ArrayList<Player> players){
        box.getChildren().clear();
        texts.clear();
        for (Player player : players) {
            texts.add(new Text(player.getName()));
        }
        box.getChildren().addAll(texts);

    }

}
