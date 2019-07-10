package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Chicken3 extends Chicken {

    static Image[] image= new Image[2];
    static {
        image[0] = new Image(new File(System.getProperty("user.dir") + "/src/pics/head3B.png").toURI().toString());
        image[1] = new Image(new File(System.getProperty("user.dir") + "/src/pics/head3.png").toURI().toString());
    }
    Chicken3(){

        super(image[0]);
        health = 200;
        level=3;
    }
    public void blink() {

        if (getImage()==image[0]) {
            setImage(image[1]);
        } else {
            setImage(image[0]);
        }
    }
}
