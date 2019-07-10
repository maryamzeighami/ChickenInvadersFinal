package models;


import Controller.Constants;
import javafx.scene.image.Image;

import java.io.File;

public class Chicken1 extends Chicken {

    static Image[] image= new Image[2];
    static {
        image[0] = new Image(new File(System.getProperty("user.dir") + "/src/pics/head1.png").toURI().toString());
        image[1] = new Image(new File(System.getProperty("user.dir") + "/src/pics/head1B.png").toURI().toString());
    }
    Chicken1(){

        super(image[0]);
        health = Constants.CHICKEN_1_HEALTH;
        level=1;

    }
    public void blink() {
//        this.image = new ImageView(new Image(new File(System.getProperty("user.dir") + "/src/pics/head2.png").toURI().toString()));
        if (getImage()==image[0]) {
            setImage(image[1]);
        } else {
            setImage(image[0]);
        }

    }
}
