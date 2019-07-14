package GUI.gameplay;

import GUI.MainStageHolder;
import GUI.menues.MainMenuSceneBuilder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import models.Player;

import java.io.File;

public class LastSceneBuilder {

    private Button goBackButton;
    static Scene scene;
    ImageView imageView;
    ImageView backGround;
    Text message = new Text();

    Media defetedSound = new Media(new File(System.getProperty("user.dir") + "/src/sounds/losing.mp3").toURI().toString());
    MediaPlayer defetingSoundPlayer = new MediaPlayer(defetedSound);
    Media winningdSound = new Media(new File(System.getProperty("user.dir") + "/src/sounds/menueSound.mp3").toURI().toString());
    MediaPlayer winningSoundPlayer = new MediaPlayer(winningdSound);

    public LastSceneBuilder build(Player player , int Score, boolean flag) {



        goBackButton = new Button(" go back to main menu");

        goBackButton.setMinHeight(20);
        goBackButton.setMinWidth(60);
        goBackButton.setStyle("-fx-background-color: #4e804d ; -fx-font-size: 30");

        goBackButton.setOnAction(event -> {
            MainStageHolder.stage.setScene(MainMenuSceneBuilder.getScene());
            if (!flag)
                defetingSoundPlayer.pause();
            else if (flag) winningSoundPlayer.pause();

        });

        if (!flag) {
            message.setText("YOU SUCK " + player.getName()+ " !");

            defetingSoundPlayer.play();
            imageView = new ImageView(new Image(new File(System.getProperty("user.dir")
                    + "/src/pics/source.gif").toURI().toString()));
            imageView.setFitWidth(600);
            imageView.setFitHeight(600);

        }
        if (flag){
            winningSoundPlayer.play();
            imageView=new ImageView(new Image(new File(System.getProperty("user.dir")
                    + "/src/pics/winning.png").toURI().toString()));

            message.setText("YOU MADE IT "+ player.getName()+ " !");

            imageView.setFitWidth(600);
            imageView.setFitHeight(600);

        }




        message.setStyle("-fx-fill: White; -fx-font-size: 40");

        backGround= new ImageView(new Image(new File(System.getProperty("user.dir")
                + "/src/pics/back3.png").toURI().toString()));

            VBox vBox = new VBox();
            vBox.getChildren().addAll(message, goBackButton, imageView);
            vBox.setAlignment(Pos.CENTER);

            StackPane pane = new StackPane();
            pane.getChildren().addAll(backGround,vBox);
            scene = new Scene(pane,800,800);
            return this;



    }


    public static Scene getScene() {
        return scene;
    }
}
