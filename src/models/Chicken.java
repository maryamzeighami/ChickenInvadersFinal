package models;

import enums.ChickenLevel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class Chicken extends ImageView {

    public int health;
    int level;
    static Image[] image = new Image[2];

    Chicken(Image image) {
        super(image);
        this.setFitHeight(60);
        this.setFitWidth(100);
    }

    public void blink() {
        setImage(new Image(new File(System.getProperty("user.dir") + "/src/pics/blink.png").toURI().toString()));

    }


    public void decHealth(int firePower) {
        health -= firePower;
    }

    public boolean isDead() {
        if (health<=0)
            return true;
        else
            return false;
    }


    public int getlevel() {
        return level;
    }


}
