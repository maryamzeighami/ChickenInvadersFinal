package models;

import javafx.scene.image.Image;

import java.io.File;

public class Giant extends Chicken {

    int level;
    static Image[] image= new Image[4];
    static {
        image[0] = new Image(new File(System.getProperty("user.dir") + "/src/pics/mrP.jpg").toURI().toString());
        image[1] = new Image(new File(System.getProperty("user.dir") + "/src/pics/mrP.jpg").toURI().toString());
        image[2] = new Image(new File(System.getProperty("user.dir") + "/src/pics/mrP.jpg").toURI().toString());
        image[3] = new Image(new File(System.getProperty("user.dir") + "/src/pics/mrP.jpg").toURI().toString());

    }
    Giant(int level ){
        super(image[level-1]);
        this.level= level;
        //todo health
        health = 500*level;

    }
}
