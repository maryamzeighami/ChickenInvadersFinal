package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class PowerUp extends ImageView {

    int type;

    Image[] image = {new Image(new File(
            System.getProperty("user.dir") + "/src/pics/powerUp.png").toURI().toString()),
    new Image(new File(
            System.getProperty("user.dir") + "/src/pics/powerUp2.png").toURI().toString())};

    public PowerUp(int type) {
        this.type= type;
        setImage(image[type-1]);
        setFitHeight(40);
        setFitWidth(40);
    }

    public int getType(){
        return type;
    }
}
