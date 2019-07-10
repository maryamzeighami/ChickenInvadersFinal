package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Chicken4 extends Chicken {

    static Image[] image = new Image[2];

    static {
        image[0] = new Image(new File(System.getProperty("user.dir") + "/src/pics/head4B.png").toURI().toString());
        image[1] = new Image(new File(System.getProperty("user.dir") + "/src/pics/head4B.png").toURI().toString());

    }

    Chicken4() {
        super(image[0]);
        health = 300;
        level = 4;
    }


    public void blink() {
        if (getImage() == image[0]) {
            setImage(image[1]);
        } else {
            setImage(image[0]);
        }

    }

}
