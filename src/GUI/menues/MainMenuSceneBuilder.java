package GUI.menues;


import Controller.Save;
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
import java.io.FileNotFoundException;
import java.util.ArrayList;


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
//    private ArrayList<Player> players;
    Media menueSound = new Media(new File(System.getProperty("user.dir") + "/src/sounds/menueSound.mp3").toURI().toString());
    MediaPlayer menuePlayer = new MediaPlayer(menueSound);
//
//    public MainMenuSceneBuilder(ArrayList<Player> players) {
//        this.players=players;
//
//    }

    public MainMenuSceneBuilder() {

    }

    public MainMenuSceneBuilder build(Player player) {

        menuePlayer.play();

        //buttons
        resumeBut = new Button("resume");
        startBut = new Button("single player");
//        settingsBut = new Button("setting");
        exitBut = new Button("exit");
        ratBut = new Button("rating");
        changbut = new Button("change player");
        multiPlayerBut= new Button(" multiplayer");


        if (player.getCurrentGame() == null){
            resumeBut.setDisable(true);
            menuePlayer.pause();
            menuePlayer.getOnStopped();
        }


        //change Button
        changbut.setOnAction(e -> MainStageHolder.stage.setScene(new ChoosePlayerSceneBuilder().
                build(DataBase.getInstance().getPlayers()).getScene()));


        // exit button
        exitBut.setOnAction(event -> {
            Save save = new Save();
            try {
                save.save();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });

        //start
        startBut.setOnAction(event -> {
            menuePlayer.pause();
            menuePlayer.getOnStopped();
            Scene scene = new GameSceneBuilder().builder(player,false).getScene();
            MainStageHolder.stage.setScene(scene);
        });
        resumeBut.setOnAction(event -> {
            //todo
            Scene scene = new GameSceneBuilder().builder(player,true).getScene();
            MainStageHolder.stage.setScene(scene);
        });

        multiPlayerBut.setOnAction(event -> {
           MultiPlayerMenu multiPlayerMenu = new MultiPlayerMenu();
           MainStageHolder.stage.setScene(multiPlayerMenu.build(player).getScene());
        });

        ratBut.setOnAction(event -> {
            Scene scene = new RankingSceneBuilder().build(ChoosePlayerSceneBuilder.players).getScene();
            MainStageHolder.stage.setScene(scene);

        });

        playerName.setText("hello " + player.getName());
        playerName.setStyle("-fx-font-size: 50 ; -fx-fill: White");
        VBox vbox = new VBox(6);
        vbox.getChildren().addAll(playerName,changbut, resumeBut, startBut, multiPlayerBut, ratBut, exitBut);


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
