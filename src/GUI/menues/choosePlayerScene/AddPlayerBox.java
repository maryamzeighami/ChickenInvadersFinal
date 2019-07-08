package GUI.menues.choosePlayerScene;

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
import models.DataBase;
import models.Player;

import java.io.File;

import static GUI.menues.choosePlayerScene.ChoosePlayerSceneBuilder.getPlayers;

public class AddPlayerBox {
    Stage stage = new Stage();
    TextField nameInput = new TextField();
    VBox vBox;

    public AddPlayerBox(VBox vbox) {
        this.vBox = vbox;
    }

    void Display(TableView table) {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Player");
        stage.setMinWidth(250);

        Label label = new Label();
        label.setText("Name:");



        Button addBut = new Button("add");
        addBut.setStyle("-fx-background-color: #4e804d");

        addBut.setOnAction(event -> {
            String name = getName();
            Player temp = new Player(name);
            DataBase.getInstance().getPlayers().add(temp);
            table.setItems(getPlayers());
            stage.close();
        });

        VBox box = new VBox();
        nameInput.setStyle("-fx-background-color: Green ; -fx-cell-size: 150 ");
        nameInput.setMaxSize(150,50);

        box.getChildren().addAll(label, nameInput, addBut);
//        Scene scene = new Scene(box, 300, 150);


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

    public String getName() {
        String name = new String();
        name = nameInput.getText();
            return name;
    }


}
