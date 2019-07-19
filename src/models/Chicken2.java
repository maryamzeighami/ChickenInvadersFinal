package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Chicken2 extends Chicken {




    static Image[] image= new Image[2];
    static {
        image[0] = new Image(new File(System.getProperty("user.dir") + "/src/pics/head2.png").toURI().toString());
        image[1] = new Image(new File(System.getProperty("user.dir") + "/src/pics/head2B.png").toURI().toString());
    }
     public Chicken2(){
        super(image[0]);
        health = 100;
        level=2;


    }
    public void blink() {

        if (getImage()==image[0]) {
            setImage(image[1]);
        } else {
            setImage(image[0]);
        }
    }

}
