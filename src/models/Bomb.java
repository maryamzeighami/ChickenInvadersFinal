package models;

import javafx.scene.image.Image;

import java.io.File;


public class Bomb extends javafx.scene.image.ImageView {


    static Image image =new Image(new File(System.getProperty("user.dir") + "/src/pics/icon.jpg").toURI().toString());

    public Bomb() {
        super( image);
        this.setFitHeight(50);
        this.setFitWidth(50);
    }
}
