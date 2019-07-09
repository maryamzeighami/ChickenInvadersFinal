package models;

import javafx.scene.image.Image;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class Bomb extends javafx.scene.image.ImageView {


    static Image image = new Image(new File(System.getProperty("user.dir") + "/src/pics/bomb.png").toURI().toString());

    public Bomb() {

        super( image);
        this.setFitHeight(70);
        this.setFitWidth(70);
    }

    public void boom() {
        if (image.equals(new Image(new File(System.getProperty("user.dir") + "/src/pics/bomb.png").toURI().toString())))
        image=new Image(new File(System.getProperty("user.dir") + "/src/pics/booom.png").toURI().toString());
        setFitWidth(400);
        setFitHeight(400);
        if (image.equals(new Image(new File(System.getProperty("user.dir") + "/src/pics/booom.png").toURI().toString())))
            image=new Image(new File(System.getProperty("user.dir") + "/src/pics/bomb.png").toURI().toString());
    }
}
