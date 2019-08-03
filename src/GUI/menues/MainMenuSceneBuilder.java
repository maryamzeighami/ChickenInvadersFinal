package GUI.menues;

import Controller.IO;
import GUI.MainStageHolder;
import GUI.gameplay.ExitMenuStage;
import GUI.gameplay.GameSceneBuilder;
import GUI.menues.choosePlayerScene.ChoosePlayerSceneBuilder;
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
import models.DataBase;
import models.Player;

import java.io.File;


public class MainMenuSceneBuilder {
    private static Scene scene;
    private Button startBut;
    private Button resumeBut;
    private Button settingsBut;
    private Button exitBut;
    private Button ratBut;
    private Button changbut;
    private Button multiPlayerBut;
    private Text playerName = new Text();
    Media menueSound = new Media(new File(System.getProperty("user.dir") + "/src/sounds/menueSound.mp3").toURI().toString());
    MediaPlayer menuePlayer = new MediaPlayer(menueSound);


    public MainMenuSceneBuilder build(Player player) {

        menuePlayer.play();

        //buttons
        resumeBut = new Button("resume");
        startBut = new Button("single player");
        settingsBut = new Button("setting");
        exitBut = new Button("exit");
        ratBut = new Button("rating");
        changbut = new Button("change player");
        multiPlayerBut= new Button(" multiplayer");


        if (player.getCurrentGame() == null){
            resumeBut.setDisable(true);
        }


        //change Button
        changbut.setOnAction(e -> MainStageHolder.stage.setScene(new ChoosePlayerSceneBuilder().
                build(DataBase.getInstance().getPlayers()).getScene()));


        // exit button
        exitBut.setOnAction(event -> {
            IO.saveGame();
            System.exit(0);
        });

        //start
        startBut.setOnAction(event -> {
            menuePlayer.pause();
            menuePlayer.getOnStopped();
            Scene scene = new GameSceneBuilder().builder(player).getScene();
            MainStageHolder.stage.setScene(scene);
        });
        resumeBut.setOnAction(event -> {
            //todo
            Scene scene = new GameSceneBuilder().builder(player).getScene();
            MainStageHolder.stage.setScene(scene);
        });

        multiPlayerBut.setOnAction(event -> {
           MultiPlayerMenu multiPlayerMenu = new MultiPlayerMenu();
           MainStageHolder.stage.setScene(multiPlayerMenu.build(player).getScene());
        });

        playerName.setText("hello " + player.getName());
        playerName.setStyle("-fx-font-size: 50 ; -fx-fill: White");
        VBox vbox = new VBox(6);
        vbox.getChildren().addAll(playerName,changbut, resumeBut, startBut, multiPlayerBut, ratBut, settingsBut, exitBut);


        //background
        String addr = System.getProperty("user.dir") + "/src/pics/back2.jpg";
        File file = new File(addr);
        Image image = new Image(file.toURI().toString());
        StackPane pane = new StackPane();
        pane.getChildren().add(new ImageView(image));
        vbox.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(vbox);
        scene = new Scene(pane, 1070, 750);

        scene.getStylesheets().add("/GUI/CSS/MainMenuStyle.css");


        return this;


    }


    public static Scene getScene() {
        return scene;
    }
}
