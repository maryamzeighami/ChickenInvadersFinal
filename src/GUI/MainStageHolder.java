package GUI;

//import Controller.IO;
import Controller.Save;
import GUI.menues.choosePlayerScene.ChoosePlayerSceneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.DataBase;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainStageHolder extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {

        Save save = new Save();
        save.load();

        stage = primaryStage;
        stage.setOnCloseRequest(event -> {
            try {
                save.save();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.exit(0) ;
        });
        stage.setTitle("Chicken Invaders");

        // icon
        setIcon();

        // setScene
        setScene();

        stage.show();
    }

    private void setScene() {
        Scene choosePlayerScene = new ChoosePlayerSceneBuilder().build(DataBase.getInstance().getPlayers()).getScene();
        stage.setScene(choosePlayerScene);
    }

    private void setIcon() {
        String path = System.getProperty("user.dir") + "/src/pics/icon.jpg";
        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        stage.getIcons().add(image);
    }


}
