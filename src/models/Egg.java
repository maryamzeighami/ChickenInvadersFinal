package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Egg extends ImageView {

    Image image = new Image(new File(
            System.getProperty("user.dir") + "/src/pics/lazer.png").toURI().toString());

    public Egg() {
        setImage(image);
        setFitHeight(40);
        setFitWidth(40);
    }
}
