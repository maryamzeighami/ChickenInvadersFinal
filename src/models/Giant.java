package models;

import javafx.scene.image.Image;

import java.io.File;

public class Giant extends Chicken {

    int level;
    static Image[] image= new Image[12];
    static {
        image[0] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant1.png").toURI().toString());
        image[1] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant1B.png").toURI().toString());
        image[2] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant1B.png").toURI().toString());
        image[3] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant2.png").toURI().toString());
        image[4] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant2B.png").toURI().toString());
        image[5] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant2C.png").toURI().toString());
        image[6] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant1.png").toURI().toString());
        image[7] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant3B.png").toURI().toString());
        image[8] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant3C.png").toURI().toString());
        image[9] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant2.png").toURI().toString());
        image[10] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant2B.png").toURI().toString());
        image[11] = new Image(new File(System.getProperty("user.dir") + "/src/pics/giant2C.png").toURI().toString());




    }
    Giant(int level ){

        super(image[level-1]);

        this.level= level;

        setFitWidth(600);
        setFitHeight(600);
//        setTranslateX(0);
//        setTranslateY(0);
        this.level= level;
        //todo health
        health = 500*level;

    }

    @Override
    public void blink() {

        if (getImage().equals(image[level-1])) setImage(image[level]);
         else if (getImage().equals(image[level])) setImage(image[level+1]);
         else if (getImage().equals(image[level+1])) setImage(image[level-1]);
    }
}
