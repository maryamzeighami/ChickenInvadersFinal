package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Seed extends ImageView {

    Image image = new Image(new File(
            System.getProperty("user.dir") + "/src/pics/seed.png").toURI().toString());

    public Seed() {
        setImage(image);
        setFitHeight(50);
        setFitWidth(50);
    }
}
