package GUI.gameplay;

import Controller.IO;
import GUI.MainStageHolder;
import GUI.menues.MainMenuSceneBuilder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;


public class ExitMenuStage {
    Stage stage = new Stage();
    javafx.scene.text.Text text = new javafx.scene.text.Text();
    VBox vBox;


    public void Display() {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.NONE);
//        stage.setTitle("Add Player");
        stage.setMinWidth(350);


        Button yes = new Button("yes");
        yes.setStyle("-fx-background-color: #4e804d");
        yes.setOnAction(event -> {
            MainStageHolder.stage.setScene(new MainMenuSceneBuilder().build(GameSceneBuilder.currentPlayer).getScene());
        });

        Button no = new Button("no");
        no.setStyle("-fx-background-color: #4e804d");
        no.setOnAction(event -> {
            GameSceneBuilder.timeline.play();
            stage.close();
        });


        VBox box = new VBox();
        text.setText(" Are you sure you want to quit?");


        box.getChildren().addAll(text, yes, no);


        String addr = System.getProperty("user.dir") + "/src/pics/green2.gif";
        File file = new File(addr);
        Image image = new Image(file.toURI().toString());
        StackPane pane = new StackPane();
        pane.getChildren().addAll(new ImageView(image));

        box.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(box);

        Scene scene = new Scene(pane, 300, 200);
        stage.setScene(scene);
        stage.show();

    }


}

